package com.issuetracker.service;

import com.issuetracker.dao.api.IssueTypeDao;
import com.issuetracker.model.IssueType;
import com.issuetracker.service.api.IssueTypeService;
import java.io.Serializable;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.List;

/**
 *
 * @author mgottval
 */
@Stateless
public class IssueTypeServiceBean implements IssueTypeService, Serializable {

    @Inject
    private IssueTypeDao issueTypeDao;
     
    @Override
    public void insert(IssueType issueType) {
        issueTypeDao.insert(issueType);
    }
    
    @Override
    public void remove(IssueType issueType) {
        issueTypeDao.remove(issueType);
    }

    @Override
    public List<IssueType> getIssueTypes() {
        return issueTypeDao.getIssueTypes();
    }

    @Override
    public boolean isIssueTypeUsed(IssueType issueType) {
        return issueTypeDao.isIssueTypeUsed(issueType);
    }

    @Override
    public IssueType getIssueTypeByName(String name) {
        return issueTypeDao.getIssueTypeByName(name);
    }

    @Override
    public void update(IssueType issueType) {
        issueTypeDao.update(issueType);
    }
    
    
}
