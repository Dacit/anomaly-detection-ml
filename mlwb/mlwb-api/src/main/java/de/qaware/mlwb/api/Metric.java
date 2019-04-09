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


import java.io.Serializable;

/**
 * Bean-compliant metrics class. Also represents a metric for solr queries. Metric names are in form 'metric [series,process,host]'
 *
 * @author Johannes Weigend
 * @author Fabian Huch
 */
public final class Metric implements Comparable<Metric>, Serializable {

    private static final long serialVersionUID = -4091118172242720577L;

    private String name;
    private String host;
    private String procs;

    /**
     * Instantiates a new metric.
     *
     * @param name  the name
     * @param host  the host the metric is measured on
     * @param procs the process the metric is measured on
     */
    public Metric(String name, String host, String procs) {
        this.name = name;
        this.host = host;
        this.procs = procs;
    }

    /**
     * No-args constructor for bean compliance
     */
    public Metric() {
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getProcs() {
        return procs;
    }

    public void setProcs(String procs) {
        this.procs = procs;
    }

    @Override
    public int compareTo(Metric metric) {
        return name.compareTo(metric.getName());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Metric metric = (Metric) o;

        if (name != null ? !name.equals(metric.name) : metric.name != null) {
            return false;
        }
        if (host != null ? !host.equals(metric.host) : metric.host != null) {
            return false;
        }
        return procs != null ? procs.equals(metric.procs) : metric.procs == null;

    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (host != null ? host.hashCode() : 0);
        result = 31 * result + (procs != null ? procs.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Metric{" +
                "name='" + name + '\'' +
                ", host='" + host + '\'' +
                ", procs='" + procs + '\'' +
                '}';
    }
}
