/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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
@Table(name = "dificulty")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Dificulty.findAll", query = "SELECT d FROM Dificulty d")
    , @NamedQuery(name = "Dificulty.findByDifName", query = "SELECT d FROM Dificulty d WHERE d.difName = :difName")
    , @NamedQuery(name = "Dificulty.findByFuel", query = "SELECT d FROM Dificulty d WHERE d.fuel = :fuel")
    , @NamedQuery(name = "Dificulty.findByTotalfuel", query = "SELECT d FROM Dificulty d WHERE d.totalfuel = :totalfuel")
    , @NamedQuery(name = "Dificulty.findByPointMultiply", query = "SELECT d FROM Dificulty d WHERE d.pointMultiply = :pointMultiply")})
public class Dificulty implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "dif_name")
    private String difName;
    @Basic(optional = false)
    @Column(name = "fuel")
    private int fuel;
    @Basic(optional = false)
    @Column(name = "totalfuel")
    private int totalfuel;
    @Basic(optional = false)
    @Column(name = "pointMultiply")
    private int pointMultiply;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "cfDificulty", fetch = FetchType.EAGER)
    private Collection<Configuration> configurationCollection;

    public Dificulty() {
    }

    public Dificulty(String difName) {
        this.difName = difName;
    }

    public Dificulty(String difName, int fuel, int totalfuel, int pointMultiply) {
        this.difName = difName;
        this.fuel = fuel;
        this.totalfuel = totalfuel;
        this.pointMultiply = pointMultiply;
    }

    public String getDifName() {
        return difName;
    }

    public void setDifName(String difName) {
        this.difName = difName;
    }

    public int getFuel() {
        return fuel;
    }

    public void setFuel(int fuel) {
        this.fuel = fuel;
    }

    public int getTotalfuel() {
        return totalfuel;
    }

    public void setTotalfuel(int totalfuel) {
        this.totalfuel = totalfuel;
    }

    public int getPointMultiply() {
        return pointMultiply;
    }

    public void setPointMultiply(int pointMultiply) {
        this.pointMultiply = pointMultiply;
    }

    @XmlTransient
    public Collection<Configuration> getConfigurationCollection() {
        return configurationCollection;
    }

    public void setConfigurationCollection(Collection<Configuration> configurationCollection) {
        this.configurationCollection = configurationCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (difName != null ? difName.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Dificulty)) {
            return false;
        }
        Dificulty other = (Dificulty) object;
        if ((this.difName == null && other.difName != null) || (this.difName != null && !this.difName.equals(other.difName))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.Dificulty[ difName=" + difName + " ]";
    }
    
}
