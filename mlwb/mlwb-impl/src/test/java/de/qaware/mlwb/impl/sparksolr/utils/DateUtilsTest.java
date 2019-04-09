package de.qaware.mlwb.impl.sparksolr.utils;

import de.qaware.mlwb.da.ValueStreamMapper;
import org.junit.Test;

import java.sql.Timestamp;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Test for the {@link DateUtils}.
 *
 * @author Fabian Huch
 */
public class DateUtilsTest {
    @Test
    public void testInsideInterval() throws Exception {
        Timestamp start = Timestamp.valueOf("2014-01-01 00:00:00");
        Timestamp end = Timestamp.valueOf("2014-01-02 00:00:00");

        Timestamp before = Timestamp.valueOf("2013-12-31 23:59:59");
        Timestamp inside1 = Timestamp.valueOf("2014-01-01 00:00:00");
        Timestamp inside2 = Timestamp.valueOf("2014-01-01 14:53:17");
        Timestamp inside3= Timestamp.valueOf("2014-01-02 00:00:00");
        Timestamp after = Timestamp.valueOf("2014-01-02 00:00:01");

        assertThat(DateUtils.insideInterval(before, start, end), is(false));
        assertThat(DateUtils.insideInterval(inside1, start, end), is(true));
        assertThat(DateUtils.insideInterval(inside2, start, end), is(true));
        assertThat(DateUtils.insideInterval(inside3, start, end), is(true));
        assertThat(DateUtils.insideInterval(after, start, end), is(false));
        assertThat(DateUtils.insideInterval(start, start, start), is(true));
    }

}