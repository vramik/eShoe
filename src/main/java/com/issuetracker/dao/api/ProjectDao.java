package com.issuetracker.dao.api;

import com.issuetracker.model.Component;
import com.issuetracker.model.Project;
import com.issuetracker.model.ProjectVersion;
import com.issuetracker.model.Workflow;

import java.util.List;

/**
 *
 * @author mgottval
 */
public interface ProjectDao {
    
    void insert(Project project);
    
    void update(Project project);
    
    Project getProjectByName(String name);
    
    List<Project> getProjects();
    
    List<Project> getProjectsWithRights(String action);
    
    List<ProjectVersion> getProjectVersions(Project project);
    
    List<Component> getProjectComponents(Project project);
    
    void remove(Project project);
    
    Project getProjectById(Long id);
    
    boolean isProjectNameInUse(String projectName);

    List<Project> getProjectsByWorkflow(Workflow workflow);
}
