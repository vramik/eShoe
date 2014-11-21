package com.issuetracker.service;

import com.github.holmistr.esannotations.indexing.AnnotationIndexManager;
import com.issuetracker.dao.api.IssueDao;
import com.issuetracker.model.*;
import com.issuetracker.service.api.IssueService;
import static com.issuetracker.web.security.KeycloakAuthSession.*;
import com.issuetracker.web.security.PermissionsUtil;
import java.io.Serializable;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.List;
import org.apache.wicket.ThreadContext;
import org.jboss.logging.Logger;

/**
 *
 * @author mgottval
 */
@Stateless
public class IssueServiceBean implements IssueService, Serializable {

    @Inject
    private PermissionsUtil serviceSecurity;
    
    @Inject
    private IssueDao issueDao;

    @Inject
    private AnnotationIndexManager indexManager;
    
    private static final Logger log = Logger.getLogger(IssueServiceBean.class);

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
    public void create(Issue issue) {
//        if (serviceSecurity.isAuthorized(log, IssueService.class, "create", Issue.class)) {
        if (isAuthorized("issue-create")) {
            System.err.println("insterting issue: " + issue.getName() + ", " + issue);
            issueDao.insert(issue);
            indexManager.index(issue);
        }
    }
    
    private boolean isAuthorized(String allowedRole) {
        if (isUserInAppRole(allowedRole)) {
            return true;
        } else {
            log.warn("User " + getIDToken().getPreferredUsername() + " doesn't have sufficient privileges to perform this");
            ThreadContext.getSession().error("Unsufficient privileges to perform this operation.");
            return false;
        }
    }

    @Override
    public void insertComment(Issue issue) {
        update(issue);
    }

    @Override
    public void removeComment(Issue issue) {
        update(issue);
    }

    @Override
    public void addWatcher(Issue issue) {
        update(issue);
    }
    
    @Override
    public void todo(Issue issue) {
        log.error("TODO");
        update(issue);
    }
    
    private void update(Issue issue) {
        issueDao.update(issue);
        indexManager.index(issue);
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
    public List<String> getIssueWatchers(Issue issue) {
        return issueDao.getIssueWatchers(issue);
    }

    @Override
    public List<Issue.Priority> getPriorities() {
        return null;
    }

    @Override
    public List<Issue> getIssuesByAffectedVersions(List<ProjectVersion> affectedVersions) {
        return issueDao.getIssuesByAffectedVersions(affectedVersions);
    }
    
    @Override
    public List<Issue> getIssuesBySearch(Project project, List<ProjectVersion> affectedVersions,
            Component component, List<IssueType> issueTypes, List<Status> statusList, String nameContainsText) {
        return issueDao.getIssuesBySearch(project, affectedVersions, component, issueTypes, statusList, nameContainsText);
    }

    @Override
    public List<Comment> getComments(Issue issue) {
        return issueDao.getComments(issue);
    }
}
