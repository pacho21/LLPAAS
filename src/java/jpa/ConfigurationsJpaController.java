/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jpa;

import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import jpa.exceptions.NonexistentEntityException;

/**
 *
 * @author admin
 */
public class ConfigurationsJpaController implements Serializable {

    public ConfigurationsJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Configurations configurations) {
        if (configurations.getScoreboardCollection() == null) {
            configurations.setScoreboardCollection(new ArrayList<Scoreboard>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Dificulty cfDificulty = configurations.getCfDificulty();
            if (cfDificulty != null) {
                cfDificulty = em.getReference(cfDificulty.getClass(), cfDificulty.getDificultyName());
                configurations.setCfDificulty(cfDificulty);
            }
            Users usId = configurations.getUsId();
            if (usId != null) {
                usId = em.getReference(usId.getClass(), usId.getUsId());
                configurations.setUsId(usId);
            }
            Collection<Scoreboard> attachedScoreboardCollection = new ArrayList<Scoreboard>();
            for (Scoreboard scoreboardCollectionScoreboardToAttach : configurations.getScoreboardCollection()) {
                scoreboardCollectionScoreboardToAttach = em.getReference(scoreboardCollectionScoreboardToAttach.getClass(), scoreboardCollectionScoreboardToAttach.getScId());
                attachedScoreboardCollection.add(scoreboardCollectionScoreboardToAttach);
            }
            configurations.setScoreboardCollection(attachedScoreboardCollection);
            em.persist(configurations);
            if (cfDificulty != null) {
                cfDificulty.getConfigurationsCollection().add(configurations);
                cfDificulty = em.merge(cfDificulty);
            }
            if (usId != null) {
                usId.getConfigurationsCollection().add(configurations);
                usId = em.merge(usId);
            }
            for (Scoreboard scoreboardCollectionScoreboard : configurations.getScoreboardCollection()) {
                Configurations oldCfIdOfScoreboardCollectionScoreboard = scoreboardCollectionScoreboard.getCfId();
                scoreboardCollectionScoreboard.setCfId(configurations);
                scoreboardCollectionScoreboard = em.merge(scoreboardCollectionScoreboard);
                if (oldCfIdOfScoreboardCollectionScoreboard != null) {
                    oldCfIdOfScoreboardCollectionScoreboard.getScoreboardCollection().remove(scoreboardCollectionScoreboard);
                    oldCfIdOfScoreboardCollectionScoreboard = em.merge(oldCfIdOfScoreboardCollectionScoreboard);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Configurations configurations) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Configurations persistentConfigurations = em.find(Configurations.class, configurations.getCfId());
            Dificulty cfDificultyOld = persistentConfigurations.getCfDificulty();
            Dificulty cfDificultyNew = configurations.getCfDificulty();
            Users usIdOld = persistentConfigurations.getUsId();
            Users usIdNew = configurations.getUsId();
            Collection<Scoreboard> scoreboardCollectionOld = persistentConfigurations.getScoreboardCollection();
            Collection<Scoreboard> scoreboardCollectionNew = configurations.getScoreboardCollection();
            if (cfDificultyNew != null) {
                cfDificultyNew = em.getReference(cfDificultyNew.getClass(), cfDificultyNew.getDificultyName());
                configurations.setCfDificulty(cfDificultyNew);
            }
            if (usIdNew != null) {
                usIdNew = em.getReference(usIdNew.getClass(), usIdNew.getUsId());
                configurations.setUsId(usIdNew);
            }
            Collection<Scoreboard> attachedScoreboardCollectionNew = new ArrayList<Scoreboard>();
            for (Scoreboard scoreboardCollectionNewScoreboardToAttach : scoreboardCollectionNew) {
                scoreboardCollectionNewScoreboardToAttach = em.getReference(scoreboardCollectionNewScoreboardToAttach.getClass(), scoreboardCollectionNewScoreboardToAttach.getScId());
                attachedScoreboardCollectionNew.add(scoreboardCollectionNewScoreboardToAttach);
            }
            scoreboardCollectionNew = attachedScoreboardCollectionNew;
            configurations.setScoreboardCollection(scoreboardCollectionNew);
            configurations = em.merge(configurations);
            if (cfDificultyOld != null && !cfDificultyOld.equals(cfDificultyNew)) {
                cfDificultyOld.getConfigurationsCollection().remove(configurations);
                cfDificultyOld = em.merge(cfDificultyOld);
            }
            if (cfDificultyNew != null && !cfDificultyNew.equals(cfDificultyOld)) {
                cfDificultyNew.getConfigurationsCollection().add(configurations);
                cfDificultyNew = em.merge(cfDificultyNew);
            }
            if (usIdOld != null && !usIdOld.equals(usIdNew)) {
                usIdOld.getConfigurationsCollection().remove(configurations);
                usIdOld = em.merge(usIdOld);
            }
            if (usIdNew != null && !usIdNew.equals(usIdOld)) {
                usIdNew.getConfigurationsCollection().add(configurations);
                usIdNew = em.merge(usIdNew);
            }
            for (Scoreboard scoreboardCollectionOldScoreboard : scoreboardCollectionOld) {
                if (!scoreboardCollectionNew.contains(scoreboardCollectionOldScoreboard)) {
                    scoreboardCollectionOldScoreboard.setCfId(null);
                    scoreboardCollectionOldScoreboard = em.merge(scoreboardCollectionOldScoreboard);
                }
            }
            for (Scoreboard scoreboardCollectionNewScoreboard : scoreboardCollectionNew) {
                if (!scoreboardCollectionOld.contains(scoreboardCollectionNewScoreboard)) {
                    Configurations oldCfIdOfScoreboardCollectionNewScoreboard = scoreboardCollectionNewScoreboard.getCfId();
                    scoreboardCollectionNewScoreboard.setCfId(configurations);
                    scoreboardCollectionNewScoreboard = em.merge(scoreboardCollectionNewScoreboard);
                    if (oldCfIdOfScoreboardCollectionNewScoreboard != null && !oldCfIdOfScoreboardCollectionNewScoreboard.equals(configurations)) {
                        oldCfIdOfScoreboardCollectionNewScoreboard.getScoreboardCollection().remove(scoreboardCollectionNewScoreboard);
                        oldCfIdOfScoreboardCollectionNewScoreboard = em.merge(oldCfIdOfScoreboardCollectionNewScoreboard);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = configurations.getCfId();
                if (findConfigurations(id) == null) {
                    throw new NonexistentEntityException("The configurations with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Configurations configurations;
            try {
                configurations = em.getReference(Configurations.class, id);
                configurations.getCfId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The configurations with id " + id + " no longer exists.", enfe);
            }
            Dificulty cfDificulty = configurations.getCfDificulty();
            if (cfDificulty != null) {
                cfDificulty.getConfigurationsCollection().remove(configurations);
                cfDificulty = em.merge(cfDificulty);
            }
            Users usId = configurations.getUsId();
            if (usId != null) {
                usId.getConfigurationsCollection().remove(configurations);
                usId = em.merge(usId);
            }
            Collection<Scoreboard> scoreboardCollection = configurations.getScoreboardCollection();
            for (Scoreboard scoreboardCollectionScoreboard : scoreboardCollection) {
                scoreboardCollectionScoreboard.setCfId(null);
                scoreboardCollectionScoreboard = em.merge(scoreboardCollectionScoreboard);
            }
            em.remove(configurations);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Configurations> findConfigurationsEntities() {
        return findConfigurationsEntities(true, -1, -1);
    }

    public List<Configurations> findConfigurationsEntities(int maxResults, int firstResult) {
        return findConfigurationsEntities(false, maxResults, firstResult);
    }

    private List<Configurations> findConfigurationsEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Configurations.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Configurations findConfigurations(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Configurations.class, id);
        } finally {
            em.close();
        }
    }

    public int getConfigurationsCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Configurations> rt = cq.from(Configurations.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
