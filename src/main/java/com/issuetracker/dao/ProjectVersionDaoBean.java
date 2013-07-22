/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.issuetracker.dao;

import com.issuetracker.dao.api.ProjectVersionDao;
import com.issuetracker.model.ProjectVersion;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 *
 * @author Monika
 */
@Stateless
public class ProjectVersionDaoBean implements ProjectVersionDao {

    @PersistenceContext
    private EntityManager em;
    private CriteriaBuilder qb;

    @Override
    public void insertProjectVersion(ProjectVersion projectVersion) {
        em.persist(projectVersion);
    }

    @Override
    public void remove(ProjectVersion projectVersion) {
        em.remove(em.merge(projectVersion));
    }
    
    @Override
    public List<ProjectVersion> getProjectVersions() {
        qb = em.getCriteriaBuilder();
        CriteriaQuery<ProjectVersion> q = qb.createQuery(ProjectVersion.class);
        Root<ProjectVersion> p = q.from(ProjectVersion.class);
        TypedQuery<ProjectVersion> pQuery = em.createQuery(q);
        List<ProjectVersion> results = pQuery.getResultList();
        if (results != null && !results.isEmpty()) {
            return results;
        } else {
            return null;
        }
    }
    
    
}
