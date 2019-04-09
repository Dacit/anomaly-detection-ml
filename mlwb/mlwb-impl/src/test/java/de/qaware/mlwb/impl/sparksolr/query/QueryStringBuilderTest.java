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

import de.qaware.mlwb.api.QueryContext;
import de.qaware.mlwb.dt.solr.SolrType;
import org.junit.Test;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import static de.qaware.mlwb.impl.sparksolr.query.QueryStringBuilder.*;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.nullValue;

/**
 * Unit test for {@link QueryStringBuilder}
 *
 * @author christian.fritz
 * @author Fabian Huch
 */
public class QueryStringBuilderTest {
    /**
     * date for 30.12.2014
     */
    public static final Instant TIMESTAMP = Instant.ofEpochMilli(1419894000000L);

    @Test
    public void testBuildQueryString() throws Exception {
        QueryContext context = new QueryContext.Builder()
                .withSeries("swekg")
                .withHost("localhost")
                .withProcess("java")
                .build();

        assertThat(buildQueryString(context), is(equalTo("series:swekg AND host:localhost AND process:java")));
    }

    @Test
    public void testAppendNoValue() throws Exception {
        StringBuilder sb = new StringBuilder();
        append(sb, "field", "");
        append(sb, "field", "*");
        assertThat(sb.toString(), is(equalTo("")));
    }

    @Test
    public void testAppendNoExistingQuery() throws Exception {
        StringBuilder sb = new StringBuilder();
        append(sb, "field", "value");
        assertThat(sb.toString(), is(equalTo("field:value")));
    }

    @Test
    public void testAppendExistingQuery() throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append("abc");
        append(sb, "field", "value");
        assertThat(sb.toString(), is(equalTo("abc AND field:value")));
    }

    @Test
    public void testAppendMultipleValues() throws Exception {
        StringBuilder sb = new StringBuilder();
        append(sb, "field", Arrays.asList("value1", "value2"));
        assertThat(sb.toString(), is(equalTo("field:( value1 OR value2 )")));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testAppendMultipleNullValues() throws Exception {
        StringBuilder sb = new StringBuilder();
        append(sb, "field", (Collection) null);
        assertThat(sb.toString(), is(equalTo("")));
    }

    @Test
    public void testAppendMultipleEmptyValues() throws Exception {
        StringBuilder sb = new StringBuilder();
        append(sb, "field", new ArrayList<>());
        assertThat(sb.toString(), is(equalTo("")));
    }

    @Test
    public void testAppendMultipleValuesExistingQuery() throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append("abc");
        append(sb, "field", Arrays.asList("value1", "value2"));
        assertThat(sb.toString(), is(equalTo("abc AND field:( value1 OR value2 )")));
    }

    @Test
    public void testBuildRangeQuery() throws Exception {
        assertThat(buildRangeQuery("abc", "def"), is(equalTo("[abc TO def]")));
        assertThat(buildRangeQuery("abc", null), is(equalTo("[abc TO *]")));
        assertThat(buildRangeQuery(null, "def"), is(equalTo("[* TO def]")));
        assertThat(buildRangeQuery(null, null), is(nullValue()));
    }

    @Test
    public void testSingleFieldQuery() throws Exception {
        assertThat(singleFieldQuery("group", ""), is(equalTo("")));
        assertThat(singleFieldQuery("group", "*"), is(equalTo("")));
        assertThat(singleFieldQuery("group", null), is(equalTo("")));
        assertThat(singleFieldQuery("group", " abc "), is(equalTo("group: abc ")));
        assertThat(singleFieldQuery("group", " abc OR abcd"), is(equalTo("group:( abc OR abcd)")));
    }

    @Test
    public void testBuildDocumentTypeQueryString() throws Exception {
        assertThat(buildDocumentTypeQueryString((SolrType[]) null), is(equalTo("")));
        assertThat(QueryStringBuilder.buildDocumentTypeQueryString(SolrType.ALL), is(equalTo("type:*")));
        assertThat(QueryStringBuilder.buildDocumentTypeQueryString(SolrType.LOG), is(equalTo("type:LOG")));
        assertThat(buildDocumentTypeQueryString(SolrType.LOG, SolrType.RECORD), is(equalTo("type:LOG OR type:RECORD")));
    }

    @Test
    public void testEscape() throws Exception {
        String escaped = QueryStringBuilder.escape("type=jmx;/heap/usage/used");
        assertThat(escaped, is("type=jmx\\;\\/heap\\/usage\\/used"));
    }
}
