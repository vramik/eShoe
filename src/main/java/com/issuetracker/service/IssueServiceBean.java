package com.issuetracker.service;

import com.github.holmistr.esannotations.indexing.AnnotationIndexManager;
import com.issuetracker.dao.api.IssueDao;
import com.issuetracker.model.*;
import com.issuetracker.service.api.ActionService;
import com.issuetracker.service.api.IssueService;
import com.issuetracker.service.api.PermissionService;
import com.issuetracker.service.api.RoleService;
import static com.issuetracker.web.Constants.roles;
import com.issuetracker.web.security.KeycloakAuthSession;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.List;
import java.util.Set;
import org.jboss.logging.Logger;

/**
 *
 * @author mgottval
 */
@Stateless
public class IssueServiceBean implements IssueService, Serializable {

    @Inject
    private IssueDao issueDao;
    @Inject
    private PermissionService permissionService;
    @Inject
    private ActionService actionService; 
    @Inject
    private RoleService roleService;

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
    public void create(Issue issue) {
        issueDao.insert(issue);
        log.error("TODO: enable indexing");
//        indexManager.index(issue);
    }
    
    @Override
    public void insertComment(Issue issue, Comment comment) {
        List<Comment> comments = issueDao.getComments(issue);
        comments.add(comment);
        issue.setComments(comments);
        
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
    public void update(Issue issue) {
        issueDao.update(issue);
        log.error("TODO: enable indexing");
//        indexManager.index(issue);
    }

    @Override
    public void remove(Issue issue) {
        issueDao.remove(issue);
    }

    @Override
    public List<String> getIssueWatchers(Issue issue) {
        return issueDao.getIssueWatchers(issue);
    }

    @Override
    public List<Issue> getIssuesBySearch(Project project, List<ProjectVersion> affectedVersions,
            Component component, List<IssueType> issueTypes, List<Status> statusList, String nameContainsText) {
        
        Set<Long> userRolesIds = roleService.getIdsByNames(KeycloakAuthSession.getUserRhelmRoles());
        
        List<Issue> issues = new ArrayList<>();
        Action action = actionService.getActionByNameAndType(roles.getProperty("it.issue.browse"), TypeId.issue);
        
        for (Issue issue : issueDao.getIssuesBySearch(project, affectedVersions, component, issueTypes, statusList, nameContainsText)) {
            
            List<Permission> permissions = permissionService.getPermissionsByAction(TypeId.issue, issue.getId(), action.getId());
            
            if (permissions.isEmpty()) {
                permissions = permissionService.getPermissionsByTypeAndAction(TypeId.global, action.getId());
                permissions.addAll(permissionService.getPermissionsByAction(TypeId.project, issue.getProject().getId(), action.getId()));
            }
            for (Permission p : permissions) {
                if (userRolesIds.contains(p.getRoleId())) {
                    issues.add(issue);
                    break;
                }
            }
        } 
        return issues;
    }

    @Override
    public List<Comment> getDisplayableComments(Issue issue) {
        Set<Long> userRolesIds = roleService.getIdsByNames(KeycloakAuthSession.getUserRhelmRoles());
        
        List<Comment> comments = new ArrayList<>();
        Action action = actionService.getActionByNameAndType(roles.getProperty("it.comment.browse"), TypeId.comment);
        
        for (Comment comment : issueDao.getComments(issue)) {
            
            List<Permission> permissions = permissionService.getPermissionsByAction(TypeId.comment, comment.getId(), action.getId());
            
            if (permissions.isEmpty()) {
                permissions = permissionService.getPermissionsByTypeAndAction(TypeId.global, action.getId());
                permissions.addAll(permissionService.getPermissionsByAction(TypeId.project, issue.getProject().getId(), action.getId()));
                permissions.addAll(permissionService.getPermissionsByAction(TypeId.issue, issue.getId(), action.getId()));
            } 
            for (Permission p : permissions) {
                if (userRolesIds.contains(p.getRoleId())) {
                    comments.add(comment);
                    break;
                }
            }
        } 
        return comments;
    }
}
