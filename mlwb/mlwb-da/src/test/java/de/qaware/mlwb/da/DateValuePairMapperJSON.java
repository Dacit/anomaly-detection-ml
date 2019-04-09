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
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.JavaType;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
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
public class DateValuePairMapperJSON {
    /**
     * Private constructor
     */
    private DateValuePairMapperJSON() {
        //utility class
    }

    /**
     * Helper to encode and compress one time series.
     *
     * @param data the data.
     * @return a compressed string.
     * @throws IOException never happens.
     */
    public static String compressAndEncodeData(List<DateValuePairEt> data) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        GZIPOutputStream gzos = new GZIPOutputStream(baos);
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(gzos, data);
        gzos.flush();
        gzos.close();
        baos.close();
        return Base64.encodeBase64String(baos.toByteArray());
    }



    /**
     * Helper to encode and compress one time series.
     *
     * @param data       the data.
     * @throws IOException never happens.
     */
    public static List<DateValuePairEt> uncompressAndDecode(String data) throws IOException {
        List<DateValuePairEt> dtPoints = null;

        ByteArrayInputStream bais = null;
        GZIPInputStream gzis = null;

        ObjectMapper mapper = new ObjectMapper();
        JavaType type = mapper.getTypeFactory().constructCollectionType(List.class, DateValuePairEt.class);

        try {
            bais = new ByteArrayInputStream(Base64.decodeBase64(data));
            gzis = new GZIPInputStream(bais);

            dtPoints = mapper.readValue(gzis, type);
        } catch (Exception e) {
            throw new IllegalArgumentException("Could not decompress and decode points", e);
        } finally {
            IOUtils.closeQuietly(gzis);
            IOUtils.closeQuietly(bais);
        }

        return dtPoints;
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
}
