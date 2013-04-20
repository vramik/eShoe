/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.issuetracker.dao.api;

import com.issuetracker.model.Issue;
import java.util.List;

/**
 *
 * @author mgottval
 */
public interface IssueDao {
    
    List<Issue> getIssues();
    
    Issue getIssueById(Long id);
    
    void addIssue(Issue issue);
    
    void updateIssue(Issue issue);
    
    void removeIssue(Issue issue);
    
}
