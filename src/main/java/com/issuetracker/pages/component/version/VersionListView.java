package com.issuetracker.pages.component.version;

import com.issuetracker.model.PermissionType;
import com.issuetracker.model.Project;
import com.issuetracker.model.ProjectVersion;
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
 */
public class VersionListView<T extends ProjectVersion> extends Panel {

    @Inject
    private ProjectService projectService;
    private List<ProjectVersion> projectVersionList;
    private final ListView<ProjectVersion> versionsListView;
    private Project project;
    private boolean hasEditPermissions = isUserInAppRole(getRequest(), "project.create");

    public VersionListView(String id, IModel<List<ProjectVersion>> versionsModel, final IModel<Project> projectModel) {
        super(id);
        if (projectModel != null) {
            project = projectService.getProjectById(projectModel.getObject().getId());
            hasEditPermissions = hasPermissionsProject(getRequest(), project, PermissionType.edit);
        }
        
        projectVersionList = versionsModel.getObject();
        add(new Label("versions", "Versions"));
        
        versionsListView = new ListView<ProjectVersion>("versionsList", versionsModel) {
            @Override
            protected void populateItem(ListItem<ProjectVersion> item) {
                final ProjectVersion projectVersion = item.getModelObject();
                
                item.add(new Label("name", projectVersion.getName()));
                item.add(new Link<ProjectVersion>("remove") {
                    @Override
                    public void onClick() {
                        if (projectModel == null) {
                            projectVersionList.remove(projectVersion);
                        } else {
                            if (projectVersionList.size() == 1) {
                                Session.get().error("Last Version cannot be removed.");
                            } else {
                                projectVersionList.remove(projectVersion);
                                project.setVersions(projectVersionList);
                                projectService.update(project);
                            }
                        }
                    }
                }.setVisible(hasEditPermissions));
            }
        };
        add(versionsListView);
    }
}
