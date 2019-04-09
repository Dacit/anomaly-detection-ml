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

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.sql.Timestamp;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

/**
 * Test for the {@link QueryMetricContext}.
 *
 * @author Fabian Huch
 */
public class QueryMetricContextTest {

    @Mock
    private QueryContext mock;
    @Mock
    private String exclude;
    private Set<String> metrics;
    @Mock
    private String series;
    @Mock
    private String host;
    @Mock
    private String process;

    private QueryMetricContext metricContext;

    @Before
    public void setUp() throws Exception {
        mock = new QueryContext.Builder()
                .withSeries(series)
                .withHost(host)
                .withProcess(process)
                .build();
        metrics = new HashSet<>();
        metrics.add("A existing name");
        exclude = "Exclude";
        metricContext = new QueryMetricContext.Builder(mock)
                .withExclude(exclude)
                .withMetrics(metrics)
                .withExpertMode(false)
                .build();
    }

    @Test
    public void testGetExcludeMetricNameCaseNormal() throws Exception {
        assertThat(metricContext.getExcludeMetric(), is(equalTo(exclude)));
    }

    @Test
    public void testConstructorCaseMetricsEmpty() throws Exception {
        metricContext = new QueryMetricContext.Builder(mock)
                .withExclude(exclude)
                .withMetrics(Collections.emptySet())
                .withExpertMode(false)
                .build();
        //ATTENZIONE! DEFAULT SIGN IS STAR
        assertThat(metricContext.getMetrics(), hasSize(1));
        assertThat(metricContext.getMetrics().iterator().next(), is(equalTo("*")));
    }

    @Test
    public void testConstructorCaseExcludeNull() throws Exception {
        metricContext = new QueryMetricContext.Builder(mock)
                .withExclude(null)
                .withMetrics(metrics)
                .withExpertMode(false)
                .build();
        //ATTENZIONE! DEFAULT SIGN IS EMPTY
        assertThat(metricContext.getExcludeMetric(), is(equalTo("")));
    }

    @Test
    public void testGetExcludeMetric() throws Exception {
        assertThat(metricContext.getExcludeMetric(), is(equalTo(exclude)));
    }

    @Test
    public void testIsExpertModeCaseTrue() throws Exception {
        metricContext = new QueryMetricContext.Builder(mock)
                .withExclude(exclude)
                .withMetrics(metrics)
                .withExpertMode(true)
                .build();
        assertTrue(metricContext.isExpertMode());
    }

    @Test
    public void testIsExpertModeCaseFalse() throws Exception {
        //false by default
        assertFalse(metricContext.isExpertMode());
    }

    @Test
    public void testGetMetricNameCaseNormal() throws Exception {
        assertThat(metricContext.getMetrics(), containsInAnyOrder(metrics.toArray(new String[metrics.size()])));
    }

    @Test
    public void testGetMetricsCaseNull() throws Exception {
        metricContext = new QueryMetricContext.Builder(mock)
                .withMetrics((String[]) null)
                .withExclude(exclude)
                .withExpertMode(false)
                .build();

        //expect star sign
        String metricName = metricContext.getMetrics().iterator().next();
        assertThat(metricName, is(equalTo("*")));
    }

    @Test
    public void testGetMetricNamesCaseFilled() throws Exception {
        metrics = new HashSet<>();
        metrics.add("first");
        metrics.add("second");
        metricContext = new QueryMetricContext.Builder(mock)
                .withExclude(exclude)
                .withMetrics(metrics)
                .withExpertMode(false)
                .build();
        List<String> expected = metrics.stream().collect(Collectors.toList());

        List<String> notEmpty = metricContext.getMetrics().stream().collect(Collectors.toList());

        assertThat(notEmpty, is(equalTo(expected)));
    }

    @Test
    public void testGetMetrics() throws Exception {
        assertThat(metricContext.getMetrics(), is(equalTo(metrics)));
    }

    @Test
    public void testGetLimit() throws Exception {
        //1000 is the maximum allowed number of rows of a solr query
        assertThat(metricContext.getLimit(), is(equalTo(1000)));
    }

    @Test
    public void testEquals() throws Exception {
        EqualsVerifier.forClass(QueryMetricContext.class)
                .withPrefabValues(Timestamp.class, Timestamp.valueOf("2014-01-01 00:00:00"), Timestamp.valueOf("2015-12-12 12:12:12"))
                .suppress(Warning.NULL_FIELDS, Warning.NONFINAL_FIELDS)
                .withRedefinedSuperclass()
                .verify();
    }
}
