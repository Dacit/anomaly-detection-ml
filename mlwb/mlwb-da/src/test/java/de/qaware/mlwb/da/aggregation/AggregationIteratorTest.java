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
package de.qaware.mlwb.da.aggregation;

import com.google.common.collect.Lists;
import de.qaware.mlwb.api.AggregationType;
import de.qaware.mlwb.api.Granularity;
import de.qaware.mlwb.api.ValuePoint;
import de.qaware.mlwb.da.aggregation.strategy.AggregateStrategyFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.junit.Assert.assertArrayEquals;

/**
 * The Class AggregationIteratorTest.
 *
 * @author Christian Fritz
 */
@RunWith(Parameterized.class)
public class AggregationIteratorTest {

    /**
     * The input values2.
     */
    private static List<ValuePoint> inputValuePoints, inputValues2;

    /**
     * The dateformat.
     */
    private static DateFormat df = new SimpleDateFormat("y-M-d H:m:s");

    /**
     * The type.
     */
    private AggregationType type;

    /**
     * The input.
     */
    private List<ValuePoint> input;

    /**
     * The expected.
     */
    private ValuePoint[] expected;

    /**
     * The granularity.
     */
    private Granularity granularity;

    /**
     * Instantiates a new aggregator test.
     *
     * @param type        the type
     * @param granularity the granularity
     * @param input       the input
     * @param expected    the expected
     */
    public AggregationIteratorTest(AggregationType type, Granularity granularity, List<ValuePoint> input, ValuePoint[] expected) {
        this.type = type;
        this.granularity = granularity;
        this.input = input;
        this.expected = expected;
    }

    /**
     * Sets the up class.
     *
     * @throws Exception the exception
     */
    public static void setUpClass() throws Exception {

        inputValuePoints = new ArrayList<>();
        inputValuePoints.add(new ValuePoint(6.5, Timestamp.valueOf("2012-05-16 14:46:15")));
        inputValuePoints.add(new ValuePoint(6, Timestamp.valueOf("2012-05-16 14:46:25")));
        inputValuePoints.add(new ValuePoint(2.5, Timestamp.valueOf("2012-05-16 14:46:55")));
        inputValuePoints.add(new ValuePoint(6, Timestamp.valueOf("2012-05-16 14:47:15")));
        inputValuePoints.add(new ValuePoint(2.5, Timestamp.valueOf("2012-05-16 14:47:25")));
        inputValuePoints.add(new ValuePoint(6.5, Timestamp.valueOf("2012-05-16 14:47:55")));
        inputValuePoints.add(new ValuePoint(2.5, Timestamp.valueOf("2012-05-16 14:48:15")));
        inputValuePoints.add(new ValuePoint(6, Timestamp.valueOf("2012-05-16 14:48:25")));
        inputValuePoints.add(new ValuePoint(6.5, Timestamp.valueOf("2012-05-16 14:48:55")));
        inputValuePoints.add(new ValuePoint(2.5, Timestamp.valueOf("2012-05-16 14:49:15")));

        inputValues2 = new ArrayList<ValuePoint>();
        inputValues2.add(new ValuePoint(6.5, Timestamp.valueOf("2012-05-16 14:46:15")));
        inputValues2.add(new ValuePoint(6, Timestamp.valueOf("2012-05-16 14:46:25")));
        inputValues2.add(new ValuePoint(2.5, Timestamp.valueOf("2012-05-16 14:46:55")));
        inputValues2.add(new ValuePoint(6, Timestamp.valueOf("2012-05-16 15:47:15")));
        inputValues2.add(new ValuePoint(2.5, Timestamp.valueOf("2012-05-16 15:47:25")));
        inputValues2.add(new ValuePoint(6.5, Timestamp.valueOf("2012-05-16 15:47:55")));
        inputValues2.add(new ValuePoint(2.5, Timestamp.valueOf("2012-05-16 16:48:15")));
        inputValues2.add(new ValuePoint(6, Timestamp.valueOf("2012-05-16 16:48:25")));
        inputValues2.add(new ValuePoint(6.5, Timestamp.valueOf("2012-05-16 16:48:55")));
    }

