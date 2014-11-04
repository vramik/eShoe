package com.issuetracker.pages.workflow;

import com.issuetracker.model.Project;
import com.issuetracker.pages.layout.PageLayout;
import com.issuetracker.model.Status;
import com.issuetracker.model.Workflow;
import com.issuetracker.pages.component.status.StatusListView;
import com.issuetracker.pages.component.workflow.WorkflowTransitionsListView;
import com.issuetracker.service.api.ProjectService;
import com.issuetracker.service.api.StatusService;
import com.issuetracker.service.api.WorkflowService;
import com.issuetracker.web.quilifiers.SecurityConstraint;
import static com.issuetracker.web.security.PermissionsUtil.getProjectWithEditPermissions;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import javax.inject.Inject;
import java.util.List;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.ListMultipleChoice;
import org.apache.wicket.markup.html.link.Link;

/**
 *
 * @author mgottval
 */
public class WorkflowDetail extends PageLayout {
    
    private Workflow workflow;
    @Inject
    private WorkflowService workflowService;
    @Inject
    private StatusService statusService;
    @Inject
    private ProjectService projectService;
    private List<Project> selectedProjects;
    
    @SecurityConstraint(allowedRole = "workflow")
    public WorkflowDetail(PageParameters parameters) {
        System.err.println("WorkflowDetail: TODO null params test");
        Long workflowId = parameters.get("workflow").toLong();
        
        workflow = workflowService.getWorkflowById(workflowId);
        selectedProjects = getProjectWithEditPermissions(getRequest(), projectService.getProjectsByWorkflow(workflow));
        
        add(new Label("workflowName", workflow.getName()));
        add(new Link("back") {
            @Override
            public void onClick() {
                setResponsePage(CreateWorkflow.class);
            }
        });
        
        Form<Workflow> setWorkflowToProjects = new Form<Workflow>("setWorkflowToProjects") {
            @Override
            protected void onSubmit() {
                for (Project project : selectedProjects) {
                    project.setWorkflow(workflow);
                    projectService.update(project);
                }
            }
        };
        add(setWorkflowToProjects);
        List<Project> projects = getProjectWithEditPermissions(getRequest(), projectService.getProjects());
        final ListMultipleChoice<Project> projectMultipleChoise = new ListMultipleChoice<>(
                "projectMultipleChoise", 
                new PropertyModel<List<Project>>(this, "selectedProjects"), 
                projects,
                new ChoiceRenderer<Project>("name"));
        projectMultipleChoise.setMaxRows(projects.size());
        setWorkflowToProjects.add(projectMultipleChoise);
        
                
        add(new Label("transitionsHeader", "Transitions:"));
        add(new WorkflowTransitionsListView("transitionListView", new PropertyModel<Workflow>(this, "workflow")));
        
        add(new Label("statusesHeader", "Click status to create transition."));
        IModel<List<Status>> statusesModel = new CompoundPropertyModel<List<Status>>(statusService.getStatuses()) {
            @Override
            public List<Status> getObject() {
                return statusService.getStatuses();
            }            
         };
              
        add(new StatusListView("statusListView", statusesModel, new CompoundPropertyModel<>(workflow)));
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
