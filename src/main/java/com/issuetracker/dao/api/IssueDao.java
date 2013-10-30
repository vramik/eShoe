/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.issuetracker.dao.api;

import com.issuetracker.model.Comment;
import com.issuetracker.model.Component;
import com.issuetracker.model.Issue;
import com.issuetracker.model.Issue.Priority;
import com.issuetracker.model.IssueType;
import com.issuetracker.model.Project;
import com.issuetracker.model.ProjectVersion;
import com.issuetracker.model.Status;
import com.issuetracker.model.User;
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
    
    void updateIssue(Issue issue);
    
    void removeIssue(Issue issue);
    
    List<Issue> getIssuesByProject(Project project);
    
    List<Issue> getIssuesByProjectName(String projectName);
    
    List<Priority> getPriorities();
    
    List<Issue> getIssuesBySearch(Project project, ProjectVersion projectVersion, 
    Component component, List<IssueType> issueTypes, List<Status> statusList, String nameContainsText);
    
    List<User> getIssueWatchers(Issue issue);
    
    List<Comment> getComments(Issue issue);
    
}
