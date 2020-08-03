/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DB;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author azizmma
 */
@Entity
@Table(name = "stree_map")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "StreeMap.findAll", query = "SELECT s FROM StreeMap s"),
    @NamedQuery(name = "StreeMap.findById", query = "SELECT s FROM StreeMap s WHERE s.id = :id"),
    @NamedQuery(name = "StreeMap.findByStreeId", query = "SELECT s FROM StreeMap s WHERE s.streeId = :streeId"),
    @NamedQuery(name = "StreeMap.findBySequenceId", query = "SELECT s FROM StreeMap s WHERE s.sequenceId = :sequenceId"),
    @NamedQuery(name = "StreeMap.findByPosition", query = "SELECT s FROM StreeMap s WHERE s.position = :position")})
public class StreeMap implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Column(name = "stree_id")
    private Integer streeId;
    @Column(name = "sequence_id")
    private Integer sequenceId;
    @Column(name = "position")
    private Integer position;

    public StreeMap() {
    }

    public StreeMap(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getStreeId() {
        return streeId;
    }

    public void setStreeId(Integer streeId) {
        this.streeId = streeId;
    }

    public Integer getSequenceId() {
        return sequenceId;
    }

    public void setSequenceId(Integer sequenceId) {
        this.sequenceId = sequenceId;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof StreeMap)) {
            return false;
        }
        StreeMap other = (StreeMap) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "DB.StreeMap[ id=" + id + " ]";
    }
    
}
