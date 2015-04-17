package com.issuetracker.dao;

import com.issuetracker.dao.api.IssueDao;
import com.issuetracker.model.*;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.jboss.logging.Logger;

/**
 *
 * @author mgottval
 */
@Stateless
public class IssueDaoBean implements IssueDao {

    private final Logger log = Logger.getLogger(IssueDaoBean.class);
    
    @PersistenceContext
    private EntityManager em;
    private CriteriaBuilder qb;
    
    @Override
    public List<Issue> getIssues() {
        qb = em.getCriteriaBuilder();
        CriteriaQuery<Issue> c = qb.createQuery(Issue.class);
        TypedQuery<Issue> q = em.createQuery(c);
        return q.getResultList();
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
    public List<String> getIssueWatchers(Issue issue) {
        qb = em.getCriteriaBuilder();
        CriteriaQuery<Issue> c = qb.createQuery(Issue.class);
        Root<Issue> i = c.from(Issue.class);
        c.select(i);
        c.where(qb.equal(i.get("name"), issue.getName()));
        TypedQuery query = em.createQuery(c);
        List<Issue> issueResults = query.getResultList();
        if (issueResults != null && !issueResults.isEmpty()) {
            List<String> watchers = issueResults.get(0).getWatchers();
            return watchers;
        } else {
            return new ArrayList<>();
        }
    }
    
    @Override
    public List<Issue> getIssuesBySearch(Project project, List<ProjectVersion> affectedVersions,
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
        List<Predicate> predicates = new ArrayList<>();
        predicates.add(qb.equal(i.get("project"), project));
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
        
        List<Predicate> versionsPredicates = new ArrayList<>();
        Expression<List<ProjectVersion>> versions = i.get("affectedVersions");
        for (ProjectVersion affectedVersion : affectedVersions) {
            versionsPredicates.add(qb.isMember(affectedVersion, versions));
        }
        Object[] versionsObjectArray = versionsPredicates.toArray();
        Predicate[] versionsPredicateArray = Arrays.copyOf(versionsObjectArray, versionsObjectArray.length, Predicate[].class);
        predicates.add(qb.or(versionsPredicateArray));
        
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
        return new ArrayList<>();
    }

        @Override
        public List<Comment> getComments(Issue issue) {
//            log.error(issue.getName());
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
                return new ArrayList<>();
            }
        }
}
