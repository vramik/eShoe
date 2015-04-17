package com.issuetracker.pages.component.permission;

import com.issuetracker.model.Project;
import com.issuetracker.model.TypeId;
import com.issuetracker.service.api.PermissionService;
import static com.issuetracker.web.Constants.roles;
import static com.issuetracker.web.security.PermissionsUtil.*;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.PropertyModel;

import javax.inject.Inject;
import java.util.ArrayList;
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
    
    private List<String> selectedRoles = new ArrayList<>();
    List<String> availableRoles = getAvailableRoles();
    
    public VisibilityProjectForm(String id, final Project project) {
        super(id);
        
        log.error("TODO: availableRoles");//jake role budou available
        
        Form visibilityForm = new Form("visibilityForm") {
            @Override
            protected void onSubmit() {
                
                if (!selectedRoles.isEmpty() && (availableRoles.size() != selectedRoles.size())) {//if NOT empty or all
                    permissionService.createPermissions(TypeId.project, project, roles.getProperty("it.project.browse"), TypeId.project, selectedRoles.toArray(new String[selectedRoles.size()]));
                } 
               
                //reset the select
                selectedRoles = null;
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
}
