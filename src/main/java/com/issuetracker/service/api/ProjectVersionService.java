package com.issuetracker.service.api;

import com.issuetracker.model.ProjectVersion;

import java.util.List;

/**
 *
 * @author Monika
 */
public interface ProjectVersionService {
    
    void insert(ProjectVersion projectVersion);
    
    void remove(ProjectVersion projectVersion);
    
    List<ProjectVersion> getProjectVersions();
}
