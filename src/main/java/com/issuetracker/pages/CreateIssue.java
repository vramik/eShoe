package com.issuetracker.pages;

import com.issuetracker.dao.api.ComponentDao;
import com.issuetracker.dao.api.IssueDao;
import com.issuetracker.dao.api.IssueTypeDao;
import com.issuetracker.dao.api.ProjectDao;
import com.issuetracker.dao.api.StatusDao;
import com.issuetracker.model.Component;
import com.issuetracker.model.Issue;
import com.issuetracker.model.Status;
import com.issuetracker.model.IssueType;
import com.issuetracker.model.Project;
import com.issuetracker.model.ProjectVersion;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Inject;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.ListMultipleChoice;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.file.File;
import org.apache.wicket.util.lang.Bytes;

/**
 *
 * @author mgottval
 */
public class CreateIssue extends PageLayout {

    @Inject
    private IssueDao issueDao;
    @Inject
    private StatusDao statusDao;
    @Inject
    private ProjectDao projectDao;
    @Inject
    private IssueTypeDao issueTypeDao;
    @Inject
    private ComponentDao componentDao;
    private Form<Issue> insertIssueForm;
    private DropDownChoice<Project> projectList;
    private DropDownChoice<IssueType> issueTypeList;
    private ListMultipleChoice<Component> listComponents;
    private FileUploadField fileUploadField;
//    private DropDownChoice<Priority> priorityList;
    private Issue issue;
    private List<Issue> issueList;
    private Project selectedProject;
    private final Map<Project, List<Component>> modelsProjectMap = new HashMap<Project, List<Component>>();
    private final Map<Project, List<ProjectVersion>> modelsProjectVersionsMap = new HashMap<Project, List<ProjectVersion>>();

