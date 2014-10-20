package com.issuetracker.pages.component.customField;

import com.issuetracker.model.CustomField;
import com.issuetracker.model.Project;
import com.issuetracker.service.api.ProjectService;
import static com.issuetracker.web.security.KeycloakAuthSession.isUserInRole;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

import javax.inject.Inject;
import java.util.List;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.PropertyModel;
/**
 *
 * @author mgottval
 * @param <T>
 */
public class CustomFieldListView<T extends CustomField> extends Panel {

    @Inject
    private ProjectService projectService;
    private final ListView customFieldsListView;
    private List<CustomField> customFieldList;

    @Override
    public void onConfigure() {
        
    }
    
    public CustomFieldListView(String id, IModel<List<CustomField>> customFieldsModel, final IModel<Project> projectModel) {
        super(id);
        customFieldList = customFieldsModel.getObject();
        add(new Label("customFields", "Custom Fields"));
        
        IModel<List<CustomField>> custoModel = new PropertyModel<>(this, "customFieldList");
        customFieldsListView = new ListView<CustomField>("customFieldsList", custoModel) {
            @Override
            protected void populateItem(final ListItem<CustomField> item) {
                final CustomField customField = item.getModelObject();
                item.add(new Label("name", customField.getCfName()));

                item.add(new Link("remove", item.getModel()) {
                    @Override
                    public void onClick() {
                        customFieldList.remove(customField);
                        if (projectModel != null) {
                            Project project = projectService.getProjectById(projectModel.getObject().getId());
                            project.setCustomFields(customFieldList);
                            projectService.update(project);
                            customFieldList = project.getCustomFields();
                        }
                    }
                }.setVisible(isUserInRole(getWebRequest(), "project.update")));
            }
        };
        add(customFieldsListView);
    }

  
}
