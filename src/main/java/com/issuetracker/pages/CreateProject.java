package com.issuetracker.pages;

import com.issuetracker.model.Component;
import com.issuetracker.model.Project;
import com.issuetracker.model.ProjectVersion;
import com.issuetracker.pages.component.component.ComponentListView;
import com.issuetracker.pages.component.project.ProjectListView;
import com.issuetracker.pages.component.version.VersionListView;
import com.issuetracker.pages.validator.ProjectNameValidator;
import com.issuetracker.service.api.ComponentService;
import com.issuetracker.service.api.ProjectService;
import com.issuetracker.service.api.ProjectVersionService;
import com.issuetracker.service.api.UserService;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxButton;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author mgottval
 */
public class CreateProject extends PageLayout {

    @Inject
    private UserService userService;
    @Inject
    private ProjectService projectService;
    @Inject
    private ProjectVersionService projectVersionService;
    @Inject
    private ComponentService componentService;
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
        projects = projectService.getProjects();
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

                projectService.insert(project);
                project = new Project();
                projects = projectService.getProjects();
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
//        usersDropDown = new DropDownChoice<User>("ownerDropDown", this, userService.get)
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
//                projectVersionService.insert(projectVersion);
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
//                issueDao.update(issue);
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
                List<Project> projectList = projectService.getProjects();
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
