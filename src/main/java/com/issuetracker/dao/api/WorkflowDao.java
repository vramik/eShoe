package com.issuetracker.dao.api;

import com.issuetracker.model.Workflow;

import java.util.List;

/**
 *
 * @author mgottval
 */
public interface WorkflowDao {
    
    void insert(Workflow workflow);
    
    List<Workflow> getWorkflows();
    
    void remove(Workflow workflow);
    
    Workflow getWorkflowById(Long id);
    
    void update(Workflow workflow);

    Workflow getWorkflowByName(String name);

    boolean isWorkflowUsed(Workflow workflow);
}
