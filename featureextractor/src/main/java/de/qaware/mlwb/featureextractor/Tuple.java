package de.qaware.mlwb.featureextractor;

import java.io.Serializable;
import java.util.Arrays;

/**
 * Bean-compliant n-tuple class for correct hashcodes/equals/tostring
 *
 * @author Fabian Huch
 */
public final class Tuple implements Serializable {
    private String[] arr;

    public String[] getArr() {
        return arr;
    }

    public void setArr(String[] arr) {
        this.arr = arr;
    }

    /**
     * Gets the size of the tuple.
     *
     * @return the size of the tuple.
     */
    public int size() {
        return arr.length;
    }

    /**
     * Constructor.
     *
     * @param args input tuple as String arrary
     */
    public Tuple(String... args) {
        this.arr = args;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Tuple tuple = (Tuple) o;

        // Probably incorrect - comparing Object[] arrays with Arrays.equals
        return Arrays.equals(arr, tuple.arr);

    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(arr);
    }

    @Override
    public String toString() {
        return Arrays.toString(arr);
    }
}
