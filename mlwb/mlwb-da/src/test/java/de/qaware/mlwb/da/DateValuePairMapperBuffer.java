//______________________________________________________________________________
//
//          Project:    Software EKG
//______________________________________________________________________________
//
//         Author:      QAware GmbH 2009 - 2017
//______________________________________________________________________________
//
// Notice: This piece of software was created, designed and implemented by
// experienced craftsmen and innovators in Munich, Germany.
// Changes should be done with respect to the original design.
//______________________________________________________________________________
package de.qaware.mlwb.da;

import de.qaware.mlwb.da.et.ProtocolBuffer;
import de.qaware.mlwb.dt.solr.et.DateValuePairEt;
import org.apache.commons.codec.binary.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * @author f.lautenschlager
 */
public final class DateValuePairMapperBuffer {


    //  The class logger: No logging here cause mapper is used in collector as well

    // private static final Logger LOGGER = EkgLogger.get();

    /**
     * Private constructor
     */
    private DateValuePairMapperBuffer() {
        //utility class
    }

    /**
     * Converts the given list of points to a protocol buffers base64 encoded string
     *
     * @param points - the list with points
     * @return a base64 encoded representation
     */
    public static String compressAndEncodeData(List<DateValuePairEt> points) {
        try {
            List<DateValuePairEt> copyPoints = new ArrayList<>(points);

            ProtocolBuffer.Points protoPoints = toProtocolBufferPoints(copyPoints);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            GZIPOutputStream gzos = new GZIPOutputStream(baos);
            gzos.write(protoPoints.toByteArray());
            gzos.flush();
            gzos.close();
            baos.flush();
            baos.close();
            return Base64.encodeBase64String(baos.toByteArray());

        }
        catch (Exception e) {
            throw new IllegalArgumentException("Could not compress and encode points", e);
        }
    }

    /**
     * Converts the base64 encoded string to a list of points
     *
     * @param base64Encoded - the base64 encoded points
     * @return the points or null, if an exception occurred
     */
    public static List<DateValuePairEt> uncompressAndDecode(String base64Encoded) {
        try {
            byte[] decoded = Base64.decodeBase64(base64Encoded);
            ProtocolBuffer.Points protcolBufferPoints = ProtocolBuffer.Points.parseFrom(new GZIPInputStream(new ByteArrayInputStream(decoded)));
            return DateValuePairMapperBuffer.fromProtocolBufferPoints(protcolBufferPoints);
        }
        catch (Exception e) {
            throw new IllegalArgumentException("Could not decompress and decode points", e);
        }

    }

    /**
     * Returns an iterator to the points from the given base 64 encoded data string
     *
     * @param base64Encoded - the base64 encoded points
     * @return an iterator to the points
     * @throws IOException if an I/O error has occurred
     */
    public static Iterator<DateValuePairEt> uncompressAndDecodeIterator(final String base64Encoded) throws IOException {
        return new PointIterator(base64Encoded);
    }

    /**
     * Converts the given list of our point class to the protocol buffer format
     *
     * @param points - the list with points
     * @return a protocol buffer points object
     */
    private static ProtocolBuffer.Points toProtocolBufferPoints(List<DateValuePairEt> points) {
        List<ProtocolBuffer.Point> protoPoints = convertToProtoPoints(points);
        return ProtocolBuffer.Points.newBuilder().addAllPoints(protoPoints).build();
    }

    /**
     * Converts the given list with points to a list with protocol buffer points
     *
     * @param points - the list with points
     * @return a list with protocol buffer points
     */
    private static List<ProtocolBuffer.Point> convertToProtoPoints(List<DateValuePairEt> points) {
        List<ProtocolBuffer.Point> protoPoints = new ArrayList<>();
        for (DateValuePairEt p : points) {
            ProtocolBuffer.Point protoPoint = ProtocolBuffer.Point.newBuilder()
                    .setDate(p.getDate().getTime())
                    .setValue(p.getValue())
                    .build();
            protoPoints.add(protoPoint);
        }
        return protoPoints;
    }

    /**
     * Converts the given protocol buffer points to our point class
     *
     * @param points - the protocol buffer points
     * @return a list with points
     */
    private static List<DateValuePairEt> fromProtocolBufferPoints(ProtocolBuffer.Points points) {
        List<DateValuePairEt> dtPoints = new ArrayList<>();
        for (ProtocolBuffer.Point point : points.getPointsList()) {
            dtPoints.add(convertToPoint(point));
        }
        return dtPoints;
    }

    /**
     * Converts the given protocol buffer point to point
     *
     * @param point - the protocol buffer point
     * @return the plain java point
     */
    private static DateValuePairEt convertToPoint(ProtocolBuffer.Point point) {
        Date dtDate = new Date(point.getDate());
        return new DateValuePairEt(point.getValue(), dtDate.toInstant());
    }

    private static class PointIterator implements Iterator<DateValuePairEt> {

        private int size;
        private int current = 0;
        private ProtocolBuffer.Points protcolBufferPoints = null;

        /**
         * Constructs a point iterator
         *
         * @param base64Encoded - the base64 encoded points
         * @throws IOException if an I/O error has occurred
         */
        private PointIterator(final String base64Encoded) throws IOException {
            byte[] decoded = Base64.decodeBase64(base64Encoded);

            protcolBufferPoints = ProtocolBuffer.Points.parseFrom(new GZIPInputStream(new ByteArrayInputStream(decoded)));
            size = protcolBufferPoints.getPointsCount();
        }

        /**
         * return true if the iterator points to more points
         */
        @Override
        public boolean hasNext() {
            return size > current;
        }

        /**
         * @return the next point
         */
        @Override
        public DateValuePairEt next() {
            DateValuePairEt p = convertToPoint(protcolBufferPoints.getPoints(current));
            current++;
            return p;
        }

        @Override
        public void remove() {
            //does not make sense
        }
    }
}
