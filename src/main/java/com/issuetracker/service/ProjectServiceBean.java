package com.issuetracker.service;

import com.issuetracker.dao.api.ComponentDao;
import com.issuetracker.dao.api.ProjectDao;
import com.issuetracker.dao.api.ProjectVersionDao;
import com.issuetracker.dao.api.UserDao;
import com.issuetracker.model.Component;
import com.issuetracker.model.Project;
import com.issuetracker.model.ProjectVersion;
import com.issuetracker.service.api.ProjectService;

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
public class ProjectServiceBean implements ProjectService {

    @Inject
    private ProjectDao projectDao;
    
    @Override
    public void insert(Project project) {
        projectDao.insert(project);
    }
    
    @Override
    public void update(Project project) {
        projectDao.update(project);
    }

    @Override
    public Project getProjectByName(String name) {
        return projectDao.getProjectByName(name);
    }
    
    @Override
    public Project getProjectById(Long id) {
        return projectDao.getProjectById(id);
    }

    @Override
    public List<Project> getProjects() {
        return projectDao.getProjects();
    }

    @Override
    public List<ProjectVersion> getProjectVersions(Project project) {
        return projectDao.getProjectVersions(project);
    }

    @Override
    public List<Component> getProjectComponents(Project project) {
        return projectDao.getProjectComponents(project);
    }

    @Override
    public void remove(Project project) {
        projectDao.remove(project);
    }
    
    @Override
    public boolean isProjectNameInUse(String projectName) {
        return projectDao.isProjectNameInUse(projectName);
    }
}
