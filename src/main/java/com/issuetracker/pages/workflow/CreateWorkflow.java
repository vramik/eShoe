package com.issuetracker.pages.workflow;

import com.issuetracker.pages.layout.PageLayout;
import com.issuetracker.model.Project;
import com.issuetracker.model.Workflow;
import com.issuetracker.pages.component.workflow.WorkflowListView;
import com.issuetracker.service.api.ProjectService;
import com.issuetracker.service.api.WorkflowService;
import com.issuetracker.web.quilifiers.ViewPageConstraint;
import java.util.ArrayList;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.ListMultipleChoice;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import javax.inject.Inject;
import java.util.List;


/**
 *
 * @author mgottval
 */
public class CreateWorkflow extends PageLayout {

    private Workflow workflow;
    @Inject
    private WorkflowService workflowService;
    @Inject
    private ProjectService projectService;
    private List<Workflow> workflows;
    private List<Project> selectedProjects;

    @ViewPageConstraint(allowedRole = "workflow")
    public CreateWorkflow() {
        
        workflows = workflowService.getWorkflows();
        
        IModel<List<Project>> projectsModel = new AbstractReadOnlyModel<List<Project>>() {
            @Override
            public List<Project> getObject() {
//                return getProjectWithEditPermissions(projectService.getProjects());
                return new ArrayList<>();
            }
        };
        
        Form<Workflow> insertWorkflowForm = new Form<Workflow>("insertWorkflowForm") {
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
        
        insertWorkflowForm.add(new RequiredTextField<>("name", new PropertyModel<String>(this, "workflow.name")));
        
        final ListMultipleChoice<Project> projectMultipleChoise = new ListMultipleChoice<>(
                "projectMultipleChoise", 
                new PropertyModel<List<Project>>(this, "selectedProjects"), 
                projectsModel,
                new ChoiceRenderer<Project>("name"));
        projectMultipleChoise.setMaxRows(projectsModel.getObject().size());
        insertWorkflowForm.add(projectMultipleChoise);
        add(insertWorkflowForm);

        IModel<List<Workflow>> workflowsModel = new CompoundPropertyModel<List<Workflow>>(workflows) {
            @Override
            public List<Workflow> getObject() {
                return workflowService.getWorkflows();
            }
        };

        add(new Label("workflowName", "Workflows"));
        WorkflowListView workflowList = new WorkflowListView<>("workflowList", workflowsModel);
        add(workflowList);
    }

    public Workflow getWorkflow() {
        return workflow;
    }

    public void setWorkflow(Workflow workflow) {
        this.workflow = workflow;
    }

    public List<Project> getSelectedProjects() {
        return selectedProjects;
    }

    public void setSelectedProjects(List<Project> selectedProjects) {
        this.selectedProjects = selectedProjects;
    }
}
