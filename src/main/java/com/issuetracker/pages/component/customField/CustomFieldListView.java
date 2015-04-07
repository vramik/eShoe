package com.issuetracker.pages.component.customField;

import com.issuetracker.model.CustomField;
import com.issuetracker.model.Project;
import com.issuetracker.service.api.ProjectService;
import static com.issuetracker.web.security.KeycloakAuthSession.isUserInAppRole;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

import javax.inject.Inject;
import java.util.List;
import org.apache.wicket.markup.html.link.Link;

/**
 *
 * @author mgottval
 * @param <T>
 */
public class CustomFieldListView<T extends CustomField> extends Panel {

    @Inject
    private ProjectService projectService;
    private final ListView<CustomField> customFieldsListView;
    private final Label label;
    private List<CustomField> customFieldList;
    private Project project;
    private boolean hasEditPermissions = isUserInAppRole("keycloak.project.create");
    
    @Override
    public void onConfigure() {
        customFieldsListView.setVisible(!customFieldList.isEmpty());
        label.setVisible(!customFieldList.isEmpty());
    }
    
    public CustomFieldListView(String id, IModel<List<CustomField>> customFieldsModel, final IModel<Project> projectModel) {
        super(id);
        if (projectModel != null) {
            project = projectService.getProjectById(projectModel.getObject().getId());
//            hasEditPermissions = hasPermissionsProject(project, PermissionType.edit);
        }
        
        customFieldList = customFieldsModel.getObject();
        add(label = new Label("customFields", "Custom Fields"));
        
        customFieldsListView = new ListView<CustomField>("customFieldsList", customFieldsModel) {
            @Override
            protected void populateItem(final ListItem<CustomField> item) {
                final CustomField customField = item.getModelObject();
                item.add(new Label("name", customField.getCfName()));
                item.add(new Link("remove") {
                    @Override
                    public void onClick() {
                        customFieldList.remove(customField);
                        if (projectModel != null) {
                            project.setCustomFields(customFieldList);
                            projectService.update(project);
                        }
                    }
                }.setVisible(hasEditPermissions));
            }
        };
        add(customFieldsListView);
    }
}
