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
import javax.persistence.NoResultException;
import model.exceptions.IllegalOrphanException;
import model.exceptions.NonexistentEntityException;

/**
 *
 * @author admin
 */
public class UserJpaController implements Serializable {

    public UserJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(User user) {
        if (user.getConfigurationCollection() == null) {
            user.setConfigurationCollection(new ArrayList<Configuration>());
        }
        if (user.getScoreboardCollection() == null) {
            user.setScoreboardCollection(new ArrayList<Scoreboard>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Configuration> attachedConfigurationCollection = new ArrayList<Configuration>();
            for (Configuration configurationCollectionConfigurationToAttach : user.getConfigurationCollection()) {
                configurationCollectionConfigurationToAttach = em.getReference(configurationCollectionConfigurationToAttach.getClass(), configurationCollectionConfigurationToAttach.getCfId());
                attachedConfigurationCollection.add(configurationCollectionConfigurationToAttach);
            }
            user.setConfigurationCollection(attachedConfigurationCollection);
            Collection<Scoreboard> attachedScoreboardCollection = new ArrayList<Scoreboard>();
            for (Scoreboard scoreboardCollectionScoreboardToAttach : user.getScoreboardCollection()) {
                scoreboardCollectionScoreboardToAttach = em.getReference(scoreboardCollectionScoreboardToAttach.getClass(), scoreboardCollectionScoreboardToAttach.getScId());
                attachedScoreboardCollection.add(scoreboardCollectionScoreboardToAttach);
            }
            user.setScoreboardCollection(attachedScoreboardCollection);
            em.persist(user);
            for (Configuration configurationCollectionConfiguration : user.getConfigurationCollection()) {
                User oldUsIdOfConfigurationCollectionConfiguration = configurationCollectionConfiguration.getUsId();
                configurationCollectionConfiguration.setUsId(user);
                configurationCollectionConfiguration = em.merge(configurationCollectionConfiguration);
                if (oldUsIdOfConfigurationCollectionConfiguration != null) {
                    oldUsIdOfConfigurationCollectionConfiguration.getConfigurationCollection().remove(configurationCollectionConfiguration);
                    oldUsIdOfConfigurationCollectionConfiguration = em.merge(oldUsIdOfConfigurationCollectionConfiguration);
                }
            }
            for (Scoreboard scoreboardCollectionScoreboard : user.getScoreboardCollection()) {
                User oldUsIdOfScoreboardCollectionScoreboard = scoreboardCollectionScoreboard.getUsId();
                scoreboardCollectionScoreboard.setUsId(user);
                scoreboardCollectionScoreboard = em.merge(scoreboardCollectionScoreboard);
                if (oldUsIdOfScoreboardCollectionScoreboard != null) {
                    oldUsIdOfScoreboardCollectionScoreboard.getScoreboardCollection().remove(scoreboardCollectionScoreboard);
                    oldUsIdOfScoreboardCollectionScoreboard = em.merge(oldUsIdOfScoreboardCollectionScoreboard);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(User user) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            User persistentUser = em.find(User.class, user.getUsId());
            Collection<Configuration> configurationCollectionOld = persistentUser.getConfigurationCollection();
            Collection<Configuration> configurationCollectionNew = user.getConfigurationCollection();
            Collection<Scoreboard> scoreboardCollectionOld = persistentUser.getScoreboardCollection();
            Collection<Scoreboard> scoreboardCollectionNew = user.getScoreboardCollection();
            List<String> illegalOrphanMessages = null;
            for (Configuration configurationCollectionOldConfiguration : configurationCollectionOld) {
                if (!configurationCollectionNew.contains(configurationCollectionOldConfiguration)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Configuration " + configurationCollectionOldConfiguration + " since its usId field is not nullable.");
                }
            }
            for (Scoreboard scoreboardCollectionOldScoreboard : scoreboardCollectionOld) {
                if (!scoreboardCollectionNew.contains(scoreboardCollectionOldScoreboard)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Scoreboard " + scoreboardCollectionOldScoreboard + " since its usId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<Configuration> attachedConfigurationCollectionNew = new ArrayList<Configuration>();
            for (Configuration configurationCollectionNewConfigurationToAttach : configurationCollectionNew) {
                configurationCollectionNewConfigurationToAttach = em.getReference(configurationCollectionNewConfigurationToAttach.getClass(), configurationCollectionNewConfigurationToAttach.getCfId());
                attachedConfigurationCollectionNew.add(configurationCollectionNewConfigurationToAttach);
            }
            configurationCollectionNew = attachedConfigurationCollectionNew;
            user.setConfigurationCollection(configurationCollectionNew);
            Collection<Scoreboard> attachedScoreboardCollectionNew = new ArrayList<Scoreboard>();
            for (Scoreboard scoreboardCollectionNewScoreboardToAttach : scoreboardCollectionNew) {
                scoreboardCollectionNewScoreboardToAttach = em.getReference(scoreboardCollectionNewScoreboardToAttach.getClass(), scoreboardCollectionNewScoreboardToAttach.getScId());
                attachedScoreboardCollectionNew.add(scoreboardCollectionNewScoreboardToAttach);
            }
            scoreboardCollectionNew = attachedScoreboardCollectionNew;
            user.setScoreboardCollection(scoreboardCollectionNew);
            user = em.merge(user);
            for (Configuration configurationCollectionNewConfiguration : configurationCollectionNew) {
                if (!configurationCollectionOld.contains(configurationCollectionNewConfiguration)) {
                    User oldUsIdOfConfigurationCollectionNewConfiguration = configurationCollectionNewConfiguration.getUsId();
                    configurationCollectionNewConfiguration.setUsId(user);
                    configurationCollectionNewConfiguration = em.merge(configurationCollectionNewConfiguration);
                    if (oldUsIdOfConfigurationCollectionNewConfiguration != null && !oldUsIdOfConfigurationCollectionNewConfiguration.equals(user)) {
                        oldUsIdOfConfigurationCollectionNewConfiguration.getConfigurationCollection().remove(configurationCollectionNewConfiguration);
                        oldUsIdOfConfigurationCollectionNewConfiguration = em.merge(oldUsIdOfConfigurationCollectionNewConfiguration);
                    }
                }
            }
            for (Scoreboard scoreboardCollectionNewScoreboard : scoreboardCollectionNew) {
                if (!scoreboardCollectionOld.contains(scoreboardCollectionNewScoreboard)) {
                    User oldUsIdOfScoreboardCollectionNewScoreboard = scoreboardCollectionNewScoreboard.getUsId();
                    scoreboardCollectionNewScoreboard.setUsId(user);
                    scoreboardCollectionNewScoreboard = em.merge(scoreboardCollectionNewScoreboard);
                    if (oldUsIdOfScoreboardCollectionNewScoreboard != null && !oldUsIdOfScoreboardCollectionNewScoreboard.equals(user)) {
                        oldUsIdOfScoreboardCollectionNewScoreboard.getScoreboardCollection().remove(scoreboardCollectionNewScoreboard);
                        oldUsIdOfScoreboardCollectionNewScoreboard = em.merge(oldUsIdOfScoreboardCollectionNewScoreboard);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = user.getUsId();
                if (findUser(id) == null) {
                    throw new NonexistentEntityException("The user with id " + id + " no longer exists.");
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
            User user;
            try {
                user = em.getReference(User.class, id);
                user.getUsId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The user with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Configuration> configurationCollectionOrphanCheck = user.getConfigurationCollection();
            for (Configuration configurationCollectionOrphanCheckConfiguration : configurationCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This User (" + user + ") cannot be destroyed since the Configuration " + configurationCollectionOrphanCheckConfiguration + " in its configurationCollection field has a non-nullable usId field.");
            }
            Collection<Scoreboard> scoreboardCollectionOrphanCheck = user.getScoreboardCollection();
            for (Scoreboard scoreboardCollectionOrphanCheckScoreboard : scoreboardCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This User (" + user + ") cannot be destroyed since the Scoreboard " + scoreboardCollectionOrphanCheckScoreboard + " in its scoreboardCollection field has a non-nullable usId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(user);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<User> findUserEntities() {
        return findUserEntities(true, -1, -1);
    }

    public List<User> findUserEntities(int maxResults, int firstResult) {
        return findUserEntities(false, maxResults, firstResult);
    }

    private List<User> findUserEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(User.class));
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

    public User findUser(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(User.class, id);
        } finally {
            em.close();
        }
    }

    public int getUserCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<User> rt = cq.from(User.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

    /**
     * Find a user.
     *
     * @param username
     * @return Object User in case you find it, otherwise null.
     */
    public User findUserByUsername(String username) {
        EntityManager em = getEntityManager();
        try {
            return (User) em.createNamedQuery("User.findByUsername").setParameter("username", username).getSingleResult();
        } catch (NoResultException e) {
            return null;
        } finally {
            em.close();
        }
    }

    /**
     * Check if the username exists in the database.
     *
     * @param username
     * @return If the user does not exist it returns false otherwise true.
     */
    public Boolean existByUsername(String username) {
        EntityManager em = getEntityManager();
        try {
            List<User> list = em.createNamedQuery("User.findByUsername").setParameter("username", username).getResultList();
            return !list.isEmpty();

        } finally {
            em.close();
        }
    }

}
