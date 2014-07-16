package com.issuetracker.service.api;

import com.issuetracker.model.*;
import com.issuetracker.model.Issue.Priority;

import java.util.List;

/**
 *
 * @author mgottval
 */
public interface IssueService {
    
    List<Issue> getIssues();
    
    Issue getIssueById(Long id);
    
    Issue getIssueByName(String name);
    
    void insert(Issue issue);
    
    void update(Issue issue);
    
    void remove(Issue issue);
    
    List<Issue> getIssuesByProject(Project project);
    
    List<Issue> getIssuesByProjectName(String projectName);
    
    List<Priority> getPriorities();
    
    List<Issue> getIssuesBySearch(Project project, ProjectVersion projectVersion,
                                  Component component, List<IssueType> issueTypes, List<Status> statusList, String nameContainsText);
    
    List<User> getIssueWatchers(Issue issue);
    
    List<Comment> getComments(Issue issue);
    
}
