package com.issuetracker.pages.transition;

import com.issuetracker.pages.layout.PageLayout;
import com.issuetracker.model.Status;
import com.issuetracker.model.Transition;
import com.issuetracker.model.Workflow;
import com.issuetracker.pages.workflow.WorkflowDetail;
import com.issuetracker.service.api.StatusService;
import com.issuetracker.service.api.TransitionService;
import com.issuetracker.service.api.WorkflowService;
import static com.issuetracker.web.Constants.HOME_PAGE;
import com.issuetracker.web.quilifiers.ViewPageConstraint;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import javax.inject.Inject;
import java.util.List;
import org.apache.wicket.request.flow.RedirectToUrlException;
import org.apache.wicket.util.string.StringValue;

/**
 *
 * @author mgottval
 */
public class AddTransition extends PageLayout {

    private Transition transition = new Transition();
    private Status fromStatus;
    private Workflow workflow;
    @Inject
    private WorkflowService workflowService;
    @Inject
    private StatusService statusService;
    @Inject
    private TransitionService transitionService;

    @ViewPageConstraint(allowedRole = "workflow")
    public AddTransition(PageParameters parameters) {
        StringValue nullSV = StringValue.valueOf((String)null);
        StringValue statusId = parameters.get("status");
        StringValue workflowId = parameters.get("workflow");
        if (workflowId.equals(nullSV) || statusId.equals(nullSV)) {
            throw new RedirectToUrlException(HOME_PAGE);
        }
        
        fromStatus = statusService.getStatusById(statusId.toLong());
        workflow = workflowService.getWorkflowById(workflowId.toLong());

        add(new Label("workflowName", workflow.getName()));
        add(new Label("statusName", fromStatus.getName()));
        
        Form<Status> editStatusForm = new Form<Status>("addTransitionForm") {
            @Override
            protected void onSubmit() {
                transition.setFromStatus(fromStatus);
                transitionService.insert(transition);
                List<Transition> transitions = workflow.getTransitions();
                transitions.add(transition);
                workflow.setTransitions(transitions);
                workflowService.update(workflow);
                PageParameters pageParameters = new PageParameters();
                pageParameters.add("workflow", workflow.getId());
                setResponsePage(WorkflowDetail.class, pageParameters);
            }
        };
        editStatusForm.add(new RequiredTextField("transitionName", new PropertyModel<String>(this, "transition.name")));
        
        IModel<List<Status>> statusesModel = new AbstractReadOnlyModel<List<Status>>() {
            @Override
            public List<Status> getObject() {
                List<Status> result = statusService.getStatuses();
                result.remove(fromStatus);
                return result;
            }
        };
        DropDownChoice<Status> dropDownStatusTo = new DropDownChoice<>("statuses", new PropertyModel<Status>(this, "transition.toStatus"), statusesModel, new ChoiceRenderer<Status>("name"));
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
