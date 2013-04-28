/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.issuetracker.dao;

import com.issuetracker.dao.api.IssueDao;
import com.issuetracker.dao.api.ProjectDao;
import com.issuetracker.model.Issue;
import com.issuetracker.model.Project;
import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
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
public class IssueDaoBean implements IssueDao {

    @PersistenceContext(unitName = "com_IssueTracker_war_1.0-SNAPSHOTPU2")
    private EntityManager em;
    private CriteriaBuilder qb;
    
    @Inject
    private ProjectDao projectDao;

    @Override
    public List<Issue> getIssues() {
        qb = em.getCriteriaBuilder();
        CriteriaQuery<Issue> c = qb.createQuery(Issue.class);
        Root<Issue> i = c.from(Issue.class);
        TypedQuery<Issue> q = em.createQuery(c);
        return q.getResultList();
        //  return em.createQuery("SELECT i FROM Issue i").getResultList();

    }

    @Override
    public Issue getIssueById(Long id) {
        qb = em.getCriteriaBuilder();
        CriteriaQuery<Issue> c = qb.createQuery(Issue.class);
        Root<Issue> i = c.from(Issue.class);
        Predicate condition = qb.equal(i.get("id"), id);
        c.where(condition);
        TypedQuery<Issue> q = em.createQuery(c);
        List<Issue> results = q.getResultList();

        // List<Issue> results = em.createQuery("SELECT i FROM Issue i WHERE id = :id", Issue.class).setParameter("id", id).getResultList();
        if (results != null && !results.isEmpty()) {
            return results.get(0);
        }
        return null;
    }

    @Override
    public List<Issue> getIssuesByProject(String projectName) {
        qb = em.getCriteriaBuilder();
        CriteriaQuery<Issue> c = qb.createQuery(Issue.class);
        Root<Issue> i = c.from(Issue.class);
        Predicate condition = qb.equal(i.get("project"), projectDao.getProjectByName(projectName));
        c.where(condition);
        TypedQuery<Issue> q = em.createQuery(c);
        List<Issue> results = q.getResultList();
        if (results != null && !results.isEmpty()) {
            return results;
        }
        return null;
    }

    @Override
    public Issue getIssueByName(String name) {
        qb = em.getCriteriaBuilder();
        CriteriaQuery<Issue> c = qb.createQuery(Issue.class);
        Root<Issue> i = c.from(Issue.class);
        Predicate condition = qb.equal(i.get("name"), name);
        c.where(condition);
        TypedQuery<Issue> q = em.createQuery(c);
        List<Issue> results = q.getResultList();

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
    public Issue updateIssue(Issue issue) {
        Issue updatedIssue = em.merge(issue);
        return updatedIssue;
    }

    @Override
    public void removeIssue(Issue issue) {
        em.remove(issue);
    }
}
