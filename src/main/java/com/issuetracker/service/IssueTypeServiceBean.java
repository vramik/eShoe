package com.issuetracker.service;

import com.issuetracker.dao.api.IssueTypeDao;
import com.issuetracker.model.IssueType;
import com.issuetracker.service.api.IssueTypeService;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.List;

/**
 *
 * @author mgottval
 */
@Stateless
public class IssueTypeServiceBean implements IssueTypeService {

    @Inject
    private IssueTypeDao issueTypeDao;
     
    @Override
    public void insert(IssueType issueType) {
        issueTypeDao.insert(issueType);
    }

    @Override
    public List<IssueType> getIssueTypes() {
        return issueTypeDao.getIssueTypes();
    }
}
