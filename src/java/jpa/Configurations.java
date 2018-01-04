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
@Table(name = "CONFIGURATIONS")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Configurations.findAll", query = "SELECT c FROM Configurations c")
    , @NamedQuery(name = "Configurations.findByCfId", query = "SELECT c FROM Configurations c WHERE c.cfId = :cfId")
    , @NamedQuery(name = "Configurations.findByCfName", query = "SELECT c FROM Configurations c WHERE c.cfName = :cfName")
    , @NamedQuery(name = "Configurations.findByCfSpaceship", query = "SELECT c FROM Configurations c WHERE c.cfSpaceship = :cfSpaceship")
    , @NamedQuery(name = "Configurations.findByCfMoon", query = "SELECT c FROM Configurations c WHERE c.cfMoon = :cfMoon")})
public class Configurations implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "CF_ID")
    private Integer cfId;
    @Column(name = "CF_NAME")
    private String cfName;
    @Column(name = "CF_SPACESHIP")
    private String cfSpaceship;
    @Column(name = "CF_MOON")
    private String cfMoon;
    @JoinColumn(name = "CF_DIFICULTY", referencedColumnName = "DIFICULTY_NAME")
    @ManyToOne
    private Dificulty cfDificulty;
    @JoinColumn(name = "US_ID", referencedColumnName = "US_ID")
    @ManyToOne
    private Users usId;
    @OneToMany(mappedBy = "cfId")
    private Collection<Scoreboard> scoreboardCollection;

    public Configurations() {
    }

    public Configurations(Integer cfId) {
        this.cfId = cfId;
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

    public String getCfSpaceship() {
        return cfSpaceship;
    }

    public void setCfSpaceship(String cfSpaceship) {
        this.cfSpaceship = cfSpaceship;
    }

    public String getCfMoon() {
        return cfMoon;
    }

    public void setCfMoon(String cfMoon) {
        this.cfMoon = cfMoon;
    }

    public Dificulty getCfDificulty() {
        return cfDificulty;
    }

    public void setCfDificulty(Dificulty cfDificulty) {
        this.cfDificulty = cfDificulty;
    }

    public Users getUsId() {
        return usId;
    }

    public void setUsId(Users usId) {
        this.usId = usId;
    }

    @XmlTransient
    public Collection<Scoreboard> getScoreboardCollection() {
        return scoreboardCollection;
    }

    public void setScoreboardCollection(Collection<Scoreboard> scoreboardCollection) {
        this.scoreboardCollection = scoreboardCollection;
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
        if (!(object instanceof Configurations)) {
            return false;
        }
        Configurations other = (Configurations) object;
        if ((this.cfId == null && other.cfId != null) || (this.cfId != null && !this.cfId.equals(other.cfId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "jpa.Configurations[ cfId=" + cfId + " ]";
    }
    
}
