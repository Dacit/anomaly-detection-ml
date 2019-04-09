package de.qaware.mlwb.featureextractor;

import java.io.Serializable;
import java.util.Arrays;

/**
 * Bean-compliant class for de-linearized metrics
 *
 * @author Fabian Huch
 */
public final class DeLinearizedMetric implements Serializable {
    private String[] properties;
    private ValueSet valueSet;

    public String[] getProperties() {
        return properties;
    }

    public ValueSet getValueSet() {
        return valueSet;
    }

    public void setProperties(String[] properties) {
        this.properties = properties;
    }

    public void setValueSet(ValueSet valueSet) {
        this.valueSet = valueSet;
    }

    /**
     * No-args constructor for bean compliance.
     */
    public DeLinearizedMetric(){
    }

    /**
     * Constructor. Use this instead of no-args bean constructor.
     *
     * @param valueSet the set of possible values for the delinarized metric
     * @param properties all properties for the delinearized metric instance
     */
    public DeLinearizedMetric(ValueSet valueSet, String[] properties) {
        this.properties = properties;
        this.valueSet = valueSet;
    }

    @Override
    public String toString() {
        return "(" + Arrays.toString(properties) + ", " + valueSet.toString() + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        DeLinearizedMetric that = (DeLinearizedMetric) o;

        // Probably incorrect - comparing Object[] arrays with Arrays.equals
        if (!Arrays.equals(properties, that.properties)) {
            return false;
        }
        return valueSet != null ? valueSet.equals(that.valueSet) : that.valueSet == null;

    }

    @Override
    public int hashCode() {
        int result = Arrays.hashCode(properties);
        result = 31 * result + (valueSet != null ? valueSet.hashCode() : 0);
        return result;
    }
}
