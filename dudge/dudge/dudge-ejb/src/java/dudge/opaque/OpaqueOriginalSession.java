/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dudge.opaque;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

/**
 *
 * @author duke
 */
@Entity
@Table(name = "opaque_sessions")
@NamedQueries({
    @NamedQuery(name = "OpaqueOriginalSession.findBySessionId", query = "SELECT o FROM OpaqueOriginalSession o WHERE o.osessionId = :osessionId"),
    @NamedQuery(name = "OpaqueOriginalSession.findBySolutionId", query = "SELECT o FROM OpaqueOriginalSession o WHERE o.solutionId = :solutionId"),
    @NamedQuery(name = "OpaqueOriginalSession.findByOriginalSessionId", query = "SELECT o FROM OpaqueOriginalSession o WHERE o.originalsessionId = :originalsessionId")
    })
public class OpaqueOriginalSession implements Serializable {
    //private static final long serialVersionUID = 1L;
    
    //@Basic(optional = false)
    //@NotNull
    @Id
    @Column(name = "osession_id")
    private Long osessionId=null;
    
    @Column(name = "solution_id")
    private Integer solutionId=null;
    
    @Column(name = "created", updatable = false, insertable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date created=null;
    
    @Column(name = "originalsession_id")
    private Long originalsessionId=null;
    
    @Column(name = "steps")
    private Integer steps=null;

    public OpaqueOriginalSession() {
    }

    public OpaqueOriginalSession(Long osessionId) {
        this.osessionId = osessionId;
    }

    public OpaqueOriginalSession(Long osessionId, Date created) {
        this.osessionId = osessionId;
        this.created = created;
    }

    public Long getSessionId() {
        return osessionId;
    }

    public void setSessionId(Long sessionId) {
        this.osessionId = sessionId;
    }

    public Integer getSolutionId() {
        return solutionId;
    }

    public void setSolutionId(Integer solutionId) {
        this.solutionId = solutionId;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Long getOriginalSessionId() {
        return originalsessionId;
    }

    public void setOriginalSessionId(Long originalsessionId) {
        this.originalsessionId = originalsessionId;
    }

    public Integer getSteps() {
        return steps;
    }

    public void setSteps(Integer steps) {
        this.steps = steps;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (osessionId != null ? osessionId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof OpaqueOriginalSession)) {
            return false;
        }
        OpaqueOriginalSession other = (OpaqueOriginalSession) object;
        if ((this.osessionId == null && other.osessionId != null) || (this.osessionId != null && !this.osessionId.equals(other.osessionId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dudge.opaque.OpaqueSessionPersistent[ osessionId=" + osessionId + " ]";
    }
    
}
