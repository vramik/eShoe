package com.issuetracker.service;

import com.issuetracker.model.Action;
import com.issuetracker.model.Issue;
import com.issuetracker.model.Permission;
import com.issuetracker.model.TypeId;
import com.issuetracker.service.api.ActionService;
import com.issuetracker.service.api.IssueService;
import com.issuetracker.service.api.PermissionService;
import com.issuetracker.service.api.ProjectService;
import com.issuetracker.service.api.RoleService;
import com.issuetracker.service.api.SecurityService;
import static com.issuetracker.web.Constants.roles;
import com.issuetracker.web.security.KeycloakAuthSession;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.ejb.Stateless;
import javax.inject.Inject;
import org.jboss.logging.Logger;

/**
 *
 * @author vramik
 */
@Stateless
public class SecurityServiceBean implements SecurityService {

    private final Logger log = Logger.getLogger(SecurityServiceBean.class);
    
    @Inject private PermissionService permissionService;
    @Inject private RoleService roleService;
    @Inject private ActionService actionService;
    @Inject private IssueService issueService;
    
    @Override
    public List<String> getPermittedActionsForUserAndItem(TypeId typeId, Long itemId) {
        Set<Long> userRolesIds = roleService.getIdsByNames(KeycloakAuthSession.getUserRealmRoles());
        Set<Long> actionIds = new HashSet<>();
        
        List<Permission> hierarchyPermissions = permissionService.getPermissions(TypeId.global, 0L, userRolesIds);
        switch (typeId) {
            case global:
                break;
            case project:
                hierarchyPermissions.addAll(permissionService.getPermissions(TypeId.project, itemId, userRolesIds));
                break;
            case issue:
                Issue issue = issueService.getIssueById(itemId);
                hierarchyPermissions.addAll(permissionService.getPermissions(TypeId.project, issue.getProject().getId(), userRolesIds));
                hierarchyPermissions.addAll(permissionService.getPermissions(TypeId.issue, issue.getId(), userRolesIds));
            default:
                throw new IllegalStateException("Unreacheble state was reached");
                
        }
        for (Permission p : hierarchyPermissions) {
            actionIds.add(p.getActionId());
        }
        return actionService.getActionNamesByIds(actionIds);
    }

    @Override
    public boolean canUserPerformAction(TypeId typeId, Long itemId, String actionName) {
        Set<String> userRoles = KeycloakAuthSession.getUserRealmRoles();
        Action action;
        
        switch (typeId) {
            case global:
                break;
            case project:
                if (actionName.equals(roles.getProperty("it.project.browse"))) {
                    action = actionService.getActionByNameAndType(actionName, typeId);
                    List<Permission> projectVisibilityPermissions = permissionService.getPermissionsByAction(typeId, itemId, action.getId());
                    if (projectVisibilityPermissions.isEmpty()) {
                        return getPermittedActionsForUserAndItem(typeId, itemId).contains(actionName);
                    } 
                    for (Permission projectVisibility : projectVisibilityPermissions) {
                        if (userRoles.contains(roleService.getRoleById(projectVisibility.getRoleId()).getName())) {
                            return true;
                        }
                    }
                    return false;
                }
                break;
            case issue:
                if (actionName.equals(roles.getProperty("it.issue.browse"))) {
                    action = actionService.getActionByNameAndType(actionName, typeId);
                    List<Permission> projectVisibilityPermissions = permissionService.getPermissionsByAction(typeId, itemId, action.getId());
                    if (projectVisibilityPermissions.isEmpty()) {
                        return getPermittedActionsForUserAndItem(typeId, itemId).contains(actionName);
                    } 
                    for (Permission projectVisibility : projectVisibilityPermissions) {
                        if (userRoles.contains(roleService.getRoleById(projectVisibility.getRoleId()).getName())) {
                            return true;
                        }
                    }
                    return false;
                }
                break;
            case comment:
                throw new UnsupportedOperationException("Not supported yet!");
            default:
                throw new IllegalStateException("Unreachable state was reached.");
        }
        
        return getPermittedActionsForUserAndItem(typeId, itemId).contains(actionName);
    }
}
