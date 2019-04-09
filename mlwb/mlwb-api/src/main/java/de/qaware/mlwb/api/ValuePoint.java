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
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * A bean-compliant value at a specific date time.
 *
 * @author Johannes Weigend
 * @author Rene Pospich
 * @author Fabian Huch
 */
public final class ValuePoint implements Comparable<ValuePoint>, Serializable {
    private double value;
    private Timestamp date;

    /**
     * Creates a counter.
     *
     * @param value the value.
     * @param date  the record date.
     */
    public ValuePoint(double value, Timestamp date) {
        this.value = value;
        this.date = date;
    }

    /**
     * No-args constructor for bean compliance.
     */
    public ValuePoint(){
    }

    /**
     * Copy.
     *
     * @param v value to copy
     */
    public ValuePoint(ValuePoint v) {
        this.value = v.value;
        this.date = v.date;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date){
        this.date = date;
    }

    @Override
    public int compareTo(ValuePoint v) {
        return date.compareTo(v.date);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ValuePoint valuePoint1 = (ValuePoint) o;

        return new EqualsBuilder()
                .append(value, valuePoint1.value)
                .append(date, valuePoint1.date)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(value)
                .append(date)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("value", value)
                .append("date", date)
                .toString();
    }
}
