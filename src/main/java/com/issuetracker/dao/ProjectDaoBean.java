/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.issuetracker.dao;

import com.issuetracker.dao.api.ComponentDao;
import com.issuetracker.dao.api.ProjectDao;
import com.issuetracker.dao.api.ProjectVersionDao;
import com.issuetracker.model.Component;
import com.issuetracker.model.Project;
import com.issuetracker.model.ProjectVersion;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
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
public class ProjectDaoBean implements ProjectDao {

    @Inject
    private ComponentDao componentDao;
    @Inject
    private ProjectVersionDao projectVersionDao;
    
    @PersistenceContext
    private EntityManager em;
    private CriteriaBuilder qb;
    
    @Override
    public void insertProject(Project project) {
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
    public List<Project> getProjects() {
        //EXAMPLE
        List<ProjectVersion> versin = new ArrayList<ProjectVersion>();
        ProjectVersion pv = new ProjectVersion();
        pv.setName("1.1");
        ProjectVersion pv2 = new ProjectVersion();
        pv2.setName("1.2");
        ProjectVersion pv3 = new ProjectVersion();
        pv3.setName("1.3");
        versin.add(pv);
        versin.add(pv2);
        versin.add(pv3);
        projectVersionDao.insertProjectVersion(pv3);
        projectVersionDao.insertProjectVersion(pv);
        projectVersionDao.insertProjectVersion(pv2);
        
        List<Component> components = new ArrayList<Component>();
        Component c = new Component();
        c.setName("JB1");
        Component c2 = new Component();
        c2.setName("JB2");
        components.add(c);
        components.add(c2);
        componentDao.insertComponent(c2);
        componentDao.insertComponent(c);
        Project project = new Project();
        project.setName("JBoss");
        project.setVersions(versin);
        project.setComponents(components);
         em.persist(project);
         
         List<ProjectVersion> versin2 = new ArrayList<ProjectVersion>();
        ProjectVersion pvv = new ProjectVersion();
        pvv.setName("11");
        ProjectVersion pvv2 = new ProjectVersion();
        pvv2.setName("12");
        ProjectVersion pvv3 = new ProjectVersion();
        pvv3.setName("13");
        versin2.add(pvv);
        versin2.add(pvv2);
        versin2.add(pvv3);
        projectVersionDao.insertProjectVersion(pvv3);
        projectVersionDao.insertProjectVersion(pvv);
        projectVersionDao.insertProjectVersion(pvv2);
        
        List<Component> components2 = new ArrayList<Component>();
        Component cc = new Component();
        cc.setName("Fed2");
        Component cc2 = new Component();
        cc2.setName("Fed1");
        components2.add(cc);
        components2.add(cc2);
        componentDao.insertComponent(cc2);
        componentDao.insertComponent(cc);
        Project project2 = new Project();
        project2.setName("Fedora");
        project2.setVersions(versin2);
        project2.setComponents(components2);
         em.persist(project2);
        //EXAMPLE
        
        qb = em.getCriteriaBuilder();
        CriteriaQuery<Project> q = qb.createQuery(Project.class);
        Root<Project> p = q.from(Project.class);
        TypedQuery<Project> pQuery = em.createQuery(q);
        List<Project> results = pQuery.getResultList();
        if (results != null && !results.isEmpty()) {
            return results;
        } else {
            return null;
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
            return null;
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
            return null;
        }
    }

    @Override
    public void remove(Project project) {        
        em.remove(em.merge(project));
    }

    @Override
    public Project getFirstProject() {
//         qb = em.getCriteriaBuilder();
//        CriteriaQuery<Project> c = qb.createQuery(Project.class);
//        Root<Project> p = c.from(Project.class);
//        c.select(p);
        return null;
    }
}
