//______________________________________________________________________________
//
//          Project:    Software EKG
//______________________________________________________________________________
//
//         Author:      QAware GmbH 2009 - 2014
//______________________________________________________________________________
//
// Notice: This piece of software was created, designed and implemented by
// experienced craftsmen and innovators in Munich, Germany.
// Changes should be done with respect to the original design.
//______________________________________________________________________________
package de.qaware.mlwb.api;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.Test;

import java.sql.Timestamp;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

/**
 * Unit test for the {@link ValuePoint}.
 *
 * @author Fabian Reißmann
 * @author Maximilian Hornung
 * @author christian.fritz
 */
public class ValuePointTest {

    /**
     * Method to return always the same Date.
     *
     * @return a date for 30.12.2014
     */
    static Timestamp makeDate() {
        return Timestamp.valueOf("2014-12-30 00:00:00");
    }

    @Test
    public void testImplementingOfComparableValue() {
        ValuePoint valuePoint = new ValuePoint(5, makeDate());
        assertThat(valuePoint, is(instanceOf(Comparable.class)));
    }

    @Test
    public void testCopyConstructor() {
        Timestamp inputDate = makeDate();
        double inputDouble = 1.337;

        ValuePoint firstValuePoint = new ValuePoint(inputDouble, inputDate);
        ValuePoint secondValuePoint = new ValuePoint(firstValuePoint);

        assertThat(secondValuePoint.getDate(), is(equalTo(inputDate)));
        assertThat(secondValuePoint.getValue(), is(equalTo(inputDouble)));
    }

    @Test
    public void testGetValue() {
        ValuePoint valuePoint = new ValuePoint(1.337, makeDate());
        assertThat(valuePoint.getValue(), is(equalTo(1.337)));
    }

    @Test
    public void testGetDate() {
        Timestamp inputDate = makeDate();
        ValuePoint valuePoint = new ValuePoint(1.337, inputDate);
        assertThat(valuePoint.getDate(), is(equalTo(inputDate)));
    }

    @Test
    public void testCompareTo() {
        ValuePoint valuePointOne = new ValuePoint(42.0, makeDate());

        Timestamp laterDate = makeDate();
        laterDate.setNanos(makeDate().getNanos() + 1);
        ValuePoint valuePointTwo = new ValuePoint(42.0, laterDate);

        assertThat(valuePointOne.compareTo(valuePointTwo), is(equalTo(-1)));
    }

    @Test
    public void testEqualsHashCode() {
        EqualsVerifier.forClass(ValuePoint.class)
                .withPrefabValues(Timestamp.class, Timestamp.valueOf("2014-01-10 00:00:00"), Timestamp.valueOf("2014-02-03 00:00:00"))
                .suppress(Warning.NONFINAL_FIELDS, Warning.STRICT_INHERITANCE)
                .verify();
    }
}