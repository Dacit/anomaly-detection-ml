//______________________________________________________________________________
//
//          Project:    Software EKG
//______________________________________________________________________________
//
//         Author:      QAware GmbH 2009 - 2014
//______________________________________________________________________________
//
// Notice: This piece of software was created, designed and implemented by
// experienced craftsmen and innovators in Munich, Germany.
// Changes should be done with respect to the original design.
//______________________________________________________________________________
package de.qaware.mlwb.dt.solr.et;

import de.qaware.mlwb.dt.solr.SolrSchema;
import de.qaware.mlwb.dt.solr.SolrType;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.solr.client.solrj.beans.Field;

import java.sql.Timestamp;

/**
 * The entity for TimeSeries. Used to store in solr.
 *
 * @author Florian Lautenschlager
 * @author christian.fritz
 */
public final class TimeSeriesEt extends AbstractEt {

    /**
     * The series.
     */
    @Field(SolrSchema.SERIES)
    private String series;

    /**
     * The host.
     */
    @Field(SolrSchema.HOST_NAME)
    private String host;

    /**
     * The process.
     */
    @Field(SolrSchema.PROCESS_NAME)
    private String process;

    /**
     * The group.
     */
    @Field(SolrSchema.GROUP)
    private String group;

    /**
     * The measurement.
     */
    @Field(SolrSchema.MEASUREMENT)
    private String measurement;

    /**
     * The metric.
     */
    @Field(SolrSchema.METRIC)
    private String metric;

    /**
     * The start.
     */
    @Field(SolrSchema.START)
    private Timestamp start;

    /**
     * The end.
     */
    @Field(SolrSchema.STOP)
    private Timestamp end;

    /**
     * The ag.
     */
    @Field(SolrSchema.AGGREGATION_LEVEL)
    private String ag;

    /**
     * The data.
     */
    @Field(SolrSchema.DATA)
    private String data;

    /**
     * Default constructor
     */
    public TimeSeriesEt() {
        super(SolrType.RECORD);
    }


    public String getSeries() {
        return series;
    }

    public void setSeries(String series) {
        this.series = series;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getProcess() {
        return process;
    }

    public void setProcess(String process) {
        this.process = process;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getMeasurement() {
        return measurement;
    }

    public void setMeasurement(String measurement) {
        this.measurement = measurement;
    }

    public String getMetric() {
        return metric;
    }

    public void setMetric(String metric) {
        this.metric = metric;
    }

    public Timestamp getStart() {
        return start;
    }

    public void setStart(Timestamp start) {
        this.start = start;
    }

    public Timestamp getEnd() {
        return end;
    }

    public void setEnd(Timestamp end) {
        this.end = end;
    }

    public String getAg() {
        return ag;
    }

    public void setAg(String ag) {
        this.ag = ag;
    }

    /**
     * @return the data of the time series (points)
     */
    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        TimeSeriesEt that = (TimeSeriesEt) o;

        return new EqualsBuilder()
                .appendSuper(super.equals(o))
                .append(series, that.series)
                .append(host, that.host)
                .append(process, that.process)
                .append(group, that.group)
                .append(measurement, that.measurement)
                .append(metric, that.metric)
                .append(start, that.start)
                .append(end, that.end)
                .append(ag, that.ag)
                .append(data, that.data)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .appendSuper(super.hashCode())
                .append(series)
                .append(host)
                .append(process)
                .append(group)
                .append(measurement)
                .append(metric)
                .append(start)
                .append(end)
                .append(ag)
                .append(data)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("series", series)
                .append("host", host)
                .append("process", process)
                .append("group", group)
                .append("measurement", measurement)
                .append("metric", metric)
                .append("start", start)
                .append("end", end)
                .append("ag", ag)
                .append("data", data)
                .toString();
    }
}
