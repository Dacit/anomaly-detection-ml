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
package de.qaware.mlwb.da;

import com.google.common.collect.Streams;
import de.qaware.mlwb.api.AggregationType;
import de.qaware.mlwb.api.Granularity;
import de.qaware.mlwb.api.ValuePoint;
import de.qaware.mlwb.da.aggregation.AggregationIterator;
import de.qaware.mlwb.da.aggregation.strategy.AggregateStrategyFactory;
import de.qaware.mlwb.dt.solr.et.DateValuePairEt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.Serializable;
import java.util.Iterator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author drexler.c
 * @author Fabian Huch
 */
public final class ValueStreamMapper implements Serializable {
    private final AggregateStrategyFactory factory;

    public ValueStreamMapper(AggregateStrategyFactory factory) {
        this.factory = factory;
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(ValueStreamMapper.class);

    /**
     * Converts the given data string to a stream of values.
     *
     * @param data            data string
     * @return a stream of values
     */
    public Stream<ValuePoint> toValueStream(String data, Granularity granularity, AggregationType aggregationType) {
        try {
            Iterator<DateValuePairEt> pointIterator = DateValuePairMapper.uncompressAndDecodeIterator(data);
            Iterator<ValuePoint> dateFiltered = Streams.stream(pointIterator)
                    .map(p -> new ValuePoint(p.getValue(), p.getDate()))
                    .iterator();

            return Streams.stream(new AggregationIterator(dateFiltered, granularity, aggregationType, factory));
        }
        catch (IOException e) {
            LOGGER.warn("Could not unzip and decode data to protocol buffer points.", e);
            return Stream.empty();
        }
    }

    /**
     * Converts the given stream of values to a data string
     *
     * @param values value stream
     * @return a data string
     */
    public String fromValueStream(Stream<ValuePoint> values){
        return DateValuePairMapper.compressAndEncodeData(values
                .map(v -> new DateValuePairEt(v.getValue(), v.getDate().getTime()))
                .collect(Collectors.toList()));
    }
}
