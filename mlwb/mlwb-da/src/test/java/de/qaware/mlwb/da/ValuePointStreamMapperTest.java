package de.qaware.mlwb.da;

import com.google.common.collect.ImmutableList;
import com.google.common.io.LineReader;
import de.qaware.mlwb.api.AggregationType;
import de.qaware.mlwb.api.Granularity;
import de.qaware.mlwb.api.ValuePoint;
import de.qaware.mlwb.da.aggregation.strategy.AggregateStrategy;
import de.qaware.mlwb.da.aggregation.strategy.AggregateStrategyFactory;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.io.FileReader;
import java.sql.Timestamp;
import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

/**
 * Test for the {@link ValueStreamMapper}.
 *
 * @author Fabian Huch
 */
public class ValuePointStreamMapperTest {
    //Testdata for MXBean(java.lang:name=PS Perm Gen,type=MemoryPool).Usage.committed [_,proc1,lp06]
    private static String testString;

    private List<ValuePoint> testValuePoints = ImmutableList.of(
            new ValuePoint(2.85212672E8, Timestamp.valueOf("2015-01-03 00:00:00")),
            new ValuePoint(2.82591232E8, Timestamp.valueOf("2015-01-03 02:18:00")),
            new ValuePoint(2.80494080E8, Timestamp.valueOf("2015-01-03 10:01:00")),
            new ValuePoint(2.78921216E8, Timestamp.valueOf("2015-01-03 19:59:00")));

    @BeforeClass
    public static void setUpClass() throws Exception {
        File binDataFile = new File(ValueStreamMapper.class.getClassLoader().getResource("TestBinaryData").getFile());
        testString = new LineReader(new FileReader(binDataFile)).readLine();
    }

    @Test
    public void testToValueStreamLaziness() throws Exception {
        // Create and configure mocks
        AggregateStrategyFactory factory = mock(AggregateStrategyFactory.class);
        AggregateStrategy strategy = mock(AggregateStrategy.class);
        when(factory.getInstance(any())).thenReturn(strategy);
        when(strategy.aggregate(any(), any())).thenReturn(new ValuePoint(1, Timestamp.valueOf("2014-01-01 00:00:00")));

        ValueStreamMapper mapper = new ValueStreamMapper(factory);

        Stream<ValuePoint> lazyResult = mapper.toValueStream(testString, Granularity.MINUTE, AggregationType.AVG);
        // No interactions yet b/c of lazyness
        verifyZeroInteractions(strategy);

        lazyResult.findFirst();
        // Only one interaction b/c only one stream element is taken
        verify(strategy, times(1)).aggregate(any(), any());
        verifyNoMoreInteractions(strategy);
    }

    @Test
    public void testToValueStream() throws Exception {
        ValueStreamMapper mapper = new ValueStreamMapper(new AggregateStrategyFactory());

        List<ValuePoint> valuePoints = mapper.toValueStream(testString, Granularity.MINUTE, AggregationType.AVG)
                .collect(Collectors.toList());

        assertThat(valuePoints, hasSize(24 * 60));

        // Check ordering
        for (int i = 1; i < valuePoints.size(); i++) {
            assertThat(valuePoints.get(i - 1).getDate().before(valuePoints.get(i).getDate()), is(true));
            assertThat(Duration.between(valuePoints.get(i - 1).getDate().toInstant(), valuePoints.get(i).getDate().toInstant()),
                    equalTo(Duration.ofMinutes(1)));
        }

        // Check result valuePoints
        List<ValuePoint> result = mapper.toValueStream(testString, Granularity.MINUTE, AggregationType.AVG)
                .collect(Collectors.groupingBy(ValuePoint::getValue, Collectors.reducing((o, o2) -> o.compareTo(o2) < 0 ? o : o2)))
                .entrySet().stream()
                .map(e -> e.getValue().orElseThrow(() -> new RuntimeException("has to be present")))
                .collect(Collectors.toList());
        assertThat(testValuePoints.containsAll(result) && result.containsAll(testValuePoints), is(true));
    }
}