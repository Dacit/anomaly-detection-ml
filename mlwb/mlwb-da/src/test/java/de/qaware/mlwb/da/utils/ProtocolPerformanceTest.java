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
package de.qaware.mlwb.da.utils;

import de.qaware.mlwb.da.*;
import de.qaware.mlwb.da.DateValuePairMapper;
import de.qaware.mlwb.dt.solr.et.DateValuePairEt;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * @author f.haaf
 *         <p>
 *         Auswertung siehe Excel-Datei:    file:///C:\Users\f.haaf\Documents\ProtocollBufferTestDaten\vergleich_auswertung.xlsx
 *         <p>
 *         Direkt in Java berechnen?
 *         werteListe.stream().mapToInt(i -> i).average().orElse(0);
 *         int ret = werteListe.stream().mapToInt(Person::getAge).average.getAsInt();
 */
public class ProtocolPerformanceTest extends AbstractProtocolConverterTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProtocolPerformanceTest.class);
    private ArrayList<Long> timeList;
    private ArrayList<Long> timeList2;
    long timeStart;
    long timeStop;

    @Before
    public void beforeEachTest() throws Exception {
        initPoints(10000);
    }

    @Test
    public void testAsRegularOutputStream() throws Exception {
        timeList = new ArrayList<>();
        timeList2 = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            timeStart = System.currentTimeMillis();
            String out = DateValuePairMapperObject.compressAndEncodeData(pointList);
            timeStop = System.currentTimeMillis();
            timeList.add(timeStop - timeStart);
            timeStart = System.currentTimeMillis();
            int erg = DateValuePairMapperObject.uncompressAndDecode(out).size();
            timeStop = System.currentTimeMillis();
            timeList2.add(timeStop - timeStart);
            assertThat(erg, is(pointList.size()));
        }
        LOGGER.info("Größe des regulären OutputStreams             : " + DateValuePairMapperObject.compressAndEncodeData(pointList).length());
        LOGGER.info("Durchschnittliche Zeit regulärer OutputStream : " + averageTime(timeList));
        LOGGER.info("Durchschnittliche Zeit regulärer InputStream  : " + averageTime(timeList2));
        LOGGER.info("Median des regulären OutputStreams            : " + median(timeList));
        LOGGER.info("Median des regulären IntputStreams            : " + median(timeList2));
    }

    @Test
    public void testMapperData() throws Exception {
        timeList = new ArrayList<>();
        timeList2 = new ArrayList<>();

        for (int i = 0; i < 100; i++) {
            timeStart = System.currentTimeMillis();
            String out = DateValuePairMapperData.compressAndEncodeData(pointList);
            timeStop = System.currentTimeMillis();
            timeList.add(timeStop - timeStart);

            timeStart = System.currentTimeMillis();
            int erg = DateValuePairMapperData.uncompressAndDecode(out).size();
            timeStop = System.currentTimeMillis();
            timeList2.add(timeStop - timeStart);
            assertThat(erg, is(pointList.size()));
        }
        LOGGER.info("Größe des Data OutputStreams              : " + DateValuePairMapperData.compressAndEncodeData(pointList).length());
        LOGGER.info("Durchschnittliche Zeit Data OutputStreams : " + averageTime(timeList));
        LOGGER.info("Durchschnittliche Zeit Data InputStreams  : " + averageTime(timeList2));
        LOGGER.info("Median des Data OutputStreams             : " + median(timeList));
        LOGGER.info("Median des Data InputStreams              : " + median(timeList2));
    }


    @Test
    public void testWithProtocollBuffer() throws Exception {
        timeList = new ArrayList<>();
        timeList2 = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            timeStart = System.currentTimeMillis();
            String out = DateValuePairMapperBuffer.compressAndEncodeData(pointList);
            timeStop = System.currentTimeMillis();
            timeList.add(timeStop - timeStart);
            timeStart = System.currentTimeMillis();
            int erg = DateValuePairMapperBuffer.uncompressAndDecode(out).size();
            timeStop = System.currentTimeMillis();
            timeList2.add(timeStop - timeStart);
            assertThat(erg, is(pointList.size()));
        }
        LOGGER.info("Größe des ProtocolBuffer OutputStreams    : " + DateValuePairMapperBuffer.compressAndEncodeData(pointList).length());
        LOGGER.info("Durchschnittliche Zeit ProtocolBuffer Out : " + averageTime(timeList));
        LOGGER.info("Durchschnittliche Zeit ProtocolBuffer in  : " + averageTime(timeList2));

        LOGGER.info("Median des ProtocolBuffers Out            : " + median(timeList));
        LOGGER.info("Median des ProtocolBuffers In             : " + median(timeList2));
    }

    /**
     * The Use of Jackson:
     * Need:    ObjectMapper mapper = new ObjectMapper();   --> com.fasterxml.jackson.databind.ObjectMapper
     * mapper.writeValue(new File("name.json", name);
     */
    @Test
    public void testWithJSON() throws Exception {
        timeList = new ArrayList<>();
        timeList2 = new ArrayList<>();

//        ObjectMapper mapper = new ObjectMapper();
//        String prettyStaff1 = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(pointList);
//        System.out.println(prettyStaff1);

//        printJSONString2();

        for (int i = 0; i < 100; i++) {
            timeStart = System.currentTimeMillis();
            String out = DateValuePairMapperJSON.compressAndEncodeData(pointList);
            timeStop = System.currentTimeMillis();
            timeList.add(timeStop - timeStart);
            timeStart = System.currentTimeMillis();
            List<DateValuePairEt> ergData = DateValuePairMapperJSON.uncompressAndDecode(out);
            int erg = ergData.size();
            timeStop = System.currentTimeMillis();
            timeList2.add(timeStop - timeStart);
            assertThat(erg, is(pointList.size()));
        }
        LOGGER.info("Größe des JSON  OutputStreams   : " + DateValuePairMapperJSON.compressAndEncodeData(pointList).length());
        LOGGER.info("Durchschnittliche Zeit JSON Out : " + averageTime(timeList));
        LOGGER.info("Durchschnittliche Zeit JSON In  : " + averageTime(timeList2));
        LOGGER.info("Median des JSON Streams Out     : " + median(timeList));
        LOGGER.info("Median des JSON Streams In      : " + median(timeList2));
    }

    private void printJSONString2() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(baos, pointList);
        baos.flush();
        baos.close();
        System.out.println(new String(baos.toByteArray()));
    }

    @Test
    public void testWithJSONString() throws Exception {
        timeList = new ArrayList<>();
        timeList2 = new ArrayList<>();

//        ObjectMapper mapper = new ObjectMapper();
//        String prettyStaff1 = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(pointList);
//        System.out.println(prettyStaff1);

//        printJSONString();

        for (int i = 0; i < 100; i++) {
            timeStart = System.currentTimeMillis();
            String out = DateValuePairMapper.compressAndEncodeData(pointList);
            timeStop = System.currentTimeMillis();
            timeList.add(timeStop - timeStart);
            timeStart = System.currentTimeMillis();
            int erg = DateValuePairMapper.uncompressAndDecode(out).size();
            timeStop = System.currentTimeMillis();
            timeList2.add(timeStop - timeStart);
            assertThat(erg, is(pointList.size()));
        }
        LOGGER.info("Größe des JSON Simple OutputStreams   : " + DateValuePairMapper.compressAndEncodeData(pointList).length());
        LOGGER.info("Durchschnittliche Zeit JSON Simple Out : " + averageTime(timeList));
        LOGGER.info("Durchschnittliche Zeit JSON Simple In  : " + averageTime(timeList2));
        LOGGER.info("Median des JSON Simple Streams Out     : " + median(timeList));
        LOGGER.info("Median des JSON Simple Streams In      : " + median(timeList2));
    }

    private void printJSONString() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        baos.write('[');
        for (DateValuePairEt d : pointList) {
            StringBuffer buffer = new StringBuffer(100);
            buffer.append("{\"v\":").append(d.getValue()).append(",\"d\":").append(d.getEpochMilli()).append("},");
            baos.write(buffer.toString().getBytes());
        }
        baos.write(']');
        baos.flush();
        baos.close();
        System.out.println(new String(baos.toByteArray()));
    }

    /**
     * Calculates the average time.
     *
     * @param timeList the list with the time parameters for calculating the average
     * @return the average time
     */
    public double averageTime(ArrayList<Long> timeList) {
        long time = 0;

        for (int i = 0; i < timeList.size(); i++) {
            time = time + timeList.get(i);
        }
        return ((double) time / (double) timeList.size());

    }

    /**
     * Calculates the median of a comparable list.
     *
     * @param values The list where the median is calculated from
     * @return the median of a comparable list
     */
    public <T extends Comparable<T>> T median(List<T> values) {
        Collections.sort(values);
        return values.get(values.size() / 2);
    }
}
