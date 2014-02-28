package com.issuetracker.dao;

import com.issuetracker.dao.api.ComponentDao;
import com.issuetracker.model.Component;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;


/**
 *
 * @author mgottval
 */
@Stateless
public class ComponentDaoBean implements ComponentDao{
    
    @PersistenceContext
    private EntityManager em;
    private CriteriaBuilder qb;

    @Override
    public void insertComponent(Component component) {
        em.persist(component);
    }

    @Override
    public List<Component> getComponents() {
        qb = em.getCriteriaBuilder();
        CriteriaQuery<Component> c = qb.createQuery(Component.class);
        Root<Component> i = c.from(Component.class);
        TypedQuery<Component> q = em.createQuery(c);
        List<Component> results = q.getResultList();
        if (results != null && !results.isEmpty()) {
            return results;
        }
        return null;
    }

    @Override
    public void remove(Component component) {
        em.remove(em.merge(component));
    }
    
}
