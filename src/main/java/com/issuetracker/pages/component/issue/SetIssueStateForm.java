package com.issuetracker.pages.component.issue;

import com.issuetracker.dao.api.IssueDao;
import com.issuetracker.dao.api.StatusDao;
import com.issuetracker.dao.api.TransitionDao;
import com.issuetracker.model.Issue;
import com.issuetracker.model.Project;
import com.issuetracker.model.Status;
import com.issuetracker.model.Transition;
import com.issuetracker.model.Workflow;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Inject;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

/**
 *
 * @author mgottval
 */
public class SetIssueStateForm extends Panel {

    @Inject
    private IssueDao issueDao;
    @Inject
    private StatusDao statusDao;
    @Inject
    private TransitionDao transitionDao;
    private Form<Issue> updateIssueForm;
    private Issue issue;
    private Project project;
    private Workflow workflow;

    public SetIssueStateForm(String id, IModel<Issue> issueModel) {
        super(id);
        this.issue = issueModel.getObject();
        this.project = issue.getProject();
        workflow = project.getWorkflow();

        add(new FeedbackPanel("feedback"));

        IModel<List<Status>> statusModelChoices;
        if (workflow != null) {
            
            statusModelChoices = new AbstractReadOnlyModel<List<Status>>() {
                @Override
                public List<Status> getObject() {
                    Status currentStatus = issue.getStatus();
                    List<Transition> projectWorkflowTransitions = transitionDao.getTransitionsByWorkflow(workflow);
                    List<Status> possibelStatuses = new ArrayList<Status>();
                    possibelStatuses.add(currentStatus);
                    for (Transition transition : projectWorkflowTransitions) {
                        if(transition.getFromStatus().equals(currentStatus)) {                            
                            possibelStatuses.add(transition.getToStatus());
                            String s = "";
                            for (Status status : possibelStatuses) {
                                s = s + status.getName();
                            }
                            Logger.getLogger(SetIssueStateForm.class.getName()).log(Level.SEVERE, s);
                        }
                        
                    }
                    return possibelStatuses;
                }
            };
        } else {
            statusModelChoices = new AbstractReadOnlyModel<List<Status>>() {
                @Override
                public List<Status> getObject() {
                    return statusDao.getStatuses();
                }
            };
        }


        final DropDownChoice<Status> priorityDropDown = new DropDownChoice<Status>("statusDropDown", new PropertyModel<Status>(this, "issue.status"), statusModelChoices, new ChoiceRenderer<Status>("name"));

        updateIssueForm = new Form<Issue>("updateIssueForm") {
            @Override
            protected void onSubmit() {
                issueDao.updateIssue(issue);
            }
        };
        updateIssueForm.add(priorityDropDown);
        add(updateIssueForm);

    }

    public Issue getIssue() {
        return issue;
    }

    public void setIssue(Issue issue) {
        this.issue = issue;
    }
}
