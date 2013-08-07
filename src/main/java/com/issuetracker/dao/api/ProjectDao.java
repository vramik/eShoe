/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.issuetracker.dao.api;

import com.issuetracker.model.Component;
import com.issuetracker.model.Project;
import com.issuetracker.model.ProjectVersion;
import java.util.List;

/**
 *
 * @author mgottval
 */
public interface ProjectDao {
    
    void insertProject(Project project);
    
    void update(Project project);
    
    Project getProjectByName(String name);
    
    List<Project> getProjects();
    
    List<ProjectVersion> getProjectVersions(Project project);
    
    List<Component> getProjectComponents(Project project);
    
    void remove(Project project);
    
    Project getProjectById(Long id);
    
    boolean isProjectNameInUse(String projectName);
}
