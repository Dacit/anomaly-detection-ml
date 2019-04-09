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

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

/**
 * Unit test for the {@link QueryMetricContext.Builder}.
 *
 * @author christian.fritz
 * @author Fabian Huch
 */
public class QueryMetricContextBuilderTest {
    public static final Instant START = Instant.now().minusSeconds(120);
    public static final Instant END = Instant.now();

    @Test
    public void testCopy() throws Exception {
        QueryMetricContext query = new QueryMetricContext.Builder()
                .withExclude("def")
                .withMetrics("abc", "abc1")
                .withExpertMode(true)
                .withSeries("abc")
                .withHost("abc")
                .withProcess("abc")
                .build();

        QueryMetricContext query1 = new QueryMetricContext.Builder(query)
                .withExclude(null)
                .build();

        assertThat(query1.getExcludeMetric(), is(equalTo("")));

        assertThat(query1.getMetrics(), is(equalTo(query.getMetrics())));
        assertThat(query1.isExpertMode(), is(equalTo(query.isExpertMode())));

        assertThat(query1.getSeries(), is(equalTo(query.getSeries())));
        assertThat(query1.getHost(), is(equalTo(query.getHost())));
        assertThat(query1.getProcess(), is(equalTo(query.getProcess())));
    }

    @Test
    public void testWithMetricsNull() throws Exception {
        QueryMetricContext context = new QueryMetricContext.Builder().withMetrics((String[]) null).build();
        assertThat(context.getMetrics(), hasSize(1));
        assertThat(context.getMetrics().iterator().next(), equalTo("*"));
    }

    @Test
    public void testWithMetrics() throws Exception {
        Set<String> metrics = new HashSet<>();
        metrics.add("abc");
        metrics.add("abc1");
        QueryMetricContext query = new QueryMetricContext.Builder().withMetrics(metrics).build();
        assertThat(query.getMetrics(), hasSize(2));
    }

    @Test
    public void testWithExclude() throws Exception {
        QueryMetricContext query = new QueryMetricContext.Builder().withExclude("abc").build();
        assertThat(query.getExcludeMetric(), is(equalTo("abc")));
    }

    @Test
    public void testWithIsExpertMode() throws Exception {
        QueryMetricContext query = new QueryMetricContext.Builder().withExpertMode(true).build();
        assertThat(query.isExpertMode(), is(true));
    }

    @Test
    public void testBuild() throws Exception {
        QueryContext.Builder builder = new QueryMetricContext.Builder();
        QueryContext build1 = builder.build();
        QueryContext build2 = builder.build();
        assertThat(build1, is(notNullValue()));
        assertThat(build2, is(notNullValue()));
        assertThat(build2, is(equalTo(build1)));
        assertThat(build2, is(not(sameInstance(build1))));
    }
}
