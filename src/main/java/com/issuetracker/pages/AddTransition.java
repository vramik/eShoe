package com.issuetracker.pages;

import com.issuetracker.model.Status;
import com.issuetracker.model.Transition;
import com.issuetracker.model.Workflow;
import com.issuetracker.service.api.StatusService;
import com.issuetracker.service.api.TransitionService;
import com.issuetracker.service.api.WorkflowService;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author mgottval
 */
public class AddTransition extends PageLayout {

    private Status status;
    private Transition transition;
    private Workflow workflow;
    @Inject
    private WorkflowService workflowService;
    @Inject
    private StatusService statusService;
    @Inject
    private TransitionService transitionService;
//    private List<Transition> transitionsList;
//    private Transition transition;
    private DropDownChoice<Status> dropDownStatusTo;
    private List<Status> statusList;
    private Form<Status> editStatusForm;

    public AddTransition(PageParameters parameters) {
        Long statusId = parameters.get("status").toLong();
        Long workflowId = parameters.get("workflow").toLong();
        status = statusService.getStatusById(statusId);
        workflow = workflowService.getWorkflowById(workflowId);
        statusList = new ArrayList<Status>();

        transition = new Transition();
        add(new Label("workflowName", workflow.getName()));
        IModel<List<Status>> statusesModel = new AbstractReadOnlyModel<List<Status>>() {
            @Override
            public List<Status> getObject() {
                List<Status> models = statusService.getStatuses();
                if (models == null) {
                    models = Collections.emptyList();
                }
                return models;
            }
        };

        add(new Label("statusName", status.getName()));
        add(new FeedbackPanel("feedbackPanel"));
        editStatusForm = new Form<Status>("addTransitionForm") {
            @Override
            protected void onSubmit() {
                transition.setFromStatus(status);
                transition.setWorkflow(workflow);
                transitionService.insert(transition);
                transition = new Transition();
                PageParameters pageParameters = new PageParameters();
                pageParameters.add("workflow", workflow.getId());
                setResponsePage(WorkflowDetail.class, pageParameters);
            }
        };
        editStatusForm.add(new RequiredTextField("transitionName", new PropertyModel<String>(this, "transition.name")));
        dropDownStatusTo = new DropDownChoice<Status>("statuses", new PropertyModel<Status>(this, "transition.toStatus"), statusesModel, new ChoiceRenderer<Status>("name"));
        dropDownStatusTo.setRequired(true);
        editStatusForm.add(dropDownStatusTo);
        add(editStatusForm);
    }

    public Transition getTransition() {
        return transition;
    }

    public void setTransition(Transition transition) {
        this.transition = transition;
    }
}
