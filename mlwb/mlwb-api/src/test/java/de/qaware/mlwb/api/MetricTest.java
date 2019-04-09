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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * Unit test for the {@link Metric}.
 *
 * @author Fabian Reißmann
 * @author Maximilian Hornung
 * @author Fabian Huch
 */
public class MetricTest {
    @Test
    public void testCompareTo() {
        Metric metricA = new Metric("A_NAME", "host", "proc");
        Metric metricB = new Metric("B_NAME", "host", "proc");
        int compare = metricA.compareTo(metricB);

        assertThat(compare, is(equalTo(-1)));
    }

    @Test
    public void testEqual() throws Exception {
        EqualsVerifier.forClass(Metric.class)
                .suppress(Warning.NONFINAL_FIELDS, Warning.STRICT_INHERITANCE)
                .verify();
    }
}