/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import model.exceptions.IllegalOrphanException;
import model.exceptions.NonexistentEntityException;

/**
 *
 * @author admin
 */
public class ConfigurationJpaController implements Serializable {

    public ConfigurationJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Configuration configuration) {
        if (configuration.getScoreboardList() == null) {
            configuration.setScoreboardList(new ArrayList<Scoreboard>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Dificulty cfDificulty = configuration.getCfDificulty();
            if (cfDificulty != null) {
                cfDificulty = em.getReference(cfDificulty.getClass(), cfDificulty.getDifName());
                configuration.setCfDificulty(cfDificulty);
            }
            User usId = configuration.getUsId();
            if (usId != null) {
                usId = em.getReference(usId.getClass(), usId.getUsId());
                configuration.setUsId(usId);
            }
            List<Scoreboard> attachedScoreboardList = new ArrayList<Scoreboard>();
            for (Scoreboard scoreboardListScoreboardToAttach : configuration.getScoreboardList()) {
                scoreboardListScoreboardToAttach = em.getReference(scoreboardListScoreboardToAttach.getClass(), scoreboardListScoreboardToAttach.getScId());
                attachedScoreboardList.add(scoreboardListScoreboardToAttach);
            }
            configuration.setScoreboardList(attachedScoreboardList);
            em.persist(configuration);
            if (cfDificulty != null) {
                cfDificulty.getConfigurationList().add(configuration);
                cfDificulty = em.merge(cfDificulty);
            }
            if (usId != null) {
                usId.getConfigurationList().add(configuration);
                usId = em.merge(usId);
            }
            for (Scoreboard scoreboardListScoreboard : configuration.getScoreboardList()) {
                Configuration oldCfIdOfScoreboardListScoreboard = scoreboardListScoreboard.getCfId();
                scoreboardListScoreboard.setCfId(configuration);
                scoreboardListScoreboard = em.merge(scoreboardListScoreboard);
                if (oldCfIdOfScoreboardListScoreboard != null) {
                    oldCfIdOfScoreboardListScoreboard.getScoreboardList().remove(scoreboardListScoreboard);
                    oldCfIdOfScoreboardListScoreboard = em.merge(oldCfIdOfScoreboardListScoreboard);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Configuration configuration) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Configuration persistentConfiguration = em.find(Configuration.class, configuration.getCfId());
            Dificulty cfDificultyOld = persistentConfiguration.getCfDificulty();
            Dificulty cfDificultyNew = configuration.getCfDificulty();
            User usIdOld = persistentConfiguration.getUsId();
            User usIdNew = configuration.getUsId();
            List<Scoreboard> scoreboardListOld = persistentConfiguration.getScoreboardList();
            List<Scoreboard> scoreboardListNew = configuration.getScoreboardList();
            List<String> illegalOrphanMessages = null;
            for (Scoreboard scoreboardListOldScoreboard : scoreboardListOld) {
                if (!scoreboardListNew.contains(scoreboardListOldScoreboard)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Scoreboard " + scoreboardListOldScoreboard + " since its cfId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (cfDificultyNew != null) {
                cfDificultyNew = em.getReference(cfDificultyNew.getClass(), cfDificultyNew.getDifName());
                configuration.setCfDificulty(cfDificultyNew);
            }
            if (usIdNew != null) {
                usIdNew = em.getReference(usIdNew.getClass(), usIdNew.getUsId());
                configuration.setUsId(usIdNew);
            }
            List<Scoreboard> attachedScoreboardListNew = new ArrayList<Scoreboard>();
            for (Scoreboard scoreboardListNewScoreboardToAttach : scoreboardListNew) {
                scoreboardListNewScoreboardToAttach = em.getReference(scoreboardListNewScoreboardToAttach.getClass(), scoreboardListNewScoreboardToAttach.getScId());
                attachedScoreboardListNew.add(scoreboardListNewScoreboardToAttach);
            }
            scoreboardListNew = attachedScoreboardListNew;
            configuration.setScoreboardList(scoreboardListNew);
            configuration = em.merge(configuration);
            if (cfDificultyOld != null && !cfDificultyOld.equals(cfDificultyNew)) {
                cfDificultyOld.getConfigurationList().remove(configuration);
                cfDificultyOld = em.merge(cfDificultyOld);
            }
            if (cfDificultyNew != null && !cfDificultyNew.equals(cfDificultyOld)) {
                cfDificultyNew.getConfigurationList().add(configuration);
                cfDificultyNew = em.merge(cfDificultyNew);
            }
            if (usIdOld != null && !usIdOld.equals(usIdNew)) {
                usIdOld.getConfigurationList().remove(configuration);
                usIdOld = em.merge(usIdOld);
            }
            if (usIdNew != null && !usIdNew.equals(usIdOld)) {
                usIdNew.getConfigurationList().add(configuration);
                usIdNew = em.merge(usIdNew);
            }
            for (Scoreboard scoreboardListNewScoreboard : scoreboardListNew) {
                if (!scoreboardListOld.contains(scoreboardListNewScoreboard)) {
                    Configuration oldCfIdOfScoreboardListNewScoreboard = scoreboardListNewScoreboard.getCfId();
                    scoreboardListNewScoreboard.setCfId(configuration);
                    scoreboardListNewScoreboard = em.merge(scoreboardListNewScoreboard);
                    if (oldCfIdOfScoreboardListNewScoreboard != null && !oldCfIdOfScoreboardListNewScoreboard.equals(configuration)) {
                        oldCfIdOfScoreboardListNewScoreboard.getScoreboardList().remove(scoreboardListNewScoreboard);
                        oldCfIdOfScoreboardListNewScoreboard = em.merge(oldCfIdOfScoreboardListNewScoreboard);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = configuration.getCfId();
                if (findConfiguration(id) == null) {
                    throw new NonexistentEntityException("The configuration with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Configuration configuration;
            try {
                configuration = em.getReference(Configuration.class, id);
                configuration.getCfId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The configuration with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Scoreboard> scoreboardListOrphanCheck = configuration.getScoreboardList();
            for (Scoreboard scoreboardListOrphanCheckScoreboard : scoreboardListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Configuration (" + configuration + ") cannot be destroyed since the Scoreboard " + scoreboardListOrphanCheckScoreboard + " in its scoreboardList field has a non-nullable cfId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Dificulty cfDificulty = configuration.getCfDificulty();
            if (cfDificulty != null) {
                cfDificulty.getConfigurationList().remove(configuration);
                cfDificulty = em.merge(cfDificulty);
            }
            User usId = configuration.getUsId();
            if (usId != null) {
                usId.getConfigurationList().remove(configuration);
                usId = em.merge(usId);
            }
            em.remove(configuration);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Configuration> findConfigurationEntities() {
        return findConfigurationEntities(true, -1, -1);
    }

    public List<Configuration> findConfigurationEntities(int maxResults, int firstResult) {
        return findConfigurationEntities(false, maxResults, firstResult);
    }

    private List<Configuration> findConfigurationEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Configuration.class));
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

    public Configuration findConfiguration(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Configuration.class, id);
        } finally {
            em.close();
        }
    }

    public int getConfigurationCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Configuration> rt = cq.from(Configuration.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
