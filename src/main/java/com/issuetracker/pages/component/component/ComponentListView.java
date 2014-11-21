package com.issuetracker.pages.component.component;

import com.issuetracker.model.Component;
import com.issuetracker.model.PermissionType;
import com.issuetracker.model.Project;
import com.issuetracker.service.api.ProjectService;
import static com.issuetracker.web.security.KeycloakAuthSession.isUserInAppRole;
import static com.issuetracker.web.security.PermissionsUtil.hasPermissionsProject;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import javax.inject.Inject;
import java.util.List;
import org.apache.wicket.Session;

/**
 *
 * @author mgottval
 * @param <T>
 *
 */
public class ComponentListView<T extends Component> extends Panel {

    @Inject
    private ProjectService projectService;
    private final ListView<Component> componentsListView;
    private List<Component> componentList;
    private Project project;
    private boolean hasEditPermissions = isUserInAppRole("project.create");

    public ComponentListView(String id, IModel<List<Component>> componentsModel, final IModel<Project> projectModel) {
        super(id);
        if (projectModel != null) {
            Long projectId = projectModel.getObject().getId();
            if (projectId != null) {
                project = projectService.getProjectById(projectId);
                hasEditPermissions = hasPermissionsProject(project, PermissionType.edit);
            }
        }
        
        componentList = componentsModel.getObject();
        add(new Label("components", "Components"));

        componentsListView = new ListView<Component>("componentsList", componentsModel) {
            @Override
            protected void populateItem(final ListItem<Component> item) {
                final Component component = item.getModelObject();
                item.add(new Label("name", component.getName()));
                item.add(new Link("remove") {
                    @Override
                    public void onClick() {
                        if (projectModel == null) {
                            componentList.remove(component);
                        } else {
                            if (componentList.size() == 1) {
                                Session.get().error("Last component cannot be removed.");
                            } else {
                                componentList.remove(component);
                                project.setComponents(componentList);
                                projectService.update(project);
                            }
                        }
                    }
                }.setVisible(hasEditPermissions));
            }
        };
        add(componentsListView);
    }
}
