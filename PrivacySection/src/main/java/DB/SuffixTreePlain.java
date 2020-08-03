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
@Table(name = "suffix_tree_plain")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SuffixTreePlain.findAll", query = "SELECT s FROM SuffixTreePlain s"),
    @NamedQuery(name = "SuffixTreePlain.findById", query = "SELECT s FROM SuffixTreePlain s WHERE s.id = :id"),
    @NamedQuery(name = "SuffixTreePlain.findByData", query = "SELECT s FROM SuffixTreePlain s WHERE s.data = :data"),
    @NamedQuery(name = "SuffixTreePlain.findByLevel", query = "SELECT s FROM SuffixTreePlain s WHERE s.level = :level"),
    @NamedQuery(name = "SuffixTreePlain.findByIsLeaf", query = "SELECT s FROM SuffixTreePlain s WHERE s.isLeaf = :isLeaf"),
    @NamedQuery(name = "SuffixTreePlain.findByHashData", query = "SELECT s FROM SuffixTreePlain s WHERE s.hashData = :hashData"),
    @NamedQuery(name = "SuffixTreePlain.findByParentId", query = "SELECT s FROM SuffixTreePlain s WHERE s.parentId = :parentId"),
    @NamedQuery(name = "SuffixTreePlain.findByRowId", query = "SELECT s FROM SuffixTreePlain s WHERE s.rowId = :rowId")})
public class SuffixTreePlain implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
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
    private Long parentId;
    @Column(name = "row_id")
    private String rowId;

    public SuffixTreePlain() {
    }

    public SuffixTreePlain(Long id) {
        this.id = id;
    }

    public SuffixTreePlain(Long id, String data, int level, boolean isLeaf) {
        this.id = id;
        this.data = data;
        this.level = level;
        this.isLeaf = isLeaf;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
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
        if (!(object instanceof SuffixTreePlain)) {
            return false;
        }
        SuffixTreePlain other = (SuffixTreePlain) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "DB.SuffixTreePlain[ id=" + id + " ]";
    }
    
}
