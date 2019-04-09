package de.qaware.mlwb.featureextractor;

import java.io.Serializable;
import java.util.Arrays;

/**
 * Bean-Compliant class for sets of values for one key tuple.
 *
 * @author Fabian Huch
 */
public final class ValueSet implements Serializable {
    private Tuple keyTuple;
    private Tuple[] values;

    public Tuple getKeyTuple() {
        return keyTuple;
    }

    public Tuple[] getValues() {
        return values;
    }

    public void setKeyTuple(Tuple keyTuple) {
        this.keyTuple = keyTuple;
    }

    public void setValues(Tuple[] values) {
        this.values = values;
    }

    /**
     * No-args constructor for bean compliance.
     */
    public ValueSet(){
    }

    /**
     * Constructor. Use this instead of no-args bean constructor.
     *
     * @param keyTuple the key tuple to build the value set for
     * @param values the value tuples
     */
    public ValueSet(Tuple keyTuple, Tuple[] values) {
        for (Tuple tuple: values){
            if(tuple.size() != keyTuple.size()){
                throw new IllegalArgumentException("Length of values not equal to keys");
            }
        }
        this.keyTuple = keyTuple;
        this.values = values;
    }

    @Override
    public String toString() {
        return "(" + keyTuple.toString() + ", " + Arrays.toString(values) + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ValueSet valueSet = (ValueSet) o;

        if (keyTuple != null ? !keyTuple.equals(valueSet.keyTuple) : valueSet.keyTuple != null) {
            return false;
        }
        // Probably incorrect - comparing Object[] arrays with Arrays.equals
        return Arrays.equals(values, valueSet.values);

    }

    @Override
    public int hashCode() {
        int result = keyTuple != null ? keyTuple.hashCode() : 0;
        result = 31 * result + Arrays.hashCode(values);
        return result;
    }
}
