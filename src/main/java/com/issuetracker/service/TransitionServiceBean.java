package com.issuetracker.service;

import com.issuetracker.dao.api.TransitionDao;
import com.issuetracker.model.Transition;
import com.issuetracker.model.Workflow;
import com.issuetracker.service.api.TransitionService;

import javax.ejb.Stateless;
import javax.inject.Inject;
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
