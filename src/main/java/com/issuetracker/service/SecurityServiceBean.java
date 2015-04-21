package com.issuetracker.service;

import com.issuetracker.model.Action;
import com.issuetracker.model.Issue;
import com.issuetracker.model.Permission;
import com.issuetracker.model.TypeId;
import com.issuetracker.service.api.ActionService;
import com.issuetracker.service.api.IssueService;
import com.issuetracker.service.api.PermissionService;
import com.issuetracker.service.api.RoleService;
import com.issuetracker.service.api.SecurityService;
import static com.issuetracker.web.Constants.roles;
import com.issuetracker.web.security.KeycloakAuthSession;
import java.util.ArrayList;
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
        Set<Long> userRolesIds = roleService.getIdsByNames(KeycloakAuthSession.getUserRhelmRoles());
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
            log.warn(p);
            actionIds.add(p.getActionId());
        }
        return actionService.getActionNamesByIds(actionIds);
    }

    @Override
    public boolean canUserPerformAction(TypeId typeId, Long itemId, String action) {
        return getPermittedActionsForUserAndItem(typeId, itemId).contains(action);
    }

}
