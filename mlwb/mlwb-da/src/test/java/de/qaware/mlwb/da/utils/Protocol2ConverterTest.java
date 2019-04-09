/*
 * ______________________________________________________________________________
 * <p>
 * Project: Software EKG
 * ______________________________________________________________________________
 * <p>
 * created by: f.lautenschlager
 * creation date: 13.03.14 12:57
 * description:
 * ______________________________________________________________________________
 * <p>
 * Copyright: (c) QAware GmbH, all rights reserved
 * ______________________________________________________________________________
 */
package de.qaware.mlwb.da.utils;

import de.qaware.mlwb.da.DateValuePairMapperBuffer;
import de.qaware.mlwb.da.DateValuePairMapperData;
import de.qaware.mlwb.dt.solr.et.DateValuePairEt;
import org.junit.Before;
import org.junit.Test;

import java.util.Iterator;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * @author f.lautenschlager
 */
public class Protocol2ConverterTest extends AbstractProtocolConverterTest {
    DateValuePairMapperData mapper;

    @Before
    public void beforeEachTest() throws Exception {
        initPoints(10);
    }

//    @Test    //Converts from our point class to protocol buffer points
//    public void testToProtocolBufferPoints() throws Exception {
//        ProtocolBuffer.Points points = PointMapper.toProtocolBufferPoints(pointList);
//
//        assertThat(points.getPointsList().size(), is(9));
//        assertProtoPointItems(points.getPointsList(), protoPoints); //EIGENE METHODE (s. unten): (actual, expected)
//    }


//    @Test   // Converts the given protocol buffer points to our point class
//    public void testFromProtocolBufferPoints() throws Exception {
//        List<PointEt> points = PointMapper.fromProtocolBufferPoints(ProtocolBuffer.Points.newBuilder().addAllPoints(protoPoints).build());
//
//        assertThat(pointList.size(), is(9));
//        assertPointItems(points, pointList); //EIGENE METHODE (s. unten): (actual, expected)
//    }


    @Test
    public void testAsPointIterator() throws Exception {
        String base64Encoded = mapper.compressAndEncodeData(pointList);

        Iterator<DateValuePairEt> pointIterator = mapper.uncompressAndDecodeIterator(base64Encoded);


        assertThat(pointIterator.hasNext(), is(true));
        while (pointIterator.hasNext()) {
            //nothing happens
            pointIterator.remove();
            assertThat(pointList.contains(pointIterator.next()), is(true));
        }
    }

    @Test
    public void testAsPoints() throws Exception {
        String base64Encoded = mapper.compressAndEncodeData(pointList);
        List<DateValuePairEt> points = mapper.uncompressAndDecode(base64Encoded);

        assertPointItems(points, pointList);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAsPointsWithIllegalArgument() throws Exception {
        mapper.uncompressAndDecode(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAsZippedAndBase64EncodedWithIllegalInput() throws Exception {
        DateValuePairMapperBuffer.compressAndEncodeData(null);

    }


}
