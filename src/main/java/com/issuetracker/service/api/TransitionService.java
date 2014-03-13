package com.issuetracker.service.api;

import com.issuetracker.model.Transition;
import com.issuetracker.model.Workflow;

import java.util.List;

/**
 *
 * @author mgottval
 */
public interface TransitionService {
    
    void insert(Transition transition);
    
    List<Transition> getTransitionsByWorkflow(Workflow workflow);
    
    void remove(Transition transition);
}
