package com.issuetracker.service.api;

import com.issuetracker.model.*;
import com.issuetracker.model.Issue.Priority;
import com.issuetracker.web.quilifiers.ServiceSecurity;

import java.util.List;

/**
 *
 * @author mgottval
 */
public interface IssueService {
    
    List<Issue> getIssues();
    
    Issue getIssueById(Long id);
    
    Issue getIssueByName(String name);
    
    @ServiceSecurity(allowedRole = "issue-create")
    void create(Issue issue);
    
    @ServiceSecurity(allowedRole = "issue-create-comment")
    void insertComment(Issue issue);
    
    @ServiceSecurity(allowedRole = "issue-remove-comment")
    void removeComment(Issue issue);
    
    @ServiceSecurity(allowedRole = "issue-add-watcher")
    void addWatcher(Issue issue);
    
    void todo(Issue issue);
    
//    void update(Issue issue);
    
    @ServiceSecurity(allowedRole = "issue-delete")
    void remove(Issue issue);
    
    List<Issue> getIssuesByProject(Project project);
    
    List<Issue> getIssuesByProjectName(String projectName);
    
    List<Priority> getPriorities();
    
    List<Issue> getIssuesByAffectedVersions(List<ProjectVersion> affectedVersions);
    
    List<Issue> getIssuesBySearch(Project project, List<ProjectVersion> affectedVersions,
                                  Component component, List<IssueType> issueTypes, List<Status> statusList, String nameContainsText);
    
    List<String> getIssueWatchers(Issue issue);
    
    List<Comment> getComments(Issue issue);
    
}
