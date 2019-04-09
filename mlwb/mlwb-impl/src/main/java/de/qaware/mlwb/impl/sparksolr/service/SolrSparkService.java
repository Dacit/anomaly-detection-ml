package de.qaware.mlwb.impl.sparksolr.service;

import de.qaware.mlwb.api.Metric;
import de.qaware.mlwb.api.QueryContext;
import de.qaware.mlwb.api.QueryMetricContext;
import de.qaware.mlwb.dt.solr.SolrSchema;
import de.qaware.mlwb.dt.solr.et.TimeSeriesEt;
import de.qaware.mlwb.impl.sparksolr.query.QueryBuilder;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.CloudSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Encoders;
import org.apache.spark.sql.SQLContext;
import org.apache.spark.sql.SaveMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import scala.Tuple2;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Provides a service which queries data from solr into dataset and returns typed results.
 *
 * @author Fabian Huch
 */
public class SolrSparkService implements Serializable {
    private static final Logger LOGGER = LoggerFactory.getLogger(SolrSparkService.class);

    private SQLContext context;
    private transient CloudSolrClient solr;


    /**
     * Constructor.
     *
     * @param context the spark sql context to use
     * @param solr    the cloud solr client to use
     */
    public SolrSparkService(SQLContext context, CloudSolrClient solr) {
        this.context = context;
        this.solr = solr;
    }

    /**
     * Queries data store for time series.
     *
     * @param queryContext      the ekg representation of query params
     * @param connectionOptions map which contains solr connection option
     * @return a typed dataset of {@link TimeSeriesEt} for further processing
     */
    public Dataset<TimeSeriesEt> queryTimeSeries(QueryMetricContext queryContext, Map<String, String> connectionOptions) {
        Map<String, String> options = QueryBuilder.buildMetricQuery(connectionOptions, queryContext);

        return context.read()
                .format("solr")
                .options(options)
                .load()
                .as(Encoders.bean(TimeSeriesEt.class))
                .dropDuplicates(SolrSchema.TYPE,
                        SolrSchema.SERIES,
                        SolrSchema.HOST_NAME,
                        SolrSchema.PROCESS_NAME,
                        SolrSchema.GROUP,
                        SolrSchema.MEASUREMENT,
                        SolrSchema.METRIC,
                        SolrSchema.DATA,
                        SolrSchema.AGGREGATION_LEVEL,
                        SolrSchema.START,
                        SolrSchema.STOP);
    }

    /**
     * Queries data store for a facet on the field {@link SolrSchema#METRIC}.
     *
     * @param queryContext      the ekg representation of query params
     * @param connectionOptions map which contains solr connection option
     * @return a typed dataset of {@link Metric}
     */
    public Dataset<Tuple2<Metric, Long>> queryMetricsFacet(QueryContext queryContext, Map<String, String> connectionOptions) {
        try {
            QueryResponse queryResponse = solr.query(connectionOptions.get("collection"),
                    QueryBuilder.buildPivotFacetQuery(queryContext, SolrSchema.METRIC, SolrSchema.HOST_NAME, SolrSchema.PROCESS_NAME));

            List<Tuple2<Metric, Long>> result = queryResponse
                    .getFacetPivot()
                    .get(SolrSchema.METRIC + "," + SolrSchema.HOST_NAME + "," + SolrSchema.PROCESS_NAME)
                    .stream()
                    .flatMap(metricPivot -> {
                        String metric = metricPivot.getValue().toString();
                        return metricPivot.getPivot().stream().flatMap(hostPivot -> {
                            String host = hostPivot.getValue().toString();
                            return hostPivot.getPivot().stream().map(procsPivot ->
                                    new Tuple2<>(new Metric(metric, host, procsPivot.getValue().toString()),
                                            (long) procsPivot.getCount()));
                        });
                    }).collect(Collectors.toList());

            return context.createDataset(result, Encoders.tuple(Encoders.bean(Metric.class), Encoders.LONG()));
        }
        catch (IOException | SolrServerException e) {
            LOGGER.error("Could not read data: {}", e);
            throw new IllegalStateException(e);
        }
    }

    /**
     * Adds time series to the data store
     *
     * @param connectionOptions the solr connection to use
     * @param data              the dataset to store
     * @param saveMode          the savemode. Note that data is saved in the same collection as it was read from.
     */
    public void storeTimeSeries(Map<String, String> connectionOptions, Dataset<TimeSeriesEt> data, SaveMode saveMode) {
        //connectionOptions.put("gen_uniq_key", "true");

        data.write().mode(saveMode).partitionBy("metric").format("solr").options(connectionOptions).save();

        try {
            // Make sure data is visible after update
            solr.commit();
        }
        catch (SolrServerException | IOException e) {
            LOGGER.error("Could not write data: {}", e);
            throw new IllegalStateException(e);
        }
    }
}
