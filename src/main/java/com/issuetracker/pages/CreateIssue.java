package com.issuetracker.pages;

import com.issuetracker.dao.api.ComponentDao;
import com.issuetracker.dao.api.IssueDao;
import com.issuetracker.dao.api.IssueTypeDao;
import com.issuetracker.dao.api.ProjectDao;
import com.issuetracker.model.Component;
import com.issuetracker.model.Issue;
import com.issuetracker.model.Issue.Status;
import com.issuetracker.model.IssueType;
import com.issuetracker.model.Project;
import com.issuetracker.model.ProjectVersion;
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
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
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
    private List<Issue> issueList;
    //TEST
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
        insertIssueForm = new Form("form") {
            @Override
            protected void onSubmit() {
                issue.setStatus(Status.NEW);//TODO ??
                issue.setProject(selectedProject);
                issueDao.addIssue(issue);
                issueList = issueDao.getIssues();
//                Logger.getLogger(EditProject.class.getName()).log(Level.SEVERE, issue.getComponent().getName() + " " + issue.getProjectVersion().getName() + " " + issue.getName() + " " + issue.getIssueType().getname());
                issue = new Issue();
            }
        };
        add(insertIssueForm);
        final DropDownChoice<Project> projectDropDown = new DropDownChoice<Project>("makes", new PropertyModel<Project>(this, "selectedProject"), makeChoices2, new ChoiceRenderer<Project>("name"));
        final DropDownChoice<Component> componentDropDown = new DropDownChoice<Component>("models", new PropertyModel<Component>(this, "issue.component"), modelChoices2, new ChoiceRenderer<Component>("name"));
        final DropDownChoice<ProjectVersion> versionDropDown = new DropDownChoice<ProjectVersion>("versionModels", new PropertyModel<ProjectVersion>(this, "issue.projectVersion"), modelChoicesVersions, new ChoiceRenderer<ProjectVersion>("name"));
        componentDropDown.setOutputMarkupId(true);
        versionDropDown.setOutputMarkupId(true);
        insertIssueForm.add(projectDropDown);
        insertIssueForm.add(componentDropDown);
        insertIssueForm.add(versionDropDown);
        insertIssueForm.add(new RequiredTextField("issueName", new PropertyModel<String>(this, "issue.name")));
        insertIssueForm.add(new RequiredTextField("issueDescription", new PropertyModel<String>(this, "issue.description")));
        issueTypeList = new DropDownChoice<IssueType>("issueTypes", new PropertyModel<IssueType>(this, "issue.issueType"), issueTypeDao.getIssueTypes(),new ChoiceRenderer<IssueType>("name"));
        insertIssueForm.add(issueTypeList);

        projectDropDown.add(new AjaxFormComponentUpdatingBehavior("onchange") {
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
                item.add(new Label("issueType", issue.getIssueType()));
                item.add(new Label("description", issue.getDescription()));
                item.add(new Label("component", issue.getComponent().getName()));
                item.add(new Label("version", issue.getProjectVersion().getName()));
                item.add(new Label("project", issue.getProject().getName()));         
            }
        };
        add(listViewIssues);
        

        //TEST project
        // issue.setProject(projectDao.getProjectByName("JBoss"));

//        insertIssueForm = new Form<Issue>("insertIssueForm") {
//            @Override
//            protected void onSubmit() {
//                issue.setStatus(Status.NEW);//TODO ??
//
//
//                issueDao.addIssue(issue);
////                setResponsePage(ListIssues.class);
//            }
//        };
//
//        insertIssueForm.add(new RequiredTextField<String>("name", new PropertyModel<String>(this, "issue.name")));
//
//        insertIssueForm.add(new RequiredTextField<String>("description", new PropertyModel<String>(this, "issue.description")));
//
//        ChoiceRenderer<Project> projectRender = new ChoiceRenderer<Project>("name");
//        projectList = new DropDownChoice<Project>("projects", new PropertyModel<Project>(this, "issue.project"), projectDao.getProjects(),
//                projectRender) {
//            @Override
//            protected boolean wantOnSelectionChangedNotifications() {
//                return true;
//            }
//        };
//        insertIssueForm.add(projectList);
//
//        ChoiceRenderer<IssueType> issueTypeRender = new ChoiceRenderer<IssueType>("name");
//        issueTypeList = new DropDownChoice<IssueType>("issueTypes", new PropertyModel<IssueType>(this, "issue.issueType"), issueTypeDao.getIssueTypes(),
//                issueTypeRender) {
//            @Override
//            protected boolean wantOnSelectionChangedNotifications() {
//                return true;
//            }
//        };
//        insertIssueForm.add(issueTypeList);
//
////        priorityList = new DropDownChoice<Priority>("priorities", new PropertyModel<Priority>(this, "issue.priority"), issueDao.getPriorities()) {
////            @Override
////            protected boolean wantOnSelectionChangedNotifications() {
////                return true;
////            }
////        };
////        insertIssueForm.add(priorityList);
//
//
//        ChoiceRenderer<Component> componentRender = new ChoiceRenderer<Component>("name");
//        final PropertyModel<List<Component>> propertyModel = new PropertyModel(this, "issue.components");
////        listComponents = new ListMultipleChoice<Component>("components", propertyModel, projectDao.getProjectComponents(issue.getProject()), componentRender);
////        insertIssueForm.add(listComponents);
//
//        add(insertIssueForm);
//

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
