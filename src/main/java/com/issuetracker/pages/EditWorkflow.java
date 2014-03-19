package com.issuetracker.pages;

import com.issuetracker.dao.api.WorkflowDao;
import com.issuetracker.model.Project;
import com.issuetracker.model.Workflow;
import java.util.List;
import javax.inject.Inject;

import com.issuetracker.service.api.WorkflowService;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

/**
 *
 * @author mgottval
 */
public class EditWorkflow extends PageLayout {

    private Form searchWorkflowForm;
    private DropDownChoice<Project> projectDropDownChoice;
    private DropDownChoice<Workflow> workflowDropDownChoice;
    private Workflow workflow;
    @Inject
    private WorkflowService workflowService;

    public EditWorkflow() {
        searchWorkflowForm = new Form("form") {
            @Override
            protected void onSubmit() {
                PageParameters parameters = new PageParameters();
                parameters.add("workflow", workflow.getId());
                setResponsePage(WorkflowDetail.class, parameters);
            }
        };
        IModel<List<Workflow>> workflowsModel = new AbstractReadOnlyModel<List<Workflow>>() {
            @Override
            public List<Workflow> getObject() {
                return workflowService.getWorkflows();
            }
        };
        workflowDropDownChoice = new DropDownChoice<Workflow>("workflowDropDownChoice", new PropertyModel<Workflow>(this, "workflow"), workflowsModel, new ChoiceRenderer<Workflow>("name"));
        searchWorkflowForm.add(workflowDropDownChoice);
//        projectDropDownChoice = new DropDownChoice<Project>("projectDropDownChoice", new PropertyModel<Project>(this, "project"), projectsModel, new ChoiceRenderer<Project>("name"));
//        form.add(projectDropDownChoice);
    }

    public Workflow getWorkflow() {
        return workflow;
    }

    public void setWorkflow(Workflow workflow) {
        this.workflow = workflow;
    }
}
