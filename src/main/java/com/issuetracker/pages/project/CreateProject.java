package com.issuetracker.pages.project;

import com.issuetracker.pages.layout.PageLayout;
import com.issuetracker.model.Component;
import com.issuetracker.model.CustomField;
import com.issuetracker.model.Project;
import com.issuetracker.model.ProjectVersion;
import static com.issuetracker.model.TypeId.global;
import com.issuetracker.pages.component.component.ComponentListView;
import com.issuetracker.pages.component.customField.CustomFieldListView;
import com.issuetracker.pages.component.version.VersionListView;
import com.issuetracker.pages.validator.ProjectNameValidator;
import com.issuetracker.service.api.ProjectService;
import com.issuetracker.service.api.SecurityService;
import static com.issuetracker.web.Constants.roles;
import com.issuetracker.web.quilifiers.ViewPageConstraint;
import static com.issuetracker.web.security.KeycloakAuthSession.*;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxButton;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.jboss.logging.Logger;


/**
 *
 * @author mgottval
 */
public class CreateProject extends PageLayout {

    private final Logger log = Logger.getLogger(CreateProject.class);
    
    @Inject
    private ProjectService projectService;
    @Inject
    private SecurityService securityService;
                
    private final WebMarkupContainer wmcVersion, wmcComponent, wmcCustomField;
    private List<ProjectVersion> projectVersionList;
    private List<Component> componentList;
    private List<CustomField> customFieldList;
    private String stringVersion, stringComponent, stringCustomField;
    
    private Project project = new Project();
 
    @ViewPageConstraint(allowedRole = "kc.project.create")
    public CreateProject() {
        projectVersionList = new ArrayList<>();
        componentList = new ArrayList<>();
        customFieldList = new ArrayList<>();

//<editor-fold defaultstate="collapsed" desc="insertProjectForm">
        Form<Project> insertProjectForm = new Form<Project>("insertProjectForm") {
            @Override
            protected void onSubmit() {
                if (securityService.canUserPerformAction(global, null, roles.getProperty("it.project.create"))) {
                    project.setVersions(projectVersionList);
                    project.setComponents(componentList);
                    project.setCustomFields(customFieldList);
                    project.setOwner(getIDToken().getPreferredUsername());
                    projectService.insert(project);
                    
                    PageParameters pageParameters = new PageParameters();
                    pageParameters.add("project", project.getId());
                    setResponsePage(ProjectDetail.class, pageParameters);
                } else {
                    error("Unsuffitient privilages");
                }
            }

            @Override
            protected void onValidate() {
                super.onValidate();
                if (projectVersionList.isEmpty()) {
                    error("There are no versions added! You need to add some versions first.");
                }
                if (componentList.isEmpty()) {
                    error("There are no components added! You need to add some components first.");
                }
            }
        };
        add(insertProjectForm);
        
        insertProjectForm.add(new RequiredTextField<>("name", new PropertyModel<String>(this, "project.name")).add(new ProjectNameValidator()));
        insertProjectForm.add(new RequiredTextField<>("summary", new PropertyModel<String>(this, "project.summary")));
        
        final TextField<String> versionTextField = new TextField<>("version", new PropertyModel<String>(this, "stringVersion"));
        versionTextField.setOutputMarkupId(true);
        insertProjectForm.add(versionTextField);
        
        IndicatingAjaxButton insertProjectVersionButton = new IndicatingAjaxButton("addVersionButton", insertProjectForm) {
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                target.add(wmcVersion);
                ProjectVersion projectVersion = new ProjectVersion();
                projectVersion.setName(versionTextField.getInput());
                versionTextField.clearInput();
                target.add(versionTextField);
                projectVersionList.add(projectVersion);
            }
        };
        insertProjectVersionButton.setDefaultFormProcessing(false);
        insertProjectForm.add(insertProjectVersionButton);
        
        final TextField<String> componentTextField = new TextField<>("componentName", new PropertyModel<String>(this, "stringComponent"));
        componentTextField.setOutputMarkupId(true);
        insertProjectForm.add(componentTextField);
        
