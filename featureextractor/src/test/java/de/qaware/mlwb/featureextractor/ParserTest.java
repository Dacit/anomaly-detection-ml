package de.qaware.mlwb.featureextractor;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Test for the {@link Parser}.
 *
 * @author Fabian Huch
 */
@RunWith(Parameterized.class)
public class ParserTest {
    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {"MXBean(com.bea:Name=datasource07,ServerRuntime=i0_lp07,Type=JDBCConnectionPoolRuntime).WaitSecondsHighCount [series,proc1,lp07]",
                        new LinearizedMetric("WaitSecondsHighCount", new String[]{"com.bea:Name", "host", "process", "Type"}, new String[]{"datasource07", "lp07", "proc1", "JDBCConnectionPoolRuntime"})},
                {"MXBean(com.bea:Name=i0_lp08,Type=Server).StuckThreadTimerInterval [series,proc1,lp08]",
                        new LinearizedMetric("StuckThreadTimerInterval", new String[]{"host", "process", "Type"}, new String[]{"lp08", "proc1", "Server"})},
                {"\\Process(java#1)\\RES [series,proc2,lp14]",
                        new LinearizedMetric("\\Process(java#1)\\RES", new String[]{"host", "process"}, new String[]{"lp14", "proc2"})},
                {"MXBean(java.lang:name=PS Eden Space,type=MemoryPool).CollectionUsage.used [ekgdata,proc2,lp09]",
                        new LinearizedMetric("CollectionUsage.used", new String[]{"host", "java.lang:name", "process", "type"}, new String[]{"lp09", "PS Eden Space", "proc2", "MemoryPool"})}
        });
    }

    private String name;
    private LinearizedMetric metric;

    public ParserTest(String name, LinearizedMetric metric) {
        this.name = name;
        this.metric = metric;
    }

    @Test
    public void testParse() {
        assertThat(Parser.parseString(name), equalTo(metric));
    }
}