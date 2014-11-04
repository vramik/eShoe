package com.issuetracker.pages.component.permission;

import com.issuetracker.model.Permission;
import com.issuetracker.model.PermissionType;
import com.issuetracker.model.Project;
import com.issuetracker.service.api.ProjectService;
import com.issuetracker.web.security.KeycloakService;
import static com.issuetracker.web.security.KeycloakService.getRhelmRoles;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import javax.inject.Inject;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.ListMultipleChoice;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

/**
 *
 * @author vramik
 * @param <T>
 */
public class PermissionsForm<T extends Permission> extends Panel {
    
    @Inject
    private ProjectService projectService;
    private List<Permission> permissions;
    private String selectedPermissionType;
    private List<String> selectedRoles;
    
    public PermissionsForm(String id, IModel<List<Permission>> modelPermissions, final IModel<Project> projectModel) {
        super(id);
        
        permissions = modelPermissions.getObject();
        Form<Permission> insertPermissionForm = new Form<Permission>("insertPermissionForm") {
            @Override
            protected void onSubmit() {
                Permission permission = new Permission();
                permission.setPermissionType(PermissionType.valueOf(selectedPermissionType));
                permission.setRoles(new HashSet<>(selectedRoles));
                if (permissions.contains(permission)) {
                    permissions.remove(permission);
                }
                permissions.add(permission);
                if (projectModel != null) {
                    Project project = projectService.getProjectById(projectModel.getObject().getId());
                    project.setPermissions(new HashSet<>(permissions));
                    projectService.update(project);
                }
                //this will clear the form
                selectedPermissionType = null;
                selectedRoles = null;
            }
        };
        add(insertPermissionForm);
        
        final DropDownChoice<PermissionType> permissionTypes = new DropDownChoice<>("permissionTypes", 
                new PropertyModel<PermissionType>(this, "selectedPermissionType"),
                Arrays.asList(PermissionType.values()));
        permissionTypes.setRequired(true);
        permissionTypes.setOutputMarkupId(true);
        insertPermissionForm.add(permissionTypes);
        
        List<String> availableRoles = null;
        try {
            availableRoles = new ArrayList<>(getRhelmRoles(getRequest()));
        } catch (KeycloakService.Failure f) {
            throw new RuntimeException("Returned status code was: " + f.getStatus(), f);
        }
        final ListMultipleChoice<String> roles = new ListMultipleChoice<>("roles", 
                new PropertyModel<List<String>>(this, "selectedRoles"),
                availableRoles);
        roles.setRequired(true);
        roles.setOutputMarkupId(true);
        roles.setMaxRows(availableRoles.size());
        insertPermissionForm.add(roles);
        add(insertPermissionForm);
    }
}
