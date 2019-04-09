//______________________________________________________________________________
//
//          Project:    Software EKG
//______________________________________________________________________________
//
//       created by:    Johannes Weigend
//       creation date: 8.5.2012
//       changed by:    $Author$
//       change date:   $Date$
//       revision:      $Revision$
//       description:   Type field for solr table scheme.
//______________________________________________________________________________
//
//        Copyright:    QAware GmbH
//______________________________________________________________________________
package de.qaware.mlwb.dt.solr;


/**
 * The Enum SolrType.
 *
 * @author johannes.weigend
 */
public enum SolrType {

    /**
     * The RECORD.
     */
    RECORD,

    /**
     * Logs
     */
    LOG,

    /**
     * On demand analysis
     */
    ANALYSIS,

    /**
     * A repository
     */
    REPOSITORY,

    /**
     * All elements
     */
    ALL
}
