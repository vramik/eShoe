/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.issuetracker.dao;

import com.issuetracker.dao.api.ProjectVersionDao;
import com.issuetracker.model.Project;
import com.issuetracker.model.ProjectVersion;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;

/**
 *
 * @author Monika
 */
@Stateless
public class ProjectVersionDaoBean implements ProjectVersionDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    public void insertProjectVersion(ProjectVersion projectVersion) {
        em.persist(projectVersion);
    }
}
