package com.issuetracker.pages.permissions;

import com.issuetracker.model.Role;
import com.issuetracker.model.TypeId;
import com.issuetracker.pages.component.permission.PermissionListView;
import com.issuetracker.pages.layout.PageLayout;
import com.issuetracker.service.api.ProjectService;
import com.issuetracker.service.api.RoleService;
import com.issuetracker.service.api.SecurityService;
import static com.issuetracker.web.Constants.roles;
import static com.issuetracker.web.security.KeycloakService.getRealmRoles;
import javax.inject.Inject;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.jboss.logging.Logger;

/**
 *
 * @author vramik
 */
public class GlobalPermission extends PageLayout {

    private final Logger log = Logger.getLogger(GlobalPermission.class);
    
    @Inject RoleService roleService;
    @Inject ProjectService projectService;
    @Inject SecurityService securityService;
    
    public GlobalPermission() {
        if (!securityService.canUserPerformAction(TypeId.global, 0L, roles.getProperty("it.permissions"))) {
            setResponsePage(AccessDenied.class);
        }
        add(new Label("permissionTitle", "Default Permissions"));
        
        roleService.addRolesByNames(getRealmRoles());
                
        ListView<Role> permissions = new ListView<Role>("permissions", roleService.getRoles()) {
            
            @Override
            protected void populateItem(ListItem<Role> item) {
                item.add(new PermissionListView("permission", item.getModelObject(), TypeId.global, 0L));
            }
        };
        add(permissions);
    }
}

