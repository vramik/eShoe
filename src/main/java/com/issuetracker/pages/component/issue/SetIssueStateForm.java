package com.issuetracker.pages.component.issue;

import com.issuetracker.dao.api.IssueDao;
import com.issuetracker.dao.api.StatusDao;
import com.issuetracker.dao.api.TransitionDao;
import com.issuetracker.model.Issue;
import com.issuetracker.model.IssuesRelationship;
import com.issuetracker.model.IssuesRelationship.RelationshipType;
import com.issuetracker.model.Project;
import com.issuetracker.model.ProjectVersion;
import com.issuetracker.model.Status;
import com.issuetracker.model.Transition;
import com.issuetracker.model.Workflow;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Inject;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
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
    private String issueRelatesToId;
    private Issue issueRelatesTo;
    private IssuesRelationship issuesRelationship;
    private List<IssuesRelationship> issuesRelationships;
    private IssueRelationsListView<IssuesRelationship> issueRelationsListView;

    public SetIssueStateForm(String id, IModel<Issue> issueModel) {
        super(id);
        this.issue = issueModel.getObject();
        this.project = issue.getProject();
        workflow = project.getWorkflow();
        issuesRelationship = new IssuesRelationship();

        add(new FeedbackPanel("feedback"));

        if (issue.getRelatesTo() != null || !issue.getRelatesTo().isEmpty()) {
            issuesRelationships = issue.getRelatesTo();
        } else {
            issuesRelationships = new ArrayList<IssuesRelationship>();
        }

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
                        if (transition.getFromStatus().equals(currentStatus)) {
                            possibelStatuses.add(transition.getToStatus());

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

       IModel relTypeModelChoices = new AbstractReadOnlyModel<List<IssuesRelationship.RelationshipType>>() {
                @Override
                public List<IssuesRelationship.RelationshipType> getObject() {
                    return Arrays.asList(IssuesRelationship.RelationshipType.values());
                }
            };

        final DropDownChoice<Status> priorityDropDown = new DropDownChoice<Status>("statusDropDown", new PropertyModel<Status>(this, "issue.status"), statusModelChoices, new ChoiceRenderer<Status>("name"));

        updateIssueForm = new Form<Issue>("updateIssueForm") {
            @Override
            protected void onSubmit() {
                
                if (issueRelatesToId != null && !issueRelatesToId.equals("")) {
                    issueRelatesTo = issueDao.getIssueById(Long.valueOf(issueRelatesToId));

                    issuesRelationship.setIsRelatedIssue(issue);
                    issuesRelationship.setRelatesToIssue(issueRelatesTo);
                    issuesRelationship.setRelationshipType(RelationshipType.RELATES_TO);
                    issuesRelationships.add(issuesRelationship);
                    issue.setRelatesTo(issuesRelationships);
                    issueRelatesToId = "";
                    issuesRelationship = new IssuesRelationship();
                }
                issueDao.updateIssue(issue);


            }
        };
        updateIssueForm.add(priorityDropDown);
        updateIssueForm.add(new TextField<String>("issueRelatesToId", new PropertyModel<String>(this, "issueRelatesToId")));
        updateIssueForm.add(new DropDownChoice<IssuesRelationship.RelationshipType>("relTypeDropDown", new PropertyModel<IssuesRelationship.RelationshipType>(this, "issuesRelationship.relationshipType"), relTypeModelChoices));
        add(updateIssueForm);


    }

    public Issue getIssue() {
        return issue;
    }

    public void setIssue(Issue issue) {
        this.issue = issue;
    }

    public String getIssueRelatesToId() {
        return issueRelatesToId;
    }

    public void setIssueRelatesToId(String issueRelatesToId) {
        this.issueRelatesToId = issueRelatesToId;
    }

    public IssuesRelationship getIssuesRelationship() {
        return issuesRelationship;
    }

    public void setIssuesRelationship(IssuesRelationship issuesRelationship) {
        this.issuesRelationship = issuesRelationship;
    }
    
    
}
