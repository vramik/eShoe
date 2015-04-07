package com.issuetracker.service;

import com.issuetracker.dao.api.ProjectDao;
import com.issuetracker.model.Component;
import com.issuetracker.model.Project;
import com.issuetracker.model.ProjectVersion;
import com.issuetracker.model.Workflow;
import com.issuetracker.service.api.ProjectService;
import java.io.Serializable;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.List;

/**
 *
 * @author mgottval
 */
@Stateless
public class ProjectServiceBean implements ProjectService, Serializable {

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
    public List<Project> getProjectsWithRights(String permissionName) {
        return projectDao.getProjectsWithRights(permissionName);
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

    @Override
    public List<Project> getProjectsByWorkflow(Workflow workflow) {
        return projectDao.getProjectsByWorkflow(workflow);
    }
}
