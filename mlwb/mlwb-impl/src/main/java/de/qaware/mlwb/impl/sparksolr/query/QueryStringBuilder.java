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


import de.qaware.mlwb.api.QueryContext;
import de.qaware.mlwb.dt.solr.SolrSchema;
import de.qaware.mlwb.dt.solr.SolrType;
import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.util.ClientUtils;

import java.time.Duration;
import java.time.Instant;
import java.util.Collection;
import java.util.function.Function;
import java.util.stream.Collectors;

import static de.qaware.mlwb.dt.solr.SolrSchema.*;
import static de.qaware.mlwb.impl.sparksolr.utils.ObjectUtils.mapOrDefaultIfNull;


/**
 * Builder to easy build solr queries from query contexts.
 *
 * @author christian.fritz
 * @author Fabian Huch
 */
public final class QueryStringBuilder {

    /**
     * String used for combining queries in Solr.
     */
    public static final String AND = " AND ";
    public static final String OR = " OR ";

    private QueryStringBuilder() {
    }

    /**
     * Build a solr query with the default fields
     *
     * @param context the query params
     * @return the solr query. can be used in q= and fq=.
     */
    public static String buildQueryString(QueryContext context) {
        StringBuilder sb = new StringBuilder();
        buildQueryString(sb, context);
        return sb.toString();
    }

    /**
     * Build a solr query with the default fields
     *
     * @param sb      append to this query
     * @param context the query params
     */
    public static void buildQueryString(StringBuilder sb, QueryContext context) {
        append(sb, SERIES, context.getSeries());
        append(sb, HOST_NAME, context.getHost());
        append(sb, PROCESS_NAME, context.getProcess());
        if (context.getStart() != null) {
            String range =  context.getEnd() == null ?
                    buildRangeQuery(Instant.ofEpochMilli(context.getStart().getTime() - Duration.ofDays(1).toMillis()), null) :
                    buildRangeQuery(Instant.ofEpochMilli(context.getStart().getTime() - Duration.ofDays(1).toMillis()),
                            Instant.ofEpochMilli(context.getEnd().getTime() + Duration.ofDays(1).toMillis()));
            // Range checks end impliclitly, if set
            append(sb, START, range);
        } else if (context.getEnd() != null) {
            String range = buildRangeQuery(null, Instant.ofEpochMilli(context.getEnd().getTime() + Duration.ofDays(1).toMillis()));
            append(sb, STOP, range);
        }
    }

    /**
     * Add the raw query to a given query.
     *
     * @param sb      append to this query.
     * @param context the context params.
     */
    public static void appendRawQuery(StringBuilder sb, QueryContext context) {
        if (StringUtils.isNotBlank(context.getRawQuery())) {
            sb.append(' ').append(context.getRawQuery());
        }
    }

    /**
     * Build the solr query to select a solr document type.
     *
     * @param types The document types to select.
     * @return the solr query to select the document type.
     */
    public static String buildDocumentTypeQueryString(SolrType... types) {
        StringBuilder q = new StringBuilder();
        if (types == null || types.length == 0) {
            return "";
        }
        for (SolrType type : types) {
            if (type == SolrType.ALL) {
                return SolrSchema.TYPE + ":*";
            }
            append(q, OR, SolrSchema.TYPE, type.name());
        }
        return q.toString();
    }

    /**
     * Append a field to existing query.
     *
     * @param sb        append to this query
     * @param fieldName the field name
     * @param value     the value
     * @param mapper    a mapper function to convert the value into a string
     */
    protected static <T> void append(StringBuilder sb, String fieldName, T value, Function<T, String> mapper) {
        append(sb, AND, fieldName, mapOrDefaultIfNull(value, null, mapper));
    }

    /**
     * Append a field to existing query.
     *
     * @param sb        append to this query
     * @param fieldName the field name
     * @param value     the value
     */
    public static void append(StringBuilder sb, String fieldName, String value) {
        append(sb, AND, fieldName, value);
    }

    /**
     * Append a field with multiple search values to existing query. The multiple field values are combined with or
     *
     * @param sb        append to this query
     * @param fieldName the field name
     * @param values    the values
     */
    public static void append(StringBuilder sb, String fieldName, Collection<String> values) {
        if (values == null || values.size() == 0) {
            return;
        }
        String subQuery = values.stream().map(QueryStringBuilder::singleValue).collect(Collectors.joining(OR));
        if (StringUtils.isNotBlank(subQuery)) {
            if (sb.length() != 0) {
                sb.append(AND);
            }
            sb.append(fieldName)
                    .append(':')
                    .append("( ")
                    .append(subQuery)
                    .append(" )");
        }
    }

    /**
     * Append a field to existing query.
     *
     * @param sb        append to this query
     * @param combine   the verb to combine the query parts
     * @param fieldName the field name
     * @param value     the value
     */
    protected static void append(StringBuilder sb, String combine, String fieldName, String value) {
        if (StringUtils.isBlank(value) || StringUtils.equals(value, "*")) {
            return;
        }
        String query = singleFieldQuery(fieldName, value);
        if (sb.length() != 0 && StringUtils.isNotBlank(query)) {
            sb.append(combine);
        }
        sb.append(query);
    }

    /**
     * Build the solr query for a single field value combination.
     *
     * @param fieldName the field name
     * @param value     the value
     * @return the solr query string.
     */
    protected static String singleFieldQuery(String fieldName, String value) {
        if (StringUtils.isBlank(value) || StringUtils.equals(value, "*")) {
            return "";
        }
        return fieldName + ":" + singleValue(value);
    }

    protected static String singleValue(String value) {
        if (StringUtils.isBlank(value) || StringUtils.equals(value, "*")) {
            return "";
        }
        return isSingleValue(value) ? value : "(" + value + ")";
    }

    /**
     * Check if the value is a single value, a rage query or a parenthesis enclosed expression.
     *
     * @param value The value to check.
     * @return True if the value is a single expression. False otherwise, may be put into parenthesis.
     */
    private static boolean isSingleValue(String value) {
        String v = value.trim();
        return v.matches("^\\[.*\\]$|^\\(.*\\)$") || !v.contains(" ");
    }

    /**
     * Build a solr range query using the parameters as start and stop. If one parameter is null it is replaced with
     * "*". If both parameters are null it returns also null
     *
     * @param start The start value.
     * @param stop  The stop value.
     * @return The solr range query or null if both params are null.
     */
    public static String buildRangeQuery(Object start, Object stop) {
        if (start == null && stop == null) {
            return null;
        }
        String startStr = start == null ? "*" : start.toString();
        String stopStr = stop == null ? "*" : stop.toString();
        return "[" + startStr + " TO " + stopStr + "]";
    }

    /**
     * Escape a solr query value.
     *
     * @param parameter the parameter
     * @return the string
     */
    public static String escape(String parameter) {
        String result = ClientUtils.escapeQueryChars(parameter);
        return result.replaceAll("\\\\\\*", "*");
    }
}
