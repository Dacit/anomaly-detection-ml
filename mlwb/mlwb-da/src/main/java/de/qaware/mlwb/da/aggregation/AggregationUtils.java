package de.qaware.mlwb.da.aggregation;

import de.qaware.mlwb.api.Granularity;

import java.sql.Timestamp;

/**
 * Utils class for value aggregation. Most code taken from ekg AbstractAggregator (by Christian Fritz)
 *
 * @author Fabian Huch
 */
public class AggregationUtils {
    private AggregationUtils() {
    }

    /**
     * Checks if the group of two timestamps are adjacent to each other.
     *
     * @param d1          timestamp one
     * @param d2          timestamp two
     * @param granularity the granularity to use
     * @return true if their groups are adjacent
     */
    public static boolean groupAdjacent(Timestamp d1, Timestamp d2, Granularity granularity) {
        long div;
        switch (granularity) {
            case HOUR:
                div = 3600000;
                break;
            case MINUTE:
                div = 60000;
                break;
            default:
                return true;
        }

        return Math.abs((d1.getTime() / div) - (d2.getTime() / div)) == 1;
    }

    /**
     * Normalize the date time value using the granularity.
     *
     * @param d           The not normalized date time value.
     * @param granularity the granularity to use
     * @return The normalized date time value.
     */
    public static Timestamp normalizeDateTime(Timestamp d, Granularity granularity) {
        long div;
        switch (granularity) {
            case HOUR:
                div = 3600000;
                break;
            case MINUTE:
                div = 60000;
                break;
            default:
                return d;
        }

        return new Timestamp((d.getTime() / div) * div);
        //Instant dI = d.toInstant();
        //return Timestamp.from(Instant.ofEpochMilli((dI.toEpochMilli() / div) * div));
    }

    /**
     * Check if d1 and d2 with in the same group defined by granularity.
     *
     * @param d1          timestamp one
     * @param d2          timestamp two
     * @param granularity the granularity to use
     * @return True if d1 and d2 are in the same granularity.
     */
    public static boolean withinSameGroup(Timestamp d1, Timestamp d2, Granularity granularity) {
        return normalizeDateTime(d1, granularity).equals(normalizeDateTime(d2, granularity));
    }
}
