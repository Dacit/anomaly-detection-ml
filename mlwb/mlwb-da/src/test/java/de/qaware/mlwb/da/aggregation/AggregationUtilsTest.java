package de.qaware.mlwb.da.aggregation;

import de.qaware.mlwb.api.Granularity;
import org.junit.Test;

import java.sql.Timestamp;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Test for the {@link AggregationUtils}.
 *
 * @author Fabian Huch
 */
public class AggregationUtilsTest {
    @Test
    public void groupAdjacent() throws Exception {
        assertThat(AggregationUtils.groupAdjacent(Timestamp.valueOf("2014-01-03 00:00:00"), Timestamp.valueOf("2014-01-03 00:00:59"), Granularity.MINUTE), is(false));
        assertThat(AggregationUtils.groupAdjacent(Timestamp.valueOf("2014-01-03 00:00:00"), Timestamp.valueOf("2014-01-03 00:01:59"), Granularity.MINUTE), is(true));
        assertThat(AggregationUtils.groupAdjacent(Timestamp.valueOf("2014-01-03 00:02:00"), Timestamp.valueOf("2014-01-03 00:00:00"), Granularity.MINUTE), is(false));

        assertThat(AggregationUtils.groupAdjacent(Timestamp.valueOf("2014-01-03 00:00:00"), Timestamp.valueOf("2014-01-03 00:59:59"), Granularity.HOUR), is(false));
        assertThat(AggregationUtils.groupAdjacent(Timestamp.valueOf("2014-01-03 00:00:00"), Timestamp.valueOf("2014-01-03 01:59:59"), Granularity.HOUR), is(true));
        assertThat(AggregationUtils.groupAdjacent(Timestamp.valueOf("2014-01-03 02:00:00"), Timestamp.valueOf("2014-01-03 00:00:00"), Granularity.HOUR), is(false));

        assertThat(AggregationUtils.groupAdjacent(Timestamp.valueOf("2014-01-03 00:00:00"), Timestamp.valueOf("2014-01-03 00:00:00"), Granularity.NONE), is(true));
        assertThat(AggregationUtils.groupAdjacent(Timestamp.valueOf("2014-01-03 00:00:00"), Timestamp.valueOf("2014-01-03 00:00:01"), Granularity.NONE), is(true));
    }

    @Test
    public void normalizeDateTime() throws Exception {
        assertThat(AggregationUtils.normalizeDateTime(Timestamp.valueOf("2014-01-03 00:00:43"), Granularity.MINUTE), equalTo(Timestamp.valueOf("2014-01-03 00:00:00")));
        assertThat(AggregationUtils.normalizeDateTime(Timestamp.valueOf("2014-01-03 00:01:00"), Granularity.MINUTE), not(equalTo(Timestamp.valueOf("2014-01-03 00:00:00"))));

        assertThat(AggregationUtils.normalizeDateTime(Timestamp.valueOf("2014-01-03 00:17:43"), Granularity.HOUR), equalTo(Timestamp.valueOf("2014-01-03 00:00:00")));
        assertThat(AggregationUtils.normalizeDateTime(Timestamp.valueOf("2014-01-03 01:00:00"), Granularity.HOUR), not(equalTo(Timestamp.valueOf("2014-01-03 00:00:00"))));

        assertThat(AggregationUtils.normalizeDateTime(Timestamp.valueOf("2014-01-03 13:17:43"), Granularity.NONE), equalTo(Timestamp.valueOf("2014-01-03 13:17:43")));
    }

    @Test
    public void withinSameGroup() throws Exception {
        assertThat(AggregationUtils.withinSameGroup(Timestamp.valueOf("2014-01-03 00:00:00"), Timestamp.valueOf("2014-01-03 00:00:59"), Granularity.MINUTE), is(true));
        assertThat(AggregationUtils.withinSameGroup(Timestamp.valueOf("2014-01-03 00:00:00"), Timestamp.valueOf("2014-01-03 00:01:59"), Granularity.MINUTE), is(false));
        assertThat(AggregationUtils.withinSameGroup(Timestamp.valueOf("2014-01-03 00:01:59"), Timestamp.valueOf("2014-01-03 00:00:00"), Granularity.MINUTE), is(false));

        assertThat(AggregationUtils.withinSameGroup(Timestamp.valueOf("2014-01-03 00:00:00"), Timestamp.valueOf("2014-01-03 00:59:59"), Granularity.HOUR), is(true));
        assertThat(AggregationUtils.withinSameGroup(Timestamp.valueOf("2014-01-03 00:00:00"), Timestamp.valueOf("2014-01-03 01:59:59"), Granularity.HOUR), is(false));

        assertThat(AggregationUtils.withinSameGroup(Timestamp.valueOf("2014-01-03 00:00:00"), Timestamp.valueOf("2014-01-03 00:00:01"), Granularity.NONE), is(false));
        assertThat(AggregationUtils.withinSameGroup(Timestamp.valueOf("2014-01-03 00:00:00"), Timestamp.valueOf("9999-12-31 23:59:59"), Granularity.NONE), is(false));
    }
}