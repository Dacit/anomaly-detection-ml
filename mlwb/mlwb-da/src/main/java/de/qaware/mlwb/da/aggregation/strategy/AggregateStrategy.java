package de.qaware.mlwb.da.aggregation.strategy;

import de.qaware.mlwb.api.Granularity;
import de.qaware.mlwb.api.ValuePoint;

import java.util.Collection;

/**
 * Strategy interface for aggregating values.
 *
 * @author Fabian Huch
 */
public interface AggregateStrategy {
    /**
     * Aggregates a list of values to a single value by given granularity.
     *
     * @param raw the values to aggregate
     * @param granularity the granularity to aggregate the final value to
     * @return the aggregated value
     */
    ValuePoint aggregate(Collection<ValuePoint> raw, Granularity granularity);
}
