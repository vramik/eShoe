/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.issuetracker.dao.api;

import com.issuetracker.model.Status;
import java.util.List;

/**
 *
 * @author mgottval
 */
public interface StatusDao {
    
    void insert(Status status);
    
    List<Status> getStatuses();
    
    void remove(Status status);
    
    Status getStatusById(Long id);
    
    Status getStatusByName(String name);
}
