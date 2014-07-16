package com.issuetracker.pages;

import com.issuetracker.model.Status;
import com.issuetracker.model.Workflow;
import com.issuetracker.pages.component.status.StatusListView;
import com.issuetracker.service.api.StatusService;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

import javax.inject.Inject;
import java.util.List;

//import com.issuetracker.model.Transition;

/**
 *
 * @author mgottval
 */
public class CreateStatuses extends PageLayout {

    private Workflow workflow;
    @Inject
    private StatusService statusService;
    private Status status;
    private List<Status> statuses;
    private Form<Status> insertStatusForm;
    private FeedbackPanel feedbackPanel;
    private RequiredTextField nameField;

    public CreateStatuses() {

        status = new Status();
        statuses = statusService.getStatuses();
        feedbackPanel = new FeedbackPanel("feedbackPanel");
        add(feedbackPanel);
        insertStatusForm = new Form<Status>("insertStatusForm") {
            @Override
            protected void onSubmit() {
                if (statusService.getStatusByName(status.getName()) != null) {
                    nameField.clearInput();

                } else {
                    statusService.insert(status);
                    statuses = statusService.getStatuses();
                }
                status = new Status();


            }
        };
        nameField = new RequiredTextField("statusName", new PropertyModel<String>(this, "status.name"));
        insertStatusForm.add(new RequiredTextField("statusName", new PropertyModel<String>(this, "status.name")));
        add(insertStatusForm);



        IModel<List<Status>> statusesModel = new CompoundPropertyModel<List<Status>>(statuses) {
            @Override
            public List<Status> getObject() {
                return statusService.getStatuses();
            }
        };

        add(new StatusListView("statusListView", statusesModel, null));
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
