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
import java.util.List;
import javax.inject.Inject;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.ListMultipleChoice;
import org.apache.wicket.model.PropertyModel;

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
    private DropDownChoice<Project> projectDropDown;
    private DropDownChoice<ProjectVersion> projectVersionDropDown;
    private DropDownChoice<Status> statusList;
    private ListMultipleChoice<Component> listMultipleComponents;
    private ListMultipleChoice<IssueType> listMultipleIssueTypes;
    private String containsText;
    
    private Project project;
    private List<Issue> issues;
    private ProjectVersion version;
    private IssueType issueType;

    public SearchIssues() {
        issues = new ArrayList<Issue>();

        listIssuesForm = new Form<List<Issue>>("getIssuesBySearch") {
            @Override
            protected void onSubmit() {
                issues = issueDao.getIssuesBySearch(project, version, null, null, null);
            }
        };
    
        ChoiceRenderer<Project> projectRender = new ChoiceRenderer<Project>("name");
        projectDropDown = new DropDownChoice<Project>("projects", new PropertyModel<Project>(this, "project"), projectDao.getProjects(),
                projectRender) {
            @Override
            protected boolean wantOnSelectionChangedNotifications() {
                return true;
            }
        };
        listIssuesForm.add(projectDropDown);
        
        ChoiceRenderer<ProjectVersion> projectVersionRender = new ChoiceRenderer<ProjectVersion>("name");
        projectVersionDropDown = new DropDownChoice<ProjectVersion>("projectVersions", new PropertyModel<ProjectVersion>(this, "version"), projectDao.getProjectVersions(project),
                projectVersionRender) {
            @Override
            protected boolean wantOnSelectionChangedNotifications() {
                return true;
            }
        };
        listIssuesForm.add(projectVersionDropDown);
        
        
        ChoiceRenderer<Component> componentRender = new ChoiceRenderer<Component>("name");       
        final PropertyModel<List<Component>> propertyModel = new PropertyModel(this, "components");
        listMultipleComponents = new ListMultipleChoice<Component>("components", propertyModel, projectDao.getProjectComponents(project), componentRender); 
        listIssuesForm.add(listMultipleComponents);
      
        ChoiceRenderer<IssueType> issueTypeRender = new ChoiceRenderer<IssueType>("name");

        listMultipleIssueTypes = new ListMultipleChoice<IssueType>("issueTypes", new PropertyModel<List<IssueType>>(this, "issueTypes"), issueTypeDao.getIssueTypes(),
                issueTypeRender);
        listIssuesForm.add(listMultipleIssueTypes);
        
        add(listIssuesForm);
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
    
    
}