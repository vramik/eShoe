package com.issuetracker.service;

import com.issuetracker.dao.api.IssueDao;
import com.issuetracker.dao.api.ProjectDao;
import com.issuetracker.model.*;
import com.issuetracker.service.api.IssueService;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author mgottval
 */
@Stateless
public class IssueServiceBean implements IssueService {

    @Inject
    private IssueDao issueDao;

    @Override
    public List<Issue> getIssues() {
        return issueDao.getIssues();
    }

    @Override
    public Issue getIssueById(Long id) {
        return issueDao.getIssueById(id);
    }

    @Override
    public List<Issue> getIssuesByProjectName(String projectName) {
        return issueDao.getIssuesByProjectName(projectName);
    }

    @Override
    public Issue getIssueByName(String name) {
        return issueDao.getIssueByName(name);
    }

    @Override
    public void insert(Issue issue) {
        issueDao.insert(issue);
    }

    @Override
    public void update(Issue issue) {
        issueDao.update(issue);
    }

    @Override
    public void remove(Issue issue) {
        issueDao.remove(issue);
    }

    @Override
    public List<Issue> getIssuesByProject(Project project) {
        return issueDao.getIssuesByProject(project);
    }

    @Override
    public List<User> getIssueWatchers(Issue issue) {
        return issueDao.getIssueWatchers(issue);
    }

    @Override
    public List<Issue.Priority> getPriorities() {
        return null;
    }

    @Override
    public List<Issue> getIssuesBySearch(Project project, ProjectVersion projectVersion,
            Component component, List<IssueType> issueTypes, List<Status> statusList, String nameContainsText) {
        return issueDao.getIssuesBySearch(project, projectVersion, component, issueTypes, statusList, nameContainsText);
    }

    @Override
    public List<Comment> getComments(Issue issue) {
        return issueDao.getComments(issue);
    }
}
