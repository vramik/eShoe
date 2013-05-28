/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.issuetracker.dao;

import com.issuetracker.dao.api.ProjectDao;
import com.issuetracker.model.Project;
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
public class ProjectDaoBean implements ProjectDao {

    @PersistenceContext
    private EntityManager em;
    private CriteriaBuilder qb;

    
    @Override
    public void insertProject(Project project) {
        em.persist(project);
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
}