        IndicatingAjaxButton insertComponentButton = new IndicatingAjaxButton("addComponentButton", insertProjectForm) {
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                target.add(wmcComponent);
                Component component = new Component();
                component.setName(componentTextField.getInput());
                componentTextField.clearInput();
                target.add(componentTextField);
                componentList.add(component);
            }
        };
        insertComponentButton.setDefaultFormProcessing(false);
        insertProjectForm.add(insertComponentButton);
        
        final TextField<String> customFieldTextField = new TextField<>("customFieldName", new PropertyModel<String>(this, "stringCustomField"));
        customFieldTextField.setOutputMarkupId(true);
        insertProjectForm.add(customFieldTextField);
        
        IndicatingAjaxButton insertCustomFieldButton = new IndicatingAjaxButton("addCustomFieldButton", insertProjectForm) {
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                target.add(wmcCustomField);
                CustomField customField = new CustomField();
                customField.setCfName(customFieldTextField.getInput());
                customFieldTextField.clearInput();
                target.add(customFieldTextField);
                customFieldList.add(customField);
            }
        };
        insertCustomFieldButton.setDefaultFormProcessing(false);
        insertProjectForm.add(insertCustomFieldButton);
//</editor-fold>
                
//<editor-fold defaultstate="collapsed" desc="versionList">
        IModel<List<ProjectVersion>> versionModel = new CompoundPropertyModel<List<ProjectVersion>>(projectVersionList) {
            @Override
            public List<ProjectVersion> getObject() {
                return projectVersionList;
            }
        };
        VersionListView<ProjectVersion> versionsListView = new VersionListView<>("versionsList", versionModel, null);
        wmcVersion = new WebMarkupContainer("wmcVersion");
        wmcVersion.add(versionsListView);
        wmcVersion.setOutputMarkupId(true);
        add(wmcVersion);
//</editor-fold>
        
//<editor-fold defaultstate="collapsed" desc="componentList">
        IModel<List<Component>> componentModel = new CompoundPropertyModel<List<Component>>(componentList) {
            @Override
            public List<Component> getObject() {
                return componentList;
            }
        };
        ComponentListView<Component> componentsListView = new ComponentListView<>("componentsList", componentModel, null);
        wmcComponent = new WebMarkupContainer("wmcComponent");
        wmcComponent.add(componentsListView);
        wmcComponent.setOutputMarkupId(true);
        add(wmcComponent);
//</editor-fold>
        
//<editor-fold defaultstate="collapsed" desc="customFieldList">
        IModel<List<CustomField>> customFieldModel = new CompoundPropertyModel<List<CustomField>>(customFieldList) {
            @Override
            public List<CustomField> getObject() {
                return customFieldList;
            }
        };
        CustomFieldListView<CustomField> customFieldListView = new CustomFieldListView<>("customFieldsList", customFieldModel, null);
        wmcCustomField = new WebMarkupContainer("wmcCustomField");
        wmcCustomField.add(customFieldListView);
        wmcCustomField.setOutputMarkupId(true);
        add(wmcCustomField);
//</editor-fold>


    }

//<editor-fold defaultstate="collapsed" desc="getters/setters">
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

    public List<Component> getComponentList() {
        return componentList;
    }

    public void setComponentList(List<Component> componentList) {
        this.componentList = componentList;
    }

    public List<CustomField> getCustomFieldList() {
        return customFieldList;
    }

    public void setCustomFieldList(List<CustomField> customFieldList) {
        this.customFieldList = customFieldList;
    }
    
    public String getStringVersion() {
        return stringVersion;
    }

    public void setStringVersion(String stringVersion) {
        this.stringVersion = stringVersion;
    }

    public String getStringComponent() {
        return stringComponent;
    }

    public void setStringComponent(String stringComponent) {
        this.stringComponent = stringComponent;
    }

    public String getStringCustomField() {
        return stringCustomField;
    }

    public void setStringCustomField(String stringCustomField) {
        this.stringCustomField = stringCustomField;
    }
//</editor-fold>
    
    
}
