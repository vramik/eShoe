package com.issuetracker.service;

import com.issuetracker.dao.api.ProjectVersionDao;
import com.issuetracker.model.ProjectVersion;
import com.issuetracker.service.api.ProjectVersionService;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

/**
 *
 * @author Monika
 */
@Stateless
public class ProjectVersionServiceBean implements ProjectVersionService {

    @Inject
    private ProjectVersionDao projectVersionDao;

    @Override
    public void insert(ProjectVersion projectVersion) {
        projectVersionDao.insert(projectVersion);
    }

    @Override
    public void remove(ProjectVersion projectVersion) {
        projectVersionDao.remove(projectVersion);
    }
    
    @Override
    public List<ProjectVersion> getProjectVersions() {
        return projectVersionDao.getProjectVersions();
    }
}
