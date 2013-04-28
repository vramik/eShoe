/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.issuetracker.pages;

import com.issuetracker.dao.api.IssueDao;
import javax.inject.Inject;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import com.issuetracker.model.Issue;
import com.issuetracker.model.Project;
import com.issuetracker.model.User;
import java.util.ArrayList;
import java.util.List;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.model.PropertyModel;

/**
 *
 * @author mgottval
 */
public class ListIssues extends PageLayout {

    @Inject
    private IssueDao issueDao;
    private Form<Project> getIssuesByProjectForm;
    
    private String projectName;
    
    private List<Issue> issues;

    public ListIssues() {
        issues = new ArrayList<Issue>();
        getIssuesByProjectForm = new Form<Project>("getIssuesByProjectForm") {
            @Override
            protected void onSubmit() {
                issues = issueDao.getIssuesByProject(projectName);
            }
        };

        getIssuesByProjectForm.add(new RequiredTextField<String>("projectName", new PropertyModel<String>(this, "projectName")));
        add(getIssuesByProjectForm);

        
        List<String> issueNames = new ArrayList<String>();
        for (Issue issue :issues) {
            issueNames.add(issue.getName());
        }
        ListView listview = new ListView("listview", issueNames) {
            @Override
            protected void populateItem(ListItem item) {
                item.add(new Label("issue", item.getModel()));
            }
        };
        add(listview);
    }

    //<editor-fold defaultstate="collapsed" desc="getter/setter">
    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }
    //</editor-fold>
}
