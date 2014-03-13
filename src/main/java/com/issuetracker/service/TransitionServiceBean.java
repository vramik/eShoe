package com.issuetracker.service;

import com.issuetracker.dao.api.TransitionDao;
import com.issuetracker.model.Transition;
import com.issuetracker.model.Workflow;
import com.issuetracker.service.api.TransitionService;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author mgottval
 */
@Stateless
public class TransitionServiceBean implements TransitionService {

    @Inject
    private TransitionDao transitionDao;
    
    @Override
    public void insert(Transition transition) {
        transitionDao.insert(transition);
    }

    @Override
    public List<Transition> getTransitionsByWorkflow(Workflow workflow) {
        return transitionDao.getTransitionsByWorkflow(workflow);
    }

    @Override
    public void remove(Transition transition) {
        transitionDao.remove(transition);
    }
}
