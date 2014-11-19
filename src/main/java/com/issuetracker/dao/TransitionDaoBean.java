package com.issuetracker.dao;

import com.issuetracker.dao.api.TransitionDao;
import com.issuetracker.model.Transition;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;

/**
 *
 * @author mgottval
 */
@Stateless
public class TransitionDaoBean implements TransitionDao{

    @PersistenceContext
    private EntityManager em;
    private CriteriaBuilder qb;
    
    @Override
    public void insert(Transition transition) {
        em.persist(transition);
    }

    @Override
    public void remove(Transition transition) {
        em.remove(em.contains(transition) ? transition : em.merge(transition));
    }
    
}
