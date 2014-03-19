package com.issuetracker.service;

import com.issuetracker.dao.api.WorkflowDao;
import com.issuetracker.model.Workflow;
import com.issuetracker.service.api.WorkflowService;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.List;

/**
 *
 * @author mgottval
 */
@Stateless
public class WorkflowServiceBean implements WorkflowService {

    @Inject
    private WorkflowDao workflowDao;
     
    @Override
    public void insert(Workflow workflow) {
        workflowDao.insert(workflow);
    }

    @Override
    public List<Workflow> getWorkflows() {
        return workflowDao.getWorkflows();
    }

    @Override
    public Workflow getWorkflowById(Long id) {
        return workflowDao.getWorkflowById(id);
    }

    @Override
    public void update(Workflow workflow) {
        workflowDao.update(workflow);
    }
    
    @Override
    public void remove(Workflow workflow) {
        workflowDao.remove(workflow);
    }
}
