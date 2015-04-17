package com.issuetracker.pages.issue;

import com.issuetracker.pages.layout.PageLayout;
import com.issuetracker.model.*;
import com.issuetracker.service.api.IssueService;
import com.issuetracker.service.api.IssueTypeService;
import com.issuetracker.service.api.ProjectService;
import com.issuetracker.service.api.StatusService;
import com.issuetracker.web.quilifiers.ViewPageConstraint;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.*;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import javax.inject.Inject;
import java.util.*;

/**
 *
 * @author Monika
 */
public class SearchIssues extends PageLayout {

    @Inject
    private IssueService issueService;
    @Inject
    private ProjectService projectService;
    @Inject
    private IssueTypeService issueTypeService;
    @Inject
    private StatusService statusService;
    private Form<List<Issue>> listIssuesForm;
    private final ListMultipleChoice<ProjectVersion> listMultipleVersions;
    private final DropDownChoice<Component> componentsDropDownChoice;
    private final ListMultipleChoice<IssueType> listMultipleIssueTypes;
    private final ListMultipleChoice<Status> listMultipleStatuses;
    private final DropDownChoice<Project> projectDropDownChoice;
    private final ListView issuesListview;
    private String containsText;
    private Project project;
    private List<Issue> issues = new ArrayList<>();
    private List<ProjectVersion> affectedVersions;
    private Component component;
    private IssueType issueType;
    private List<Status> statusList;
    private List<Component> components;
    private List<IssueType> issueTypes;
    private final Map<Project, List<Component>> modelsProjectComponentsMap = new HashMap<>();
    private final Map<Project, List<ProjectVersion>> modelsProjectVersionsMap = new HashMap<>();

    public SearchIssues() {
        List<Project> projects = projectService.getDisplayableProjects();
        for (Project p : projects) {
            modelsProjectComponentsMap.put(p, p.getComponents());
            modelsProjectVersionsMap.put(p, p.getVersions());
        }
        
        IModel<List<Project>> projectsModel = new AbstractReadOnlyModel<List<Project>>() {
            @Override
            public List<Project> getObject() {
                List<Project> list = new ArrayList<>(modelsProjectComponentsMap.keySet());
                return list;
            }
        };
        IModel<List<Component>> modelProjectComponentsChoices = new AbstractReadOnlyModel<List<Component>>() {
            @Override
            public List<Component> getObject() {
                List<Component> models = modelsProjectComponentsMap.get(project);
                if (models == null) {
                    models = Collections.emptyList();
                }
                return models;
            }
        };
        IModel<List<ProjectVersion>> modelProjectVersionsCoices = new AbstractReadOnlyModel<List<ProjectVersion>>() {
            @Override
            public List<ProjectVersion> getObject() {
                List<ProjectVersion> models = modelsProjectVersionsMap.get(project);
                if (models == null) {
                    models = Collections.emptyList();
                }
                return models;
            }
        };
        Form form = new Form("searchIssuesForm") {
            @Override
            protected void onSubmit() {
//                issues = issueService.getIssuesByAffectedVersions(affectedVersions);
                issues = issueService.getIssuesBySearch(project, affectedVersions, component, issueTypes, statusList, containsText);
            }
        };
        form.add(new TextField("containsText", new PropertyModel<String>(this, "containsText")));
        projectDropDownChoice = new DropDownChoice<>("projectDropDownChoice", new PropertyModel<Project>(this, "project"), projectsModel, new ChoiceRenderer<Project>("name"));
        projectDropDownChoice.setRequired(true);
        form.add(projectDropDownChoice);

        listMultipleVersions = new ListMultipleChoice<>(
                "versionDropDownChoice", 
                new PropertyModel<List<ProjectVersion>>(this, "affectedVersions"), 
                modelProjectVersionsCoices, 
                new ChoiceRenderer<ProjectVersion>("name"));
        
        listMultipleVersions.setOutputMarkupId(true);
//        listMultipleVersions.setRequired(true);
        form.add(listMultipleVersions);

        componentsDropDownChoice = new DropDownChoice<>("componentsDropDownChoice", new PropertyModel<Component>(this, "component"), modelProjectComponentsChoices, new ChoiceRenderer<Component>("name"));
        componentsDropDownChoice.setOutputMarkupId(true);
//        componentsDropDownChoice.setRequired(true);
        form.add(componentsDropDownChoice);

        listMultipleStatuses = new ListMultipleChoice<>(
                "statusLMC", 
                new PropertyModel<List<Status>>(this, "statusList"), 
                statusService.getStatuses(), 
                new ChoiceRenderer<Status>("name"));
        form.add(listMultipleStatuses);
        listMultipleIssueTypes = new ListMultipleChoice<>("issueTypes", new PropertyModel<List<IssueType>>(this, "issueTypes"), issueTypeService.getIssueTypes(), new ChoiceRenderer<IssueType>("name"));
        form.add(listMultipleIssueTypes);


        projectDropDownChoice.add(new AjaxFormComponentUpdatingBehavior("onchange") {
            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                target.add(componentsDropDownChoice);
                target.add(listMultipleVersions);
            }
        });

        add(form);


        issuesListview = new ListView<Issue>("issues", new PropertyModel<List<Issue>>(this, "issues")) {
            @Override
            protected void populateItem(final ListItem<Issue> item) {
                Issue issue = item.getModelObject();
                Link nameLink = new Link<Issue>("showIssue", item.getModel()) {
                    @Override
                    public void onClick() {
                        PageParameters pageParameters = new PageParameters();
                        pageParameters.add("issue", ((Issue) item.getModelObject()).getIssueId());
                        setResponsePage(IssueDetail.class, pageParameters);
                    }
                };
                nameLink.add(new Label("name", issue.getName()));
                item.add(nameLink);
                item.add(new Label("description", issue.getDescription()));
//                item.add(new Link<Issue>("remove", item.getModel()) {
//                    @Override
//                    public void onClick() {
//                        issueService.remove(item.getModelObject());
//                    }
//                });
            }
        };
        add(issuesListview);

    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public List<ProjectVersion> getAffectedVersions() {
        return affectedVersions;
    }

    public void setAffectedVersions(List<ProjectVersion> affectedVersions) {
        this.affectedVersions = affectedVersions;
    }

    public IssueType getIssueType() {
        return issueType;
    }

    public void setIssueType(IssueType issueType) {
        this.issueType = issueType;
    }

    public List<Component> getComponents() {
        return components;
    }

    public void setComponents(List<Component> components) {
        this.components = components;
    }

    public List<IssueType> getIssueTypes() {
        return issueTypes;
    }

    public void setIssueTypes(List<IssueType> issueTypes) {
        this.issueTypes = issueTypes;
    }

    public String getContainsText() {
        return containsText;
    }

    public void setContainsText(String containsText) {
        this.containsText = containsText;
    }

    public List<Issue> getIssues() {
        return issues;
    }

    public void setIssues(List<Issue> issues) {
        this.issues = issues;
    }

    public Component getComponent() {
        return component;
    }

    public void setComponent(Component component) {
        this.component = component;
    }

    public List<Status> getStatusList() {
        return statusList;
    }

    public void setStatusList(List<Status> statusList) {
        this.statusList = statusList;
    }

    
    
    
}