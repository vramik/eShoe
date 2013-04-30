/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.issuetracker.pages;

import com.issuetracker.dao.api.IssueDao;
import com.issuetracker.dao.api.ProjectDao;
import com.issuetracker.model.Issue;
import com.issuetracker.model.Project;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;

/**
 *
 * @author mgottval
 */
public class CreateIssue extends PageLayout {

    @Inject
    private IssueDao issueDao;
    @Inject
    private ProjectDao projectDao;
    private Form<Issue> insertIssueForm;
    private DropDownChoice<Project> projectList;
    private Issue issue;

    public CreateIssue() {
        issue = new Issue();
        insertIssueForm = new Form<Issue>("insertIssueForm") {
            @Override
            protected void onSubmit() {
                issueDao.addIssue(issue);
                setResponsePage(ListIssues.class);
            }            
        };
        
        insertIssueForm.add(new RequiredTextField<String>("name", new PropertyModel<String>(this, "issue.name")));
        insertIssueForm.add(new RequiredTextField<String>("description", new PropertyModel<String>(this, "issue.description")));
        

    //    Model<Project> listProjectModel = new Model<Project>();
        ChoiceRenderer<Project> projectRender = new ChoiceRenderer<Project>("name");

        projectList = new DropDownChoice<Project>("projects", new PropertyModel<Project>(this, "issue.project"), projectDao.getProjects(),
                projectRender) {
            @Override
            protected boolean wantOnSelectionChangedNotifications() {
                return true;
            }
        };
        insertIssueForm.add(projectList);
        
        add(insertIssueForm);


    }

    //<editor-fold defaultstate="collapsed" desc="getter/setter">
    public Issue getIssue() {
        return issue;
    }

    public void setIssue(Issue issue) {
        this.issue = issue;
    }


    //</editor-fold>
}
