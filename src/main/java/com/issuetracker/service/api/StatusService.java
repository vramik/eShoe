package com.issuetracker.service.api;

import com.issuetracker.model.Status;

import java.util.List;

/**
 *
 * @author mgottval
 */
public interface StatusService {
    
    void insert(Status status);
    
    List<Status> getStatuses();
    
    void remove(Status status);
    
    Status getStatusById(Long id);
    
    Status getStatusByName(String name);
}
