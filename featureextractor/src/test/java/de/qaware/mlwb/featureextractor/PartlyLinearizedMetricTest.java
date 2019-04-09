package de.qaware.mlwb.featureextractor;

import de.qaware.mlwb.featureextractor.PartlyLinearizedMetric;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.Test;

/**
 * Test for the {@link PartlyLinearizedMetric}.
 *
 * @author Fabian Huch
 */
public class PartlyLinearizedMetricTest {
    @Test
    public void testEquals() throws Exception {
        EqualsVerifier.forClass(PartlyLinearizedMetric.class).suppress(Warning.NONFINAL_FIELDS).verify();
    }

}