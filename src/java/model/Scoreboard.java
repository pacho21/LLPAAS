/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
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
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author admin
 */
@Entity
@Table(name = "scoreboard")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Scoreboard.findAll", query = "SELECT s FROM Scoreboard s")
    , @NamedQuery(name = "Scoreboard.findByScId", query = "SELECT s FROM Scoreboard s WHERE s.scId = :scId")
    , @NamedQuery(name = "Scoreboard.findByScPoints", query = "SELECT s FROM Scoreboard s WHERE s.scPoints = :scPoints")
    , @NamedQuery(name = "Scoreboard.findByScStartTime", query = "SELECT s FROM Scoreboard s WHERE s.scStartTime = :scStartTime")
    , @NamedQuery(name = "Scoreboard.findByScEndTime", query = "SELECT s FROM Scoreboard s WHERE s.scEndTime = :scEndTime")})
public class Scoreboard implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "sc_id")
    private Integer scId;
    @Column(name = "sc_points")
    private Integer scPoints;
    @Basic(optional = false)
    @Column(name = "sc_start_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date scStartTime;
    @Column(name = "sc_end_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date scEndTime;
    @JoinColumn(name = "cf_id", referencedColumnName = "cf_id")
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private Configuration cfId;
    @JoinColumn(name = "us_id", referencedColumnName = "us_id")
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private User usId;

    public Scoreboard() {
    }

    public Scoreboard(Integer scId) {
        this.scId = scId;
    }

    public Scoreboard(Integer scId, Date scStartTime) {
        this.scId = scId;
        this.scStartTime = scStartTime;
    }

    public Integer getScId() {
        return scId;
    }

    public void setScId(Integer scId) {
        this.scId = scId;
    }

    public Integer getScPoints() {
        return scPoints;
    }

    public void setScPoints(Integer scPoints) {
        this.scPoints = scPoints;
    }

    public Date getScStartTime() {
        return scStartTime;
    }

    public void setScStartTime(Date scStartTime) {
        this.scStartTime = scStartTime;
    }

    public Date getScEndTime() {
        return scEndTime;
    }

    public void setScEndTime(Date scEndTime) {
        this.scEndTime = scEndTime;
    }

    public Configuration getCfId() {
        return cfId;
    }

    public void setCfId(Configuration cfId) {
        this.cfId = cfId;
    }

    public User getUsId() {
        return usId;
    }

    public void setUsId(User usId) {
        this.usId = usId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (scId != null ? scId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Scoreboard)) {
            return false;
        }
        Scoreboard other = (Scoreboard) object;
        if ((this.scId == null && other.scId != null) || (this.scId != null && !this.scId.equals(other.scId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.Scoreboard[ scId=" + scId + " ]";
    }
    
}
