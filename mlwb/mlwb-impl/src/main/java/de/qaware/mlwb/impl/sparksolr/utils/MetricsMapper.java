package de.qaware.mlwb.impl.sparksolr.utils;

import de.qaware.mlwb.api.Metric;
import de.qaware.mlwb.dt.solr.et.TimeSeriesEt;

/**
 * @author Fabian Huch
 *
 */
public class MetricsMapper {
    public static Metric fromTimeSeriesET(TimeSeriesEt tsEt) {
        return (new Metric(tsEt.getMetric(), tsEt.getHost(), tsEt.getProcess()));
    }

    public static TimeSeriesEt fromMetric(Metric metric) {
        TimeSeriesEt tsET = new TimeSeriesEt();
        tsET.setMetric(metric.getName());
        tsET.setHost(metric.getHost());
        tsET.setProcess(metric.getProcs());

        return tsET;
    }
}
