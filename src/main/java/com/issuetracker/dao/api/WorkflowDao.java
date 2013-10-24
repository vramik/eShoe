/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.issuetracker.dao.api;

import com.issuetracker.model.IssueType;
import com.issuetracker.model.Workflow;
import java.util.List;

/**
 *
 * @author mgottval
 */
public interface WorkflowDao {
    
    void insertWorkflow(Workflow workflow);
    
    List<Workflow> getWorkflows();
    
    void remove(Workflow workflow);
    
    Workflow getWorkflowById(Long id);
    
    void update(Workflow workflow);
}
