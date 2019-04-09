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
 * Test for the MaxAggregator.
 *
 * @author Maximilian Hornung
 */
public class MaxAggregatorTest {

    @Test
    public void testAggregateCaseEmpty() throws Exception {
        MaxAggregator aggregator = new MaxAggregator();
        ValuePoint returnValuePoint = aggregator.aggregate(new LinkedList<>(), Granularity.NONE);
        assertEquals("empty input returns null", null, returnValuePoint);
    }

    @Test
    public void testAggregateCaseNotEmpty() throws Exception {
        MaxAggregator aggregator = new MaxAggregator();
        int MAXIMUM = Integer.MAX_VALUE;
        List<ValuePoint> valuePoints = new ArrayList<>();
        valuePoints.add(new ValuePoint(-47, Timestamp.from(Instant.now())));
        valuePoints.add(new ValuePoint(MAXIMUM, Timestamp.from(Instant.now())));
        valuePoints.add(new ValuePoint(55, Timestamp.from(Instant.now())));

        ValuePoint returnValuePoint = aggregator.aggregate(valuePoints, Granularity.MINUTE);
        assertEquals("maximum element returned", MAXIMUM, returnValuePoint.getValue(), 0.00001);
    }
}
