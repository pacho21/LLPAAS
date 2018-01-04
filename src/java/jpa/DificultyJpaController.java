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
import jpa.exceptions.PreexistingEntityException;

/**
 *
 * @author admin
 */
public class DificultyJpaController implements Serializable {

    public DificultyJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Dificulty dificulty) throws PreexistingEntityException, Exception {
        if (dificulty.getConfigurationsCollection() == null) {
            dificulty.setConfigurationsCollection(new ArrayList<Configurations>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Configurations> attachedConfigurationsCollection = new ArrayList<Configurations>();
            for (Configurations configurationsCollectionConfigurationsToAttach : dificulty.getConfigurationsCollection()) {
                configurationsCollectionConfigurationsToAttach = em.getReference(configurationsCollectionConfigurationsToAttach.getClass(), configurationsCollectionConfigurationsToAttach.getCfId());
                attachedConfigurationsCollection.add(configurationsCollectionConfigurationsToAttach);
            }
            dificulty.setConfigurationsCollection(attachedConfigurationsCollection);
            em.persist(dificulty);
            for (Configurations configurationsCollectionConfigurations : dificulty.getConfigurationsCollection()) {
                Dificulty oldCfDificultyOfConfigurationsCollectionConfigurations = configurationsCollectionConfigurations.getCfDificulty();
                configurationsCollectionConfigurations.setCfDificulty(dificulty);
                configurationsCollectionConfigurations = em.merge(configurationsCollectionConfigurations);
                if (oldCfDificultyOfConfigurationsCollectionConfigurations != null) {
                    oldCfDificultyOfConfigurationsCollectionConfigurations.getConfigurationsCollection().remove(configurationsCollectionConfigurations);
                    oldCfDificultyOfConfigurationsCollectionConfigurations = em.merge(oldCfDificultyOfConfigurationsCollectionConfigurations);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findDificulty(dificulty.getDificultyName()) != null) {
                throw new PreexistingEntityException("Dificulty " + dificulty + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Dificulty dificulty) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Dificulty persistentDificulty = em.find(Dificulty.class, dificulty.getDificultyName());
            Collection<Configurations> configurationsCollectionOld = persistentDificulty.getConfigurationsCollection();
            Collection<Configurations> configurationsCollectionNew = dificulty.getConfigurationsCollection();
            Collection<Configurations> attachedConfigurationsCollectionNew = new ArrayList<Configurations>();
            for (Configurations configurationsCollectionNewConfigurationsToAttach : configurationsCollectionNew) {
                configurationsCollectionNewConfigurationsToAttach = em.getReference(configurationsCollectionNewConfigurationsToAttach.getClass(), configurationsCollectionNewConfigurationsToAttach.getCfId());
                attachedConfigurationsCollectionNew.add(configurationsCollectionNewConfigurationsToAttach);
            }
            configurationsCollectionNew = attachedConfigurationsCollectionNew;
            dificulty.setConfigurationsCollection(configurationsCollectionNew);
            dificulty = em.merge(dificulty);
            for (Configurations configurationsCollectionOldConfigurations : configurationsCollectionOld) {
                if (!configurationsCollectionNew.contains(configurationsCollectionOldConfigurations)) {
                    configurationsCollectionOldConfigurations.setCfDificulty(null);
                    configurationsCollectionOldConfigurations = em.merge(configurationsCollectionOldConfigurations);
                }
            }
            for (Configurations configurationsCollectionNewConfigurations : configurationsCollectionNew) {
                if (!configurationsCollectionOld.contains(configurationsCollectionNewConfigurations)) {
                    Dificulty oldCfDificultyOfConfigurationsCollectionNewConfigurations = configurationsCollectionNewConfigurations.getCfDificulty();
                    configurationsCollectionNewConfigurations.setCfDificulty(dificulty);
                    configurationsCollectionNewConfigurations = em.merge(configurationsCollectionNewConfigurations);
                    if (oldCfDificultyOfConfigurationsCollectionNewConfigurations != null && !oldCfDificultyOfConfigurationsCollectionNewConfigurations.equals(dificulty)) {
                        oldCfDificultyOfConfigurationsCollectionNewConfigurations.getConfigurationsCollection().remove(configurationsCollectionNewConfigurations);
                        oldCfDificultyOfConfigurationsCollectionNewConfigurations = em.merge(oldCfDificultyOfConfigurationsCollectionNewConfigurations);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = dificulty.getDificultyName();
                if (findDificulty(id) == null) {
                    throw new NonexistentEntityException("The dificulty with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(String id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Dificulty dificulty;
            try {
                dificulty = em.getReference(Dificulty.class, id);
                dificulty.getDificultyName();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The dificulty with id " + id + " no longer exists.", enfe);
            }
            Collection<Configurations> configurationsCollection = dificulty.getConfigurationsCollection();
            for (Configurations configurationsCollectionConfigurations : configurationsCollection) {
                configurationsCollectionConfigurations.setCfDificulty(null);
                configurationsCollectionConfigurations = em.merge(configurationsCollectionConfigurations);
            }
            em.remove(dificulty);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Dificulty> findDificultyEntities() {
        return findDificultyEntities(true, -1, -1);
    }

    public List<Dificulty> findDificultyEntities(int maxResults, int firstResult) {
        return findDificultyEntities(false, maxResults, firstResult);
    }

    private List<Dificulty> findDificultyEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Dificulty.class));
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

    public Dificulty findDificulty(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Dificulty.class, id);
        } finally {
            em.close();
        }
    }

    public int getDificultyCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Dificulty> rt = cq.from(Dificulty.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
