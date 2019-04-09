/*
 * ______________________________________________________________________________
 * <p>
 * Project: Software EKG
 * ______________________________________________________________________________
 * <p>
 * created by: f.lautenschlager
 * creation date: 13.03.14 09:08
 * description:
 * ______________________________________________________________________________
 * <p>
 * Copyright: (c) QAware GmbH, all rights reserved
 * ______________________________________________________________________________
 */
package de.qaware.mlwb.dt.solr;

/**
 * Defines the constants for the mapping of AbstractEt
 * to solr fields.
 *
 * @author christian.fritz
 */
public final class SolrSchema {

    /**
     * Solr id field name.
     */
    public static final String ID = "id";
    /**
     * Field name identifying the concrete solr document type.
     */
    public static final String TYPE = "type";
    public static final String SERIES = "series";
    public static final String HOST_NAME = "host";
    public static final String PROCESS_NAME = "process";
    public static final String GROUP = "group";
    public static final String MEASUREMENT = "measurement";
    public static final String METRIC = "metric";
    public static final String DATA = "data";
    public static final String AGGREGATION_LEVEL = "ag";
    public static final String START = "start";
    public static final String STOP = "end";
    public static final String EXCLUDE = "exclude";
    public static final String IMPORT_DATE = "importDate";

    /**
     * Private class cause this class only contains constants.
     */
    private SolrSchema() {
    }
}
