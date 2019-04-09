package de.qaware.mlwb.featureextractor;

import java.io.Serializable;

/**
 * Bean-compliant class for partly de-linearized metrics, i.e. grouped by name and property keys.
 *
 * @author Fabian Huch
 */
public final class PartlyLinearizedMetric implements Serializable {
    private PropertyKey propertyKey;
    private ValueSet valueSet;

    public PropertyKey getPropertyKey() {
        return propertyKey;
    }

    public ValueSet getValueSet() {
        return valueSet;
    }

    public void setPropertyKey(PropertyKey propertyKey) {
        this.propertyKey = propertyKey;
    }

    public void setValueSet(ValueSet valueSet) {
        this.valueSet = valueSet;
    }

    /**
     * No-args constructor for bean compliance.
     */
    public PartlyLinearizedMetric(){
    }

    /**
     * Constructor. Use this instead of no-args bean constructor.
     *
     * @param propertyKey the property key for this partly linearized metric
     * @param valueTuples the value tuples
     */
    public PartlyLinearizedMetric(PropertyKey propertyKey, Tuple[] valueTuples) {
        this.propertyKey = propertyKey;
        this.valueSet = new ValueSet(propertyKey.getPropertyKeyTuple(), valueTuples);
    }

    @Override
    public String toString() {
        return "PartlyLinearizedMetric{" +
                "propertyKey=" + propertyKey +
                ", valueSet=" + valueSet +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        PartlyLinearizedMetric that = (PartlyLinearizedMetric) o;

        if (propertyKey != null ? !propertyKey.equals(that.propertyKey) : that.propertyKey != null) {
            return false;
        }
        return valueSet != null ? valueSet.equals(that.valueSet) : that.valueSet == null;

    }

    @Override
    public int hashCode() {
        int result = propertyKey != null ? propertyKey.hashCode() : 0;
        result = 31 * result + (valueSet != null ? valueSet.hashCode() : 0);
        return result;
    }
}
