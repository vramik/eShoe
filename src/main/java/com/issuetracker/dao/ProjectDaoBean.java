package com.issuetracker.dao;

import com.issuetracker.dao.api.ComponentDao;
import com.issuetracker.dao.api.ProjectDao;
import com.issuetracker.dao.api.ProjectVersionDao;
import com.issuetracker.dao.api.UserDao;
import com.issuetracker.model.Component;
import com.issuetracker.model.Project;
import com.issuetracker.model.ProjectVersion;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author mgottval
 */
@Stateless
public class ProjectDaoBean implements ProjectDao {

//    @Inject
//    private ComponentDao componentDao;
//    @Inject
//    private ProjectVersionDao projectVersionDao;
//    @Inject
//    private UserDao userDao;
//    
    @PersistenceContext
    private EntityManager em;
    private CriteriaBuilder qb;
    
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
        qb = em.getCriteriaBuilder();
        CriteriaQuery<Project> projectQuery = qb.createQuery(Project.class);
        Root<Project> p = projectQuery.from(Project.class);
        Predicate pCondition = qb.equal(p.get("name"), name);
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
        qb = em.getCriteriaBuilder();
        CriteriaQuery<Project> projectQuery = qb.createQuery(Project.class);
        Root<Project> p = projectQuery.from(Project.class);
        Predicate pCondition = qb.equal(p.get("id"), id);
        projectQuery = projectQuery.where(pCondition);
        TypedQuery<Project> pQuery = em.createQuery(projectQuery);
        List<Project> projectResults = pQuery.getResultList();
        if (projectResults != null && !projectResults.isEmpty()) {
            Project project = projectResults.get(0);
            System.out.println("DAO getVersions: " + project.getVersions());
            System.out.println("DAO ---------------------------------------------------------------");
            return projectResults.get(0);
        } else {
            return null;
        }
    }

    @Override
    public List<Project> getProjects() {
        qb = em.getCriteriaBuilder();
        CriteriaQuery<Project> q = qb.createQuery(Project.class);
        Root<Project> p = q.from(Project.class);
        TypedQuery<Project> pQuery = em.createQuery(q);
        List<Project> results = pQuery.getResultList();
        if (results != null && !results.isEmpty()) {
            return results;
        } else {
            return new ArrayList<>();
        }
    }

    @Override
    public List<ProjectVersion> getProjectVersions(Project project) {
        qb = em.getCriteriaBuilder();
        CriteriaQuery<Project> c = qb.createQuery(Project.class);
        Root<Project> p = c.from(Project.class);
        c.select(p);
        c.where(qb.equal(p.get("name"), project.getName()));
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
        qb = em.getCriteriaBuilder();
        CriteriaQuery<Project> c = qb.createQuery(Project.class);
        Root<Project> p = c.from(Project.class);
        c.select(p);
        c.where(qb.equal(p.get("name"), project.getName()));
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
        if (getProjectByName(projectName) == null) {
            return false;
        }
        return true;
    }
}