    public CreateIssue() {
        issueList = new ArrayList<Issue>();
        List<Project> projects = projectDao.getProjects();
        for (Project p : projects) {
            modelsProjectMap.put(p, p.getComponents());
        }
        for (Project p : projects) {
            modelsProjectVersionsMap.put(p, p.getVersions());
        }
        //model of projects list
        IModel<List<Project>> makeChoices2 = new AbstractReadOnlyModel<List<Project>>() {
            @Override
            public List<Project> getObject() {
                List<Project> list = new ArrayList<Project>(modelsProjectMap.keySet());
                return list;
            }
        };//model of components of selected project
        IModel<List<Component>> modelChoices2 = new AbstractReadOnlyModel<List<Component>>() {
            @Override
            public List<Component> getObject() {
                List<Component> models = modelsProjectMap.get(selectedProject);
                if (models == null) {
                    models = Collections.emptyList();
                }
                return models;
            }
        };
        IModel<List<ProjectVersion>> modelChoicesVersions = new AbstractReadOnlyModel<List<ProjectVersion>>() {
            @Override
            public List<ProjectVersion> getObject() {
                List<ProjectVersion> models = modelsProjectVersionsMap.get(selectedProject);
                if (models == null) {
                    models = Collections.emptyList();
                }
                return models;
            }
        };
        
        IModel<List<Issue.Priority>> priorityModelChoices = new AbstractReadOnlyModel<List<Issue.Priority>>() {

            @Override
            public List<Issue.Priority> getObject() {
                List<Issue.Priority> models = new ArrayList<Issue.Priority>();
                models.add(Issue.Priority.LOW);
                models.add(Issue.Priority.HIGH);
                models.add(Issue.Priority.MEDIUM);
                models.add(Issue.Priority.MEDIUM_HIGH);
                models.add(Issue.Priority.MEDIUM_LOW);
                return models;
            }
        };
        insertIssueForm = new Form("form") {
            @Override
            protected void onSubmit() {
                FileUpload fileUpload = fileUploadField.getFileUpload();
                if (fileUpload != null) {
                    File file = null;
                    try {
                        file = new File("/home/mgottval/TEST-GATEINplug/" + fileUpload.getClientFileName());
                        fileUpload.writeTo(file);
                        Logger.getLogger(CreateIssue.class.getName()).log(Level.SEVERE, fileUpload.getClientFileName());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    issue.setFileLocation(file.getAbsolutePath());
                }
                issue.setStatus(statusDao.getStatusByName("New"));//TODO ??
                issue.setProject(selectedProject);
                issueDao.addIssue(issue);
                issueList = issueDao.getIssues();
                PageParameters pageParameters = new PageParameters();
                Logger.getLogger(CreateIssue.class.getName()).log(Level.SEVERE, issue.getIssueId().toString());
                pageParameters.add("issue", issue.getIssueId());
                setResponsePage(IssueDetail.class, pageParameters);
                issue = new Issue();

            }
        };
        add(insertIssueForm);
        final DropDownChoice<Project> projectDropDown = new DropDownChoice<Project>("makes", new PropertyModel<Project>(this, "selectedProject"), makeChoices2, new ChoiceRenderer<Project>("name"));
        projectDropDown.setMarkupId("projectDD");
        final DropDownChoice<Component> componentDropDown = new DropDownChoice<Component>("models", new PropertyModel<Component>(this, "issue.component"), modelChoices2, new ChoiceRenderer<Component>("name"));
        final DropDownChoice<ProjectVersion> versionDropDown = new DropDownChoice<ProjectVersion>("versionModels", new PropertyModel<ProjectVersion>(this, "issue.projectVersion"), modelChoicesVersions, new ChoiceRenderer<ProjectVersion>("name"));
        final DropDownChoice<Issue.Priority> priorityDropDown = new DropDownChoice<Issue.Priority>("priorityDropDown", new PropertyModel<Issue.Priority>(this, "issue.priority"), priorityModelChoices);

        componentDropDown.setOutputMarkupId(true);
        versionDropDown.setOutputMarkupId(true);
        insertIssueForm.add(projectDropDown);
        insertIssueForm.add(componentDropDown);
        insertIssueForm.add(versionDropDown);
        insertIssueForm.add(priorityDropDown);
        insertIssueForm.add(new RequiredTextField("issueName", new PropertyModel<String>(this, "issue.name")));
        insertIssueForm.add(new RequiredTextField("issueDescription", new PropertyModel<String>(this, "issue.description")));
        issueTypeList = new DropDownChoice<IssueType>("issueTypes", new PropertyModel<IssueType>(this, "issue.issueType"), issueTypeDao.getIssueTypes(), new ChoiceRenderer<IssueType>("name"));
        insertIssueForm.add(issueTypeList);
        fileUploadField = new FileUploadField("fileUploadField");
        insertIssueForm.add(fileUploadField);
        insertIssueForm.setMultiPart(true);
//set a limit for uploaded file's size
        insertIssueForm.setMaxSize(Bytes.kilobytes(100));
        add(new FeedbackPanel("feedbackPanel"));

        projectDropDown.add(
                new AjaxFormComponentUpdatingBehavior("onchange") {
            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                target.add(componentDropDown);
                target.add(versionDropDown);
            }
        });



        ListView listViewIssues = new ListView<Issue>("issueList", new PropertyModel<List<Issue>>(this, "issueList")) {
            @Override
            protected void populateItem(final ListItem<Issue> item) {
                final Issue issue = item.getModelObject();
//                item.add(new Link<I>("edit", item.getModel()) {
//                    @Override
//                    public void onClick() {
//                        PageParameters pageParameters = new PageParameters();
//                        pageParameters.add("projectName", project.getName());
//                        setResponsePage(EditProject.class, pageParameters);
//                    }
//                });
                item.add(new Label("name", issue.getName()));
                item.add(new Label("issueType", issue.getIssueType().getname()));
                item.add(new Label("description", issue.getDescription()));
                item.add(new Label("component", issue.getComponent().getName()));
                item.add(new Label("version", issue.getProjectVersion().getName()));
                item.add(new Label("project", issue.getProject().getName()));
                item.add(new Label("file", issue.getFileLocation()));
            }
        };

        add(listViewIssues);

        add(new FeedbackPanel("feedback"));

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

    public List<Issue> getIssueList() {
        return issueList;
    }

    public void setIssueList(List<Issue> issueList) {
        this.issueList = issueList;
    }
    //</editor-fold>
}