    /**
     * Data.
     *
     * @return the list
     * @throws Exception the exception
     */
    @Parameters
    public static List<Object[]> data() throws Exception {
        setUpClass();

        List<Object[]> lst = new ArrayList<Object[]>();

        lst.add(new Object[]{AggregationType.MIN, Granularity.MINUTE, inputValuePoints, new ValuePoint[]{
                new ValuePoint(2.5, Timestamp.valueOf("2012-05-16 14:46:00")), new ValuePoint(2.5, Timestamp.valueOf("2012-05-16 14:47:00")),
                new ValuePoint(2.5, Timestamp.valueOf("2012-05-16 14:48:00")), new ValuePoint(2.5, Timestamp.valueOf("2012-05-16 14:49:00"))
        }});

        lst.add(new Object[]{AggregationType.MAX, Granularity.MINUTE, inputValuePoints, new ValuePoint[]{
                new ValuePoint(6.5, Timestamp.valueOf("2012-05-16 14:46:00")), new ValuePoint(6.5, Timestamp.valueOf("2012-05-16 14:47:00")),
                new ValuePoint(6.5, Timestamp.valueOf("2012-05-16 14:48:00")), new ValuePoint(2.5, Timestamp.valueOf("2012-05-16 14:49:00"))
        }});

        lst.add(new Object[]{AggregationType.MEDIAN, Granularity.MINUTE, inputValuePoints, new ValuePoint[]{
                new ValuePoint(6, Timestamp.valueOf("2012-05-16 14:46:00")), new ValuePoint(6, Timestamp.valueOf("2012-05-16 14:47:00")),
                new ValuePoint(6, Timestamp.valueOf("2012-05-16 14:48:00")), new ValuePoint(2.5, Timestamp.valueOf("2012-05-16 14:49:00"))
        }});

        lst.add(new Object[]{AggregationType.AVG, Granularity.MINUTE, inputValuePoints, new ValuePoint[]{
                new ValuePoint(5, Timestamp.valueOf("2012-05-16 14:46:00")), new ValuePoint(5, Timestamp.valueOf("2012-05-16 14:47:00")),
                new ValuePoint(5, Timestamp.valueOf("2012-05-16 14:48:00")), new ValuePoint(2.5, Timestamp.valueOf("2012-05-16 14:49:00"))
        }});


        lst.add(new Object[]{AggregationType.MIN, Granularity.HOUR, inputValues2, new ValuePoint[]{
                new ValuePoint(2.5, Timestamp.valueOf("2012-05-16 14:00:00")),
                new ValuePoint(2.5, Timestamp.valueOf("2012-05-16 15:00:00")),
                new ValuePoint(2.5, Timestamp.valueOf("2012-05-16 16:00:00"))
        }});

        lst.add(new Object[]{AggregationType.MAX, Granularity.HOUR, inputValues2, new ValuePoint[]{
                new ValuePoint(6.5, Timestamp.valueOf("2012-05-16 14:00:00")),
                new ValuePoint(6.5, Timestamp.valueOf("2012-05-16 15:00:00")),
                new ValuePoint(6.5, Timestamp.valueOf("2012-05-16 16:00:00"))
        }});

        lst.add(new Object[]{AggregationType.MEDIAN, Granularity.HOUR, inputValues2, new ValuePoint[]{
                new ValuePoint(6, Timestamp.valueOf("2012-05-16 14:00:00")),
                new ValuePoint(6, Timestamp.valueOf("2012-05-16 15:00:00")),
                new ValuePoint(6, Timestamp.valueOf("2012-05-16 16:00:00"))
        }});

        lst.add(new Object[]{AggregationType.AVG, Granularity.HOUR, inputValues2, new ValuePoint[]{
                new ValuePoint(5, Timestamp.valueOf("2012-05-16 14:00:00")),
                new ValuePoint(5, Timestamp.valueOf("2012-05-16 15:00:00")),
                new ValuePoint(5, Timestamp.valueOf("2012-05-16 16:00:00"))
        }});
        return lst;
    }

    /**
     * Gets the result.
     *
     * @return the result
     */
    private ValuePoint[] getResult() {
        Iterator<ValuePoint> valueIt = new AggregationIterator(input.iterator(), granularity, type, new AggregateStrategyFactory());

        List<ValuePoint> valuePoints = Lists.newArrayList(valueIt);

        return valuePoints.toArray(new ValuePoint[valuePoints.size()]);
    }

    /**
     * Test aggregator.
     *
     * @throws Exception the exception
     */
    @Test
    public void testAggregator() throws Exception {
        ValuePoint[] result = getResult();

        assertArrayEquals(expected, result);
    }
}
