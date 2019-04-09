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
package de.qaware.mlwb.impl.sparksolr.query;

import de.qaware.mlwb.api.QueryMetricContext;
import org.junit.Test;

import static de.qaware.mlwb.impl.sparksolr.query.MetricQueryStringBuilder.buildQueryString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * Unit test for the {@link MetricQueryStringBuilder}.
 *
 * @author christian.fritz
 * @author Fabian Huch
 */
public class MetricQueryStringBuilderTest {

    private final QueryMetricContext builder = new QueryMetricContext.Builder()
            .withMetrics("metric:1")
            .withExclude("metric2")
            .withSeries("series")
            .withHost("localhost")
            .build();

    @Test
    public void testBuildQueryString() throws Exception {
        assertThat(buildQueryString(builder),
                is(equalTo("series:series AND host:localhost AND metric:metric\\:1 !metric:metric2")));

        assertThat(buildQueryString(new QueryMetricContext.Builder(builder).withExpertMode(true).build()),
                is(equalTo("")));
    }
}
