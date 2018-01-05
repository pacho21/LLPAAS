/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "configuration")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Configuration.findAll", query = "SELECT c FROM Configuration c")
    , @NamedQuery(name = "Configuration.findByCfId", query = "SELECT c FROM Configuration c WHERE c.cfId = :cfId")
    , @NamedQuery(name = "Configuration.findByCfName", query = "SELECT c FROM Configuration c WHERE c.cfName = :cfName")
    , @NamedQuery(name = "Configuration.findByCfMoon", query = "SELECT c FROM Configuration c WHERE c.cfMoon = :cfMoon")
    , @NamedQuery(name = "Configuration.findByCfSpaceship", query = "SELECT c FROM Configuration c WHERE c.cfSpaceship = :cfSpaceship")})
public class Configuration implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "cf_id")
    private Integer cfId;
    @Basic(optional = false)
    @Column(name = "cf_name")
    private String cfName;
    @Basic(optional = false)
    @Column(name = "cf_moon")
    private String cfMoon;
    @Basic(optional = false)
    @Column(name = "cf_spaceship")
    private String cfSpaceship;
    @JoinColumn(name = "cf_dificulty", referencedColumnName = "dif_name")
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private Dificulty cfDificulty;
    @JoinColumn(name = "us_id", referencedColumnName = "us_id")
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private User usId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "cfId", fetch = FetchType.EAGER)
    private List<Scoreboard> scoreboardList;

    public Configuration() {
    }

    public Configuration(Integer cfId) {
        this.cfId = cfId;
    }

    public Configuration(Integer cfId, String cfName, String cfMoon, String cfSpaceship) {
        this.cfId = cfId;
        this.cfName = cfName;
        this.cfMoon = cfMoon;
        this.cfSpaceship = cfSpaceship;
    }

    public Integer getCfId() {
        return cfId;
    }

    public void setCfId(Integer cfId) {
        this.cfId = cfId;
    }

    public String getCfName() {
        return cfName;
    }

    public void setCfName(String cfName) {
        this.cfName = cfName;
    }

    public String getCfMoon() {
        return cfMoon;
    }

    public void setCfMoon(String cfMoon) {
        this.cfMoon = cfMoon;
    }

    public String getCfSpaceship() {
        return cfSpaceship;
    }

    public void setCfSpaceship(String cfSpaceship) {
        this.cfSpaceship = cfSpaceship;
    }

    public Dificulty getCfDificulty() {
        return cfDificulty;
    }

    public void setCfDificulty(Dificulty cfDificulty) {
        this.cfDificulty = cfDificulty;
    }

    public User getUsId() {
        return usId;
    }

    public void setUsId(User usId) {
        this.usId = usId;
    }

    @XmlTransient
    public List<Scoreboard> getScoreboardList() {
        return scoreboardList;
    }

    public void setScoreboardList(List<Scoreboard> scoreboardList) {
        this.scoreboardList = scoreboardList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (cfId != null ? cfId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Configuration)) {
            return false;
        }
        Configuration other = (Configuration) object;
        if ((this.cfId == null && other.cfId != null) || (this.cfId != null && !this.cfId.equals(other.cfId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.Configuration[ cfId=" + cfId + " ]";
    }
    
}
