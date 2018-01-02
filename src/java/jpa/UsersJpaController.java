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
public class UsersJpaController implements Serializable {

    public UsersJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Users users) {
        if (users.getConfigurationsCollection() == null) {
            users.setConfigurationsCollection(new ArrayList<Configurations>());
        }
        if (users.getScoreboardCollection() == null) {
            users.setScoreboardCollection(new ArrayList<Scoreboard>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Configurations> attachedConfigurationsCollection = new ArrayList<Configurations>();
            for (Configurations configurationsCollectionConfigurationsToAttach : users.getConfigurationsCollection()) {
                configurationsCollectionConfigurationsToAttach = em.getReference(configurationsCollectionConfigurationsToAttach.getClass(), configurationsCollectionConfigurationsToAttach.getCfId());
                attachedConfigurationsCollection.add(configurationsCollectionConfigurationsToAttach);
            }
            users.setConfigurationsCollection(attachedConfigurationsCollection);
            Collection<Scoreboard> attachedScoreboardCollection = new ArrayList<Scoreboard>();
            for (Scoreboard scoreboardCollectionScoreboardToAttach : users.getScoreboardCollection()) {
                scoreboardCollectionScoreboardToAttach = em.getReference(scoreboardCollectionScoreboardToAttach.getClass(), scoreboardCollectionScoreboardToAttach.getScId());
                attachedScoreboardCollection.add(scoreboardCollectionScoreboardToAttach);
            }
            users.setScoreboardCollection(attachedScoreboardCollection);
            em.persist(users);
            for (Configurations configurationsCollectionConfigurations : users.getConfigurationsCollection()) {
                Users oldUsIdOfConfigurationsCollectionConfigurations = configurationsCollectionConfigurations.getUsId();
                configurationsCollectionConfigurations.setUsId(users);
                configurationsCollectionConfigurations = em.merge(configurationsCollectionConfigurations);
                if (oldUsIdOfConfigurationsCollectionConfigurations != null) {
                    oldUsIdOfConfigurationsCollectionConfigurations.getConfigurationsCollection().remove(configurationsCollectionConfigurations);
                    oldUsIdOfConfigurationsCollectionConfigurations = em.merge(oldUsIdOfConfigurationsCollectionConfigurations);
                }
            }
            for (Scoreboard scoreboardCollectionScoreboard : users.getScoreboardCollection()) {
                Users oldUsIdOfScoreboardCollectionScoreboard = scoreboardCollectionScoreboard.getUsId();
                scoreboardCollectionScoreboard.setUsId(users);
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

    public void edit(Users users) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Users persistentUsers = em.find(Users.class, users.getUsId());
            Collection<Configurations> configurationsCollectionOld = persistentUsers.getConfigurationsCollection();
            Collection<Configurations> configurationsCollectionNew = users.getConfigurationsCollection();
            Collection<Scoreboard> scoreboardCollectionOld = persistentUsers.getScoreboardCollection();
            Collection<Scoreboard> scoreboardCollectionNew = users.getScoreboardCollection();
            Collection<Configurations> attachedConfigurationsCollectionNew = new ArrayList<Configurations>();
            for (Configurations configurationsCollectionNewConfigurationsToAttach : configurationsCollectionNew) {
                configurationsCollectionNewConfigurationsToAttach = em.getReference(configurationsCollectionNewConfigurationsToAttach.getClass(), configurationsCollectionNewConfigurationsToAttach.getCfId());
                attachedConfigurationsCollectionNew.add(configurationsCollectionNewConfigurationsToAttach);
            }
            configurationsCollectionNew = attachedConfigurationsCollectionNew;
            users.setConfigurationsCollection(configurationsCollectionNew);
            Collection<Scoreboard> attachedScoreboardCollectionNew = new ArrayList<Scoreboard>();
            for (Scoreboard scoreboardCollectionNewScoreboardToAttach : scoreboardCollectionNew) {
                scoreboardCollectionNewScoreboardToAttach = em.getReference(scoreboardCollectionNewScoreboardToAttach.getClass(), scoreboardCollectionNewScoreboardToAttach.getScId());
                attachedScoreboardCollectionNew.add(scoreboardCollectionNewScoreboardToAttach);
            }
            scoreboardCollectionNew = attachedScoreboardCollectionNew;
            users.setScoreboardCollection(scoreboardCollectionNew);
            users = em.merge(users);
            for (Configurations configurationsCollectionOldConfigurations : configurationsCollectionOld) {
                if (!configurationsCollectionNew.contains(configurationsCollectionOldConfigurations)) {
                    configurationsCollectionOldConfigurations.setUsId(null);
                    configurationsCollectionOldConfigurations = em.merge(configurationsCollectionOldConfigurations);
                }
            }
            for (Configurations configurationsCollectionNewConfigurations : configurationsCollectionNew) {
                if (!configurationsCollectionOld.contains(configurationsCollectionNewConfigurations)) {
                    Users oldUsIdOfConfigurationsCollectionNewConfigurations = configurationsCollectionNewConfigurations.getUsId();
                    configurationsCollectionNewConfigurations.setUsId(users);
                    configurationsCollectionNewConfigurations = em.merge(configurationsCollectionNewConfigurations);
                    if (oldUsIdOfConfigurationsCollectionNewConfigurations != null && !oldUsIdOfConfigurationsCollectionNewConfigurations.equals(users)) {
                        oldUsIdOfConfigurationsCollectionNewConfigurations.getConfigurationsCollection().remove(configurationsCollectionNewConfigurations);
                        oldUsIdOfConfigurationsCollectionNewConfigurations = em.merge(oldUsIdOfConfigurationsCollectionNewConfigurations);
                    }
                }
            }
            for (Scoreboard scoreboardCollectionOldScoreboard : scoreboardCollectionOld) {
                if (!scoreboardCollectionNew.contains(scoreboardCollectionOldScoreboard)) {
                    scoreboardCollectionOldScoreboard.setUsId(null);
                    scoreboardCollectionOldScoreboard = em.merge(scoreboardCollectionOldScoreboard);
                }
            }
            for (Scoreboard scoreboardCollectionNewScoreboard : scoreboardCollectionNew) {
                if (!scoreboardCollectionOld.contains(scoreboardCollectionNewScoreboard)) {
                    Users oldUsIdOfScoreboardCollectionNewScoreboard = scoreboardCollectionNewScoreboard.getUsId();
                    scoreboardCollectionNewScoreboard.setUsId(users);
                    scoreboardCollectionNewScoreboard = em.merge(scoreboardCollectionNewScoreboard);
                    if (oldUsIdOfScoreboardCollectionNewScoreboard != null && !oldUsIdOfScoreboardCollectionNewScoreboard.equals(users)) {
                        oldUsIdOfScoreboardCollectionNewScoreboard.getScoreboardCollection().remove(scoreboardCollectionNewScoreboard);
                        oldUsIdOfScoreboardCollectionNewScoreboard = em.merge(oldUsIdOfScoreboardCollectionNewScoreboard);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = users.getUsId();
                if (findUsers(id) == null) {
                    throw new NonexistentEntityException("The users with id " + id + " no longer exists.");
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
            Users users;
            try {
                users = em.getReference(Users.class, id);
                users.getUsId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The users with id " + id + " no longer exists.", enfe);
            }
            Collection<Configurations> configurationsCollection = users.getConfigurationsCollection();
            for (Configurations configurationsCollectionConfigurations : configurationsCollection) {
                configurationsCollectionConfigurations.setUsId(null);
                configurationsCollectionConfigurations = em.merge(configurationsCollectionConfigurations);
            }
            Collection<Scoreboard> scoreboardCollection = users.getScoreboardCollection();
            for (Scoreboard scoreboardCollectionScoreboard : scoreboardCollection) {
                scoreboardCollectionScoreboard.setUsId(null);
                scoreboardCollectionScoreboard = em.merge(scoreboardCollectionScoreboard);
            }
            em.remove(users);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Users> findUsersEntities() {
        return findUsersEntities(true, -1, -1);
    }

    public List<Users> findUsersEntities(int maxResults, int firstResult) {
        return findUsersEntities(false, maxResults, firstResult);
    }

    private List<Users> findUsersEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Users.class));
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

    public Users findUsers(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Users.class, id);
        } finally {
            em.close();
        }
    }

    public int getUsersCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Users> rt = cq.from(Users.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
