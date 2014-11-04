/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.issuetracker.pages.component.workflow;

import com.issuetracker.model.Project;
import com.issuetracker.model.Workflow;
import com.issuetracker.service.api.ProjectService;
import com.issuetracker.service.api.WorkflowService;
import java.util.List;
import javax.inject.Inject;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.ListMultipleChoice;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

/**
 *
 * @author vramik
 * @param <T>
 */
public class WorkflowForm<T extends Workflow> extends Panel {

    @Inject
    private WorkflowService workflowService;
    @Inject
    private ProjectService projectService;
    private Workflow workflow;
    private List<Project> selectedProjects;
    
    public WorkflowForm(String id) {
        super(id);
        
        Form<Workflow> workflowForm = new Form<Workflow>("workflowForm") {
            @Override
            protected void onSubmit() {
                if (workflowService.getWorkflowByName(workflow.getName()) != null) {
                    error("Specified workflow is already added.");
                } else {
                    workflowService.insert(workflow);
                    for (Project project : selectedProjects) {
                        project.setWorkflow(workflow);
                        projectService.update(project);
                    }
                }
                workflow = new Workflow();
                //this will clear the form
                selectedProjects = null;
            }
        };
        
        workflowForm.add(new RequiredTextField<>("name", new PropertyModel<String>(this, "workflow.name")));
        
        IModel<List<Project>> projectsModel = new AbstractReadOnlyModel<List<Project>>() {
            @Override
            public List<Project> getObject() {
                return projectService.getProjects();
            }
        };
        
        
        final ListMultipleChoice<Project> projectMultipleChoise = new ListMultipleChoice<>(
                "projectMultipleChoise", 
                new PropertyModel<List<Project>>(this, "selectedProjects"), 
                projectsModel,
                new ChoiceRenderer<Project>("name"));
        workflowForm.add(projectMultipleChoise);
        add(workflowForm);
    }
    
}
