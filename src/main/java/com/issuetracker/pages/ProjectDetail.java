/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.issuetracker.pages;

import com.issuetracker.dao.api.ComponentDao;
import com.issuetracker.dao.api.ProjectDao;
import com.issuetracker.dao.api.ProjectVersionDao;
import com.issuetracker.model.Component;
import com.issuetracker.model.CustomField;
import com.issuetracker.model.Project;
import com.issuetracker.model.ProjectVersion;
import com.issuetracker.pages.component.customField.CustomFieldListView;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.jboss.logging.Logger;

/**
 *
 * @author mgottval
 */
public class ProjectDetail extends PageLayout {

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
    private List<CustomField> projectCustomFieldsList;    
    private List<CustomField> customFieldsList;    
    private CustomField customField;
    
    public ProjectDetail(PageParameters parameters) {
        final Long projectId = parameters.get("project").toLong();
        final Project project = projectDao.getProjectById(projectId);
        
        projectVersionList = project.getVersions();
        projectComponentList = project.getComponents();
        projectCustomFieldsList = project.getCustomFields();
        customFieldsList = project.getCustomFields();
        
        add(new Label("name", project.getName()));
        
        projectVersionListView = new ListView("projectVersionListView", new PropertyModel<List<ProjectVersion>>(this, "projectVersionList")) {
            @Override
            protected void populateItem(ListItem item) {
                final ProjectVersion projectVersion = (ProjectVersion) item.getModelObject();
                item.add(new Label("versionName", projectVersion.getName()));
            }
        };
        add(projectVersionListView);
 
        projectComponentListView = new ListView<Component>("projectComponentListView", projectComponentList) {
            @Override
            protected void populateItem(ListItem<Component> item) {
                final Component component = item.getModelObject();
                item.add(new Label("componentName", component.getName()));
//                item.add(new Link<Component>("remove", item.getModel()) {
//                    @Override
//                    public void onClick() {
//                        projectComponentList.remove(component);                        
//                        project.setComponents(projectComponentList);
//                        projectDao.update(project);
//                        componentDao.remove(component);
//                    }
//                });
            }
        };
        add(projectComponentListView);



        //CUSTOM FIELD
        add(new Label("cfHeader", "Add Custom Field:"));
//        final List<CustomField> customFields = new ArrayList<CustomField>();
        
        final Form<CustomField> cfForm;
        cfForm = new Form("form") {
            @Override
            protected void onSubmit() { 
                List<CustomField> getF = project.getCustomFields();
                projectCustomFieldsList.add(customField);
                project.setCustomFields(projectCustomFieldsList);
                projectDao.update(project);
                customField = new CustomField();
                projectCustomFieldsList.clear();
                
            }
        };
        
        cfForm.add(new RequiredTextField("cfName", new PropertyModel<String>(this, "customField.cfName")));
        add(cfForm);
        
        IModel<List<CustomField>> cfModel = new CompoundPropertyModel<List<CustomField>>(customFieldsList) {
            @Override
            public List<CustomField> getObject() {
                Project p = projectDao.getProjectById(projectId);
                return p.getCustomFields();
            }
        };
        
        add(new CustomFieldListView("cfListView", cfModel));
        //CUSTOM F

        
    }
    
    public CustomField getCustomField() {
        return customField;
    }
    
    public void setCustomField(CustomField customField) {
        this.customField = customField;
    }
}
