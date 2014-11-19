package com.issuetracker.pages.component.issue;

import com.issuetracker.model.*;
import com.issuetracker.model.IssuesRelationship.RelationshipType;
import com.issuetracker.service.api.IssueService;
import com.issuetracker.service.api.StatusService;
import com.issuetracker.service.api.TransitionService;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author mgottval
 */
public class SetIssueStateForm extends Panel {

    @Inject
    private IssueService issueService;
    @Inject
    private StatusService statusService;
    @Inject
    private TransitionService transitionService;
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
        issuesRelationship.setRelationshipType(RelationshipType.RELATES_TO);

        add(new FeedbackPanel("feedback"));

        if (issue.getRelatesTo() != null || !issue.getRelatesTo().isEmpty()) {
            issuesRelationships = issue.getRelatesTo();
        } else {
            issuesRelationships = new ArrayList<IssuesRelationship>();
        }

        IModel<List<Status>> statusModelChoices;
        if (workflow != null && !workflow.getTransitions().isEmpty()) {
            Logger.getLogger(SetIssueStateForm.class.getName()).log(Level.SEVERE, workflow.getTransitions().toString());

            statusModelChoices = new AbstractReadOnlyModel<List<Status>>() {
                @Override
                public List<Status> getObject() {
                    Status currentStatus = issue.getStatus();
                    List<Transition> projectWorkflowTransitions = workflow.getTransitions();
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
                    List<Status> statuses = new ArrayList<Status>();
                    statuses.add(statusService.getStatusByName("New"));
                    statuses.add(statusService.getStatusByName("Modified"));
                    statuses.add(statusService.getStatusByName("Closed"));
                    return statuses;
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
                if (issueRelatesToId != null && !issueRelatesToId.equals("") && issueRelatesToId.matches("[0-9]+") && issueService.getIssueById(Long.valueOf(issueRelatesToId))!=null) {
                    issueRelatesTo = issueService.getIssueById(Long.valueOf(issueRelatesToId));

                    issuesRelationship.setIsRelatedIssue(issue);
                    issuesRelationship.setRelatesToIssue(issueRelatesTo);                    
                    issuesRelationships.add(issuesRelationship);
                    issue.setRelatesTo(issuesRelationships);
                    issueRelatesToId = "";
                    issuesRelationship = new IssuesRelationship();
                    issuesRelationship.setRelationshipType(RelationshipType.RELATES_TO);
                }
                issueService.update(issue);


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
