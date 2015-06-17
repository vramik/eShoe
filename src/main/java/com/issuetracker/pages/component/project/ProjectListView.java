package com.issuetracker.pages.component.project;

import com.issuetracker.model.Project;
import com.issuetracker.pages.project.ProjectDetail;
import com.issuetracker.service.api.ProjectService;
import static com.issuetracker.web.Constants.roles;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;

import javax.inject.Inject;
import java.util.List;
import org.apache.wicket.markup.html.form.IFormModelUpdateListener;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

/**
 *
 * @author mgottval
 * @param <T>
 */
public class ProjectListView<T extends Project> extends Panel implements IFormModelUpdateListener {

    @Inject
    private ProjectService projectService;
    private List<Project> projects;
    private final ListView listViewProjects;

    public ProjectListView(String id, final List<Project> projects) {
        super(id);
        if (projects == null) {
            this.projects = projectService.getDisplayableProjects();
        } else {
            this.projects = projects;
        }
        listViewProjects = new ListView<Project>("projectList", new PropertyModel<List<Project>>(this, "projects")) {
            @Override
            protected void populateItem(final ListItem<Project> item) {
                final Project project = item.getModelObject();

                Link projectDetailLink = new Link<Project>("showProject", item.getModel()) {
                    @Override
                    public void onClick() {
                        PageParameters pageParameters = new PageParameters();
                        pageParameters.add("project", project.getId());
                        setResponsePage(ProjectDetail.class, pageParameters);
                    }
                };
                projectDetailLink.add(new Label("name", project.getName()));
                item.add(projectDetailLink);
                
                
                item.add(new Label("projectLead", project.getOwner()).setVisible(projects == null));
            }
        };
        add(listViewProjects);
        add(new Label("projectNameLabel", "Name").setVisible(projects == null));
        add(new Label("projectLeadLabel", "Project Leader").setVisible(projects == null));
    }

    public List<Project> getProjects() {
        return projects;
    }

    public void setProjects(List<Project> projects) {
        this.projects = projects;
    }

    @Override
    public void updateModel() {
        projects = projectService.getDisplayableProjects();
    }
}
