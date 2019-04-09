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

import com.google.common.collect.ImmutableSet;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Context for querying metrics.
 *
 * @author Florian Lautenschlager
 * @author christian.fritz
 * @author Fabian Huch
 */
public final class QueryMetricContext extends QueryContext {

    /**
     * Maximum of rows: 1000
     */
    private static final int ROW_LIMIT = 1000;

    /**
     * Multi metrics
     */
    private Set<String> metrics = Collections.unmodifiableSet(Collections.emptySet());

    /**
     * The exclude.
     */
    private String exclude;

    /**
     * The expert mode.
     */
    private boolean isExpertMode;

    private QueryMetricContext() {
    }

    /**
     * Gets the exclude metric. Note: Exclude may not be null. There is no setter and if we pass null in the
     * constructor, an empty metric is saved.
     *
     * @return the exclude metric
     */
    public String getExcludeMetric() {
        return this.exclude;
    }

    /**
     * Gets the expert mode.
     *
     * @return the expert mode
     */
    public boolean isExpertMode() {
        return this.isExpertMode;
    }

    /**
     * Gets all metrics.
     *
     * @return all metrics.
     */
    public Set<String> getMetrics() {
        return Collections.unmodifiableSet(metrics);
    }

    /**
     * Getter for the limit.
     *
     * @return the limit for Solr query ranges.
     */
    public int getLimit() {
        return ROW_LIMIT;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        QueryMetricContext that = (QueryMetricContext) o;

        return new EqualsBuilder()
                .appendSuper(super.equals(o))
                .append(isExpertMode, that.isExpertMode)
                .append(metrics, that.metrics)
                .append(exclude, that.exclude)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .appendSuper(super.hashCode())
                .append(metrics)
                .append(exclude)
                .append(isExpertMode)
                .toHashCode();
    }

    /**
     * Builder for the {@link QueryMetricContext}.
     */
    public static class Builder extends QueryContext.Builder<QueryMetricContext> {
        private Set<String> metrics = new HashSet<>();
        private String exclude;
        private boolean isExpertMode;

        /**
         * Init a new builder without predefined values.
         */
        public Builder() {
        }

        /**
         * Init a new builder and set the given context as predefined values.
         *
         * @param context the predefined values context.
         */
        public Builder(QueryContext context) {
            super(context);
        }

        /**
         * Init a new builder and set the given context as predefined values.
         *
         * @param context the predefined values context.
         */
        public Builder(QueryMetricContext context) {
            super(context);
            this.exclude = context.exclude;
            this.isExpertMode = context.isExpertMode;
            context.metrics.forEach(this.metrics::add);
        }

        /**
         * Add metrics to builder.
         *
         * @param metrics the metric names
         * @return this for fluent interface
         */
        public Builder withMetrics(String... metrics){
            if (metrics != null) {
                this.metrics.clear();
                Collections.addAll(this.metrics, metrics);
            }
            return this;
        }

        /**
         * Add metrics to builder.
         *
         * @param metrics the metrics name
         * @return this for fluent interface
         */
        public Builder withMetrics(Set<String> metrics) {
            this.metrics.clear();
            if (metrics != null) {
                metrics.forEach(this.metrics::add);
            }
            return this;
        }

        /**
         * Add exclude to builder.
         *
         * @param exclude the exclude name
         * @return this for fluent interface
         */
        public Builder withExclude(String exclude) {
            this.exclude = exclude;
            return this;
        }

        /**
         * Add isExpertMode to builder.
         *
         * @param isExpertMode the isExpertMode name
         * @return this for fluent interface
         */
        public Builder withExpertMode(boolean isExpertMode) {
            this.isExpertMode = isExpertMode;
            return this;
        }

        /**
         * Add rawQuery to builder.
         *
         * @param rawQuery the rawQuery
         * @return this for fluent interface
         */
        public Builder withRawQuery(String rawQuery) {
            super.withRawQuery(rawQuery);
            return this;
        }

        @Override
        public QueryMetricContext build() {
            QueryMetricContext context = new QueryMetricContext();
            build(context);
            context.exclude = this.exclude != null ? this.exclude : "";
            context.metrics = !this.metrics.isEmpty() ? Collections.unmodifiableSet(this.metrics) : ImmutableSet.of("*");
            context.isExpertMode = this.isExpertMode;

            return context;
        }
    }
}
