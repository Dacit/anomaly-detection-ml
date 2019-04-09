package de.qaware.mlwb.featureextractor;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.Test;

/**
 * Test for the {@link LinearizedMetric}.
 *
 * @author Fabian Huch
 */
public class LinearizedMetricTest {
    @Test
    public void testEquals() throws Exception {
        EqualsVerifier.forClass(LinearizedMetric.class).suppress(Warning.NONFINAL_FIELDS).verify();
    }

}