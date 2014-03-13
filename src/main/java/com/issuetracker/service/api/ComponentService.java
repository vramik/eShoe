package com.issuetracker.service.api;

import com.issuetracker.model.Component;

import java.util.List;

/**
 *
 * @author mgottval
 */
public interface ComponentService {
    
    void insert(Component component);
    
    List<Component> getComponents();
    
    void remove(Component component);
}
