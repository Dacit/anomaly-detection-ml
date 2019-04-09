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
package de.qaware.mlwb.api;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;
import java.sql.Timestamp;


/**
 * Context for querying several metrics from backend.
 *
 * @author Johannes Weigend
 * @author christian.fritz
 * @author Fabian Huch
 */
public class QueryContext implements Serializable {
    private static final String DEFAULT_SERIES = "*";
    private static final String DEFAULT_HOST = "*";
    private static final String DEFAULT_PROCS = "*";

    /**
     * The series.
     */
    private String series;

    /**
     * The host.
     */
    private String host;

    /**
     * The process.
     */
    private String process;

    /**
     * An additional query part appended as it is to the of all field queries.
     */
    private String rawQuery;

    /**
     * Start for the query
     */
    private Timestamp start;

    /**
     * Stop for the query
     */
    private Timestamp end;

    /**
     * Gets the host.
     *
     * @return the host
     */
    public String getHost() {
        return host;
    }

    /**
     * Gets the process.
     *
     * @return the process
     */
    public String getProcess() {
        return process;
    }

    /**
     * Gets the series.
     *
     * @return the series
     */
    public String getSeries() {
        return series;
    }

    /**
     * Gets the start date.
     *
     * @return the start date
     */
    public Timestamp getStart() {
        return start;
    }

    /**
     * Gets the end date.
     *
     * @return the end date
     */
    public Timestamp getEnd() {
        return end;
    }

    /**
     * Gets the raw query
     *
     * @return the raw query
     */
    public String getRawQuery() {
        return rawQuery;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("series", series)
                .append("host", host)
                .append("process", process)
                .append("start", start)
                .append("end", end)
                .append("rawQuery", rawQuery)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        QueryContext that = (QueryContext) o;

        return new EqualsBuilder()
                .append(series, that.series)
                .append(host, that.host)
                .append(process, that.process)
                .append(start, that.start)
                .append(end, that.end)
                .append(rawQuery, that.rawQuery)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(series)
                .append(host)
                .append(process)
                .append(start)
                .append(end)
                .append(rawQuery)
                .toHashCode();
    }

    /**
     * Builder for query contexts or derived classes of query context
     *
     * @param <T> The concrete type for this query.
     */
    public static class Builder<T extends QueryContext> {
        private String rawQuery;
        private String series;
        private String host;
        private String process;
        private Timestamp start;
        private Timestamp end;

        /**
         * Init a new query context builder without predefined values
         */
        public Builder() {
        }

        /**
         * Init the builder with predefined values.
         *
         * @param context the context to predefine values.
         */
        public Builder(QueryContext context) {
            this.series = context.getSeries();
            this.host = context.getHost();
            this.process = context.getProcess();
            this.rawQuery = context.getRawQuery();
        }

        /**
         * Add series to builder.
         *
         * @param series the series name
         * @return this for fluent interface
         */
        public Builder<T> withSeries(String series) {
            this.series = series;
            return this;
        }

        /**
         * Add host to builder.
         *
         * @param host the host name
         * @return this for fluent interface
         **/
        public Builder<T> withHost(String host) {
            this.host = host;
            return this;
        }

        /**
         * Set the builder value "rawQuery"
         *
         * @param rawQuery raw query
         * @return fluent builder interface
         */
        public Builder<T> withRawQuery(String rawQuery) {
            this.rawQuery = rawQuery;
            return this;
        }

        /**
         * Add process to builder.
         *
         * @param process the process name
         * @return this for fluent interface
         */
        public Builder<T> withProcess(String process) {
            this.process = process;
            return this;
        }

        /**
         * Add start date to builder.
         *
         * @param start the start date
         * @return this for fluent interface
         */
        public Builder<T> withStart(Timestamp start) {
            this.start = start;
            return this;
        }

        /**
         * Add end date to builder.
         *
         * @param end the end date
         * @return this for fluent interface
         */
        public Builder<T> withEnd(Timestamp end) {
            this.end = end;
            return this;
        }

        /**
         * Build the query context
         *
         * @return the built query context.
         */
        @SuppressWarnings("unchecked")
        public T build() {
            return build(new QueryContext());
        }

        /**
         * Build the query context.
         *
         * @param context The instance to fill.
         * @return build query context.
         */
        @SuppressWarnings("unchecked")
        protected T build(QueryContext context) {
            context.rawQuery = this.rawQuery;
            context.series = this.series != null ? this.series : DEFAULT_SERIES;
            context.host = this.host != null ? this.host : DEFAULT_HOST;
            context.process = this.process != null ? this.process : DEFAULT_PROCS;
            context.start = this.start;
            context.end = this.end;
            return (T) context;
        }
    }
}