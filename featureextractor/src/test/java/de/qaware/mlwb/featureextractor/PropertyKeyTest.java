package de.qaware.mlwb.featureextractor;

import de.qaware.mlwb.featureextractor.PropertyKey;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.Test;

/**
 * Test for the {@link PropertyKey}.
 *
 * @author Fabian Huch
 */
public class PropertyKeyTest {
    @Test
    public void testEquals() throws Exception {
        EqualsVerifier.forClass(PropertyKey.class).suppress(Warning.NONFINAL_FIELDS).verify();
    }
}