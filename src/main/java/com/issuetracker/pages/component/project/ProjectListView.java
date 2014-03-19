package com.issuetracker.pages.component.project;

import com.issuetracker.model.Project;
import com.issuetracker.service.api.IssueService;
import com.issuetracker.service.api.ProjectService;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

import javax.inject.Inject;
import java.util.List;

/**
 *
 * @author mgottval
 */
public class ProjectListView<T extends Project> extends Panel {

    @Inject
    private ProjectService projectService;
    @Inject
    private IssueService issueService;
    private List<Project> projects;
    private ListView listViewProjects;

    public ProjectListView(String id, IModel<List<Project>> projectsModel) {
        super(id);
        listViewProjects = new ListView<Project>("projectList", projectsModel) {
            @Override
            protected void populateItem(final ListItem<Project> item) {
                final Project project = item.getModelObject();
                item.add(new Label("name", project.getName()));
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
