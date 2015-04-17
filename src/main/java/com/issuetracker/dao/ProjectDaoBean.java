package com.issuetracker.dao;

import com.issuetracker.dao.api.ProjectDao;
import com.issuetracker.model.Component;
import com.issuetracker.model.Project;
import com.issuetracker.model.ProjectVersion;
import com.issuetracker.model.Workflow;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.jboss.logging.Logger;

/**
 *
 * @author mgottval
 */
@Stateless
public class ProjectDaoBean implements ProjectDao {

    private final Logger log = Logger.getLogger(ProjectDaoBean.class);
    
    @PersistenceContext
    private EntityManager em;
    private CriteriaBuilder cb;
    
    @Override
    public void insert(Project project) {
        em.persist(project);
    }
    
    @Override
    public void update(Project project) {
        em.merge(project);
    }

    @Override
    public Project getProjectByName(String name) {
        cb = em.getCriteriaBuilder();
        CriteriaQuery<Project> projectQuery = cb.createQuery(Project.class);
        Root<Project> p = projectQuery.from(Project.class);
        Predicate pCondition = cb.equal(p.get("name"), name);
        projectQuery.where(pCondition);
        TypedQuery<Project> pQuery = em.createQuery(projectQuery);
        List<Project> projectResults = pQuery.getResultList();
        if (projectResults != null && !projectResults.isEmpty()) {
            return projectResults.get(0);
        } else {
            return null;
        }
    }
    
    @Override
    public Project getProjectById(Long id) {
        cb = em.getCriteriaBuilder();
        CriteriaQuery<Project> projectQuery = cb.createQuery(Project.class);
        Root<Project> p = projectQuery.from(Project.class);
        Predicate pCondition = cb.equal(p.get("id"), id);
        projectQuery = projectQuery.where(pCondition);
        TypedQuery<Project> pQuery = em.createQuery(projectQuery);
        List<Project> projectResults = pQuery.getResultList();
        if (projectResults != null && !projectResults.isEmpty()) {
            return projectResults.get(0);
        } else {
            return null;
        }
    }

    @Override
    public List<Project> getProjects() {
        cb = em.getCriteriaBuilder();
        CriteriaQuery<Project> query = cb.createQuery(Project.class);
        Root<Project> fromProject = query.from(Project.class);
        query.select(fromProject);
        List<Project> results = em.createQuery(query).getResultList();
        if (results != null && !results.isEmpty()) {
            return results;
        } else {
            return new ArrayList<>();
        }
    }
    
    @Override
    public List<ProjectVersion> getProjectVersions(Project project) {
        cb = em.getCriteriaBuilder();
        CriteriaQuery<Project> c = cb.createQuery(Project.class);
        Root<Project> p = c.from(Project.class);
        c.select(p);
        c.where(cb.equal(p.get("name"), project.getName()));
        TypedQuery query = em.createQuery(c);
        List<Project> projectResults = query.getResultList();
        if (projectResults != null && !projectResults.isEmpty()) {
            List<ProjectVersion> versions = projectResults.get(0).getVersions();
            return versions;
        } else {
            return new ArrayList<>();
        }
    }

    @Override
    public List<Component> getProjectComponents(Project project) {
        cb = em.getCriteriaBuilder();
        CriteriaQuery<Project> c = cb.createQuery(Project.class);
        Root<Project> p = c.from(Project.class);
        c.select(p);
        c.where(cb.equal(p.get("name"), project.getName()));
        TypedQuery query = em.createQuery(c);
        List<Project> projectResults = query.getResultList();
        if (projectResults != null && !projectResults.isEmpty()) {
            List<Component> components = projectResults.get(0).getComponents();
            return components;
        } else {
            return new ArrayList<>();
        }
    }

    @Override
    public void remove(Project project) {        
        em.remove(em.merge(project));
    }

    @Override
    public boolean isProjectNameInUse(String projectName) {
        return getProjectByName(projectName) != null;
    }

    @Override
    public List<Project> getProjectsByWorkflow(Workflow workflow) {
        cb = em.getCriteriaBuilder();
        CriteriaQuery<Project> projectQuery = cb.createQuery(Project.class);
        Root<Project> p = projectQuery.from(Project.class);
        Predicate pCondition = cb.equal(p.get("workflow"), workflow.getId());
        projectQuery.where(pCondition);
        TypedQuery<Project> pQuery = em.createQuery(projectQuery);
        List<Project> results = pQuery.getResultList();
        if (results != null && !results.isEmpty()) {
            return results;
        } else {
            return new ArrayList<>();
        }
    }

    @Override
    public List<Project> getProjectsByIds(Set<Long> projectIds) {
        cb = em.getCriteriaBuilder();
        CriteriaQuery<Project> projectQuery = cb.createQuery(Project.class);
        Root<Project> fromProject = projectQuery.from(Project.class);
        
        List<Predicate> predicates = new ArrayList<>();
        for (Long projectId : projectIds) {
            predicates.add(cb.equal(fromProject.get("id"), projectId));
        }
        projectQuery.where(cb.or(predicates.toArray(new Predicate[predicates.size()])));
        TypedQuery<Project> pQuery = em.createQuery(projectQuery);
        List<Project> results = pQuery.getResultList();
        if (results != null && !results.isEmpty()) {
            return results;
        } else {
            return new ArrayList<>();
        }
    }

    @Override
    public Set<Long> getProjectsIDs() {
        cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> projectQuery = cb.createQuery(Long.class);
        Root<Project> fromProject = projectQuery.from(Project.class);
        projectQuery.select(fromProject.<Long>get("id"));
        TypedQuery<Long> pQuery = em.createQuery(projectQuery);
        List<Long> resultList = pQuery.getResultList();
        if (resultList != null && !resultList.isEmpty()) {
            return new HashSet<>(resultList);
        } else {
            return new HashSet<>();
        }
    }
}
