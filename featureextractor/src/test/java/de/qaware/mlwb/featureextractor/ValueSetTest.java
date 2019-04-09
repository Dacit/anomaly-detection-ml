package de.qaware.mlwb.featureextractor;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.Test;

/**
 * Test for the {@link ValueSet}.
 *
 * @author Fabian Huch
 */
public class ValueSetTest {
    @Test
    public void testEquals() throws Exception {
        EqualsVerifier.forClass(ValueSet.class).suppress(Warning.NONFINAL_FIELDS).verify();
    }
}