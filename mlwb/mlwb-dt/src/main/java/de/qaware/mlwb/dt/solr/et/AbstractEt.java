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
package de.qaware.mlwb.dt.solr.et;

import de.qaware.mlwb.dt.solr.SolrSchema;
import de.qaware.mlwb.dt.solr.SolrType;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.solr.client.solrj.beans.Field;

import java.io.Serializable;
import java.util.UUID;

/**
 * This class contains two fields required by all ET classes to be stored in SOLR.
 *
 * @author christian.fritz
 */
public abstract class AbstractEt implements Serializable {
    private static final long serialVersionUID = -6751734116154672022L;
    /**
     * The row id.
     */
    @Field(SolrSchema.ID)
    private String id;

    /**
     * SOLR Info-Type.
     */
    @Field(SolrSchema.TYPE)
    private String type;

    /**
     * Default constructor for internal purposes.
     */
    protected AbstractEt() {
    }

    /**
     * Constructor for internal purposes.
     *
     * @param type the info type, may not be NULL
     */
    protected AbstractEt(SolrType type) {
        this.type = type.name();
        this.id = UUID.randomUUID().toString();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AbstractEt)) {
            return false;
        }
        AbstractEt that = (AbstractEt) o;
        return new EqualsBuilder()
                .append(getId(), that.getId())
                .append(getType(), that.getType())
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(getId())
                .append(getType())
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("type", type)
                .toString();
    }
}
