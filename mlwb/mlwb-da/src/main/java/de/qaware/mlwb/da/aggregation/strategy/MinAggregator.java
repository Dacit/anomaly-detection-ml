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
package de.qaware.mlwb.da.aggregation.strategy;

import com.google.common.collect.Iterables;
import de.qaware.mlwb.api.Granularity;
import de.qaware.mlwb.api.ValuePoint;
import de.qaware.mlwb.da.aggregation.AggregationUtils;

import java.sql.Timestamp;
import java.util.Collection;

/**
 * Minimum Aggregator.
 *
 * @author Christian Fritz
 * @author Fabian Huch
 */
public class MinAggregator implements AggregateStrategy {
    @Override
    public ValuePoint aggregate(Collection<ValuePoint> valuePoints, Granularity granularity) {

        if (valuePoints.isEmpty()) {
            return null;
        }

        Timestamp date = AggregationUtils.normalizeDateTime(Iterables.get(valuePoints, 0).getDate(), granularity);
        double min = Double.MAX_VALUE;

        for (ValuePoint v : valuePoints) {

            if (min > v.getValue()) {
                min = v.getValue();
            }
        }
        return new ValuePoint(min, date);
    }
}
