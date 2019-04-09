package de.qaware.mlwb.da.aggregation.strategy;

import de.qaware.mlwb.api.AggregationType;

import java.io.Serializable;

/**
 * Factory for retrieving {@link AggregateStrategy}s.
 *
 * @author Fabian Huch
 */
public class AggregateStrategyFactory implements Serializable {
    public AggregateStrategy getInstance(AggregationType aggType){
        switch (aggType){
            case AVG: {
                return new AvgAggregator();
            }
            case MAX: {
                return new MaxAggregator();
            }
            case MEDIAN: {
                return new MedianAggregator();
            }
            case MIN: {
                return new MinAggregator();
            }
            default: {
                throw new IllegalArgumentException("No strategy for AggregationType " + aggType + " exists!");
            }
        }
    }
}
