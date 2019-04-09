package de.qaware.mlwb.featureextractor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Parses from Encoded Metric String to linearized metric object.
 *
 * @author Fabian Huch
 */
public class Parser implements Serializable {
    private static final String HOST_KEY = "host";
    private static final String PROCESS_KEY = "process";

    /**
     * Parses a metric string.
     *
     * @param metric the metric to parse in form 'metric [series,process,host]'
     * @return a linearized metric object
     */
    public static LinearizedMetric parseString(String metric) {
        try {
            // split at last space
            int splitIndex = metric.lastIndexOf(' ');

            // name with key-value-paris
            String property = metric.substring(0, splitIndex);

            String[] metricLocators = metric.substring(splitIndex, metric.length() - 1).split(",");

            List<Tuple> propertyPairs = new ArrayList<>();
            propertyPairs.add(new Tuple(PROCESS_KEY, metricLocators[1]));
            propertyPairs.add(new Tuple(HOST_KEY, metricLocators[2]));

            if (property.startsWith("MXBean(")) {
                int kvEndIndex = property.lastIndexOf(')');

                String[] kvPairs = property.substring(7, kvEndIndex).split(",");

                for (String pair : kvPairs) {
                    String[] splitPair = pair.split("=");

                    // Skip if value contains Process and/or Host
                    if (splitPair[0].equals("ServerRuntime") || splitPair[1].contains(metricLocators[2])) {
                        continue;
                    }

                    propertyPairs.add(new Tuple(splitPair[0], splitPair[1]));
                }

                property = property.substring(kvEndIndex + 2, property.length());
            }

            propertyPairs.sort((o1, o2) -> String.CASE_INSENSITIVE_ORDER.compare(o1.getArr()[0], o2.getArr()[0]));

            String[] keyTuple = new String[propertyPairs.size()];
            String[] valueTuple = new String[propertyPairs.size()];
            for (int i = 0; i < propertyPairs.size(); i++) {
                Tuple tuple = propertyPairs.get(i);
                keyTuple[i] = tuple.getArr()[0];
                valueTuple[i] = tuple.getArr()[1];
            }

            return new LinearizedMetric(property, keyTuple, valueTuple);
        } catch (IndexOutOfBoundsException e){
            throw new IllegalArgumentException(metric + " is not valid!", e);
        }
    }
}