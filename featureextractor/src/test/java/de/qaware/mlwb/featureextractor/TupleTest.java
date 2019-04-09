package de.qaware.mlwb.featureextractor;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.Test;

/**
 * Test for the {@link TupleTest}.
 *
 * @author Fabian Huch
 */
public class TupleTest {
    @Test
    public void testEquals() throws Exception {
        EqualsVerifier.forClass(Tuple.class).suppress(Warning.NONFINAL_FIELDS).verify();
    }

}