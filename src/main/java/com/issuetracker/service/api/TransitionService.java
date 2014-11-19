package com.issuetracker.service.api;

import com.issuetracker.model.Transition;

/**
 *
 * @author mgottval
 */
public interface TransitionService {
    
    void insert(Transition transition);
    
    void remove(Transition transition);
}
