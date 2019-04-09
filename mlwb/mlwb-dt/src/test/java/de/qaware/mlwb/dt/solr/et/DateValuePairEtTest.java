package de.qaware.mlwb.dt.solr.et;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.Test;

/**
 * Test for the {@link DateValuePairEt}.
 *
 * @author Fabian Huch
 */
public class DateValuePairEtTest {
    @Test
    public void testEquals() throws Exception {
        EqualsVerifier.forClass(DateValuePairEt.class)
                .suppress(Warning.NONFINAL_FIELDS, Warning.STRICT_INHERITANCE)
                .verify();
    }
}
