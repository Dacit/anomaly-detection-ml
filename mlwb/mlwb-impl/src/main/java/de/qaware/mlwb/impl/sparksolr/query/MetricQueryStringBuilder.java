//______________________________________________________________________________
//
//                  Project:    Software EKG
//______________________________________________________________________________
//
//                   Author:    QAware GmbH 2009 - 2017
//______________________________________________________________________________
//
// Notice: This piece of software was created, designed and implemented by
// experienced craftsmen and innovators in Munich, Germany.
// Changes should be done with respect to the original design.
//______________________________________________________________________________
package de.qaware.mlwb.impl.sparksolr.query;

import de.qaware.mlwb.api.QueryMetricContext;
import org.apache.commons.lang.StringUtils;

import java.util.stream.Collectors;

import static de.qaware.mlwb.dt.solr.SolrSchema.METRIC;


/**
 * Builder to easy build solr queries from metric query contexts.
 *
 * @author christian.fritz
 */
public final class MetricQueryStringBuilder {

    private MetricQueryStringBuilder() {
    }

    /**
     * Generate a query string from a query context object.
     *
     * @param params the query context object.
     * @return a valid solr query string.
     */
    public static String buildQueryString(QueryMetricContext params) {
        StringBuilder sb = new StringBuilder();
        if (params.isExpertMode()) {
            QueryStringBuilder.appendRawQuery(sb, params);
        }
        else {
            QueryStringBuilder.buildQueryString(sb, params);
            buildMetricQueryString(sb, params);
        }

        return sb.toString();
    }

    /**
     * Generate a query string from a query context object.
     *
     * @param sb     append to this query
     * @param params the query context object.
     */
    private static void buildMetricQueryString(StringBuilder sb, QueryMetricContext params) {
        // normal mode -> escape metric
        if (params.getMetrics().size() > 1) {
            QueryStringBuilder.append(sb, METRIC, params.getMetrics().stream().map(QueryStringBuilder::escape).collect(Collectors.toList()));
        }
        else if (params.getMetrics().size() == 1){
            createMetricQueryString(sb, QueryStringBuilder.AND, params.getMetrics().iterator().next());
        }

        createMetricQueryString(sb, " !", params.getExcludeMetric());
    }


    private static void createMetricQueryString(StringBuilder sb, String concat, String metric) {
        if (StringUtils.isNotBlank(metric)) {
            if (sb.length() != 0) {
                sb.append(concat);
            }


            // 1. check simple or complex metric
            boolean isSimple = !metric.matches(".*( AND | OR | NOT| \\|\\| | && ).*");

            if (isSimple) {
                // 1a. escape simple metric and quote it if necessary
                createMetricQueryStringSimple(sb, metric);

            }
            else {
                // 1b. split complex metric and escape split parts
                String m = (" " + metric + " ").replace(" (", " ( ").replace(") ", " ) ").replace(" !", " ! ").replace("\"", " \" ");
                final String andnot = "AND_NOT";
                m = m.replace("AND NOT", andnot).replace("AND !", andnot).replace("&& NOT", andnot).replace("&& !", andnot);


                sb.append('(');
                boolean needsBlank = false;
                boolean isQuotedString = false;
                String quotedString = "";
                for (String metricPart : m.split(" ")) {
                    if (needsBlank && !")".equals(metricPart.trim())) {
                        sb.append(' ');
                        needsBlank = false;
                    }
                    switch (metricPart.trim()) {
                        case "\"":
                            if (isQuotedString) {
                                // 2a. escape simple metric and quote it if necessary
                                createMetricQueryStringSimple(sb, quotedString);
                                quotedString = "";
                                needsBlank = true;
                            }
                            else {
                                needsBlank = false;
                            }
                            isQuotedString = !isQuotedString;
                            break;
                        case "(":
                            sb.append('(');
                            needsBlank = false;
                            break;
                        case ")":
                            sb.append(')');
                            needsBlank = true;
                            break;
                        case andnot:
                            sb.append('!');
                            needsBlank = false;
                            break;
                        case "&&":
                        case "||":
                        case "AND":
                        case "OR":
                            sb.append(metricPart.toUpperCase());
                            needsBlank = true;
                            break;
                        case "!":
                        case "NOT":
                            sb.append('-');
                            needsBlank = false;
                            break;
                        default:
                            if (isQuotedString) {
                                quotedString += (StringUtils.isBlank(quotedString) ? "" : " ") + metricPart;
                            }
                            else {
                                if (StringUtils.isNotBlank(metricPart)) {
                                    sb.append(METRIC).append(':').append(QueryStringBuilder.escape(metricPart));
                                    needsBlank = true;
                                }
                            }
                    }
                }
                sb.append(')');
            }
        }
    }

    private static void createMetricQueryStringSimple(StringBuilder sb, String metric) {
        String quote = metric.contains(" ") ? "\"" : "";
        if (StringUtils.isNotBlank(metric)) {
            sb.append(METRIC).append(':').append(quote).append(QueryStringBuilder.escape(metric)).append(quote);
        }
    }
}
