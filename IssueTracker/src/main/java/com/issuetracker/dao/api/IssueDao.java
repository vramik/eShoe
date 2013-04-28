/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.issuetracker.dao.api;

import com.issuetracker.model.Issue;
import com.issuetracker.model.Project;
import java.util.List;

/**
 *
 * @author mgottval
 */
public interface IssueDao {
    
    List<Issue> getIssues();
    
    Issue getIssueById(Long id);
    
    Issue getIssueByName(String name);
    
    void addIssue(Issue issue);
    
    Issue updateIssue(Issue issue);
    
    void removeIssue(Issue issue);
    
    List<Issue> getIssuesByProject(String projectName);
    
}
