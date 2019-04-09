package de.qaware.mlwb.impl.sparksolr.utils;

import java.sql.Timestamp;

/**
 * Date util class.
 *
 * @author Fabian Huch
 */
public class DateUtils {
    /**
     * Checks if a timestamp is inside a given interval.
     *
     * @param timestamp the timestamp.
     * @param begin     the begin - if null only the end will be checked
     * @param end       @return
     */
    public static boolean insideInterval(Timestamp timestamp, Timestamp begin, Timestamp end) {
        return !outsideInterval(timestamp, begin, end);
    }

    /**
     * Checks if a timestamp is outside a given interval.
     *
     * @param timestamp the timestamp.
     * @param begin     the begin - if null only the end will be checked
     * @param end       @return
     */
    private static boolean outsideInterval(Timestamp timestamp, Timestamp begin, Timestamp end) {
        if (begin == null) {
            return end != null && timestamp.after(end);
        }
        else {
            if (end == null) {
                return timestamp.before(begin);
            }
            else {
                return timestamp.before(begin) || timestamp.after(end);
            }
        }
    }
}
