package com.issuetracker.pages.issue;

import com.issuetracker.model.*;
import com.issuetracker.pages.component.issue.CreateIssueCustomFIeldsListView;
import com.issuetracker.pages.layout.PageLayout;
import com.issuetracker.service.api.*;
import static com.issuetracker.web.Constants.roles;
import com.issuetracker.web.quilifiers.ViewPageConstraint;
import static com.issuetracker.web.security.KeycloakAuthSession.*;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.markup.html.form.*;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.file.File;
import org.apache.wicket.util.lang.Bytes;

import javax.inject.Inject;
import java.io.IOException;
import java.util.*;
import org.jboss.logging.Logger;


/**
 *
 * @author mgottval
 */
public class CreateIssue extends PageLayout {

    private final Logger log = Logger.getLogger(CreateIssue.class);
    
    @Inject
    private IssueService issueService;
    @Inject
    private StatusService statusService;
    @Inject
    private ProjectService projectService;
    @Inject
    private IssueTypeService issueTypeService;
    
    private final Form<Issue> insertIssueForm;
    private final DropDownChoice<IssueType> issueTypeList;
    private FileUploadField fileUploadField;
    private Issue issue;
//    private List<Issue> issueList;
    private Project selectedProject;
    private List<CustomField> projectCustomFieldList;
    private final Map<Project, List<Component>> modelsProjectMap = new HashMap<>();
    private final Map<Project, List<ProjectVersion>> modelsProjectVersionsMap = new HashMap<>();
    private CreateIssueCustomFIeldsListView cfListView;
    private List<CustomFieldIssueValue> cfIssueValues;

