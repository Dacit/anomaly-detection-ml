package de.qaware.mlwb.impl.sparksolr;

import de.qaware.mlwb.api.MetricsService;
import de.qaware.mlwb.da.ValueStreamMapper;
import de.qaware.mlwb.da.aggregation.strategy.AggregateStrategyFactory;
import de.qaware.mlwb.impl.sparksolr.service.SolrSparkService;
import org.apache.spark.sql.SQLContext;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.Mockito.mock;

/**
 * Tests all the wiring happening in the {@link MetricsServiceImpl.Factory}.
 *
 * @author Fabian Huch
 */
public class MetricsServiceImplFactoryTest {
    @Test
    public void testWiring() throws Exception {
        SQLContext context = mock(SQLContext.class);

        MetricsService service = new MetricsServiceImpl.Factory(context).getInstance("solr", "collection1");
        Class serviceClass = service.getClass();

        Field solrOptionsField = serviceClass.getDeclaredField("solrOptions");
        solrOptionsField.setAccessible(true);
        Map<String, String> solrOptions = (Map<String, String>) solrOptionsField.get(service);
        assertThat(solrOptions, hasEntry("zkhost", "solr"));
        assertThat(solrOptions, hasEntry("collection", "collection1"));

        Field queryServiceField = serviceClass.getDeclaredField("solrSparkService");
        queryServiceField.setAccessible(true);
        SolrSparkService queryService = (SolrSparkService) queryServiceField.get(service);
        Class queryServiceClass = queryService.getClass();

        Field sqlContextField = queryServiceClass.getDeclaredField("context");
        sqlContextField.setAccessible(true);
        SQLContext sqlContext = (SQLContext) sqlContextField.get(queryService);
        assertThat(sqlContext, equalTo(context));

        Field mapperField = serviceClass.getDeclaredField("mapper");
        mapperField.setAccessible(true);
        ValueStreamMapper mapper = (ValueStreamMapper) mapperField.get(service);

        Field aggregateStrategyFactoryField = mapper.getClass().getDeclaredField("factory");
        aggregateStrategyFactoryField.setAccessible(true);
        AggregateStrategyFactory factory = (AggregateStrategyFactory) aggregateStrategyFactoryField.get(mapper);
        assertThat(factory, notNullValue());
    }
}