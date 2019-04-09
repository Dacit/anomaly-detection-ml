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

import de.qaware.mlwb.dt.solr.et.DateValuePairEt;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsCollectionContaining.hasItem;

/**
 * @author f.haaf
 *         <p>
 *         Initializes a List of Points
 */
class AbstractProtocolConverterTest {
    /**
     * A list with our data type points
     */
    List<DateValuePairEt> pointList;
    // List<ProtocolBuffer.Point> protoPoints;

    /**
     * Creates a bunch of points
     *
     * @throws Exception if something goes wrong
     */
    void initPoints(int size) throws Exception {
        pointList = new ArrayList<>();
        // protoPoints = new ArrayList<>();

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");

        for (int i = 1; i < size; i++) {
            Date date = sdf.parse(String.format("12-03-2014 11:3%s:00", i));
            addPoint(i, date);
            // addProtoPoint(i, date);
        }

    }

//    //in initPoints()verwendet
//    private void addProtoPoint(int i, Date date) {
//        protoPoints.add(ProtocolBuffer.Point.newBuilder().setDate(date.getTime()).setValue(i).build());
//    }

    //in initPoints()verwendet
    private void addPoint(int i, Date date) {
        pointList.add(new DateValuePairEt(i, date.toInstant()));
    }

    /**
     * Checks if every item from expected is contained in actual
     *
     * @param actual   - the value from the  call
     * @param expected - the expected result
     */
    void assertPointItems(List<DateValuePairEt> actual, List<DateValuePairEt> expected) {
        for (DateValuePairEt expectedPoint : expected) {
            assertThat(actual, hasItem(expectedPoint));
        }
    }

//    /**
//     * Checks if every item from expected is contained in actual
//     *
//     * @param actual   - the value from the  call
//     * @param expected - the expected result
//     */
//    void assertProtoPointItems(List<ProtocolBuffer.Point> actual, List<ProtocolBuffer.Point> expected) {
//        for (ProtocolBuffer.Point expectedPoint : expected) {
//            assertThat(actual, hasItem(expectedPoint));
//        }
//    }
}
