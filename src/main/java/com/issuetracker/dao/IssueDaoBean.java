package com.issuetracker.dao;

import com.issuetracker.dao.api.IssueDao;
import com.issuetracker.dao.api.ProjectDao;
import com.issuetracker.model.*;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author mgottval
 */
@Stateless
public class IssueDaoBean implements IssueDao {

    @PersistenceContext
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
        Predicate condition = qb.equal(i.get("issueId"), id);
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
    public List<Issue> getIssuesByProjectName(String projectName) {
        qb = em.getCriteriaBuilder();
        CriteriaQuery<Issue> c = qb.createQuery(Issue.class);
        Root<Issue> i = c.from(Issue.class);
        c.select(i);
        c.where(qb.equal(i.get("project").get("name"), projectName));
        List<Issue> results = em.createQuery(c).getResultList();
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
    public void insert(Issue issue) {
        em.persist(issue);
    }

    @Override
    public void update(Issue issue) {
        em.merge(issue);
    }

    @Override
    public void remove(Issue issue) {
        em.remove(em.contains(issue)? issue : em.merge(issue));
    }

    @Override
    public List<Issue> getIssuesByProject(Project project) {
        qb = em.getCriteriaBuilder();
        CriteriaQuery<Issue> c = qb.createQuery(Issue.class);
        Root<Issue> i = c.from(Issue.class);
        c.select(i);
        c.where(qb.equal(i.get("project").get("name"), project.getName()));
        List<Issue> results = em.createQuery(c).getResultList();
        if (results != null && !results.isEmpty()) {
            return results;
        }
        return new ArrayList<Issue>();
    }
//

    @Override
    public List<User> getIssueWatchers(Issue issue) {
        qb = em.getCriteriaBuilder();
        CriteriaQuery<Issue> c = qb.createQuery(Issue.class);
        Root<Issue> i = c.from(Issue.class);
        c.select(i);
        c.where(qb.equal(i.get("name"), issue.getName()));
        TypedQuery query = em.createQuery(c);
        List<Issue> issueResults = query.getResultList();
        if (issueResults != null && !issueResults.isEmpty()) {
            List<User> watchers = issueResults.get(0).getWatches();
            return watchers;
        } else {
            return null;
        }
    }

    @Override
    public List<Issue.Priority> getPriorities() {
//        qb = em.getCriteriaBuilder();
//        CriteriaQuery<Issue> c = qb.createQuery(Issue.class);
//        Root<Issue> i = c.from(Issue.class);
//        
//        TypedQuery<Issue.Priority> q = em.createQuery(c);
//        List<Issue.Priority> results = q.getResultList();
//
//        if (results != null && !results.isEmpty()) {
//            return results;
//        }
        return null;
    }

    @Override
    public List<Issue> getIssuesBySearch(Project project, ProjectVersion projectVersion,
            Component component, List<IssueType> issueTypes, List<Status> statusList, String nameContainsText) {
        qb = em.getCriteriaBuilder();
        List<Issue> results;
        CriteriaQuery<Issue> c = qb.createQuery(Issue.class);
        Root<Issue> i = c.from(Issue.class);
        c.select(i);
        Expression<String> name = i.get("name");
//        if (nameContainsText != null) {
//            c.where(qb.like(qb.lower(name), "%" + nameContainsText.toLowerCase() + "%"));
//        
//        };
        List<Predicate> predicates = new ArrayList<Predicate>();
        predicates.add(qb.equal(i.get("project"), project));
        predicates.add(qb.equal(i.get("projectVersion"), projectVersion));
        predicates.add(qb.equal(i.get("component"), component));

        if (nameContainsText != null) {
            predicates.add(qb.like(qb.lower(name), "%" + nameContainsText.toLowerCase() + "%"));
        }
        if (issueTypes != null && !issueTypes.isEmpty()) {
            predicates.add(i.get("issueType").in(issueTypes));
        }
        if (statusList != null && !statusList.isEmpty()) {
            predicates.add(i.get("status").in(statusList));
        }
        Object[] objectArray = predicates.toArray();
        Predicate[] predicateArray = Arrays.copyOf(objectArray, objectArray.length, Predicate[].class);
        c.where(predicateArray);

//            c.where(qb.like(qb.lower(name), "%" + nameContainsText.toLowerCase() + "%"), qb.equal(i.get("project"), project),
//                    qb.equal(i.get("projectVersion"), projectVersion), qb.equal(i.get("component"), component), i.get("issueType").in(issueTypes),
//                    i.get("status").in(statusList));



            results = em.createQuery(c).getResultList();
            if (results != null && !results.isEmpty()) {
                return results;
            }
            return new ArrayList<Issue>();
        }

        @Override
        public List<Comment> getComments
        (Issue issue
        
            ) {
        Logger.getLogger(IssueDaoBean.class.getName()).log(Level.SEVERE, issue.getName());
            qb = em.getCriteriaBuilder();
            CriteriaQuery<Issue> c = qb.createQuery(Issue.class);
            Root<Issue> i = c.from(Issue.class);
            c.select(i);
            c.where(qb.equal(i.get("name"), issue.getName()));
            TypedQuery query = em.createQuery(c);
            List<Issue> issueResults = query.getResultList();
            if (issueResults != null && !issueResults.isEmpty()) {
                List<Comment> comments = issueResults.get(0).getComments();
                return comments;
            } else {
                return null;
            }
        }
    }
