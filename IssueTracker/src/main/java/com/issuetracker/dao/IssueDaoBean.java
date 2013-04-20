/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.issuetracker.dao;

import com.issuetracker.dao.api.IssueDao;
import com.issuetracker.model.Issue;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;

/**
 *
 * @author mgottval
 */
@Stateless
public class IssueDaoBean implements IssueDao{
    
    @PersistenceContext(unitName = "com_IssueTracker_war_1.0-SNAPSHOTPU2")
    private EntityManager em;
    
    @Override
    public List<Issue> getIssues() {
        return em.createQuery("SELECT i FROM Issue i").getResultList();
        
    }
    
    @Override
    public Issue getIssueById(Long id) {
        List<Issue> results = em.createQuery("SELECT i FROM Issue i WHERE id = :id", Issue.class).setParameter("id", id).getResultList();
        if (results != null && !results.isEmpty()) {
            return results.get(0);
        }
        return null;
    }
    
    @Override
    public void addIssue(Issue issue) {
        em.persist(issue);
    }
    
    @Override
    public void updateIssue(Issue issue) {
        em.merge(issue);
    }
    
    @Override
    public void removeIssue(Issue issue) {
        em.remove(issue);
    }
    
}