    @ViewPageConstraint(allowedAction = "kc.issue.create")
    public CreateIssue() {
//        issueList = new ArrayList<>();
        List<Project> projects = projectService.getProjectsWithRights(roles.getProperty("it.issue.create"));
        log.warn("Projects with create issue permission:");
        for (Project project : projects) {
            log.info(project);
        }
        for (Project p : projects) {
            modelsProjectMap.put(p, projectService.getProjectComponents(p));
            modelsProjectVersionsMap.put(p, projectService.getProjectVersions(p));
        }
        //model of projects list
        IModel<List<Project>> projectsModel = new AbstractReadOnlyModel<List<Project>>() {
            @Override
            public List<Project> getObject() {
                List<Project> list = new ArrayList<>(modelsProjectMap.keySet());
                return list;
            }
        };//model of components of selected project
        IModel<List<Component>> componentsModel = new AbstractReadOnlyModel<List<Component>>() {
            @Override
            public List<Component> getObject() {
                List<Component> models = modelsProjectMap.get(selectedProject);
                if (models == null) {
                    models = Collections.emptyList();
                }
                return models;
            }
        };
        IModel<List<ProjectVersion>> versionsModel = new AbstractReadOnlyModel<List<ProjectVersion>>() {
            @Override
            public List<ProjectVersion> getObject() {
                List<ProjectVersion> models = modelsProjectVersionsMap.get(selectedProject);
                if (models == null) {
                    models = Collections.emptyList();
                }
                Collections.sort(models);
                return models;
            }
        };

        IModel<List<Issue.Priority>> prioritiesModel = new AbstractReadOnlyModel<List<Issue.Priority>>() {
            @Override
            public List<Issue.Priority> getObject() {
                List<Issue.Priority> models = new ArrayList<>();
                models.add(Issue.Priority.LOW);
                models.add(Issue.Priority.HIGH);
                models.add(Issue.Priority.MEDIUM);
                models.add(Issue.Priority.MEDIUM_HIGH);
                models.add(Issue.Priority.MEDIUM_LOW);
                return models;
            }
        };

        IModel<List<CustomFieldIssueValue>> customFieldsModel = new PropertyModel<List<CustomFieldIssueValue>>(this, "cfIssueValues") {
            @Override
            public List<CustomFieldIssueValue> getObject() {
                if (selectedProject == null) {
                    projectCustomFieldList = new ArrayList<>();
                } else {
                    projectCustomFieldList = selectedProject.getCustomFields();
                }
                cfIssueValues = new ArrayList<>();
                for (CustomField customField : projectCustomFieldList) {
                    CustomFieldIssueValue cfIssueValue = new CustomFieldIssueValue();
                    cfIssueValue.setCustomField(customField);
                    cfIssueValue.setIssue(issue);
                    cfIssueValues.add(cfIssueValue);
                }
                return cfIssueValues;
            }
        };

        cfListView = new CreateIssueCustomFIeldsListView("cfListView", customFieldsModel);
        cfListView.setOutputMarkupId(true);
        insertIssueForm = new Form("form") {
            @Override
            protected void onSubmit() {
                FileUpload fileUpload = fileUploadField.getFileUpload();
                if (fileUpload != null) {
                    File file = null;
                    try {
                        file = new File("/home/mgottval/TEST-GATEINplug/" + fileUpload.getClientFileName());
                        fileUpload.writeTo(file);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    issue.setFileLocation(file.getAbsolutePath());
                }

//                if (issueService.getIssues().isEmpty()) {
                if (statusService.getStatusByName("New") == null) {
                    statusService.insert(new Status("New"));
                }
//                    if (statusService.getStatusByName("Modified") == null) {
//                        Status modifiedStatus = new Status("Modified");
//                        statusService.insert(modifiedStatus);
//                    }
//                    if (statusService.getStatusByName("Closed") == null) {
//                        Status closedStatus = new Status("Closed");
//                        statusService.insert(closedStatus);
//                    }
//                }
                issue.setStatus(statusService.getStatusByName("New"));
                issue.setProject(selectedProject);
                issue.setCustomFields(cfIssueValues);
                if (isSignedIn()) {
                    issue.setCreator(getIDToken().getPreferredUsername());
                } else {
                    issue.setCreator("");
                }
                issueService.create(issue);
//                issueList = issueService.getIssues();
                if (issue.getId() != null) { // if insert doesn't happen from any reason: 
                    PageParameters pageParameters = new PageParameters();
                    pageParameters.add("issue", issue.getId());
                    setResponsePage(IssueDetail.class, pageParameters);
                }
//                issue = new Issue();
            }
        };
        add(insertIssueForm);
        final DropDownChoice<Project> projectDropDown = new DropDownChoice<>(
                "projectDropDown",
                new PropertyModel<Project>(this, "selectedProject"),
                projectsModel,
                new ChoiceRenderer<Project>("name"));
        projectDropDown.setMarkupId("projectDD");
        projectDropDown.setNullValid(false);
//        if (projectDropDown.getChoices().get(0) != null) {
//            projectDropDown.setModelObject(projectDropDown.getChoices().get(0));
//        }
        projectDropDown.setRequired(true);
        final DropDownChoice<Component> componentDropDown = new DropDownChoice<>(
                "componentDropDown",
                new PropertyModel<Component>(this, "issue.component"),
                componentsModel,
                new ChoiceRenderer<Component>("name"));
        componentDropDown.setRequired(true);
        final ListMultipleChoice<ProjectVersion> versionDropDown = new ListMultipleChoice<>(
                "versionsDropDown", 
                new PropertyModel <List<ProjectVersion>>(this, "issue.affectedVersions"), 
                versionsModel, 
                new ChoiceRenderer<ProjectVersion>("name"));
        versionDropDown.setRequired(true);
        versionDropDown.setMaxRows(versionsModel.getObject().size());
        final DropDownChoice<Issue.Priority> priorityDropDown = new DropDownChoice<>("priorityDropDown", new PropertyModel<Issue.Priority>(this, "issue.priority"), prioritiesModel);

        componentDropDown.setOutputMarkupId(true);
        versionDropDown.setOutputMarkupId(true);
        insertIssueForm.add(projectDropDown);
        insertIssueForm.add(componentDropDown);
        insertIssueForm.add(versionDropDown);
        insertIssueForm.add(priorityDropDown);
        insertIssueForm.add(new RequiredTextField("issueName", new PropertyModel<String>(this, "issue.name")));
        insertIssueForm.add(new RequiredTextField("issueSummary", new PropertyModel<String>(this, "issue.summary")));
        insertIssueForm.add(new TextArea<>("description", new PropertyModel<String>(this, "issue.description")));

        issueTypeList = new DropDownChoice<>("issueTypes", new PropertyModel<IssueType>(this, "issue.issueType"), issueTypeService.getIssueTypes(), new ChoiceRenderer<IssueType>("name"));
        issueTypeList.setRequired(true);
        insertIssueForm.add(issueTypeList);
        fileUploadField = new FileUploadField("fileUploadField");
        insertIssueForm.add(fileUploadField);
        insertIssueForm.setMultiPart(true);
//set a limit for uploaded file's size
        insertIssueForm.setMaxSize(Bytes.kilobytes(100));
        insertIssueForm.add(cfListView);

        projectDropDown.add(
            new AjaxFormComponentUpdatingBehavior("onchange") {
                @Override
                protected void onUpdate(AjaxRequestTarget target) {
                    target.add(componentDropDown);
                    target.add(versionDropDown);
                    target.add(cfListView);
                }
            }
        );
    }
//<editor-fold defaultstate="collapsed" desc="getter/setter">

    public Issue getIssue() {
        return issue;
    }

    public void setIssue(Issue issue) {
        this.issue = issue;
    }

    public Project getSelectedProject() {
        return selectedProject;
    }

    public void setSelectedProject(Project selectedProject) {
        this.selectedProject = selectedProject;
    }

//    public List<Issue> getIssueList() {
//        return issueList;
//    }
//
//    public void setIssueList(List<Issue> issueList) {
//        this.issueList = issueList;
//    }

    public List<CustomField> getProjectCustomFieldList() {
        return projectCustomFieldList;
    }

    public void setProjectCustomFieldList(List<CustomField> projectCustomFieldList) {
        this.projectCustomFieldList = projectCustomFieldList;
    }

    public List<CustomFieldIssueValue> getCfIssueValues() {
        return cfIssueValues;
    }

    public void setCfIssueValues(List<CustomFieldIssueValue> cfIssueValues) {
        this.cfIssueValues = cfIssueValues;
    }
    //</editor-fold>
}
