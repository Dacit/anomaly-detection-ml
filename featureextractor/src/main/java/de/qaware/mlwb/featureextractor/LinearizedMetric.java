package de.qaware.mlwb.featureextractor;

import java.io.Serializable;

/**
 * Bean-compliant class for a linearized metric.
 *
 * @author Fabian Huch
 */
public final class LinearizedMetric implements Serializable {
    private String name;
    private Tuple propertyKeyTuple;
    private Tuple propertyValueTuple;

    public void setName(String name) {
        this.name = name;
    }

    public void setPropertyKeyTuple(Tuple propertyKeyTuple) {
        this.propertyKeyTuple = propertyKeyTuple;
    }

    public void setPropertyValueTuple(Tuple propertyValueTuple) {
        this.propertyValueTuple = propertyValueTuple;
    }

    public String getName() {
        return name;
    }

    public Tuple getPropertyKeyTuple() {
        return propertyKeyTuple;
    }

    public Tuple getPropertyValueTuple() {
        return propertyValueTuple;
    }

    /**
     * No-args constructor for bean-compliance.
     */
    public LinearizedMetric(){
    }

    /**
     * Constructor. Use this instead of no-args constructor.
     *
     * @param name the name of the metric
     * @param propertyKeyTuple the keys of the metric's properties
     * @param propertyValueTuple the values of the metric's properties
     */
    public LinearizedMetric(String name, String[] propertyKeyTuple, String[] propertyValueTuple) {
        if (propertyKeyTuple.length != propertyValueTuple.length) {
            throw new IllegalArgumentException("Keys and values differ in length");
        }

        this.name = name;
        this.propertyKeyTuple = new Tuple(propertyKeyTuple);
        this.propertyValueTuple = new Tuple(propertyValueTuple);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        LinearizedMetric that = (LinearizedMetric) o;

        if (name != null ? !name.equals(that.name) : that.name != null) {
            return false;
        }
        if (propertyKeyTuple != null ? !propertyKeyTuple.equals(that.propertyKeyTuple) : that.propertyKeyTuple != null) {
            return false;
        }
        return propertyValueTuple != null ? propertyValueTuple.equals(that.propertyValueTuple) : that.propertyValueTuple == null;

    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (propertyKeyTuple != null ? propertyKeyTuple.hashCode() : 0);
        result = 31 * result + (propertyValueTuple != null ? propertyValueTuple.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "LinearizedMetric{" +
                "name='" + name + '\'' +
                ", propertyKeyTuple=" + propertyKeyTuple +
                ", propertyValueTuple=" + propertyValueTuple +
                '}';
    }
}
