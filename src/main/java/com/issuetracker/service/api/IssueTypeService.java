package com.issuetracker.service.api;

import com.issuetracker.model.IssueType;

import java.util.List;

/**
 *
 * @author mgottval
 */
public interface IssueTypeService {
    
    void insert(IssueType issueType);
    
    List<IssueType> getIssueTypes();
}
