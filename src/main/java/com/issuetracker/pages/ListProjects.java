package com.issuetracker.pages;

import com.issuetracker.model.Project;
import com.issuetracker.model.User;
import com.issuetracker.service.api.ProjectService;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import javax.inject.Inject;
import java.util.List;

/**
 *
 * @author Monika
 */
public class ListProjects extends PageLayout {

    @Inject
    private ProjectService projectService;
    private ListView listViewProjects;
    private List<Project> projects;

    public ListProjects() {
        projects = projectService.getProjects();

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
                User projectOwner =  project.getOwner();
                String owner;
                if(projectOwner!=null) {
                    owner = project.getOwner().getName() + " " + project.getOwner().getLastName();
                } else {
                    owner = "Not set";
                }
                item.add(new Label("projectLead", owner));

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
