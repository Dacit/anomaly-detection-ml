//______________________________________________________________________________
//
//                  Project:    Software EKG
//______________________________________________________________________________
//
//                   Author:    QAware GmbH 2009 - 2017
//______________________________________________________________________________
//
// Notice: This piece of software was created, designed and implemented by
// experienced craftsmen and innovators in Munich, Germany.
// Changes should be done with respect to the original design.
//______________________________________________________________________________
package de.qaware.mlwb.da.aggregation.strategy;

import de.qaware.mlwb.api.Granularity;
import de.qaware.mlwb.api.ValuePoint;
import org.junit.Test;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Test for the Average-Aggregator.
 *
 * @author Maximilian Hornung
 */
public class AvgAggregatorTest {
    @Test
    public void testAggregateCaseEmpty() throws Exception {
        AvgAggregator aggregator = new AvgAggregator();
        ValuePoint returnValuePoint = aggregator.aggregate(new LinkedList<>(), Granularity.NONE);
        assertEquals("empty input returns null", null, returnValuePoint);
    }

    @Test
    public void testAggregateCaseNotEmpty() throws Exception {
        AvgAggregator aggregator = new AvgAggregator();
        List<ValuePoint> valuePoints = new ArrayList<>();
        valuePoints.add(new ValuePoint(1, Timestamp.from(Instant.now())));
        valuePoints.add(new ValuePoint(2, Timestamp.from(Instant.now())));
        valuePoints.add(new ValuePoint(3, Timestamp.from(Instant.now())));
        valuePoints.add(new ValuePoint(4, Timestamp.from(Instant.now())));
        valuePoints.add(new ValuePoint(5, Timestamp.from(Instant.now())));
        valuePoints.add(new ValuePoint(6, Timestamp.from(Instant.now())));

        double avg = 3.5;//Expected average
        ValuePoint returnValuePoint = aggregator.aggregate(valuePoints, Granularity.MINUTE);

        assertEquals("average element returned", avg, returnValuePoint.getValue(), 0.00001);
    }
}
