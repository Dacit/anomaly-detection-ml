package de.qaware.mlwb.impl.sparksolr;

import org.junit.BeforeClass;

import java.util.Properties;

/**
 * Base IT class that retrieves solr config that should be set in gradle build. Otherwise, loads defaults.
 *
 * @author Fabian Huch
 */
public class AbstractSolrCloudIT {
    private static final String ZKHOST_KEY = "zkHost";
    private static final String COLLECTION_KEY = "collection";

    protected static String zkHost = "localhost:19983";
    protected static String collection = "ekgdata";

    @BeforeClass
    public static void retrieveConfig() {
        Properties properties = System.getProperties();
        String zkHost = properties.getProperty(ZKHOST_KEY);
        if (zkHost != null) {
            AbstractSolrCloudIT.zkHost = zkHost;
        }
        String collection  = properties.getProperty(COLLECTION_KEY);
        if (collection != null) {
            AbstractSolrCloudIT.collection = collection;
        }
    }
}
