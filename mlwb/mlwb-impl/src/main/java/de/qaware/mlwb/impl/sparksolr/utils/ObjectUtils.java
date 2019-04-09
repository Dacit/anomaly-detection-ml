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
package de.qaware.mlwb.impl.sparksolr.utils;

import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Some additional util functions for working with objects.
 *
 * @author christian.fritz
 */
public final class ObjectUtils {
    private ObjectUtils() {
    }

    /**
     * Try to map the object or returns the default value if the object is null.
     *
     * @param object       the {@code Object} to test, may be {@code null}
     * @param defaultValue the default value to return, may be {@code null}
     * @param map          the mapping function
     * @param <T>          the type of the object
     * @param <R>          the type of mapped value and the default value.
     * @return {@code object} if it is not {@code null}, defaultValue otherwise
     */
    public static <T, R> R mapOrDefaultIfNull(T object, R defaultValue, Function<T, R> map) {
        Objects.requireNonNull(map, "The mapping function must not be null!");
        return object != null ? map.apply(object) : defaultValue;
    }

    /**
     * Shorthand function for an no op {@link Consumer}.
     *
     * @param <T> type of consumer argument.
     * @return the no op consumer
     */
    public static <T> Consumer<T> noOpConsumer() {
        return p -> {
        };
    }

    /**
     * Shorthand function for a no op {@link BiConsumer}
     *
     * @param <T> first type of consumer arguments
     * @param <U> second type of consumer arguments
     * @return the no op bi consumer
     */
    public static <T, U> BiConsumer<T, U> noOpBiConsumer() {
        return (p, q) -> {
        };
    }
}
