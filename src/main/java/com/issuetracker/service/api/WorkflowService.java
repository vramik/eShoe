package com.issuetracker.service.api;

import com.issuetracker.model.Workflow;

import java.util.List;

/**
 *
 * @author mgottval
 */
public interface WorkflowService {
    
    void insert(Workflow workflow);
    
    List<Workflow> getWorkflows();
    
    void remove(Workflow workflow);
    
    Workflow getWorkflowById(Long id);
    
    void update(Workflow workflow);
}
