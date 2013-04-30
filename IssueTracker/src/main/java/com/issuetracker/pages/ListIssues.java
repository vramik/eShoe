/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.issuetracker.pages;

import com.issuetracker.dao.api.IssueDao;
import com.issuetracker.dao.api.ProjectDao;
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
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;

/**
 *
 * @author mgottval
 */
public class ListIssues extends PageLayout {

    @Inject
    private IssueDao issueDao;
    @Inject
    private ProjectDao projectDao;
    private Form<Issue> getIssuesByProjectForm;
    private DropDownChoice<Project> projectList;
    // private String projectName;
    private Project project;
    private List<Issue> issues;
    
    //Issues list
     private Form form;
    private DropDownChoice<Issue> issuesList;

    public ListIssues() {
    

//    private class InputForm extends Form {
//        // holds NameWrapper elements
//
//        public InputForm(String name) {
//            super(name);
//            ChoiceRenderer<Project> projectRender = new ChoiceRenderer<Project>("name");
//
//            projectList = new DropDownChoice<Project>("projects", new PropertyModel<Project>(this, "project"), projectDao.getProjects(),
//                    projectRender) {
//                @Override
//                protected boolean wantOnSelectionChangedNotifications() {
//                    return true;
//                }
//            };
//
//            add(projectList);
//
//
//            // add a nested list view; as the list is nested in the form, the form will
//            // update all FormComponent childs automatically.
//            ListView listView = new ListView<Issue>("issues", issues) {
//                @Override
//                protected void populateItem(final ListItem<Issue> item) {
//                    Issue issue = item.getModelObject();
//                    item.add(new Label("name", issue.getName()));
//                    item.add(new Label("description", issue.getDescription()));
//                    item.add(new Label("projectName", issue.getProject().getName()));
//                    item.add(new Link<Issue>("delete", item.getModel()) {
//                        @Override
//                        public void onClick() {
//                            issueDao.removeIssue(item.getModelObject());
//                            setResponsePage(ListIssues.class);
//                        }
//                    });
//
//                }
//            };
//            listView.setReuseItems(true);
//            add(listView);
//        }
//
//        public void onSubmit() {
//            issues = issueDao.getIssuesByProject(project);
//            info("data: " + data); // print current contents
//        }
//    }
        
        issues = new ArrayList<Issue>();
        issues = issueDao.getIssuesByProjectName("JBoss");
        
        getIssuesByProjectForm = new Form<Issue>("getIssuesByProjectForm") {       
            @Override
            protected void onSubmit() {
                issues = new ArrayList<Issue>();
                issues = issueDao.getIssuesByProjectName(project.getName());
                if (issues.isEmpty()) {
                    Logger.getLogger(ListIssues.class.getName()).log(Level.SEVERE, "empty");
                } else{
                    String s = null;
                    for (Issue issue : issues) {
                        s = issue.getName();
                    }
                    Logger.getLogger(ListIssues.class.getName()).log(Level.SEVERE, s);
                    }
              Logger.getLogger(ListIssues.class.getName()).log(Level.SEVERE, project.getName());
                //issues = issueDao.getIssuesByProject(projectName);
                issues = issueDao.getIssuesByProjectName(project.getName());
                String s = "";
                for (Issue issue : issues) {
                    s = s+" "+issue.getName();
                }
                 Logger.getLogger(ListIssues.class.getName()).log(Level.SEVERE, s+" string s");
            }   
        };
        
        
         ListView listview = new ListView<Issue>("issues", issues) {
            @Override
            protected void populateItem(final ListItem<Issue> item) {
                Issue issue = item.getModelObject();
               item.add(new Label("name", issue.getName()));
                item.add(new Label("description", issue.getDescription()));
                item.add(new Label("projectName", issue.getProject().getName()));
                item.add(new Link<Issue>("delete", item.getModel()) {
                     @Override
                    public void onClick() {
                        issueDao.removeIssue(item.getModelObject());
                    }
                });
                }
        };
         listview.setReuseItems(true);
         getIssuesByProjectForm.add(listview);
         ChoiceRenderer<Project> projectRender = new ChoiceRenderer<Project>("name");

        projectList = new DropDownChoice<Project>("projects", new PropertyModel<Project>(this, "project"), projectDao.getProjects(),
                projectRender) {
            @Override
            protected boolean wantOnSelectionChangedNotifications() {
                return true;
            }
        };
      getIssuesByProjectForm.add(projectList);
        add(getIssuesByProjectForm);
       // add(listview);

       
        
        
        
        //TEST just view of added issues
        
         Model<Issue> listModel = new Model<Issue>();
        ChoiceRenderer<Issue> issueRender = new ChoiceRenderer<Issue>("name");
        issuesList = new DropDownChoice<Issue>("issuesDrop", listModel, issueDao.getIssues(),
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
    //<editor-fold defaultstate="collapsed" desc="getter/setter">
            //    public String getProjectName() {
            //        return projectName;
            //    }
            //
            //    public void setProjectName(String projectName) {
            //        this.projectName = projectName;
            //    }


    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }
    //</editor-fold>
}
