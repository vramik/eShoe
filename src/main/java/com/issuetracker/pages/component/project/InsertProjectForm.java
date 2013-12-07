///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//package com.issuetracker.pages.component.project;
//
//import com.issuetracker.dao.api.ComponentDao;
//import com.issuetracker.dao.api.ProjectDao;
//import com.issuetracker.dao.api.ProjectVersionDao;
//import com.issuetracker.dao.api.UserDao;
//import com.issuetracker.model.Component;
//import com.issuetracker.model.Project;
//import com.issuetracker.model.ProjectVersion;
//import com.issuetracker.pages.CreateProject;
//import com.issuetracker.pages.component.component.ComponentListView;
//import com.issuetracker.pages.component.version.VersionListView;
//import com.issuetracker.pages.validator.ProjectNameValidator;
//import java.util.List;
//import java.util.logging.Level;
//import java.util.logging.Logger;
//import javax.inject.Inject;
//import org.apache.wicket.ajax.AjaxRequestTarget;
//import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxButton;
//import org.apache.wicket.markup.html.WebMarkupContainer;
//import org.apache.wicket.markup.html.form.Form;
//import org.apache.wicket.markup.html.form.RequiredTextField;
//import org.apache.wicket.markup.html.form.TextField;
//import org.apache.wicket.markup.html.panel.Panel;
//import org.apache.wicket.model.PropertyModel;
//
///**
// *
// * @author mgottval
// */
//public class InsertProjectForm extends Panel {
//
//    @Inject
//    private ProjectDao projectDao;
//    @Inject
//    private ComponentDao componentDao;
//    private Form<Project> insertProjectForm;
//    private TextField<String> textField;
//    private TextField<String> componentTextField;
//    
//     private final IndicatingAjaxButton insertProjectVersionButton;
//    private final IndicatingAjaxButton insertComponentButton;
//  
//    private List<ProjectVersion> projectVersionList;
//    private List<Component> componentList;
//    private Project project;
//    private List<Project> projects;
//        private String string;
//    private String stringComponent;
//
//    public InsertProjectForm (String id) {
//        super(id);
//       insertProjectForm = new Form<Project>("insertProjectForm") {
//            @Override
//            protected void onSubmit() {
//                project.setVersions(projectVersionList);
//                project.setComponents(componentList);
//                //TODO set owner
//
//                projectDao.insertProject(project);
//                project = new Project();
//                projects = projectDao.getProjects();
//                projectVersionList.clear();
//                componentList.clear();
//
//            }
//        };
//        add(insertProjectForm);
//
//        insertProjectForm.add(new RequiredTextField<String>("name", new PropertyModel<String>(this, "project.name")).add(new ProjectNameValidator()));
//        insertProjectForm.add(new RequiredTextField<String>("summary", new PropertyModel<String>(this, "project.summary")));
//        textField = new TextField<String>("version", new PropertyModel<String>(this, "string"));
//        textField.setOutputMarkupId(true);
//        componentTextField = new TextField<String>("componentName", new PropertyModel<String>(this, "stringComponent"));
//        componentTextField.setOutputMarkupId(true);
////        usersDropDown = new DropDownChoice<User>("ownerDropDown", this, userDao.get)
//        insertProjectForm.add(componentTextField);
//        insertProjectForm.add(textField);
//        
//        
//        
//        //project version button
//         insertProjectVersionButton = new IndicatingAjaxButton("evalButton", insertProjectForm) {
//            @Override
//            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
//                target.add(wmc);
//                projectVersion = new ProjectVersion();
//                projectVersion.setName(textField.getInput());
//                textField.clearInput();
//                target.add(textField);
////                projectVersionDao.insertProjectVersion(projectVersion);
//                projectVersionList.add(projectVersion);
//            }
//
//            @Override
//            protected void onError(AjaxRequestTarget target, Form<?> form) {
//            }
//        };
//
//        insertProjectVersionButton.setDefaultFormProcessing(false);
//        insertProjectForm.add(insertProjectVersionButton);
//        
//        //coponnt button
//         insertComponentButton = new IndicatingAjaxButton("addComponentButton", insertProjectForm) {
//            @Override
//            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
//                target.add(wmcComponent);
//                component = new Component();
//                component.setName(componentTextField.getInput());
//                componentTextField.clearInput();
//                target.add(componentTextField);
//                componentList.add(component);
//            }
//
//            @Override
//            protected void onError(AjaxRequestTarget target, Form<?> form) {
//            }
//        };
//
//        insertComponentButton.setDefaultFormProcessing(false);
//        insertProjectForm.add(insertComponentButton);
//        
//    }
//
//    public Project getProject() {
//        return project;
//    }
//
//    public void setProject(Project project) {
//        this.project = project;
//    }
//
//    public String getString() {
//        return string;
//    }
//
//    public void setString(String string) {
//        this.string = string;
//    }
//
//    public String getStringComponent() {
//        return stringComponent;
//    }
//
//    public void setStringComponent(String stringComponent) {
//        this.stringComponent = stringComponent;
//    }
//    
//    
//}

