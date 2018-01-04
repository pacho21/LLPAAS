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
import model.exceptions.PreexistingEntityException;

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
        if (dificulty.getConfigurationCollection() == null) {
            dificulty.setConfigurationCollection(new ArrayList<Configuration>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Configuration> attachedConfigurationCollection = new ArrayList<Configuration>();
            for (Configuration configurationCollectionConfigurationToAttach : dificulty.getConfigurationCollection()) {
                configurationCollectionConfigurationToAttach = em.getReference(configurationCollectionConfigurationToAttach.getClass(), configurationCollectionConfigurationToAttach.getCfId());
                attachedConfigurationCollection.add(configurationCollectionConfigurationToAttach);
            }
            dificulty.setConfigurationCollection(attachedConfigurationCollection);
            em.persist(dificulty);
            for (Configuration configurationCollectionConfiguration : dificulty.getConfigurationCollection()) {
                Dificulty oldCfDificultyOfConfigurationCollectionConfiguration = configurationCollectionConfiguration.getCfDificulty();
                configurationCollectionConfiguration.setCfDificulty(dificulty);
                configurationCollectionConfiguration = em.merge(configurationCollectionConfiguration);
                if (oldCfDificultyOfConfigurationCollectionConfiguration != null) {
                    oldCfDificultyOfConfigurationCollectionConfiguration.getConfigurationCollection().remove(configurationCollectionConfiguration);
                    oldCfDificultyOfConfigurationCollectionConfiguration = em.merge(oldCfDificultyOfConfigurationCollectionConfiguration);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findDificulty(dificulty.getDifName()) != null) {
                throw new PreexistingEntityException("Dificulty " + dificulty + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Dificulty dificulty) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Dificulty persistentDificulty = em.find(Dificulty.class, dificulty.getDifName());
            Collection<Configuration> configurationCollectionOld = persistentDificulty.getConfigurationCollection();
            Collection<Configuration> configurationCollectionNew = dificulty.getConfigurationCollection();
            List<String> illegalOrphanMessages = null;
            for (Configuration configurationCollectionOldConfiguration : configurationCollectionOld) {
                if (!configurationCollectionNew.contains(configurationCollectionOldConfiguration)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Configuration " + configurationCollectionOldConfiguration + " since its cfDificulty field is not nullable.");
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
            dificulty.setConfigurationCollection(configurationCollectionNew);
            dificulty = em.merge(dificulty);
            for (Configuration configurationCollectionNewConfiguration : configurationCollectionNew) {
                if (!configurationCollectionOld.contains(configurationCollectionNewConfiguration)) {
                    Dificulty oldCfDificultyOfConfigurationCollectionNewConfiguration = configurationCollectionNewConfiguration.getCfDificulty();
                    configurationCollectionNewConfiguration.setCfDificulty(dificulty);
                    configurationCollectionNewConfiguration = em.merge(configurationCollectionNewConfiguration);
                    if (oldCfDificultyOfConfigurationCollectionNewConfiguration != null && !oldCfDificultyOfConfigurationCollectionNewConfiguration.equals(dificulty)) {
                        oldCfDificultyOfConfigurationCollectionNewConfiguration.getConfigurationCollection().remove(configurationCollectionNewConfiguration);
                        oldCfDificultyOfConfigurationCollectionNewConfiguration = em.merge(oldCfDificultyOfConfigurationCollectionNewConfiguration);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = dificulty.getDifName();
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

    public void destroy(String id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Dificulty dificulty;
            try {
                dificulty = em.getReference(Dificulty.class, id);
                dificulty.getDifName();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The dificulty with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Configuration> configurationCollectionOrphanCheck = dificulty.getConfigurationCollection();
            for (Configuration configurationCollectionOrphanCheckConfiguration : configurationCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Dificulty (" + dificulty + ") cannot be destroyed since the Configuration " + configurationCollectionOrphanCheckConfiguration + " in its configurationCollection field has a non-nullable cfDificulty field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
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
