package com.issuetracker.service.api;

import com.issuetracker.model.IssueType;

import java.util.List;

/**
 *
 * @author mgottval
 */
public interface IssueTypeService {
    
    void insert(IssueType issueType);
    
    void remove(IssueType issueType);
    
    boolean isIssueTypeUsed(IssueType issueType);
    
    List<IssueType> getIssueTypes();

    IssueType getIssueTypeByName(String name);

    void update(IssueType issueType);
}
