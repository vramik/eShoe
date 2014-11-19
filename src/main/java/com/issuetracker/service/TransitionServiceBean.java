package com.issuetracker.service;

import com.issuetracker.dao.api.TransitionDao;
import com.issuetracker.model.Transition;
import com.issuetracker.service.api.TransitionService;
import java.io.Serializable;

import javax.ejb.Stateless;
import javax.inject.Inject;

/**
 *
 * @author mgottval
 */
@Stateless
public class TransitionServiceBean implements TransitionService, Serializable {

    @Inject
    private TransitionDao transitionDao;
    
    @Override
    public void insert(Transition transition) {
        transitionDao.insert(transition);
    }

    @Override
    public void remove(Transition transition) {
        transitionDao.remove(transition);
    }
}
