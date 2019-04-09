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

/**
 * ValueType defines how the measurement values are aggregated.
 *
 * @author Johannes Weigend
 * @author Rene Pospich
 */
public enum AggregationType {

    /**
     * The AVG.
     */
    AVG,

    /**
     * The MIN.
     */
    MIN,

    /**
     * The MAX.
     */
    MAX,

    /**
     * The MEDIAN.
     */
    MEDIAN
}
