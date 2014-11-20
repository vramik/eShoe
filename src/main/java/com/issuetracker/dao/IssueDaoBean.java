package com.issuetracker.dao;

import com.issuetracker.dao.api.IssueDao;
import com.issuetracker.model.*;
import com.issuetracker.pages.HomePage;
import com.issuetracker.service.api.IssueService;
import com.issuetracker.web.quilifiers.MyNewAnnotation;
import static com.issuetracker.web.security.KeycloakAuthSession.*;
import java.lang.reflect.Method;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.wicket.RestartResponseException;
import org.apache.wicket.ThreadContext;
import org.apache.wicket.request.Request;
import org.apache.wicket.request.cycle.RequestCycle;
import org.jboss.logging.Logger;

/**
 *
 * @author mgottval
 */
@Stateless
public class IssueDaoBean implements IssueDao {

    @PersistenceContext
    private EntityManager em;
    private CriteriaBuilder qb;
    
    private static final Logger log = Logger.getLogger(IssueDaoBean.class);

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
        if (authorized("insert", Issue.class)) {
            em.persist(issue);
        }
    }
    
    private boolean authorized(String methodName, Class<?> parameterTypes) {
        try {
            Method method = IssueService.class.getMethod(methodName, parameterTypes);
            if (!method.isAnnotationPresent(MyNewAnnotation.class)) {
                throw new NoSuchAnnotationException("Specified " + method.getName() + " doesn't have sufficient privileges to perform " 
                        + MyNewAnnotation.class.getName() + " annotation.");
            }
            MyNewAnnotation annotation = method.getAnnotation(MyNewAnnotation.class);
           
            Request request = RequestCycle.get().getRequest();
            if (isUserInAppRole(request, annotation.allowedRole())) {
                return true;
            } else {
                log.warn("User " + getIDToken(request).getPreferredUsername() + "doesn't have  to perform " 
                        + IssueService.class.getName() + "." + method.getName());
                ThreadContext.getSession().error("Unsufficient privileges to perform this operation.");
                return false;
            }
        } catch (NoSuchMethodException | NoSuchAnnotationException e) {
            log.error(e);
            ThreadContext.getSession().error("Internal Application Error, redirecting to home page.");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                log.warn(ex);
            }
            throw new RestartResponseException(HomePage.class);
        }
    }
    
    private class NoSuchAnnotationException extends Exception {
        public NoSuchAnnotationException(String message) {
            super(message);
        }
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
        return new ArrayList<>();
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
    public List<Issue> getIssuesByAffectedVersions(List<ProjectVersion> affectedVersions) {
        qb = em.getCriteriaBuilder();
        
        CriteriaQuery<Issue> c = qb.createQuery(Issue.class);
        
        Root<Issue> i = c.from(Issue.class);

        c.select(i);
        
        List<Predicate> versionsPredicates = new ArrayList<>();
        Expression<List<ProjectVersion>> versions = i.get("affectedVersions");
        for (ProjectVersion affectedVersion : affectedVersions) {
            versionsPredicates.add(qb.isMember(affectedVersion, versions));
        }
        Object[] objectArray = versionsPredicates.toArray();
        Predicate[] predicateArray = Arrays.copyOf(objectArray, objectArray.length, Predicate[].class);
        Predicate or = qb.or(predicateArray);
        c.where(or);
        
        TypedQuery<Issue> createQuery = em.createQuery(c);
        return createQuery.getResultList();
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
            log.error(issue.getName());
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
