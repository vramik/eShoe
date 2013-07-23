/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.issuetracker.pages;

import com.issuetracker.dao.api.IssueDao;
import com.issuetracker.dao.api.IssueTypeDao;
import com.issuetracker.dao.api.ProjectDao;
import com.issuetracker.model.Component;
import com.issuetracker.model.Issue;
import com.issuetracker.model.IssueType;
import com.issuetracker.model.Project;
import com.issuetracker.model.ProjectVersion;
import com.issuetracker.model.Status;
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
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

/**
 *
 * @author Monika
 */
public class SearchIssues extends PageLayout {

    @Inject
    private IssueDao issueDao;
    @Inject
    private ProjectDao projectDao;
    @Inject
    private IssueTypeDao issueTypeDao;
    
    private Form<List<Issue>> listIssuesForm;
    private final DropDownChoice<ProjectVersion> versionDropDownChoice;
    private DropDownChoice<Status> statusList;
    private final ListMultipleChoice<Component> componentsListMultiple;
    private ListMultipleChoice<IssueType> listMultipleIssueTypes;
    private DropDownChoice<Project> projectDropDownChoice;
    private ListView issuesListview;
    
    private String containsText;
    private Project project;
    private List<Issue> issues;
    private ProjectVersion version;
    private IssueType issueType;
    private List<Component> components;
    private List<IssueType> issueTypes;
    private final Map<Project, List<Component>> modelsProjectComponentsMap = new HashMap<Project, List<Component>>();
    private final Map<Project, List<ProjectVersion>> modelsProjectVersionsMap = new HashMap<Project, List<ProjectVersion>>();

    public SearchIssues() {
        issues = new ArrayList<Issue>();

        List<Project> projects = projectDao.getProjects();
        for (Project p : projects) {
            modelsProjectComponentsMap.put(p, p.getComponents());
        }
        for (Project p : projects) {
            modelsProjectVersionsMap.put(p, p.getVersions());
        }
        IModel<List<Project>> projectsModel = new AbstractReadOnlyModel<List<Project>>() {
            @Override
            public List<Project> getObject() {
                List<Project> list = new ArrayList<Project>(modelsProjectComponentsMap.keySet());
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
                issues = issueDao.getIssuesBySearch(project, version, components, issueTypes, null, containsText); //TODO
            }
        };
        form.add(new TextField("containsText", new PropertyModel<String>(this, "containsText")));
        projectDropDownChoice  = new DropDownChoice<Project>("projectDropDownChoice", new PropertyModel<Project>(this, "project"), projectsModel, new ChoiceRenderer<Project>("name"));
        form.add(projectDropDownChoice);
        versionDropDownChoice = new DropDownChoice<ProjectVersion>("versionDropDownChoice", new PropertyModel<ProjectVersion>(this, "version"), modelProjectVersionsCoices, new ChoiceRenderer<ProjectVersion>("name"));
         versionDropDownChoice.setOutputMarkupId(true);
        form.add(versionDropDownChoice);       
        componentsListMultiple = new ListMultipleChoice<Component>("componentsListMultiple", new PropertyModel<List<Component>>(this, "components"), modelProjectComponentsChoices, new ChoiceRenderer<Component>("name"));
        componentsListMultiple.setOutputMarkupId(true);
        form.add(componentsListMultiple);
        listMultipleIssueTypes = new ListMultipleChoice<IssueType>("issueTypes", new PropertyModel<List<IssueType>>(this, "issueTypes"), issueTypeDao.getIssueTypes(), new ChoiceRenderer<IssueType>("name"));
        form.add(listMultipleIssueTypes);


        projectDropDownChoice.add(new AjaxFormComponentUpdatingBehavior("onchange") {
            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                target.add(componentsListMultiple);
                target.add(versionDropDownChoice);
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
                        setResponsePage(ShowIssue.class, pageParameters);
                    }
                };
                nameLink.add(new Label("name", issue.getName()));
                item.add(nameLink);                
                item.add(new Label("description", issue.getDescription()));
//                item.add(new Link<Issue>("delete", item.getModel()) {
//                    @Override
//                    public void onClick() {
//                        issueDao.removeIssue(item.getModelObject());
//                    }
//                });
            }
        };
        add(issuesListview);
        
        
        
        
        //TEST just view of added issues

        Model<Issue> listModel = new Model<Issue>();
        ChoiceRenderer<Issue> issueRender = new ChoiceRenderer<Issue>("name");
        DropDownChoice<Issue> issuesList = new DropDownChoice<Issue>("issuesDrop", listModel, issueDao.getIssues(),
                issueRender) {
            @Override
            protected boolean wantOnSelectionChangedNotifications() {
                return true;
            }
        };
        add(issuesList);
        form = new Form("form", new CompoundPropertyModel<Issue>(listModel));
        form.add(new TextField("name"));
        form.add(new TextField("description"));
        form.add(new TextField("project.name"));
        add(form);


    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public ProjectVersion getVersion() {
        return version;
    }

    public void setVersion(ProjectVersion version) {
        this.version = version;
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
}