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
    private Issue issue;
    private String projectName;
    
    private Project project;

    public CreateIssue() {
        issue = new Issue();
        setDefaultModel(new CompoundPropertyModel(issue));
        insertIssueForm = new Form<Issue>("insertIssueForm") {
            @Override
            protected void onSubmit() {
                project = new Project();
                project.setName(projectName);
                issue.setProject(project);               
                issueDao.addIssue(issue);
            }
        };

        insertIssueForm.add(new RequiredTextField<String>("name"));
        insertIssueForm.add(new RequiredTextField<String>("description"));

        List<Project> projects = projectDao.getProjects();
        List<String> projectNames = new ArrayList<String>();
        for (Project p:projects) {
            projectNames.add(p.getName());
        }
        insertIssueForm.add(new DropDownChoice<String>("projectName", new PropertyModel(this, "projectName"), projectNames));


        add(insertIssueForm);
    }

    //<editor-fold defaultstate="collapsed" desc="getter/setter">
    public Issue getIssue() {
        return issue;
    }

    public void setIssue(Issue issue) {
        this.issue = issue;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }
    //</editor-fold>
}
