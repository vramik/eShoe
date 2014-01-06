/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.issuetracker.pages;

import com.issuetracker.dao.api.ComponentDao;
import com.issuetracker.dao.api.ProjectDao;
import com.issuetracker.dao.api.ProjectVersionDao;
import com.issuetracker.dao.api.UserDao;
import com.issuetracker.model.Component;
import com.issuetracker.model.CustomField;
import com.issuetracker.model.Project;
import com.issuetracker.model.ProjectVersion;
import com.issuetracker.model.User;
import com.issuetracker.pages.component.component.ComponentListView;
import com.issuetracker.pages.component.customField.CustomFieldListView;
import com.issuetracker.pages.component.project.ProjectListView;
import com.issuetracker.pages.component.version.VersionListView;
import com.issuetracker.pages.validator.ProjectNameValidator;
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
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.ComponentPropertyModel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

/**
 *
 * @author mgottval
 */
public class CreateProject extends PageLayout {

    @Inject
    private UserDao userDao;
    @Inject
    private ProjectDao projectDao;
    @Inject
    private ProjectVersionDao projectVersionDao;
    @Inject
    private ComponentDao componentDao;
    private Form<Project> insertProjectForm;
    private TextField<String> textField;
    private TextField<String> componentTextField;
//    private DropDownChoice<User> v;
    private final IndicatingAjaxButton insertProjectVersionButton;
    private final IndicatingAjaxButton insertComponentButton;
    private final VersionListView<ProjectVersion> versionsListView;
    private final ComponentListView<Component> componentsListView;
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
        projects = projectDao.getProjects();
        if (projects == null) {
            projects = new ArrayList<Project>();
        }
        projectVersion = new ProjectVersion();
        projectVersionList = new ArrayList<ProjectVersion>();
        componentList = new ArrayList<Component>();

        add(new FeedbackPanel("feedbackPanel"));
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

            }
        };
        add(insertProjectForm);

        insertProjectForm.add(new RequiredTextField<String>("name", new PropertyModel<String>(this, "project.name")).add(new ProjectNameValidator()));
        insertProjectForm.add(new RequiredTextField<String>("summary", new PropertyModel<String>(this, "project.summary")));
        textField = new TextField<String>("version", new PropertyModel<String>(this, "string"));
        textField.setOutputMarkupId(true);
        componentTextField = new TextField<String>("componentName", new PropertyModel<String>(this, "stringComponent"));
        componentTextField.setOutputMarkupId(true);
//        usersDropDown = new DropDownChoice<User>("ownerDropDown", this, userDao.get)
        insertProjectForm.add(componentTextField);
        insertProjectForm.add(textField);

        versionsListView = new VersionListView<ProjectVersion>("versionsList", new PropertyModel<List<ProjectVersion>>(this, "projectVersionList"), null);
        add(versionsListView);
        wmc = new WebMarkupContainer("wmc");
        wmc.add(versionsListView);

        add(wmc);
        wmcComponent = new WebMarkupContainer("wmcComponent");
        add(wmcComponent);

        wmc.setOutputMarkupId(true);

        insertProjectVersionButton = new IndicatingAjaxButton("evalButton", insertProjectForm) {
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                target.add(wmc);
                projectVersion = new ProjectVersion();
                projectVersion.setName(textField.getInput());
                textField.clearInput();
                target.add(textField);
//                projectVersionDao.insertProjectVersion(projectVersion);
                projectVersionList.add(projectVersion);
            }

            @Override
            protected void onError(AjaxRequestTarget target, Form<?> form) {
            }
        };

        insertProjectVersionButton.setDefaultFormProcessing(false);
        insertProjectForm.add(insertProjectVersionButton);
        IModel<List<Component>> componModel = new CompoundPropertyModel<List<Component>>(componentList) {
            @Override
            public List<Component> getObject() {
                return componentList;            }
        };

        componentsListView = new ComponentListView<Component>("componentsList", componModel, null);
        wmcComponent.add(componentsListView);
        wmcComponent.setOutputMarkupId(true);

        insertComponentButton = new IndicatingAjaxButton("addComponentButton", insertProjectForm) {
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                target.add(wmcComponent);
                component = new Component();
                component.setName(componentTextField.getInput());
                componentTextField.clearInput();
                target.add(componentTextField);
                componentList.add(component);
            }

            @Override
            protected void onError(AjaxRequestTarget target, Form<?> form) {
            }
        };

        insertComponentButton.setDefaultFormProcessing(false);
        insertProjectForm.add(insertComponentButton);
        
//        
//                //CUSTOM FIELD
//        add(new Label("cfHeader", "Add Custom Field:"));
//        final List<CustomField> customFields = new ArrayList<CustomField>();
//        final Form<CustomField> cfForm;
//        cfForm = new Form("form") {
//            @Override
//            protected void onSubmit() {                
//                customFields.add(customField);
//                issue.setCustomFields(null);
//                issueDao.updateIssue(issue);  
//                customField = new CustomField();
//            }
//        };
//
//        cfForm.add(new RequiredTextField("cfName", new PropertyModel<String>(this, "customField.cfName")));
//        cfForm.add(new RequiredTextField("cfValue", new PropertyModel<String>(this, "customField.cfValue")));
//        add(cfForm);
//
//        IModel<List<CustomField>> cfModel = new CompoundPropertyModel<List<CustomField>>(customFields) {
//            @Override
//            public List<CustomField> getObject() {
//                return customFields;
//            }
//        };
//
//        add(new CustomFieldListView("cfListView", cfModel));
//        //CUSTOM F

        IModel<List<Project>> projectModel = new CompoundPropertyModel<List<Project>>(projects) {
            @Override
            public List<Project> getObject() {
                List<Project> projectList = projectDao.getProjects();
                if (projectList == null) {
                    return new ArrayList<Project>();
                }
                return projectList; //To change body of generated methods, choose Tools | Templates.
            }
        };

        add(new Label("projectName", "Projects"));
        ProjectListView listViewProjects = new ProjectListView<Project>("projectList", projectModel);
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
