//______________________________________________________________________________
//
//                  Project:    Software EKG
//______________________________________________________________________________
//
//                   Author:    QAware GmbH 2009 - 2017
//______________________________________________________________________________
//
// Notice: This piece of software was created, designed and implemented by
// experienced craftsmen and innovators in Munich, Germany.
// Changes should be done with respect to the original design.
//______________________________________________________________________________
package de.qaware.mlwb.impl.sparksolr.utils;

import org.junit.Test;

import static de.qaware.mlwb.impl.sparksolr.utils.ObjectUtils.mapOrDefaultIfNull;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

/**
 * Unit test for {@link ObjectUtils}.
 *
 * @author christian.fritz
 */
public class ObjectUtilsTest {

    @Test
    public void testMapOrDefaultIfNull() throws Exception {
        assertThat(mapOrDefaultIfNull(null, null, Object::toString), is(nullValue()));
        assertThat(mapOrDefaultIfNull(null, "", Object::toString), is(equalTo("")));
        assertThat(mapOrDefaultIfNull(null, "abc", Object::toString), is(equalTo("abc")));
        assertThat(mapOrDefaultIfNull(10, "abc", Object::toString), is(equalTo("10")));
    }

    @Test(expected = NullPointerException.class)
    public void testMapOrDefaultIfNullMappingNull() throws Exception {
        mapOrDefaultIfNull(null, null, null);
    }
}
