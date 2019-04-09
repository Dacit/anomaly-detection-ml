package de.qaware.mlwb.da.aggregation.strategy;

import de.qaware.mlwb.api.AggregationType;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsInstanceOf.instanceOf;

/**
 * Test for the {@link AggregateStrategyFactory}.
 *
 * @author Fabian Huch
 */
public class AggregateStrategyFactoryTest {
    @Test
    public void testGetInstance() throws Exception {
        assertThat(new AggregateStrategyFactory().getInstance(AggregationType.AVG), instanceOf(AvgAggregator.class));
        assertThat(new AggregateStrategyFactory().getInstance(AggregationType.MAX), instanceOf(MaxAggregator.class));
        assertThat(new AggregateStrategyFactory().getInstance(AggregationType.MEDIAN), instanceOf(MedianAggregator.class));
        assertThat(new AggregateStrategyFactory().getInstance(AggregationType.MIN), instanceOf(MinAggregator.class));
    }

}