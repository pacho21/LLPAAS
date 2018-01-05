/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import model.exceptions.NonexistentEntityException;

/**
 *
 * @author admin
 */
public class ScoreboardJpaController implements Serializable {

    public ScoreboardJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Scoreboard scoreboard) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Configuration cfId = scoreboard.getCfId();
            if (cfId != null) {
                cfId = em.getReference(cfId.getClass(), cfId.getCfId());
                scoreboard.setCfId(cfId);
            }
            User usId = scoreboard.getUsId();
            if (usId != null) {
                usId = em.getReference(usId.getClass(), usId.getUsId());
                scoreboard.setUsId(usId);
            }
            em.persist(scoreboard);
            if (cfId != null) {
                cfId.getScoreboardList().add(scoreboard);
                cfId = em.merge(cfId);
            }
            if (usId != null) {
                usId.getScoreboardList().add(scoreboard);
                usId = em.merge(usId);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Scoreboard scoreboard) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Scoreboard persistentScoreboard = em.find(Scoreboard.class, scoreboard.getScId());
            Configuration cfIdOld = persistentScoreboard.getCfId();
            Configuration cfIdNew = scoreboard.getCfId();
            User usIdOld = persistentScoreboard.getUsId();
            User usIdNew = scoreboard.getUsId();
            if (cfIdNew != null) {
                cfIdNew = em.getReference(cfIdNew.getClass(), cfIdNew.getCfId());
                scoreboard.setCfId(cfIdNew);
            }
            if (usIdNew != null) {
                usIdNew = em.getReference(usIdNew.getClass(), usIdNew.getUsId());
                scoreboard.setUsId(usIdNew);
            }
            scoreboard = em.merge(scoreboard);
            if (cfIdOld != null && !cfIdOld.equals(cfIdNew)) {
                cfIdOld.getScoreboardList().remove(scoreboard);
                cfIdOld = em.merge(cfIdOld);
            }
            if (cfIdNew != null && !cfIdNew.equals(cfIdOld)) {
                cfIdNew.getScoreboardList().add(scoreboard);
                cfIdNew = em.merge(cfIdNew);
            }
            if (usIdOld != null && !usIdOld.equals(usIdNew)) {
                usIdOld.getScoreboardList().remove(scoreboard);
                usIdOld = em.merge(usIdOld);
            }
            if (usIdNew != null && !usIdNew.equals(usIdOld)) {
                usIdNew.getScoreboardList().add(scoreboard);
                usIdNew = em.merge(usIdNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = scoreboard.getScId();
                if (findScoreboard(id) == null) {
                    throw new NonexistentEntityException("The scoreboard with id " + id + " no longer exists.");
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
            Scoreboard scoreboard;
            try {
                scoreboard = em.getReference(Scoreboard.class, id);
                scoreboard.getScId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The scoreboard with id " + id + " no longer exists.", enfe);
            }
            Configuration cfId = scoreboard.getCfId();
            if (cfId != null) {
                cfId.getScoreboardList().remove(scoreboard);
                cfId = em.merge(cfId);
            }
            User usId = scoreboard.getUsId();
            if (usId != null) {
                usId.getScoreboardList().remove(scoreboard);
                usId = em.merge(usId);
            }
            em.remove(scoreboard);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Scoreboard> findScoreboardEntities() {
        return findScoreboardEntities(true, -1, -1);
    }

    public List<Scoreboard> findScoreboardEntities(int maxResults, int firstResult) {
        return findScoreboardEntities(false, maxResults, firstResult);
    }

    private List<Scoreboard> findScoreboardEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Scoreboard.class));
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

    public Scoreboard findScoreboard(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Scoreboard.class, id);
        } finally {
            em.close();
        }
    }

    public int getScoreboardCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Scoreboard> rt = cq.from(Scoreboard.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
