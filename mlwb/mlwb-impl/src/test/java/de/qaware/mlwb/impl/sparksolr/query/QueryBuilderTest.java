package de.qaware.mlwb.impl.sparksolr.query;

import de.qaware.mlwb.api.QueryContext;
import de.qaware.mlwb.api.QueryMetricContext;
import org.apache.solr.client.solrj.SolrQuery;
import org.junit.BeforeClass;
import org.junit.Test;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.arrayContaining;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.collection.IsMapContaining.hasEntry;

/**
 * Test for the {@link QueryBuilder}.
 *
 * @author Fabian Huch
 */
public class QueryBuilderTest {
    private static String ALL_METRICS_QUERY;

    @BeforeClass
    public static void setUpClass() throws Exception {
        ALL_METRICS_QUERY = "fq=" + URLEncoder.encode("metric:*", "UTF-8") + "&" + "fq=" +
                URLEncoder.encode("type:RECORD", "UTF-8") + "&q=" + URLEncoder.encode("*:*", "UTF-8");
    }

    @Test
    public void buildMetricQuery() throws Exception {
        QueryMetricContext context = new QueryMetricContext.Builder().build();
        Map<String, String> connectionParams = new HashMap<>();
        connectionParams.put("zkhost", "host1");
        connectionParams.put("collection", "collection1");
        Map<String, String> result = QueryBuilder.buildMetricQuery(connectionParams, context);

        assertThat(result.entrySet(), hasSize(3));
        assertThat(result, hasEntry("zkhost", "host1"));
        assertThat(result, hasEntry("collection", "collection1"));
        assertThat(result, hasEntry("query", ALL_METRICS_QUERY));
    }

    @Test
    public void buildFacetQuery() throws Exception {
        QueryContext context = new QueryContext.Builder().withHost("lp*").build();

        SolrQuery query = QueryBuilder.buildPivotFacetQuery(context, "metric", "host", "process");

        assertThat(query.getFacetLimit(), equalTo(Integer.MAX_VALUE));
        assertThat(query.getFacetMinCount(), is(1));
        assertThat(query.getRows(), is(0));
        assertThat(query.getQuery(), equalTo("*:*"));
        assertThat(query.getFilterQueries(), arrayContaining("host:lp*"));
    }

}