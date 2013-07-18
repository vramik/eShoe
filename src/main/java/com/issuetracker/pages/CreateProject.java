/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.issuetracker.pages;

import com.issuetracker.dao.api.ProjectDao;
import com.issuetracker.dao.api.ProjectVersionDao;
import com.issuetracker.model.Project;
import com.issuetracker.model.ProjectVersion;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.extensions.markup.html.tabs.TabbedPanel;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.list.PropertyListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.util.ListModel;

/**
 *
 * @author mgottval
 */
public class CreateProject extends PageLayout {

    private Form<Project> insertProjectForm;
    private Form<ProjectVersion> versionsForm;
    private Project project;
    private List<ProjectVersion> projectVersionList;
    private ProjectVersion projectVersion;
    @Inject
    private ProjectDao projectDao;
    @Inject
    private ProjectVersionDao projectVersionDao;

    public CreateProject() {
        project = new Project();
        projectVersionList = new ArrayList<ProjectVersion>();
        projectVersion = new ProjectVersion();

        final Form<ArrayList<ProjectVersion>> multiTextForm = new Form<ArrayList<ProjectVersion>>("multiTextForm");
        add(multiTextForm);

//        final ListView<String> listView = new ListView<String>("listView", new PropertyListView<String>("projectTasks", projectVersionList) {
//            @Override
//            protected void populateItem(final ListItem<String> item) {
//                // TODO Auto-generated method stub
//                TextField<String> textField = new TextField<String>("textField", item.getModel());
//                add(textField);
//                AjaxSubmitLink removeButton = new AjaxSubmitLink("removeButton", multiTextForm) {
//                    @Override
//                    protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
//                        multiTextForm.getModelObject().remove(item.getModelObject());
//                        target.add(multiTextForm);
//                    }
//                    @Override
//                    protected void onError(AjaxRequestTarget target, Form<?> form) {
//                        //errors should be ignored, we shoudlnt validate in our form, so this shouldnt happen anyway
//                        multiTextForm.getModelObject().remove(item.getModelObject());
//                        target.add(multiTextForm);
//                    }
//                };
//                add(removeButton);
//            }
//        });
//        add(listView);



//         setDefaultModel(new CompoundPropertyModel(project));
//        insertProjectForm = new Form<Project>("insertProjectForm") {
//            @Override
//            protected void onSubmit() {
//                projectVersionDao.insertProjectVersion(projectVersion);
//                projectVersionList.add(projectVersion);
//                project.setVersions(projectVersionList);
//                projectDao.insertProject(project);
//                project = new Project();
//                projectVersion = new ProjectVersion();
//            }
//        };
//
//        insertProjectForm.add(new RequiredTextField<String>("name", new PropertyModel<String>(this, "project.name")));
//        insertProjectForm.add(new RequiredTextField<String>("projectVersion", new PropertyModel<String>(this, "projectVersion.name")));
////        insertProjectForm.add(button);
//        insertProjectForm.add(new Button("button") {
//            @Override
//            public void onSubmit() {
//                projectVersionList.add(projectVersion);
//                projectVersion = new ProjectVersion();
//                insertProjectForm.add(new RequiredTextField<String>("projectVersion", new PropertyModel<String>(this, "projectVersion.name")));
//            }
//        });
//        add(insertProjectForm);

    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

//    public List<ProjectVersion> getProjectVersionList() {
//        return projectVersionList;
//    }
//
//    public void setProjectVersionList(List<ProjectVersion> projectVersionList) {
//        this.projectVersionList = projectVersionList;
//    }
    public ProjectVersion getProjectVersion() {
        return projectVersion;
    }

    public void setProjectVersion(ProjectVersion projectVersion) {
        this.projectVersion = projectVersion;
    }
}
