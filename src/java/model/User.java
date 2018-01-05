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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
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
@Table(name = "user")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "User.findAll", query = "SELECT u FROM User u")
    , @NamedQuery(name = "User.findByUsId", query = "SELECT u FROM User u WHERE u.usId = :usId")
    , @NamedQuery(name = "User.findByUsername", query = "SELECT u FROM User u WHERE u.username = :username")
    , @NamedQuery(name = "User.findByPassword", query = "SELECT u FROM User u WHERE u.password = :password")
    , @NamedQuery(name = "User.findByRealname", query = "SELECT u FROM User u WHERE u.realname = :realname")
    , @NamedQuery(name = "User.findByGamesplayed", query = "SELECT u FROM User u WHERE u.gamesplayed = :gamesplayed")})
public class User implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "us_id")
    private Integer usId;
    @Basic(optional = false)
    @Column(name = "username")
    private String username;
    @Basic(optional = false)
    @Column(name = "password")
    private String password;
    @Basic(optional = false)
    @Column(name = "realname")
    private String realname;
    @Basic(optional = false)
    @Column(name = "gamesplayed")
    private int gamesplayed;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "usId", fetch = FetchType.EAGER)
    private Collection<Configuration> configurationCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "usId", fetch = FetchType.EAGER)
    private Collection<Scoreboard> scoreboardCollection;

    public User() {
    }

    public User(Integer usId) {
        this.usId = usId;
    }

    public User(Integer usId, String username, String password, String realname, int gamesplayed) {
        this.usId = usId;
        this.username = username;
        this.password = password;
        this.realname = realname;
        this.gamesplayed = gamesplayed;
    }

    public Integer getUsId() {
        return usId;
    }

    public void setUsId(Integer usId) {
        this.usId = usId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    public int getGamesplayed() {
        return gamesplayed;
    }

    public void setGamesplayed(int gamesplayed) {
        this.gamesplayed = gamesplayed;
    }

    @XmlTransient
    public Collection<Configuration> getConfigurationCollection() {
        return configurationCollection;
    }

    public void setConfigurationCollection(Collection<Configuration> configurationCollection) {
        this.configurationCollection = configurationCollection;
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
        hash += (usId != null ? usId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof User)) {
            return false;
        }
        User other = (User) object;
        if ((this.usId == null && other.usId != null) || (this.usId != null && !this.usId.equals(other.usId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.User[ usId=" + usId + " ]";
    }
    
}
