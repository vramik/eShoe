/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.issuetracker.pages;

import com.issuetracker.TEST.Status;
import com.issuetracker.dao.api.ComponentDao;
import com.issuetracker.dao.api.IssueDao;
import com.issuetracker.dao.api.IssueTypeDao;
import com.issuetracker.dao.api.ProjectDao;
import com.issuetracker.model.Component;
import com.issuetracker.model.Issue;
import com.issuetracker.model.IssueType;
import com.issuetracker.model.Project;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Inject;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.ListMultipleChoice;
import org.apache.wicket.markup.html.form.RequiredTextField;
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
    @Inject
    private IssueTypeDao issueTypeDao;
    @Inject
    private ComponentDao componentDao;
    
    private Form<Issue> insertIssueForm;
    private DropDownChoice<Project> projectList;
    private DropDownChoice<IssueType> issueTypeList;
    private ListMultipleChoice<Component> listComponents;
//    private DropDownChoice<Priority> priorityList;
    private Issue issue;

    public CreateIssue() {
        issue = new Issue();
        insertIssueForm = new Form<Issue>("insertIssueForm") {
            @Override
            protected void onSubmit() {
                issue.setStatus(Status.NEW);//TODO ??
                List<Component> comps = issue.getComponents();
                String s = "";
                for (Component component : comps) {
                    s = s+component.getName();
                }
                 Logger.getLogger(ListIssues.class.getName()).log(Level.SEVERE, s);
                issueDao.addIssue(issue);
                setResponsePage(ListIssues.class);
            }            
        };
        
        insertIssueForm.add(new RequiredTextField<String>("name", new PropertyModel<String>(this, "issue.name")));
        insertIssueForm.add(new RequiredTextField<String>("description", new PropertyModel<String>(this, "issue.description")));
        

        ChoiceRenderer<Project> projectRender = new ChoiceRenderer<Project>("name");

        projectList = new DropDownChoice<Project>("projects", new PropertyModel<Project>(this, "issue.project"), projectDao.getProjects(),
                projectRender) {
            @Override
            protected boolean wantOnSelectionChangedNotifications() {
                return true;
            }
        };
        insertIssueForm.add(projectList);
        
        ChoiceRenderer<IssueType> issueTypeRender = new ChoiceRenderer<IssueType>("name");

        issueTypeList = new DropDownChoice<IssueType>("issueTypes", new PropertyModel<IssueType>(this, "issue.issueType"), issueTypeDao.getIssueTypes(),
                issueTypeRender) {
            @Override
            protected boolean wantOnSelectionChangedNotifications() {
                return true;
            }
        };
        insertIssueForm.add(issueTypeList);
        
//        priorityList = new DropDownChoice<Priority>("priorities", new PropertyModel<Priority>(this, "issue.priority"), issueDao.getPriorities()) {
//            @Override
//            protected boolean wantOnSelectionChangedNotifications() {
//                return true;
//            }
//        };
//        insertIssueForm.add(priorityList);
        

       ChoiceRenderer<Component> componentRender = new ChoiceRenderer<Component>("name");
       
        listComponents = new ListMultipleChoice<Component>("components", new PropertyModel<List<Component>>(this, "issue.components"), componentDao.getComponents(), componentRender);
//        ListMultipleChoice<Component> list = new ListMultipleChoice<Component>
        
        insertIssueForm.add(listComponents);
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
