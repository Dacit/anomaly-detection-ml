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
package de.qaware.mlwb.api;

import org.junit.Test;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

/**
 * Unit test for the {@link QueryContext.Builder}.
 *
 * @author christian.fritz
 * @author Fabian Huch
 */
public class QueryContextBuilderTest {

    @Test
    public void testWithProcess() throws Exception {
        QueryContext query = new QueryContext.Builder().withProcess("abc").build();
        assertThat(query.getProcess(), is(equalTo("abc")));
    }

    @Test
    public void testWithRawQuery() throws Exception {
        QueryContext query = new QueryContext.Builder().withRawQuery("a Raw Query").build();
        assertThat(query.getRawQuery(), is(equalTo("a Raw Query")));
    }

    @Test
    public void testBuild() throws Exception {
        QueryContext.Builder builder = new QueryContext.Builder();
        QueryContext build1 = builder.build();
        QueryContext build2 = builder.build();
        assertThat(build1, is(notNullValue()));
        assertThat(build2, is(notNullValue()));
        assertThat(build2, is(equalTo(build1)));
        assertThat(build2, is(not(sameInstance(build1))));
    }
}
