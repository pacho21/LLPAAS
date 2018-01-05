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
import java.util.Collection;
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
        if (configuration.getScoreboardCollection() == null) {
            configuration.setScoreboardCollection(new ArrayList<Scoreboard>());
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
            Collection<Scoreboard> attachedScoreboardCollection = new ArrayList<Scoreboard>();
            for (Scoreboard scoreboardCollectionScoreboardToAttach : configuration.getScoreboardCollection()) {
                scoreboardCollectionScoreboardToAttach = em.getReference(scoreboardCollectionScoreboardToAttach.getClass(), scoreboardCollectionScoreboardToAttach.getScId());
                attachedScoreboardCollection.add(scoreboardCollectionScoreboardToAttach);
            }
            configuration.setScoreboardCollection(attachedScoreboardCollection);
            em.persist(configuration);
            if (cfDificulty != null) {
                cfDificulty.getConfigurationCollection().add(configuration);
                cfDificulty = em.merge(cfDificulty);
            }
            if (usId != null) {
                usId.getConfigurationCollection().add(configuration);
                usId = em.merge(usId);
            }
            for (Scoreboard scoreboardCollectionScoreboard : configuration.getScoreboardCollection()) {
                Configuration oldCfIdOfScoreboardCollectionScoreboard = scoreboardCollectionScoreboard.getCfId();
                scoreboardCollectionScoreboard.setCfId(configuration);
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
            Collection<Scoreboard> scoreboardCollectionOld = persistentConfiguration.getScoreboardCollection();
            Collection<Scoreboard> scoreboardCollectionNew = configuration.getScoreboardCollection();
            List<String> illegalOrphanMessages = null;
            for (Scoreboard scoreboardCollectionOldScoreboard : scoreboardCollectionOld) {
                if (!scoreboardCollectionNew.contains(scoreboardCollectionOldScoreboard)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Scoreboard " + scoreboardCollectionOldScoreboard + " since its cfId field is not nullable.");
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
            Collection<Scoreboard> attachedScoreboardCollectionNew = new ArrayList<Scoreboard>();
            for (Scoreboard scoreboardCollectionNewScoreboardToAttach : scoreboardCollectionNew) {
                scoreboardCollectionNewScoreboardToAttach = em.getReference(scoreboardCollectionNewScoreboardToAttach.getClass(), scoreboardCollectionNewScoreboardToAttach.getScId());
                attachedScoreboardCollectionNew.add(scoreboardCollectionNewScoreboardToAttach);
            }
            scoreboardCollectionNew = attachedScoreboardCollectionNew;
            configuration.setScoreboardCollection(scoreboardCollectionNew);
            configuration = em.merge(configuration);
            if (cfDificultyOld != null && !cfDificultyOld.equals(cfDificultyNew)) {
                cfDificultyOld.getConfigurationCollection().remove(configuration);
                cfDificultyOld = em.merge(cfDificultyOld);
            }
            if (cfDificultyNew != null && !cfDificultyNew.equals(cfDificultyOld)) {
                cfDificultyNew.getConfigurationCollection().add(configuration);
                cfDificultyNew = em.merge(cfDificultyNew);
            }
            if (usIdOld != null && !usIdOld.equals(usIdNew)) {
                usIdOld.getConfigurationCollection().remove(configuration);
                usIdOld = em.merge(usIdOld);
            }
            if (usIdNew != null && !usIdNew.equals(usIdOld)) {
                usIdNew.getConfigurationCollection().add(configuration);
                usIdNew = em.merge(usIdNew);
            }
            for (Scoreboard scoreboardCollectionNewScoreboard : scoreboardCollectionNew) {
                if (!scoreboardCollectionOld.contains(scoreboardCollectionNewScoreboard)) {
                    Configuration oldCfIdOfScoreboardCollectionNewScoreboard = scoreboardCollectionNewScoreboard.getCfId();
                    scoreboardCollectionNewScoreboard.setCfId(configuration);
                    scoreboardCollectionNewScoreboard = em.merge(scoreboardCollectionNewScoreboard);
                    if (oldCfIdOfScoreboardCollectionNewScoreboard != null && !oldCfIdOfScoreboardCollectionNewScoreboard.equals(configuration)) {
                        oldCfIdOfScoreboardCollectionNewScoreboard.getScoreboardCollection().remove(scoreboardCollectionNewScoreboard);
                        oldCfIdOfScoreboardCollectionNewScoreboard = em.merge(oldCfIdOfScoreboardCollectionNewScoreboard);
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
            Collection<Scoreboard> scoreboardCollectionOrphanCheck = configuration.getScoreboardCollection();
            for (Scoreboard scoreboardCollectionOrphanCheckScoreboard : scoreboardCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Configuration (" + configuration + ") cannot be destroyed since the Scoreboard " + scoreboardCollectionOrphanCheckScoreboard + " in its scoreboardCollection field has a non-nullable cfId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Dificulty cfDificulty = configuration.getCfDificulty();
            if (cfDificulty != null) {
                cfDificulty.getConfigurationCollection().remove(configuration);
                cfDificulty = em.merge(cfDificulty);
            }
            User usId = configuration.getUsId();
            if (usId != null) {
                usId.getConfigurationCollection().remove(configuration);
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

    public Boolean existByConfigName(String configname, User user) {
        EntityManager em = getEntityManager();
        try {
            List<User> list = em.createNamedQuery("Configuration.findByConfignameAndUserId").setParameter("configname", configname).setParameter("userId", user).getResultList();
            return !list.isEmpty();

        } finally {
            em.close();
        }
    }

}
