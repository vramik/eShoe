package com.issuetracker.dao;

import com.issuetracker.dao.api.IssueTypeDao;
import com.issuetracker.model.Issue;
import com.issuetracker.model.IssueType;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.criteria.Predicate;

/**
 *
 * @author mgottval
 */
@Stateless
public class IssueTypeDaoBean implements IssueTypeDao {

    @PersistenceContext
    private EntityManager em;
    private CriteriaBuilder qb;
     
    @Override
    public void insert(IssueType issueType) {
        em.persist(issueType);
    }
     
    @Override
    public void remove(IssueType issueType) {
        em.remove(em.contains(issueType) ? issueType : em.merge(issueType));
    }

    @Override
    public List<IssueType> getIssueTypes() {     
        
        qb = em.getCriteriaBuilder();
        CriteriaQuery<IssueType> c = qb.createQuery(IssueType.class);
        Root<IssueType> i = c.from(IssueType.class);
        TypedQuery<IssueType> q = em.createQuery(c);
        List<IssueType> results =  q.getResultList();
        if (results != null && !results.isEmpty()) {
            return results;
        }
        return new ArrayList<>();
    }

    @Override
    public boolean isIssueTypeUsed(IssueType issueType) {
        qb = em.getCriteriaBuilder();
        CriteriaQuery<Issue> c = qb.createQuery(Issue.class);
        Root<Issue> i = c.from(Issue.class);
        c.select(i);
        c.where(qb.equal(i.get("issueType"), issueType.getId()));
        TypedQuery<Issue> q = em.createQuery(c);
        return !q.getResultList().isEmpty();
    }

    @Override
    public IssueType getIssueTypeByName(String name) {
        qb = em.getCriteriaBuilder();
        CriteriaQuery<IssueType> statusQuery = qb.createQuery(IssueType.class);
        Root<IssueType> p = statusQuery.from(IssueType.class);
        Predicate pCondition = qb.equal(p.get("name"), name);
        statusQuery.where(pCondition);
        TypedQuery<IssueType> pQuery = em.createQuery(statusQuery);
        List<IssueType> statusResults = pQuery.getResultList();
        if (statusResults != null && !statusResults.isEmpty()) {
            return statusResults.get(0);
        } else {
            return null;
        }
    }

    @Override
    public void update(IssueType issueType) {
        em.merge(issueType);
    }
}
