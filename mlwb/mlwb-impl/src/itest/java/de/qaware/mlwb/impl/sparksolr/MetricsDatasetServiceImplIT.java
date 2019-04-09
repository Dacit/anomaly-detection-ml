package de.qaware.mlwb.impl.sparksolr;

import de.qaware.mlwb.api.*;
import org.apache.spark.SparkConf;
import org.apache.spark.SparkContext;
import org.apache.spark.sql.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import scala.Tuple2;

import java.sql.Timestamp;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.hasSize;

/**
 * Integration test for the spark solr implementation.
 *
 * @author Fabian Huch
 */
public class MetricsDatasetServiceImplIT extends AbstractSolrCloudIT {
    private static final String SPARK_MASTER = "local[2]";
    private static final String ITEST_SERIES = "mlwb-it";

    private static SQLContext sparkSQLContext;

    private MetricsService service;

    @BeforeClass
    public static void setUpClass() throws Exception {
        SparkContext sc = SparkContext.getOrCreate(new SparkConf().setAppName("mlwb-itest").setMaster(SPARK_MASTER));
        sparkSQLContext = new SQLContext(new SparkSession(sc));
    }

    @Before
    public void setUp() throws Exception {
        this.service = new MetricsServiceImpl.Factory(sparkSQLContext).getInstance(zkHost, collection);
    }

    @Test
    public void testOrdering() throws Exception {
        List<Counter> counters = service.getCounters(new QueryMetricContext.Builder().build(),
                Granularity.NONE, AggregationType.AVG).collectAsList();

        counters.forEach(counter -> {
            assertThat(counter.getValuePoints(), contains( // compare list with sorted instance of itself
                    counter.getValuePoints().stream().sorted(Comparator.comparing(ValuePoint::getDate)).collect(Collectors.toList()).toArray()));
        });
    }

    @Test
    public void testDateRanges() throws Exception {
        QueryContext.Builder<QueryMetricContext> context = new QueryMetricContext.Builder().withMetrics("metric1")
                .withSeries(ITEST_SERIES);

        Dataset<Counter> counterDataset = service.getCounters(context.withStart(Timestamp.valueOf("2015-01-03 00:00:14"))
                .withEnd(Timestamp.valueOf("2015-01-03 00:01:03")).build(), Granularity.NONE, AggregationType.AVG);

        long count = counterDataset.count();
        assertThat(count, is(1L));

        count = service.getCounters(context.withStart(Timestamp.valueOf("2015-01-04 00:00:14"))
                .withEnd(Timestamp.valueOf("2015-01-04 22:59:03")).build(), Granularity.NONE, AggregationType.AVG).count();
        assertThat(count, is(0L));
    }

    @Test
    public void testGetCount() throws Exception {
        QueryMetricContext context = new QueryMetricContext.Builder().withSeries(ITEST_SERIES)
                .withHost("lp07").withProcess("proc1").build();

        Dataset<Counter> counterDataset = service.getCounters(context, Granularity.NONE, AggregationType.AVG);

        assertThat(counterDataset.count(), is(2L));
    }

    @Test
    public void testGetMetrics() throws Exception {
        QueryContext context = new QueryMetricContext.Builder().withSeries(ITEST_SERIES).build();

        Dataset<Tuple2<Metric, Long>> counterDataset = service.getMetrics(context);

        assertThat(counterDataset.map(Tuple2::_2, Encoders.LONG()).reduce((v1, v2) -> v1 + v2), is(4L));
    }

    @Test
    public void testAddMetric() throws Exception {
        QueryMetricContext context = new QueryMetricContext.Builder().withSeries(ITEST_SERIES)
                .withHost("lp07").withProcess("proc2").build();

        Dataset<Counter> counterDataset = service.getCounters(context, Granularity.NONE, AggregationType.AVG);

        List<Counter> counterList = counterDataset.collectAsList();

        service.putCounters(counterDataset, "iTestAdd", SaveMode.Append);


        QueryMetricContext context1 = new QueryMetricContext.Builder().withSeries("iTestAdd")
                .withHost("lp07").withProcess("proc2").build();
        List<Counter> counterList1 = service.getCounters(context1, Granularity.NONE, AggregationType.AVG)
                .collectAsList();

        assertThat(counterList1.containsAll(counterList), is(true));
        assertThat(counterList.containsAll(counterList1), is(true));
    }

    @Test
    public void testDistinctMetrics() throws Exception {
        // Identifier for this test
        String id = "testHost" + UUID.randomUUID().toString();

        // Store counter
        Counter counter = new Counter();
        counter.setMetric(new Metric("testMetric", id, "testProcs"));
        counter.setValuePoints(Collections.singletonList(new ValuePoint(1.0, Timestamp.valueOf("2017-01-01 00:00:00"))));
        Dataset<Counter> counterDataset = sparkSQLContext.createDataset(Collections.singletonList(counter), Encoders.bean(Counter.class));
        service.putCounters(counterDataset, "distinctIT", SaveMode.Ignore);

        // Store second time (with Different solr ID b/c newly created)
        counter = new Counter();
        counter.setMetric(new Metric("testMetric", id, "testProcs"));
        counter.setValuePoints(Collections.singletonList(new ValuePoint(1.0, Timestamp.valueOf("2017-01-01 00:00:00"))));
        counterDataset = sparkSQLContext.createDataset(Collections.singletonList(counter), Encoders.bean(Counter.class));
        service.putCounters(counterDataset, "distinctIT", SaveMode.Ignore);

        // Read and check if duplication is filtered out
        Timestamp start = Timestamp.valueOf("2016-01-01 00:00:00");
        Timestamp stop = Timestamp.valueOf("2018-01-01 00:00:00");

        Dataset<Counter> readDataset = service.getCounters(
                new QueryMetricContext.Builder().withSeries("distinctIT").withHost(id).build(),
                Granularity.MINUTE, AggregationType.AVG);

        assertThat(readDataset.count(), is(1L));
        assertThat(readDataset.first().getValuePoints(), hasSize(1));
    }
}
