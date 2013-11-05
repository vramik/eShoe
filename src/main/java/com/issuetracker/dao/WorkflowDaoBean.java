package com.issuetracker.dao;

import com.issuetracker.dao.api.WorkflowDao;
import com.issuetracker.model.Workflow;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 *
 * @author mgottval
 */
@Stateless
public class WorkflowDaoBean implements WorkflowDao {

    @PersistenceContext(unitName = "issuetrackerPU2")
    private EntityManager em;
     private CriteriaBuilder qb;
     
    @Override
    public void insertWorkflow(Workflow workflow) {
        em.persist(workflow);
    }

    @Override
    public List<Workflow> getWorkflows() {     
        
        qb = em.getCriteriaBuilder();
        CriteriaQuery<Workflow> c = qb.createQuery(Workflow.class);
        Root<Workflow> i = c.from(Workflow.class);
        TypedQuery<Workflow> q = em.createQuery(c);
        List<Workflow> results =  q.getResultList();
        if (results != null && !results.isEmpty()) {
            return results;
        }
        return null;
    }

    

    @Override
    public Workflow getWorkflowById(Long id) {
        qb = em.getCriteriaBuilder();
        CriteriaQuery<Workflow> workflowQuery = qb.createQuery(Workflow.class);
        Root<Workflow> p = workflowQuery.from(Workflow.class);
        Predicate pCondition = qb.equal(p.get("id"), id);
        workflowQuery.where(pCondition);
        TypedQuery<Workflow> pQuery = em.createQuery(workflowQuery);
        List<Workflow> workflowResults = pQuery.getResultList();
        if (workflowResults != null && !workflowResults.isEmpty()) {
            return workflowResults.get(0);
        } else {
            return null;
        }
    }

    @Override
    public void update(Workflow workflow) {
        em.merge(workflow);
    }
    
    @Override
    public void remove(Workflow workflow) {
        em.remove(em.contains(workflow) ? workflow : em.merge(workflow));
    }
}
