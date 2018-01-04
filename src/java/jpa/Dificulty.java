/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jpa;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author admin
 */
@Entity
@Table(name = "DIFICULTY")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Dificulty.findAll", query = "SELECT d FROM Dificulty d")
    , @NamedQuery(name = "Dificulty.findByDificultyName", query = "SELECT d FROM Dificulty d WHERE d.dificultyName = :dificultyName")
    , @NamedQuery(name = "Dificulty.findByFuel", query = "SELECT d FROM Dificulty d WHERE d.fuel = :fuel")
    , @NamedQuery(name = "Dificulty.findByTotalfuel", query = "SELECT d FROM Dificulty d WHERE d.totalfuel = :totalfuel")
    , @NamedQuery(name = "Dificulty.findByPointsmultiplyer", query = "SELECT d FROM Dificulty d WHERE d.pointsmultiplyer = :pointsmultiplyer")})
public class Dificulty implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "DIFICULTY_NAME")
    private String dificultyName;
    @Column(name = "FUEL")
    private Integer fuel;
    @Column(name = "TOTALFUEL")
    private Integer totalfuel;
    @Column(name = "POINTSMULTIPLYER")
    private Integer pointsmultiplyer;
    @OneToMany(mappedBy = "cfDificulty")
    private Collection<Configurations> configurationsCollection;

    public Dificulty() {
    }

    public Dificulty(String dificultyName) {
        this.dificultyName = dificultyName;
    }

    public String getDificultyName() {
        return dificultyName;
    }

    public void setDificultyName(String dificultyName) {
        this.dificultyName = dificultyName;
    }

    public Integer getFuel() {
        return fuel;
    }

    public void setFuel(Integer fuel) {
        this.fuel = fuel;
    }

    public Integer getTotalfuel() {
        return totalfuel;
    }

    public void setTotalfuel(Integer totalfuel) {
        this.totalfuel = totalfuel;
    }

    public Integer getPointsmultiplyer() {
        return pointsmultiplyer;
    }

    public void setPointsmultiplyer(Integer pointsmultiplyer) {
        this.pointsmultiplyer = pointsmultiplyer;
    }

    @XmlTransient
    public Collection<Configurations> getConfigurationsCollection() {
        return configurationsCollection;
    }

    public void setConfigurationsCollection(Collection<Configurations> configurationsCollection) {
        this.configurationsCollection = configurationsCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (dificultyName != null ? dificultyName.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Dificulty)) {
            return false;
        }
        Dificulty other = (Dificulty) object;
        if ((this.dificultyName == null && other.dificultyName != null) || (this.dificultyName != null && !this.dificultyName.equals(other.dificultyName))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "jpa.Dificulty[ dificultyName=" + dificultyName + " ]";
    }
    
}
