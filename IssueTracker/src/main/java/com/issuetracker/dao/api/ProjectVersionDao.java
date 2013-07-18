/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.issuetracker.dao.api;

import com.issuetracker.model.Project;
import com.issuetracker.model.ProjectVersion;

/**
 *
 * @author Monika
 */
public interface ProjectVersionDao {
    void insertProjectVersion(ProjectVersion projectVersion);
}
