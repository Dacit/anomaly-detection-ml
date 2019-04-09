package de.qaware.mlwb.da.aggregation;

import de.qaware.mlwb.api.AggregationType;
import de.qaware.mlwb.api.Granularity;
import de.qaware.mlwb.api.ValuePoint;
import de.qaware.mlwb.da.aggregation.strategy.AggregateStrategy;
import de.qaware.mlwb.da.aggregation.strategy.AggregateStrategyFactory;

import java.sql.Timestamp;
import java.util.*;

import static de.qaware.mlwb.da.aggregation.AggregationUtils.groupAdjacent;
import static de.qaware.mlwb.da.aggregation.AggregationUtils.withinSameGroup;

/**
 * Iterator that aggregates values lazily. Also interpolates up to one missing value between two groups.
 *
 * @author Fabian Huch
 */
public class AggregationIterator implements Iterator<ValuePoint> {
    private Iterator<ValuePoint> input;

    private Granularity granularity;
    private AggregateStrategy aggregateStrategy;

    private Deque<ValuePoint> rawAcc;
    private LinkedList<ValuePoint> resultAcc;

    /**
     * Constructor.
     *
     * @param input           the input iterator
     * @param granularity     the granularity to use
     * @param aggregationType the aggregation type
     */
    public AggregationIterator(Iterator<ValuePoint> input, Granularity granularity, AggregationType aggregationType,
                               AggregateStrategyFactory aggregateStrategyFactory) {
        this.input = input;
        this.granularity = granularity;

        this.aggregateStrategy = aggregateStrategyFactory.getInstance(aggregationType);

        this.rawAcc = new LinkedList<>();
        this.resultAcc = new LinkedList<>();
    }

    @Override
    public boolean hasNext() {
        return !(resultAcc.isEmpty() && rawAcc.isEmpty() && !input.hasNext());
    }

    @Override
    public ValuePoint next() {
        while (resultAcc.isEmpty()) {
            if (!input.hasNext()) {
                if (rawAcc.isEmpty()) {
                    throw new NoSuchElementException();
                }
                // No more input, but filled aggregator, so create result
                aggregate();
            }
            else {
                ValuePoint next = input.next();

                if (!rawAcc.isEmpty() && !withinSameGroup(
                        next.getDate(), rawAcc.peek().getDate(), granularity)) {
                    // Aggregate values in acc, passing next value as reference for potential interpolation
                    aggregate(next);
                }
                rawAcc.add(next);
            }
        }

        return resultAcc.remove();
    }

    private void aggregate() {
        resultAcc.add(aggregateStrategy.aggregate(rawAcc, granularity));
        rawAcc.clear();
    }

    private void aggregate(ValuePoint next) {
        ValuePoint last = rawAcc.getLast();

        aggregate();

        // Interpolate if there is a max. of one value missing
        if (!groupAdjacent(last.getDate(), next.getDate(), granularity)) {
            Timestamp interpolatedDate = new Timestamp((last.getDate().getTime() + next.getDate().getTime()) / 2L);

            // Only add if in between
            if (groupAdjacent(last.getDate(), interpolatedDate, granularity)
                    && groupAdjacent(interpolatedDate, next.getDate(), granularity)) {
                resultAcc.add(aggregateStrategy.aggregate(Arrays.asList(
                        new ValuePoint(last.getValue(), interpolatedDate),
                        new ValuePoint(next.getValue(), interpolatedDate)), granularity));
            }
        }
    }
}
