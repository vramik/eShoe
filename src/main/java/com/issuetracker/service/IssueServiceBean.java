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
        return issueDao.getIssuesBySearch(project, affectedVersions, component, issueTypes, statusList, nameContainsText);
    }

    @Override
    public List<Comment> getComments(Issue issue) {
        return issueDao.getComments(issue);
    }

    @Override
    public List<Comment> getDisplayableComments(Issue issue) {
        List<Comment> comments = new ArrayList<>();
        Action action = actionService.getActionByNameAndType(roles.getProperty("it.comment.browse"), TypeId.comment);
        
        for (Comment comment : issueDao.getComments(issue)) {
            
            List<Permission> permissionsByAction = permissionService.getPermissionsByAction(TypeId.comment, comment.getId(), action.getId());
            
            if (permissionsByAction.isEmpty()) {
                comments.add(comment);
            } else {
                Set<String> userRoles = KeycloakAuthSession.getUserRhelmRoles();
                for (Permission p : permissionsByAction) {
                    if (userRoles.contains(roleService.getRoleById(p.getRoleId()).getName())) {
                        comments.add(comment);
                        break;
                    }
                }
            }
        } 
        return comments;
    }
}
