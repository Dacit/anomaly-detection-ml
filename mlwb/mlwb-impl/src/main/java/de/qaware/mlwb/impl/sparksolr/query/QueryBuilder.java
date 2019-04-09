package de.qaware.mlwb.impl.sparksolr.query;

import de.qaware.mlwb.api.QueryContext;
import de.qaware.mlwb.api.QueryMetricContext;
import de.qaware.mlwb.dt.solr.SolrType;
import org.apache.solr.client.solrj.SolrQuery;

import java.util.HashMap;
import java.util.Map;

/**
 * Creates solr options maps for sparksolr queries.
 *
 * @author Fabian Huch
 */
public class QueryBuilder {
    private static final String QUERY_ALL = "*:*";

    private QueryBuilder() {
    }

    /**
     * Builds the query map for a metric query.
     *
     * @param connectionParams the map containing solr connection parameters
     * @param queryContext     the metric query context
     * @return the completed query map
     */
    public static Map<String, String> buildMetricQuery(Map<String, String> connectionParams, QueryMetricContext queryContext) {
        Map<String, String> options = new HashMap<>(connectionParams);

        SolrQuery query = new SolrQuery();
        query.setFacet(false);
        query.setFilterQueries(MetricQueryStringBuilder.buildQueryString(queryContext),
                QueryStringBuilder.buildDocumentTypeQueryString(SolrType.RECORD));
        query.setQuery(QUERY_ALL);

        String qs = query.toQueryString();

        options.put("query", qs.substring(1, qs.length()));

        return options;
    }

    /**
     * Builds the query for a facet query.
     *
     * @param queryContext the query context
     * @param firstFacet   the first field to facet on. For fluent varargs interface with at least one facet.
     * @param otherFacets  the fother fields to facet on
     * @return the completed query
     */
    public static SolrQuery buildPivotFacetQuery(QueryContext queryContext, String firstFacet, String... otherFacets) {
        SolrQuery query = new SolrQuery();
        query.setRows(0);
        query.setFacetMinCount(1);
        query.setFacetLimit(Integer.MAX_VALUE);
        query.setFacet(true);
        query.setFilterQueries(QueryStringBuilder.buildQueryString(queryContext));
        query.setQuery(QUERY_ALL);
        for (String s : otherFacets) {
            firstFacet += "," + s;
        }
        query.addFacetPivotField(firstFacet);

        return query;
    }
}
