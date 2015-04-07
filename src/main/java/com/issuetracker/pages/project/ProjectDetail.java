package com.issuetracker.pages.project;

import com.issuetracker.pages.layout.PageLayout;
import com.issuetracker.model.Component;
import com.issuetracker.model.CustomField;
import com.issuetracker.model.Project;
import com.issuetracker.model.ProjectVersion;
import com.issuetracker.model.TypeId;
import com.issuetracker.pages.component.component.ComponentListView;
import com.issuetracker.pages.component.customField.CustomFieldListView;
import com.issuetracker.pages.component.version.VersionListView;
import com.issuetracker.pages.permissions.ProjectPermission;
import com.issuetracker.pages.validator.ProjectNameValidator;
import com.issuetracker.service.api.PermissionService;
import com.issuetracker.service.api.ProjectService;
import com.issuetracker.service.api.SecurityService;
import static com.issuetracker.web.Constants.*;
import java.util.ArrayList;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import javax.inject.Inject;
import java.util.List;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.Model;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.request.flow.RedirectToUrlException;
import org.apache.wicket.util.string.StringValue;
import org.jboss.logging.Logger;

/**
 *
 * @author mgottval
 */
public class ProjectDetail extends PageLayout {

    @Inject
    private ProjectService projectService;
    @Inject
    private SecurityService securityService;
    @Inject
    private PermissionService permissionService;
    
    private final Form<String> renameForm;
    private final Form<ProjectVersion> projectVersionForm;
    private final Form<Component> projectComponentForm;
    private final Form<CustomField> projectCustomFieldForm;
    private Link deleteProject;
    private List<ProjectVersion> projectVersionList;    
    private List<Component> projectComponentList;    
    private List<CustomField> projectCustomFieldsList;    
    private TextField versionTextField;
    private TextField componentTextField;
    private TextField customFieldTextField;
    private Project project;
    private String stringVersion, stringComponent, stringCustomField;
    
    List<String> permittedActions = new ArrayList<>();
    
    @Override
    public void onConfigure() {
        super.onConfigure();
        
        renameForm.setVisible(permittedActions.contains(roles.getProperty("it.project.rename")));
        projectVersionForm.setVisible(permittedActions.contains(roles.getProperty("it.project.versions")));
        projectComponentForm.setVisible(permittedActions.contains(roles.getProperty("it.project.components")));
        projectCustomFieldForm.setVisible(permittedActions.contains(roles.getProperty("it.project.custom.fields")));
        deleteProject.setVisible(permittedActions.contains(roles.getProperty("it.project.delete")));
    }
    
