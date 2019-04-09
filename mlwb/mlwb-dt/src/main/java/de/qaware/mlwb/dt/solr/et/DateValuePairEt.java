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
package de.qaware.mlwb.dt.solr.et;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

import java.io.Serializable;
import java.sql.Timestamp;
import java.time.Instant;

/**
 * The entity for date value pairs. Used to store in a zipped string.
 *
 * @author drexler.c
 */
public class DateValuePairEt implements Comparable<DateValuePairEt>, Serializable {
    private static final long serialVersionUID = -4300246192241166788L;

    /**
     * The value.
     */
    @JsonProperty("v")
    private double value;

    /**
     * The date.
     */

    @JsonProperty("d")
    private long date;


    /**
     * Constructs an empty point
     * (for JSON)
     */
    public DateValuePairEt() {
    }

    /**
     * Creates a counter.
     *
     * @param value the value.
     * @param date  the record date.
     */
    public DateValuePairEt(
            double value,
            Instant date) {
        this.value = value;
        this.date = date.toEpochMilli();
    }

    /**
     * Creates a counter.
     *
     * @param value       the value.
     * @param epochMillis the record date.
     */
    public DateValuePairEt(
            @JsonProperty("v") double value,
            @JsonProperty("d") long epochMillis) {
        this.value = value;
        this.date = epochMillis;
    }

    /**
     * Copy.
     *
     * @param v value to copy
     */
    public DateValuePairEt(DateValuePairEt v) {
        this.value = v.value;
        this.date = v.date;
    }

    /**
     * Getter for property 'value'.
     *
     * @return Value for property 'value'.
     */
    public double getValue() {
        return value;
    }

    /**
     * Getter for property 'date'.
     *
     * @return Value for property 'date'.
     */
    @JsonIgnore
    public Timestamp getDate() {
        return new Timestamp(date);
    }


    /**
     * Returns the date represented as epoch milli
     *
     * @return the date represented as epoch milli
     */
    @JsonIgnore
    public long getEpochMilli() {
        return date;
    }

    @Override
    public int compareTo(DateValuePairEt o) {
        return Long.valueOf(date).compareTo(o.getEpochMilli());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DateValuePairEt)) {
            return false;
        }
        DateValuePairEt value1 = (DateValuePairEt) o;
        return new EqualsBuilder()
                .append(getValue(), value1.getValue())
                .append(getDate(), value1.getDate())
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(getValue())
                .append(getDate())
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
