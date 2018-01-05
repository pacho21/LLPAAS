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
        if (dificulty.getConfigurationList() == null) {
            dificulty.setConfigurationList(new ArrayList<Configuration>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Configuration> attachedConfigurationList = new ArrayList<Configuration>();
            for (Configuration configurationListConfigurationToAttach : dificulty.getConfigurationList()) {
                configurationListConfigurationToAttach = em.getReference(configurationListConfigurationToAttach.getClass(), configurationListConfigurationToAttach.getCfId());
                attachedConfigurationList.add(configurationListConfigurationToAttach);
            }
            dificulty.setConfigurationList(attachedConfigurationList);
            em.persist(dificulty);
            for (Configuration configurationListConfiguration : dificulty.getConfigurationList()) {
                Dificulty oldCfDificultyOfConfigurationListConfiguration = configurationListConfiguration.getCfDificulty();
                configurationListConfiguration.setCfDificulty(dificulty);
                configurationListConfiguration = em.merge(configurationListConfiguration);
                if (oldCfDificultyOfConfigurationListConfiguration != null) {
                    oldCfDificultyOfConfigurationListConfiguration.getConfigurationList().remove(configurationListConfiguration);
                    oldCfDificultyOfConfigurationListConfiguration = em.merge(oldCfDificultyOfConfigurationListConfiguration);
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
            List<Configuration> configurationListOld = persistentDificulty.getConfigurationList();
            List<Configuration> configurationListNew = dificulty.getConfigurationList();
            List<String> illegalOrphanMessages = null;
            for (Configuration configurationListOldConfiguration : configurationListOld) {
                if (!configurationListNew.contains(configurationListOldConfiguration)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Configuration " + configurationListOldConfiguration + " since its cfDificulty field is not nullable.");
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
            dificulty.setConfigurationList(configurationListNew);
            dificulty = em.merge(dificulty);
            for (Configuration configurationListNewConfiguration : configurationListNew) {
                if (!configurationListOld.contains(configurationListNewConfiguration)) {
                    Dificulty oldCfDificultyOfConfigurationListNewConfiguration = configurationListNewConfiguration.getCfDificulty();
                    configurationListNewConfiguration.setCfDificulty(dificulty);
                    configurationListNewConfiguration = em.merge(configurationListNewConfiguration);
                    if (oldCfDificultyOfConfigurationListNewConfiguration != null && !oldCfDificultyOfConfigurationListNewConfiguration.equals(dificulty)) {
                        oldCfDificultyOfConfigurationListNewConfiguration.getConfigurationList().remove(configurationListNewConfiguration);
                        oldCfDificultyOfConfigurationListNewConfiguration = em.merge(oldCfDificultyOfConfigurationListNewConfiguration);
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
            List<Configuration> configurationListOrphanCheck = dificulty.getConfigurationList();
            for (Configuration configurationListOrphanCheckConfiguration : configurationListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Dificulty (" + dificulty + ") cannot be destroyed since the Configuration " + configurationListOrphanCheckConfiguration + " in its configurationList field has a non-nullable cfDificulty field.");
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
