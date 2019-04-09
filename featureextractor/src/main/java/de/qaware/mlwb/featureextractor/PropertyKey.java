package de.qaware.mlwb.featureextractor;

import java.io.Serializable;

/**
 * Bean-compliant class for metric keys.
 *
 * @author Fabain Huch
 */
public final class PropertyKey implements Serializable {
    private String name;
    private Tuple propertyKeyTuple;

    public String getName() {
        return name;
    }

    public Tuple getPropertyKeyTuple() {
        return propertyKeyTuple;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPropertyKeyTuple(Tuple propertyKeyTuple) {
        this.propertyKeyTuple = propertyKeyTuple;
    }

    /**
     * No-args constructor for bean compliance.
     */
    public PropertyKey(){
    }

    /**
     * Constructor. Use this instead of no-args bean constructor.
     *
     * @param linearizedMetric the metric to get the key from
     */
    public PropertyKey(LinearizedMetric linearizedMetric) {
        this.name = linearizedMetric.getName();
        this.propertyKeyTuple = linearizedMetric.getPropertyKeyTuple();
    }

    @Override
    public String toString() {
        return name + ' ' + propertyKeyTuple;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        PropertyKey that = (PropertyKey) o;

        if (name != null ? !name.equals(that.name) : that.name != null) {
            return false;
        }
        return propertyKeyTuple != null ? propertyKeyTuple.equals(that.propertyKeyTuple) : that.propertyKeyTuple == null;

    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (propertyKeyTuple != null ? propertyKeyTuple.hashCode() : 0);
        return result;
    }
}
