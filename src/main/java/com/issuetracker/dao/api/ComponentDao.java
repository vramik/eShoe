/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.issuetracker.dao.api;

import com.issuetracker.model.Component;
import java.util.List;

/**
 *
 * @author mgottval
 */
public interface ComponentDao {
    
    void insertComponent(Component component);
    
    List<Component> getComponents();
    
    void remove(Component component);
}
