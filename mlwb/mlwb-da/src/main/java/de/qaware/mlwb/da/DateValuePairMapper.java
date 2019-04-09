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

import de.qaware.mlwb.dt.solr.et.DateValuePairEt;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * Util class to convert list of pointEt to a String and vice versa
 * The String consists as a base64 encoded zip file of a JSON representation
 *
 * @author drexler.c
 */
public final class DateValuePairMapper {
    /**
     * Private constructor
     */
    private DateValuePairMapper() {
        //utility class
    }

    /**
     * Helper to encode and compress one time series.
     *
     * @param data the data.
     * @return a compressed string.
     */
    public static String compressAndEncodeData(List<DateValuePairEt> data) {
        if (data == null) {
            throw new IllegalArgumentException("Could not compress and encode points");
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        GZIPOutputStream gzos = null;
        try {
            gzos = new GZIPOutputStream(baos);
            writeValues(gzos, data);
            gzos.flush();
        }
        catch (IOException e) {
            throw new IllegalArgumentException("Could not compress and encode points", e);
        }
        finally {
            IOUtils.closeQuietly(gzos);
            IOUtils.closeQuietly(baos);
        }

        return Base64.encodeBase64String(baos.toByteArray());
    }

    private static void writeValues(GZIPOutputStream gzos, List<DateValuePairEt> data) throws IOException {
        gzos.write('[');
        String buffer = "";
        for (DateValuePairEt d : data) {
            buffer += "{\"v\":" + d.getValue() + ",\"d\":" + d.getEpochMilli() + "}";
            gzos.write(buffer.getBytes());
            buffer = ",";
        }
        gzos.write(']');
    }


    /**
     * Helper to encode and compress one time series.
     *
     * @param data the data.
     * @return a date value pair Entity list
     */
    public static List<DateValuePairEt> uncompressAndDecode(String data) {
        if (data == null) {
            throw new IllegalArgumentException("Could not compress and encode points");
        }
        List<DateValuePairEt> dtPoints = null;

        ByteArrayInputStream bais = null;
        GZIPInputStream gzis = null;

        try {
            bais = new ByteArrayInputStream(Base64.decodeBase64(data));
            gzis = new GZIPInputStream(bais);

            byte[] buffer = new byte[100];
            StringBuilder out = new StringBuilder();
            int length;
            while ((length = gzis.read(buffer, 0, 100)) != -1) {
                out.append(new String(buffer, 0, length));
            }

            dtPoints = parseValues(out.toString());
        }
        catch (IOException e) {
            throw new IllegalArgumentException("Could not decompress and decode points", e);
        }
        finally {
            IOUtils.closeQuietly(gzis);
            IOUtils.closeQuietly(bais);
        }

        return dtPoints;
    }

    private static List<DateValuePairEt> parseValues(String json) {
        List<DateValuePairEt> result = new ArrayList<>();
        DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols();
        otherSymbols.setDecimalSeparator('.');

        int len = "v\":".length();
        for (int i = 0; i < json.length(); i++) {
            int idx = json.indexOf("v\":", i);
            if (idx == -1) {
                break;
            }
            int valueIdx = idx + len;
            int endValueIdx = json.indexOf(',', valueIdx);
            int dateIdx = endValueIdx + 2 + len;
            int endDateIdx = json.indexOf('}', dateIdx);
            double value = Double.valueOf(json.substring(valueIdx, endValueIdx));
            long date = Long.valueOf(json.substring(dateIdx, endDateIdx));
            result.add(new DateValuePairEt(value, date));
            i = endDateIdx;
        }
        return result;
    }

    /**
     * Returns an iterator to the points from the given base 64 encoded data string
     *
     * @param base64Encoded - the base64 encoded points
     * @return an iterator to the points
     * @throws IOException if an I/O error has occurred
     */
    public static Iterator<DateValuePairEt> uncompressAndDecodeIterator(final String base64Encoded) throws IOException {
        return new DateValuePairMapper.PointIterator(uncompressAndDecode(base64Encoded));
    }


    static class PointIterator implements Iterator<DateValuePairEt> {

        private int size;
        private int current = 0;
        private final List<DateValuePairEt> uncompressAndDecodeData;

        /**
         * Constructs a point iterator
         *
         * @param uncompressAndDecodeData - the uncompressed and decoded data
         * @throws IOException if an I/O error has occurred
         */
        PointIterator(final List<DateValuePairEt> uncompressAndDecodeData) throws IOException {
            this.uncompressAndDecodeData = uncompressAndDecodeData;
            size = uncompressAndDecodeData.size();
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
            DateValuePairEt p = uncompressAndDecodeData.get(current);
            current++;
            return p;
        }

        @Override
        public void remove() {
            //does not make sense
        }
    }
}
