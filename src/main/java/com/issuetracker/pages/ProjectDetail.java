package com.issuetracker.pages;

import com.issuetracker.dao.api.ComponentDao;
import com.issuetracker.dao.api.ProjectDao;
import com.issuetracker.dao.api.ProjectVersionDao;
import com.issuetracker.model.Component;
import com.issuetracker.model.Project;
import com.issuetracker.model.ProjectVersion;
import java.util.List;
import javax.inject.Inject;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

/**
 *
 * @author mgottval
 */
public class ProjectDetail extends PageLayout{
    @Inject
    private ProjectDao projectDao;
    @Inject
    private ProjectVersionDao projectVersionDao;
    @Inject
    private ComponentDao componentDao;
    
    private ListView<ProjectVersion> projectVersionListView;
    private ListView<Component> projectComponentListView;
    
    private List<ProjectVersion> projectVersionList; 
    private List<Component> projectComponentList; 
    
    public ProjectDetail(PageParameters parameters) {
        Long projectId = parameters.get("project").toLong();
        final Project project = projectDao.getProjectById(projectId);
            
        projectVersionList = project.getVersions();
        projectComponentList = project.getComponents();
        
        
        add(new Label("name", project.getName()));
        
        projectVersionListView = new ListView("projectVersionListView", new PropertyModel<List<ProjectVersion>>(this, "projectVersionList")) {
            @Override
            protected void populateItem(ListItem item) {
                final ProjectVersion projectVersion = (ProjectVersion)item.getModelObject();
                item.add(new Label("versionName", projectVersion.getName()));
                //ADMINS ONLY
                item.add(new Link<ProjectVersion>("remove", item.getModel()) {
                    @Override
                    public void onClick() {
                        projectVersionList.remove(projectVersion);     
                        project.setVersions(projectVersionList);
                        projectDao.update(project);
                        projectVersionDao.remove(projectVersion);
                    }
                });
            }
            
        };
        add(projectVersionListView);
        
        projectComponentListView = new ListView<Component>("projectComponentListView", projectComponentList) {
            @Override
            protected void populateItem(ListItem<Component> item) {
                final Component component = item.getModelObject();
                item.add(new Label("componentName", component.getName()));
                item.add(new Link<Component>("remove", item.getModel()) {
                    @Override
                    public void onClick() {
                        projectComponentList.remove(component);   
                        project.setComponents(projectComponentList);
                        projectDao.update(project);
                        componentDao.remove(component);
                    }
                });
            }
        };
        add(projectComponentListView);                    
    }
    
}
