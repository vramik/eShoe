package com.issuetracker.pages.component.permission;

import com.issuetracker.model.Permission;
import com.issuetracker.model.PermissionType;
import com.issuetracker.model.Project;
import com.issuetracker.service.api.ProjectService;
import static com.issuetracker.web.security.KeycloakAuthSession.isUserInAppRole;
import static com.issuetracker.web.security.PermissionsUtil.*;
import java.util.ArrayList;
import java.util.HashSet;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;

import java.util.List;
import javax.inject.Inject;
import org.apache.wicket.model.IModel;

/**
 *
 * @author vramik
 * @param <T>
 */
public class PermissionsListView<T extends Permission> extends Panel {

    @Inject
    private ProjectService projectService;
    private final ListView<Permission> permissionsListView;
    private List<Permission> permissions;
    private Project project;
    private boolean hasEditPermissions = isUserInAppRole("project.create");
    
    public PermissionsListView(String id, IModel<List<Permission>> permissionsModel, final IModel<Project> projectModel) {
        super(id);
        if (projectModel != null) {
            Long projectId = projectModel.getObject().getId();
            if (projectId != null) {
                project = projectService.getProjectById(projectId);
                hasEditPermissions = hasPermissionsProject(project, PermissionType.edit);
            }
        } 
        
        permissions = permissionsModel.getObject();
        add(new Label("permissions", "Permissions"));
        add(new Link<Permission>("reset") {
            @Override
            public void onClick() {
                permissions.clear();
                if (projectModel != null) {
                    permissions = checkDefaultProjectPermissions(permissions);
                    project.setPermissions(new HashSet<>(permissions));
                    projectService.update(project);
                }
            } 
        }.setVisible(hasEditPermissions));
        
        permissionsListView = new ListView<Permission>("permissionList", permissionsModel) {

            @Override
            protected void populateItem(ListItem<Permission> item) {
                final Permission permission = item.getModelObject();
                
                item.add(new Label("type", permission.getPermissionType()));
                item.add(new ListView<String>("roles", new ArrayList(permission.getRoles())) {

                    @Override
                    protected void populateItem(ListItem<String> item) {
                        item.add(new Label("role", item.getModelObject()));
                    }
                });
            }
        };
        add(permissionsListView);
    }
}
