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
        if (user.getConfigurationList() == null) {
            user.setConfigurationList(new ArrayList<Configuration>());
        }
        if (user.getScoreboardList() == null) {
            user.setScoreboardList(new ArrayList<Scoreboard>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Configuration> attachedConfigurationList = new ArrayList<Configuration>();
            for (Configuration configurationListConfigurationToAttach : user.getConfigurationList()) {
                configurationListConfigurationToAttach = em.getReference(configurationListConfigurationToAttach.getClass(), configurationListConfigurationToAttach.getCfId());
                attachedConfigurationList.add(configurationListConfigurationToAttach);
            }
            user.setConfigurationList(attachedConfigurationList);
            List<Scoreboard> attachedScoreboardList = new ArrayList<Scoreboard>();
            for (Scoreboard scoreboardListScoreboardToAttach : user.getScoreboardList()) {
                scoreboardListScoreboardToAttach = em.getReference(scoreboardListScoreboardToAttach.getClass(), scoreboardListScoreboardToAttach.getScId());
                attachedScoreboardList.add(scoreboardListScoreboardToAttach);
            }
            user.setScoreboardList(attachedScoreboardList);
            em.persist(user);
            for (Configuration configurationListConfiguration : user.getConfigurationList()) {
                User oldUsIdOfConfigurationListConfiguration = configurationListConfiguration.getUsId();
                configurationListConfiguration.setUsId(user);
                configurationListConfiguration = em.merge(configurationListConfiguration);
                if (oldUsIdOfConfigurationListConfiguration != null) {
                    oldUsIdOfConfigurationListConfiguration.getConfigurationList().remove(configurationListConfiguration);
                    oldUsIdOfConfigurationListConfiguration = em.merge(oldUsIdOfConfigurationListConfiguration);
                }
            }
            for (Scoreboard scoreboardListScoreboard : user.getScoreboardList()) {
                User oldUsIdOfScoreboardListScoreboard = scoreboardListScoreboard.getUsId();
                scoreboardListScoreboard.setUsId(user);
                scoreboardListScoreboard = em.merge(scoreboardListScoreboard);
                if (oldUsIdOfScoreboardListScoreboard != null) {
                    oldUsIdOfScoreboardListScoreboard.getScoreboardList().remove(scoreboardListScoreboard);
                    oldUsIdOfScoreboardListScoreboard = em.merge(oldUsIdOfScoreboardListScoreboard);
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
            List<Configuration> configurationListOld = persistentUser.getConfigurationList();
            List<Configuration> configurationListNew = user.getConfigurationList();
            List<Scoreboard> scoreboardListOld = persistentUser.getScoreboardList();
            List<Scoreboard> scoreboardListNew = user.getScoreboardList();
            List<String> illegalOrphanMessages = null;
            for (Configuration configurationListOldConfiguration : configurationListOld) {
                if (!configurationListNew.contains(configurationListOldConfiguration)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Configuration " + configurationListOldConfiguration + " since its usId field is not nullable.");
                }
            }
            for (Scoreboard scoreboardListOldScoreboard : scoreboardListOld) {
                if (!scoreboardListNew.contains(scoreboardListOldScoreboard)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Scoreboard " + scoreboardListOldScoreboard + " since its usId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Configuration> attachedConfigurationListNew = new ArrayList<Configuration>();
            for (Configuration configurationListNewConfigurationToAttach : configurationListNew) {
                configurationListNewConfigurationToAttach = em.getReference(configurationListNewConfigurationToAttach.getClass(), configurationListNewConfigurationToAttach.getCfId());
                attachedConfigurationListNew.add(configurationListNewConfigurationToAttach);
            }
            configurationListNew = attachedConfigurationListNew;
            user.setConfigurationList(configurationListNew);
            List<Scoreboard> attachedScoreboardListNew = new ArrayList<Scoreboard>();
            for (Scoreboard scoreboardListNewScoreboardToAttach : scoreboardListNew) {
                scoreboardListNewScoreboardToAttach = em.getReference(scoreboardListNewScoreboardToAttach.getClass(), scoreboardListNewScoreboardToAttach.getScId());
                attachedScoreboardListNew.add(scoreboardListNewScoreboardToAttach);
            }
            scoreboardListNew = attachedScoreboardListNew;
            user.setScoreboardList(scoreboardListNew);
            user = em.merge(user);
            for (Configuration configurationListNewConfiguration : configurationListNew) {
                if (!configurationListOld.contains(configurationListNewConfiguration)) {
                    User oldUsIdOfConfigurationListNewConfiguration = configurationListNewConfiguration.getUsId();
                    configurationListNewConfiguration.setUsId(user);
                    configurationListNewConfiguration = em.merge(configurationListNewConfiguration);
                    if (oldUsIdOfConfigurationListNewConfiguration != null && !oldUsIdOfConfigurationListNewConfiguration.equals(user)) {
                        oldUsIdOfConfigurationListNewConfiguration.getConfigurationList().remove(configurationListNewConfiguration);
                        oldUsIdOfConfigurationListNewConfiguration = em.merge(oldUsIdOfConfigurationListNewConfiguration);
                    }
                }
            }
            for (Scoreboard scoreboardListNewScoreboard : scoreboardListNew) {
                if (!scoreboardListOld.contains(scoreboardListNewScoreboard)) {
                    User oldUsIdOfScoreboardListNewScoreboard = scoreboardListNewScoreboard.getUsId();
                    scoreboardListNewScoreboard.setUsId(user);
                    scoreboardListNewScoreboard = em.merge(scoreboardListNewScoreboard);
                    if (oldUsIdOfScoreboardListNewScoreboard != null && !oldUsIdOfScoreboardListNewScoreboard.equals(user)) {
                        oldUsIdOfScoreboardListNewScoreboard.getScoreboardList().remove(scoreboardListNewScoreboard);
                        oldUsIdOfScoreboardListNewScoreboard = em.merge(oldUsIdOfScoreboardListNewScoreboard);
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
            List<Configuration> configurationListOrphanCheck = user.getConfigurationList();
            for (Configuration configurationListOrphanCheckConfiguration : configurationListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This User (" + user + ") cannot be destroyed since the Configuration " + configurationListOrphanCheckConfiguration + " in its configurationList field has a non-nullable usId field.");
            }
            List<Scoreboard> scoreboardListOrphanCheck = user.getScoreboardList();
            for (Scoreboard scoreboardListOrphanCheckScoreboard : scoreboardListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This User (" + user + ") cannot be destroyed since the Scoreboard " + scoreboardListOrphanCheckScoreboard + " in its scoreboardList field has a non-nullable usId field.");
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
