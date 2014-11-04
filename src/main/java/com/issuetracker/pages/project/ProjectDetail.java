package com.issuetracker.pages.project;

import com.issuetracker.pages.layout.PageLayout;
import com.issuetracker.model.Component;
import com.issuetracker.model.CustomField;
import com.issuetracker.model.Permission;
import com.issuetracker.model.PermissionType;
import com.issuetracker.model.Project;
import com.issuetracker.model.ProjectVersion;
import com.issuetracker.pages.component.component.ComponentListView;
import com.issuetracker.pages.component.customField.CustomFieldListView;
import com.issuetracker.pages.component.permission.PermissionsForm;
import com.issuetracker.pages.component.version.VersionListView;
import com.issuetracker.pages.component.permission.PermissionsListView;
import com.issuetracker.service.api.ProjectService;
import static com.issuetracker.web.Constants.*;
import static com.issuetracker.web.security.KeycloakAuthSession.*;
import static com.issuetracker.web.security.PermissionsUtil.*;
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
import java.util.ArrayList;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.request.flow.RedirectToUrlException;
import org.apache.wicket.util.string.StringValue;

/**
 *
 * @author mgottval
 */
public class ProjectDetail extends PageLayout {

    @Inject
    private ProjectService projectService;
    
    private final Form<ProjectVersion> projectVersionForm;
    private final Form<Component> projectComponentForm;
    private final Form<CustomField> projectCustomFieldForm;
    private Link deleteProject;
    private List<ProjectVersion> projectVersionList;    
    private List<Component> projectComponentList;    
    private List<CustomField> projectCustomFieldsList;    
    private List<Permission> projectPermissionsList;    
    private TextField versionTextField;
    private TextField componentTextField;
    private TextField customFieldTextField;
    private Project project = new Project();
    private String stringVersion, stringComponent, stringCustomField;
    private WebMarkupContainer wmcPermission;
    private PermissionsForm<Permission> permissionForm;
    
    @Override
    public void onConfigure() {
        super.onConfigure();
        boolean hasEditPermissions = hasPermissionsProject(getRequest(), project, PermissionType.edit);
        
        projectVersionForm.setVisible(hasEditPermissions);
        projectComponentForm.setVisible(hasEditPermissions);
        projectCustomFieldForm.setVisible(hasEditPermissions);
        wmcPermission.setVisible(hasEditPermissions);
        permissionForm.setVisible(hasEditPermissions);
        
        boolean hasDeletePermission = 
                isProjectOwner(getRequest(), project) ||
                isUserInAppRole(getRequest(), "project.delete") ||
                isSuperUser(getRequest());
        deleteProject.setVisible(hasDeletePermission);
    }
    
    public ProjectDetail(PageParameters parameters) {
        StringValue projectId = parameters.get("project");
        if (projectId.equals(StringValue.valueOf((String)null))) {
            throw new RedirectToUrlException(HOME_PAGE);
        }
        
        project = projectService.getProjectById(projectId.toLong());
        
        projectVersionList = project.getVersions();
        projectComponentList = project.getComponents();
        projectCustomFieldsList = project.getCustomFields();
        projectPermissionsList = new ArrayList<>(project.getPermissions());
        
        add(new Label("name", project.getName()));
        add(new Label("summary", project.getSummary()));
        deleteProject = new Link("delete") {

            @Override
            public void onClick() {
                projectService.remove(project);
                setResponsePage(ListProjects.class);
            }
        };
        add(deleteProject);
        
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
        IModel<List<Permission>> permissionsModel = new CompoundPropertyModel<List<Permission>>(projectPermissionsList) {
            @Override
            public List<Permission> getObject() {
                return projectPermissionsList;            
            }
        };
        PermissionsListView<Permission> permissionsListView = new PermissionsListView<>("permissionsListView", permissionsModel, new Model<>(project));
        wmcPermission = new WebMarkupContainer("wmcPermission");
        wmcPermission.add(permissionsListView);
        wmcPermission.setOutputMarkupId(true);
        add(wmcPermission);
        
        permissionForm = new PermissionsForm("permissionForm", permissionsModel, new Model<>(project));
        add(permissionForm);
//</editor-fold>
        
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
}
