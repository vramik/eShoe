package com.issuetracker.dao.api;

import com.issuetracker.model.Transition;

/**
 *
 * @author mgottval
 */
public interface TransitionDao {
    
    void insert(Transition transition);
    
    void remove(Transition transition);
}
