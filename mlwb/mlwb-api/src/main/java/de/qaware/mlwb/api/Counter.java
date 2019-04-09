//______________________________________________________________________________
//
//          Project:    Software EKG
//______________________________________________________________________________
//
//         Author:      QAware GmbH 2009 - 2014
//______________________________________________________________________________
//
// Notice: This piece of software was created, designed and implemented by
// experienced craftsmen and innovators in Munich, Germany.
// Changes should be done with respect to the original design.
//______________________________________________________________________________
package de.qaware.mlwb.api;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Represent a bean-compliant counter for a given host and all recorded valuePoints for that counter.
 *
 * @author Johannes Weigend
 * @author Rene Pospich
 * @author Fabian Huch
 */
public final class Counter implements Comparable<Counter>, Serializable {

    /**
     * The metric identifier.
     */
    private Metric metric;

    /**
     * The valuePoints.
     */
    private List<ValuePoint> valuePoints;

    /**
     * Creates a counter.
     *
     * @param metric the metric.
     */
    public Counter(Metric metric) {
        this.metric = metric;
        valuePoints = new ArrayList<>();
    }

    /**
     * No-arg constructor for bean compliance
     */
    public Counter(){
    }

    public Metric getMetric() {
        return metric;
    }

    public void setMetric(Metric metric) {
        this.metric = metric;
    }

    public List<ValuePoint> getValuePoints() {
        return valuePoints;
    }

    public void setValuePoints(List<ValuePoint> valuePoints) {
        this.valuePoints = valuePoints;
    }

    /**
     * Adds the valuePoint.
     *
     * @param valuePoint the valuePoint
     */
    public void addValue(ValuePoint valuePoint) {
        this.valuePoints.add(valuePoint);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Counter counter = (Counter) o;

        return new EqualsBuilder()
                .append(metric, counter.metric)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(metric)
                .toHashCode();
    }

    @Override
    public int compareTo(Counter o) {
        return metric.compareTo(o.getMetric());
    }
}
