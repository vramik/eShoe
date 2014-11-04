package com.issuetracker.dao.api;

import com.issuetracker.model.IssueType;

import java.util.List;

/**
 *
 * @author mgottval
 */
public interface IssueTypeDao {
    
    void insert(IssueType issueType);
    
    void remove(IssueType sueType);
    
    List<IssueType> getIssueTypes();

    boolean isIssueTypeUsed(IssueType issueType);

    IssueType getIssueTypeByName(String name);

    void update(IssueType issueType);
}
