/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.issuetracker.pages;

import com.issuetracker.dao.api.ProjectDao;
import com.issuetracker.model.Project;
import javax.inject.Inject;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.model.PropertyModel;

/**
 *
 * @author mgottval
 */
public class CreateProject extends PageLayout{
    
    private Form<Project> insertProjectForm;
    
    private Project project;
    
    @Inject
    private ProjectDao projectDao;
    
    public CreateProject() {
        project = new Project();
//         setDefaultModel(new CompoundPropertyModel(project));
         insertProjectForm = new Form<Project>("insertProjectForm") {
            @Override
            protected void onSubmit() {
                
                projectDao.insertProject(project);
                project = new Project();
            }
        };

        insertProjectForm.add(new RequiredTextField<String>("name", new PropertyModel<String>(this, "project.name")));
        add(insertProjectForm);
        
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }
    
}
