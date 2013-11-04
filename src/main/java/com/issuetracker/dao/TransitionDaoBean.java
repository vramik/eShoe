package com.issuetracker.dao;

import com.issuetracker.dao.api.TransitionDao;
import com.issuetracker.model.Transition;
import com.issuetracker.model.Workflow;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

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
    public void insertTransition(Transition transition) {
        em.persist(transition);
    }

    @Override
    public List<Transition> getTransitionsByWorkflow(Workflow workflow) {
        qb = em.getCriteriaBuilder();
        CriteriaQuery<Transition> transitionQuery = qb.createQuery(Transition.class);
        Root<Transition> p = transitionQuery.from(Transition.class);
        Predicate pCondition = qb.equal(p.get("workflow"), workflow);
        transitionQuery.where(pCondition);
        TypedQuery<Transition> pQuery = em.createQuery(transitionQuery);
        List<Transition> results = pQuery.getResultList();
        if (results != null && !results.isEmpty()) {
            return results;
        } else {
            return null;
        }
    }

    @Override
    public void remove(Transition transition) {
        em.remove(em.contains(transition) ? transition : em.merge(transition));
    }
    
}
