package com.issuetracker.pages.component.project;

import com.issuetracker.dao.api.ProjectDao;
import com.issuetracker.model.Project;
import java.util.List;
import javax.inject.Inject;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

/**
 *
 * @author mgottval
 */
public class ProjectListView<T extends Project> extends Panel {

    @Inject
    private ProjectDao projectDao;
    private List<Project> projects;
    private ListView listViewProjects;

    public ProjectListView(String id, IModel<List<Project>> projectsModel) {
        super(id);
        listViewProjects = new ListView<Project>("projectList", projectsModel) {
            @Override
            protected void populateItem(final ListItem<Project> item) {
                final Project project = item.getModelObject();
                item.add(new Label("name", project.getName()));
                // item.add(new Label("owner", )); //owner

                item.add(new Link<Project>("delete", item.getModel()) {
                    @Override
                    public void onClick() {
                        projectDao.remove(project);
                        projects = projectDao.getProjects();
                    }
                });
            }
        };
        add(listViewProjects);
    }

    public List<Project> getProjects() {
        return projects;
    }

    public void setProjects(List<Project> projects) {
        this.projects = projects;
    }
}
