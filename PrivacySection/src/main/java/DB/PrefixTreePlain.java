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
@Table(name = "prefix_tree_plain")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PrefixTreePlain.findAll", query = "SELECT p FROM PrefixTreePlain p"),
    @NamedQuery(name = "PrefixTreePlain.findById", query = "SELECT p FROM PrefixTreePlain p WHERE p.id = :id"),
    @NamedQuery(name = "PrefixTreePlain.findByData", query = "SELECT p FROM PrefixTreePlain p WHERE p.data = :data"),
    @NamedQuery(name = "PrefixTreePlain.findByLevel", query = "SELECT p FROM PrefixTreePlain p WHERE p.level = :level"),
    @NamedQuery(name = "PrefixTreePlain.findByIsLeaf", query = "SELECT p FROM PrefixTreePlain p WHERE p.isLeaf = :isLeaf"),
    @NamedQuery(name = "PrefixTreePlain.findByHashData", query = "SELECT p FROM PrefixTreePlain p WHERE p.hashData = :hashData"),
    @NamedQuery(name = "PrefixTreePlain.findByParentId", query = "SELECT p FROM PrefixTreePlain p WHERE p.parentId = :parentId"),
    @NamedQuery(name = "PrefixTreePlain.findByRowId", query = "SELECT p FROM PrefixTreePlain p WHERE p.rowId = :rowId")})
public class PrefixTreePlain implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "data")
    private String data;
    @Basic(optional = false)
    @Column(name = "level")
    private int level;
    @Basic(optional = false)
    @Column(name = "is_leaf")
    private boolean isLeaf;
    @Column(name = "hash_data")
    private String hashData;
    @Column(name = "parent_id")
    private Integer parentId;
    @Column(name = "row_id")
    private String rowId;

    public PrefixTreePlain() {
    }

    public PrefixTreePlain(Integer id) {
        this.id = id;
    }

    public PrefixTreePlain(Integer id, String data, int level, boolean isLeaf) {
        this.id = id;
        this.data = data;
        this.level = level;
        this.isLeaf = isLeaf;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public boolean getIsLeaf() {
        return isLeaf;
    }

    public void setIsLeaf(boolean isLeaf) {
        this.isLeaf = isLeaf;
    }

    public String getHashData() {
        return hashData;
    }

    public void setHashData(String hashData) {
        this.hashData = hashData;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public String getRowId() {
        return rowId;
    }

    public void setRowId(String rowId) {
        this.rowId = rowId;
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
        if (!(object instanceof PrefixTreePlain)) {
            return false;
        }
        PrefixTreePlain other = (PrefixTreePlain) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "DB.PrefixTreePlain[ id=" + id + " ]";
    }
    
}