    public ProjectDetail(PageParameters parameters) {
        StringValue stringProjectId = parameters.get("project");
        if (stringProjectId.equals(StringValue.valueOf((String)null))) {
            throw new RedirectToUrlException(HOME_PAGE);
        }
        Long projectId = stringProjectId.toLong();
        permittedActions = securityService.getPermittedActionsForUserAndItem(TypeId.project, projectId);
        
        project = projectService.getProjectById(projectId);
        
        projectVersionList = project.getVersions();
        projectComponentList = project.getComponents();
        projectCustomFieldsList = project.getCustomFields();
        
        add(new Label("name", new PropertyModel<String>(this, "project.name")));
        
        renameForm = new Form<String>("renameForm") {
            @Override
            protected void onSubmit() {
                projectService.update(project);
            }
        };
        renameForm.add(new RequiredTextField<>("rename", new PropertyModel<String>(this, "project.name")).add(new ProjectNameValidator()));
        add(renameForm);
        
        add(new Label("summary", project.getSummary()));
        deleteProject = new Link("delete") {

            @Override
            public void onClick() {
                projectService.remove(project);
                setResponsePage(ListProjects.class);
            }
        };
        add(deleteProject);
        
        add(new Link("permissions") {
            @Override
            public void onClick() {
                PageParameters pageParameters = new PageParameters();
                pageParameters.add("project", project.getId());
                setResponsePage(ProjectPermission.class, pageParameters);
            }
        });
        
//<editor-fold defaultstate="collapsed" desc="versions">
        IModel<List<ProjectVersion>> versionModel = new CompoundPropertyModel<List<ProjectVersion>>(projectVersionList) {
            @Override
            public List<ProjectVersion> getObject() {
                return projectVersionList;            
            }
        };
        VersionListView<ProjectVersion> versionsListView = new VersionListView<>("versionsList", versionModel, new Model<>(project));
        final WebMarkupContainer wmcVersion = new WebMarkupContainer("wmcVersion");
        wmcVersion.add(versionsListView);
        wmcVersion.setOutputMarkupId(true);
        add(wmcVersion);
        
        versionTextField = new TextField<>("version", new PropertyModel<String>(this, "stringVersion"));
        versionTextField.setOutputMarkupId(true);
        projectVersionForm = new Form<ProjectVersion>("projectVersionForm") {
            @Override
            protected void onSubmit() {
                ProjectVersion projectVersion = new ProjectVersion();
                projectVersion.setName(versionTextField.getInput());
                stringVersion = null; //this will clear version textfield
                projectVersionList.add(projectVersion);
                project.setVersions(projectVersionList);
                projectService.update(project);
            }
        };
        projectVersionForm.add(versionTextField);
        add(projectVersionForm);
//</editor-fold>
        
//<editor-fold defaultstate="collapsed" desc="components">
        IModel<List<Component>> componentModel = new CompoundPropertyModel<List<Component>>(projectComponentList) {
            @Override
            public List<Component> getObject() {
                return projectComponentList;            
            }
        };
        ComponentListView<Component> componentsListView = new ComponentListView<>("componentsList", componentModel, new Model<>(project));
        final WebMarkupContainer wmcComponent = new WebMarkupContainer("wmcComponent");
        wmcComponent.add(componentsListView);
        wmcComponent.setOutputMarkupId(true);
        add(wmcComponent);
        
        componentTextField = new TextField<>("component", new PropertyModel<String>(this, "stringComponent"));
        componentTextField.setOutputMarkupId(true);
        projectComponentForm = new Form<Component>("projectComponentForm") {
            @Override
            protected void onSubmit() {
                Component component = new Component();
                component.setName(componentTextField.getInput());
                stringComponent = null; //this will clear component textfield
                projectComponentList.add(component);
                project.setComponents(projectComponentList);
                projectService.update(project);
            }
        };
        projectComponentForm.add(componentTextField);
        add(projectComponentForm);
//</editor-fold>
        
//<editor-fold defaultstate="collapsed" desc="custom fields">
        IModel<List<CustomField>> customFieldsModel = new CompoundPropertyModel<List<CustomField>>(projectCustomFieldsList) {
            @Override
            public List<CustomField> getObject() {
                return projectCustomFieldsList;            
            }
        };
        CustomFieldListView<CustomField> customFieldsListView = new CustomFieldListView<>("customFieldsList", customFieldsModel, new Model<>(project));
        final WebMarkupContainer wmcCustomField = new WebMarkupContainer("wmcCustomField");
        wmcCustomField.add(customFieldsListView);
        wmcCustomField.setOutputMarkupId(true);
        add(wmcCustomField);
        
        customFieldTextField = new TextField<>("customField", new PropertyModel<String>(this, "stringCustomField"));
        customFieldTextField.setOutputMarkupId(true);
        projectCustomFieldForm = new Form<CustomField>("projectCustomFieldForm") {
            @Override
            protected void onSubmit() {
                CustomField customField = new CustomField();
                customField.setCfName(customFieldTextField.getInput());
                stringCustomField = null; //this will clear custom field text field
                projectCustomFieldsList.add(customField);
                project.setCustomFields(projectCustomFieldsList);
                projectService.update(project);
            }
        };
        projectCustomFieldForm.add(customFieldTextField);
        add(projectCustomFieldForm);
//</editor-fold>
        
//<editor-fold defaultstate="collapsed" desc="permissions">
//        final List<PermissionToDisplay> defaultPermissions = permissionService.getDefaultPermissions();
//        IModel<List<PermissionToDisplay>> permissionsModel = new CompoundPropertyModel<List<PermissionToDisplay>>(defaultPermissions) {
//            @Override
//            public List<PermissionToDisplay> getObject() {
//                return defaultPermissions;            
//            }
//        };
//        PermissionsListView<PermissionToDisplay> permissionsListView = new PermissionsListView<>("permissionsListView", permissionsModel, new Model<>(project));
//        wmcPermission = new WebMarkupContainer("wmcPermission");
//        wmcPermission.add(permissionsListView);
//        wmcPermission.setOutputMarkupId(true);
//        add(wmcPermission);
//</editor-fold>
        
        Logger.getLogger(ProjectDetail.class).warn("TODO: link to Project's issues.");
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

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }
}
