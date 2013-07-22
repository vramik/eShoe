/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.issuetracker.pages;

import com.issuetracker.dao.api.ComponentDao;
import com.issuetracker.dao.api.ProjectDao;
import com.issuetracker.dao.api.ProjectVersionDao;
import com.issuetracker.model.Component;
import com.issuetracker.model.Project;
import com.issuetracker.model.ProjectVersion;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Inject;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxButton;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

/**
 *
 * @author mgottval
 */
public class CreateProject extends PageLayout {

    @Inject
    private ProjectDao projectDao;
    @Inject
    private ProjectVersionDao projectVersionDao;
    @Inject
    private ComponentDao componentDao;
    
    private Form<Project> insertProjectForm;
    private TextField<String> textField;
    private TextField<String> componentTextField;
    private final IndicatingAjaxButton insertProjectVersionButton;
    private final IndicatingAjaxButton insertComponentButton;
    private final ListView<ProjectVersion> versionsListView;
    private final ListView<Component> componentsListView;
    private final WebMarkupContainer wmc;
    private final WebMarkupContainer wmcComponent;
    
    private List<Project> projects;
    private List<ProjectVersion> projectVersionList;
    private List<Component> componentList;
    private Project project;        
    private ProjectVersion projectVersion;
    private Component component;
    private String string;
    private String stringComponent;
    
    

    public CreateProject() {
        project = new Project();
        projects = new ArrayList<Project>();
        projects = projectDao.getProjects();
        projectVersion = new ProjectVersion();
        projectVersionList = new ArrayList<ProjectVersion>(); 
        componentList = new ArrayList<Component>();

        insertProjectForm = new Form<Project>("insertProjectForm") {
            @Override
            protected void onSubmit() {
                project.setVersions(projectVersionList);
                project.setComponents(componentList);
                projectDao.insertProject(project);
                project = new Project();
                projects = projectDao.getProjects();
                projectVersionList.clear();
                componentList.clear();                               
                
                //test
                List<Component> versionsTest = componentDao.getComponents();
                String s = "";
                for (Component projectVersion1 : versionsTest) {
                    s = s + projectVersion1.getName();
                }
                Logger.getLogger(CreateProject.class.getName()).log(Level.SEVERE, s);
            }
        };
        add(insertProjectForm);

        insertProjectForm.add(new RequiredTextField<String>("name", new PropertyModel<String>(this, "project.name")));
        textField = new RequiredTextField<String>("version", new PropertyModel<String>(this, "string"));
        componentTextField = new RequiredTextField<String>("componentName", new PropertyModel<String>(this, "stringComponent"));
        insertProjectForm.add(componentTextField);
        insertProjectForm.add(textField);
                
        versionsListView = new ListView<ProjectVersion>("versionsList",  new PropertyModel<List<ProjectVersion>>(this, "projectVersionList")) {
            @Override
            protected void populateItem(ListItem<ProjectVersion> item) {
                final ProjectVersion projectVersion = item.getModelObject();
                item.add(new Link<ProjectVersion>("remove", item.getModel()) {
                    @Override
                    public void onClick() {
                        projectVersionList.remove(projectVersion);                       
                        projectVersionDao.remove(projectVersion);
                    }
                });
                item.add(new Label("name", projectVersion.getName()));
            }
        };
        add(versionsListView);
        wmc = new WebMarkupContainer("wmc");
        add(wmc);
        wmcComponent = new WebMarkupContainer("wmcComponent");
        add(wmcComponent);
        wmc.add(versionsListView);
        wmc.setOutputMarkupId(true);

        insertProjectVersionButton = new IndicatingAjaxButton("evalButton", insertProjectForm) {
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                target.add(wmc);
                projectVersion = new ProjectVersion();
                projectVersion.setName(textField.getInput());
                Logger.getLogger(CreateProject.class.getName()).log(Level.SEVERE, textField.getValue());
                projectVersionDao.insertProjectVersion(projectVersion);
                projectVersionList.add(projectVersion);
            }
            @Override
            protected void onError(AjaxRequestTarget target, Form<?> form) {
            }
        };
        
        insertProjectVersionButton.setDefaultFormProcessing(false);
        insertProjectForm.add(insertProjectVersionButton);


        componentsListView = new ListView<Component>("componentsList",  new PropertyModel<List<Component>>(this, "componentList")) {
            @Override
            protected void populateItem(ListItem<Component> item) {
                final Component component = item.getModelObject();
                item.add(new Link<Component>("remove", item.getModel()) {
                    @Override
                    public void onClick() {
                        componentList.remove(component);                       
                        componentDao.remove(component);
                    }
                });
                item.add(new Label("name", component.getName()));
            }
        };
        add(componentsListView);
        wmcComponent.add(componentsListView);
        wmcComponent.setOutputMarkupId(true);
        
        insertComponentButton = new IndicatingAjaxButton("addComponentButton", insertProjectForm) {
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                target.add(wmcComponent);
                component = new Component();
                component.setName(componentTextField.getInput());
                Logger.getLogger(CreateProject.class.getName()).log(Level.SEVERE, componentTextField.getValue());
                componentDao.insertComponent(component);
                componentList.add(component);
            }
            @Override
            protected void onError(AjaxRequestTarget target, Form<?> form) {
            }
        };
        
        insertComponentButton.setDefaultFormProcessing(false);
        insertProjectForm.add(insertComponentButton);


        





        ListView listViewProjects = new ListView<Project>("projectList", new PropertyModel<List<Project>>(this, "projects")) {
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

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

        public List<ProjectVersion> getProjectVersionList() {
            return projectVersionList;
        }
    
        public void setProjectVersionList(List<ProjectVersion> projectVersionList) {
            this.projectVersionList = projectVersionList;
        }
    
    

    public String getString() {
        return string;
    }

    public void setString(String string) {
        this.string = string;
    }

    public String getStringComponent() {
        return stringComponent;
    }

    public void setStringComponent(String stringComponent) {
        this.stringComponent = stringComponent;
    }
  

    public List<Project> getProjects() {
        return projects;
    }

    public void setProjects(List<Project> projects) {
        this.projects = projects;
    }

    public List<Component> getComponentList() {
        return componentList;
    }

    public void setComponentList(List<Component> componentList) {
        this.componentList = componentList;
    }
    
    
}
