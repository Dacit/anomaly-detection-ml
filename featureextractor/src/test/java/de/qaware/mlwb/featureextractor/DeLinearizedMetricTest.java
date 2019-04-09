package de.qaware.mlwb.featureextractor;

import de.qaware.mlwb.featureextractor.DeLinearizedMetric;
import de.qaware.mlwb.featureextractor.Tuple;
import de.qaware.mlwb.featureextractor.ValueSet;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Test for the {@link DeLinearizedMetric}.
 *
 * @author Fabian Huch
 */
public class DeLinearizedMetricTest {
    @Test
    public void testEquals() throws Exception {
        EqualsVerifier.forClass(DeLinearizedMetric.class).suppress(Warning.NONFINAL_FIELDS).verify();
    }

    @Test
    public void testToString () throws Exception{
        DeLinearizedMetric metric = new DeLinearizedMetric(new ValueSet(
                new Tuple("key1", "key2"),
                new Tuple[]{new Tuple("val1.1", "val2.1"), new Tuple("val2.1", "val2.2")}),
                new String[]{"name1", "name2"});

        assertThat(metric.toString(), equalTo("([name1, name2], ([key1, key2], [[val1.1, val2.1], [val2.1, val2.2]]))"));
    }
}