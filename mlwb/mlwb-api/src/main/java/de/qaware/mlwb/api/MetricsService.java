package de.qaware.mlwb.api;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.SaveMode;
import scala.Tuple2;

/**
 * Retrieves the metrics information for a {@link QueryContext} query from a solr cloud and enables spark processing.
 * Uses its spark context for spark pre processing.
 *
 * @author Fabian Huch
 */
public interface MetricsService {
    /**
     * Gets all Counters by the given criterias as dataset of counters. Values within a counter are sorted.
     *
     * @param queryParams     the query params
     * @param granularity     the granularity
     * @param aggregationType specifies the aggregation type of the values (min, max, average, median).
     * @return a dataset of counters, which is already partly evaluated (because of grouping).
     */
    Dataset<Counter> getCounters(QueryMetricContext queryParams, Granularity granularity, AggregationType aggregationType);

    /**
     * Stores the counter dataset in the db.
     *
     * @param counters the counter dataset. Counter values have to be sorted by time.
     * @param seriesName the series to store the data into
     * @param saveMode the save mode to use -- no op yet since latest com.lucidworks.spark:spark-solr (3.0.2) ignores this
     */
    void putCounters(Dataset<Counter> counters, String seriesName, SaveMode saveMode);

    /**
     * Gets all Metrics by a given measurement series.
     *
     * @param queryParams the query params
     * @return a Dataset of all metrics.
     */
    Dataset<Tuple2<Metric, Long>> getMetrics(QueryContext queryParams);

    /**
     * Factory to create a {@link MetricsService}
     */
    interface Factory {
        /**
         * Creates a new instance with the given zookeeper host, collection and spark context.
         *
         * @param zkHost     the zookeeper host in form host:port
         * @param collection the solr cloud collection to use. Usually is 'ekgdata'
         * @return a configured {@link MetricsService}
         */
        MetricsService getInstance(String zkHost, String collection);
    }
}
