package de.qaware.mlwb.impl.sparksolr;

import com.google.common.collect.Iterables;
import com.google.common.collect.Streams;
import com.google.common.util.concurrent.UncheckedExecutionException;
import com.lucidworks.spark.util.SolrSupport;
import de.qaware.mlwb.api.*;
import de.qaware.mlwb.da.ValueStreamMapper;
import de.qaware.mlwb.da.aggregation.strategy.AggregateStrategyFactory;
import de.qaware.mlwb.dt.solr.et.TimeSeriesEt;
import de.qaware.mlwb.impl.sparksolr.service.SolrSparkService;
import de.qaware.mlwb.impl.sparksolr.utils.DateUtils;
import de.qaware.mlwb.impl.sparksolr.utils.MetricsMapper;
import org.apache.solr.client.solrj.impl.CloudSolrClient;
import org.apache.spark.sql.*;
import scala.Tuple2;

import java.io.Serializable;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Implementation of the {@link MetricsService} api, connection with the provided spark context to a solr cloud.
 *
 * @author Fabian Huch
 */
public class MetricsServiceImpl implements MetricsService, Serializable {
    private static final String SOLR_KEY = "zkhost";
    private static final String COLLECTION_KEY = "collection";

    private final SolrSparkService solrSparkService;
    private final ValueStreamMapper mapper;
    private final Map<String, String> solrOptions;

    /**
     * Constructor.
     *
     * @param zkHost       the zookeeper host
     * @param collection   the collection to use
     * @param solrSparkService the solr query service
     * @param mapper       the time series mapper
     */
    public MetricsServiceImpl(String zkHost, String collection, SolrSparkService solrSparkService, ValueStreamMapper mapper) {
        this.solrSparkService = solrSparkService;
        this.mapper = mapper;
        this.solrOptions = new HashMap<>();
        solrOptions.put(SOLR_KEY, zkHost);
        solrOptions.put(COLLECTION_KEY, collection);
    }

    @Override
    public Dataset<Counter> getCounters(QueryMetricContext queryParams, Granularity granularity, AggregationType aggregationType) {
        Dataset<TimeSeriesEt> dataset = solrSparkService.queryTimeSeries(queryParams, solrOptions);

        KeyValueGroupedDataset<Metric, TimeSeriesEt> groupedDataset = dataset.groupByKey(MetricsMapper::fromTimeSeriesET, Encoders.bean(Metric.class));

        // Map each group because TimeSeriesEts for the same group need to be always ordered
        return groupedDataset.mapGroups((key, values) -> {
            final Counter c = new Counter(key);

            Streams.stream(values)
                    // Ordering by start - might not be completely ordered if start is during other time series
                    .sorted(Comparator.comparing(TimeSeriesEt::getStart))
                    .map(ts -> mapper.toValueStream(ts.getData(), granularity, aggregationType)
                            .filter(vp -> DateUtils.insideInterval(vp.getDate(), queryParams.getStart(), queryParams.getEnd()))
                            .collect(Collectors.toList()))
                    .forEachOrdered(decodedValues -> c.getValuePoints().addAll(decodedValues));

            // Compares only date
            c.getValuePoints().sort(ValuePoint::compareTo);
            return c;
        }, Encoders.bean(Counter.class)).filter(c -> !c.getValuePoints().isEmpty());
    }

    @Override
    public Dataset<Tuple2<Metric, Long>> getMetrics(QueryContext queryParams) {
        return solrSparkService.queryMetricsFacet(queryParams, solrOptions);
    }

    @Override
    public void putCounters(Dataset<Counter> counters, String series, SaveMode saveMode) {
        Dataset<TimeSeriesEt> timeSeries = counters.filter(c -> !c.getValuePoints().isEmpty()).flatMap(counter -> {
            Map<Long, List<ValuePoint>> dateByDays = counter.getValuePoints().stream().collect(
                    Collectors.groupingBy(o -> TimeUnit.DAYS.convert(o.getDate().getTime(), TimeUnit.MILLISECONDS)));

            return dateByDays.entrySet().stream().map(e -> {
                TimeSeriesEt tsEt = MetricsMapper.fromMetric(counter.getMetric());
                tsEt.setData(mapper.fromValueStream(e.getValue().stream()));
                tsEt.setStart(e.getValue().iterator().next().getDate());
                tsEt.setEnd(Iterables.getLast(e.getValue()).getDate());
                tsEt.setGroup("feature");
                tsEt.setSeries(series);
                tsEt.setAg("ALL");
                tsEt.setMeasurement("");
                return tsEt;
            }).iterator();
        }, Encoders.bean(TimeSeriesEt.class));

        solrSparkService.storeTimeSeries(solrOptions, timeSeries, saveMode);
    }

    /**
     * Default Factory class.
     */
    public static class Factory implements MetricsService.Factory {
        private SQLContext context;

        /**
         * Constructor, which stores a spark sql context in this factory.
         *
         * @param context the spark sql context to use for the impl
         */
        public Factory(SQLContext context) {
            if (context == null) {
                throw new IllegalArgumentException("Context must not be null!");
            }
            this.context = context;
        }

        @Override
        public MetricsService getInstance(String zkHost, String collection) {
            if (zkHost == null || collection == null) {
                throw new IllegalArgumentException("String args must not be null!");
            }
            // Create dependencies
            AggregateStrategyFactory factory = new AggregateStrategyFactory();
            ValueStreamMapper mapper = new ValueStreamMapper(factory);
            // Get cached solr client, if possible
            CloudSolrClient cloudSolrClient;
            try {
                cloudSolrClient = SolrSupport.getCachedCloudClient(zkHost);
            } catch (UncheckedExecutionException e) {
                cloudSolrClient = new CloudSolrClient.Builder().withZkHost(zkHost).build();
            }
            cloudSolrClient.setDefaultCollection(collection);
            SolrSparkService queryService = new SolrSparkService(context, cloudSolrClient);

            return new MetricsServiceImpl(zkHost, collection, queryService, mapper);
        }
    }
}
