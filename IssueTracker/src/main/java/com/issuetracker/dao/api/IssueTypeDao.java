/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.issuetracker.dao.api;

import com.issuetracker.model.IssueType;
import java.util.List;

/**
 *
 * @author mgottval
 */
public interface IssueTypeDao {
    
    void insertIssueType(IssueType issueType);
    
    List<IssueType> getIssueTypes();
}
