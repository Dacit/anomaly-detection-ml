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
import java.time.Instant;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

/**
 * Unit test for the {@link Counter}.
 *
 * @author Fabian Reißmann
 * @author Fabian Huch
 */
public class CounterTest {
    @Test
    public void testGetValuesWithoutAddingValuesBefore() {
        Counter counter = new Counter(new Metric("This_is_my_name", "This_is_my_host", "This_is_my_proc"));
        List<ValuePoint> valuePoints = counter.getValuePoints();

        assertThat(valuePoints.size(), is(equalTo(0)));
    }

    @Test
    public void testGetValuesWithOneAddedValue() {
        Counter counter = new Counter(new Metric("This_is_my_name", "This_is_my_host", "This_is_my_proc"));
        ValuePoint valuePointToAdd = new ValuePoint(1.0, Timestamp.from(Instant.now()));
        counter.addValue(valuePointToAdd);
        List<ValuePoint> valuePoints = counter.getValuePoints();

        assertThat(valuePoints.size(), is(equalTo(1)));

        ValuePoint firstValuePoint = valuePoints.get(0);
        assertThat(valuePointToAdd, is(equalTo(firstValuePoint)));
    }

    @Test
    public void testEqual() throws Exception {
        EqualsVerifier.forClass(Counter.class)
                .suppress(Warning.NONFINAL_FIELDS)
                .withIgnoredFields("valuePoints")
                .withPrefabValues(Timestamp.class, Timestamp.valueOf("2014-01-10 00:00:00"), Timestamp.valueOf("2014-02-03 00:00:00"))
                .verify();
    }
}
