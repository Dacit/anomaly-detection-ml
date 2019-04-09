package de.qaware.mlwb.impl.sparksolr.service;

import com.google.common.collect.ImmutableMap;
import de.qaware.mlwb.api.QueryContext;
import de.qaware.mlwb.api.QueryMetricContext;
import org.apache.solr.client.solrj.impl.CloudSolrClient;
import org.apache.solr.client.solrj.response.PivotField;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.params.SolrParams;
import org.apache.solr.common.util.NamedList;
import org.apache.spark.sql.DataFrameReader;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Encoder;
import org.apache.spark.sql.SQLContext;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mock;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

/**
 * Test for the {@link SolrSparkService}.
 *
 * @author Fabian Huch
 */
public class SolrSparkServiceTest {
    private static String ALL_METRICS_QUERY;

    @Mock
    private SQLContext context;
    @Mock
    private DataFrameReader reader;
    @Mock
    private Dataset dataset;
    private Map<String, String> connectionOptions = ImmutableMap.of("zkhost", "solr", "collection", "collection1");

    @BeforeClass
    public static void setUpClass() throws Exception {
        ALL_METRICS_QUERY = "fq=" + URLEncoder.encode("metric:*", "UTF-8") + "&" + "fq=" +
                URLEncoder.encode("type:RECORD", "UTF-8") + "&q=" + URLEncoder.encode("*:*", "UTF-8");
    }

    @Before
    public void setUp() throws Exception {
        context = mock(SQLContext.class);
        reader = mock(DataFrameReader.class);
        dataset = mock(Dataset.class);

        when(context.read()).thenReturn(reader);
        when(reader.format(anyString())).thenReturn(reader);
        when(reader.options(any(Map.class))).thenReturn(reader);
        when(reader.load()).thenReturn(dataset);
        when(dataset.as(any(Encoder.class))).thenReturn(dataset);
    }

    @Test
    public void queryTimeSeries() throws Exception {
        new SolrSparkService(context, mock(CloudSolrClient.class)).queryTimeSeries(new QueryMetricContext.Builder().build(), connectionOptions);

        Map<String, String> tsConnectionOptions = new HashMap<>(connectionOptions);
        tsConnectionOptions.put("query", ALL_METRICS_QUERY);

        verify(context).read();
        verify(reader).format("solr");
        verify(reader).options(tsConnectionOptions);
        verify(reader).load();
        verify(dataset).as(any(Encoder.class));
        verify(dataset).dropDuplicates(anyString(), (String[]) anyVararg());
        verifyNoMoreInteractions(reader);
        verifyNoMoreInteractions(dataset);
    }

    @Test
    public void queryMetricsFacet() throws Exception {
        CloudSolrClient solr = mock(CloudSolrClient.class);
        QueryResponse rsp = mock(QueryResponse.class);
        NamedList<List<PivotField>> result = mock(NamedList.class);
        List<PivotField> pivotFields = new ArrayList<>();

        when(result.get(anyString())).thenReturn(pivotFields);
        when(rsp.getFacetPivot()).thenReturn(result);
        when(solr.query(anyString(), any(SolrParams.class))).thenReturn(rsp);

        new SolrSparkService(context, solr).queryMetricsFacet(new QueryContext.Builder().build(), connectionOptions);

        verify(solr).query(same(connectionOptions.get("collection")), any());
        verify(context).createDataset(isA(List.class), any());
        verifyNoMoreInteractions(context);
    }
}