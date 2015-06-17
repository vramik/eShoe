package com.issuetracker.pages.component.permission;

import com.issuetracker.model.Action;
import com.issuetracker.model.Permission;
import com.issuetracker.model.Project;
import com.issuetracker.model.TypeId;
import com.issuetracker.service.api.ActionService;
import com.issuetracker.service.api.PermissionService;
import com.issuetracker.service.api.RoleService;
import static com.issuetracker.web.Constants.roles;
import static com.issuetracker.web.security.KeycloakService.getRealmRoles;
import java.util.ArrayList;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.PropertyModel;

import javax.inject.Inject;
import java.util.List;
import org.apache.wicket.markup.html.form.ListMultipleChoice;
import org.jboss.logging.Logger;

/**
 *
 * @author mgottval
 */
public class VisibilityProjectForm extends Panel {

    private final Logger log = Logger.getLogger(VisibilityProjectForm.class);
    
    @Inject private PermissionService permissionService;
    @Inject private ActionService actionService;
    @Inject private RoleService roleService;
    
    private List<String> selectedRoles;
    private List<String> availableRoles = getRealmRoles();
    
    private final Action visibilityAction = actionService.getActionByNameAndType(roles.getProperty("it.project.browse"), TypeId.project);
    
    public VisibilityProjectForm(String id, final Project project) {
        super(id);
        
        selectedRoles = getSelectedRoles(project);
        
        Form visibilityForm = new Form("visibilityForm") {
            @Override
            protected void onSubmit() {
                if (!selectedRoles.isEmpty() && (availableRoles.size() != selectedRoles.size())) {//if NOT empty or all
                    permissionService.createPermissions(TypeId.project, project, roles.getProperty("it.project.browse"), TypeId.project, selectedRoles.toArray(new String[selectedRoles.size()]));
                } 
            }
        };
        add(visibilityForm);

        final ListMultipleChoice<String> roles = new ListMultipleChoice<>("roles", 
                new PropertyModel<List<String>>(this, "selectedRoles"),
                availableRoles);
        roles.setOutputMarkupId(true);
        roles.setMaxRows(availableRoles.size());
        visibilityForm.add(roles);
    }

    public List<String> getSelectedRoles() {
        return selectedRoles;
    }

    public void setSelectedRoles(List<String> selectedRoles) {
        this.selectedRoles = selectedRoles;
    }
    
    private List<String> getSelectedRoles(Project project) {
        List<String> result = new ArrayList<>();
        List<Permission> visibilityPermissions = permissionService.getPermissionsByAction(TypeId.project, project.getId(), visibilityAction.getId());
        for (Permission p : visibilityPermissions) {
            result.add(roleService.getRoleById(p.getRoleId()).getName());
        }
        return result;
    }
}
