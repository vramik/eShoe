package com.issuetracker.service.api;

import com.issuetracker.model.Comment;
import com.issuetracker.model.Component;
import com.issuetracker.model.Issue;
import com.issuetracker.model.IssueType;
import com.issuetracker.model.Project;
import com.issuetracker.model.ProjectVersion;
import com.issuetracker.model.Status;
import java.util.List;

/**
 *
 * @author mgottval
 */
public interface IssueService {
    
    List<Issue> getIssues();
    
    Issue getIssueById(Long id);
    
    void create(Issue issue);
    
    void insertComment(Issue issue, Comment comment);
    
    void removeComment(Issue issue);
    
    void addWatcher(Issue issue);
    
    void remove(Issue issue);
    
    void update(Issue issue);
            
    List<Issue> getIssuesBySearch(Project project, List<ProjectVersion> affectedVersions,
                                  Component component, List<IssueType> issueTypes, List<Status> statusList, String nameContainsText);
    
    List<String> getIssueWatchers(Issue issue);
    
    List<Comment> getDisplayableComments(Issue issue);
}
