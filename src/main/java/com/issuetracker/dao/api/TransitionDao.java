package com.issuetracker.dao.api;

import com.issuetracker.model.Transition;
import com.issuetracker.model.Workflow;
import java.util.List;

/**
 *
 * @author mgottval
 */
public interface TransitionDao {
    
    void insertTransition(Transition transition);
    
    List<Transition> getTransitionsByWorkflow(Workflow workflow);
    
    void remove(Transition transition);
}
