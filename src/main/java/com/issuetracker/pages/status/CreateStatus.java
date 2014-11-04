package com.issuetracker.pages.status;

import com.issuetracker.pages.layout.PageLayout;
import com.issuetracker.model.Status;
import com.issuetracker.pages.component.status.StatusListView;
import com.issuetracker.service.api.StatusService;
import com.issuetracker.web.quilifiers.SecurityConstraint;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

import javax.inject.Inject;
import java.util.List;


/**
 *
 * @author mgottval
 */
public class CreateStatus extends PageLayout {

    @Inject
    private StatusService statusService;
    private Status status = new Status();
    private List<Status> statuses;
    
    @SecurityConstraint(allowedRole = "status")
    public CreateStatus() {
        if (statusService.getStatuses().isEmpty()) {
            statusService.insert(new Status("New"));
            statusService.insert(new Status("Modified"));
            statusService.insert(new Status("Closed"));
        }
        statuses = statusService.getStatuses();
        Form<Status> insertStatusForm = new Form<Status>("insertStatusForm") {
            @Override
            protected void onSubmit() {
                // if specified status is already in DB
                if (statusService.getStatusByName(status.getName()) != null) {
                    error("Specified status is already added.");
                } else {
                    statusService.insert(status);
                    statuses.add(status);
//                    statuses = statusService.getStatuses();
                }
                status = new Status();
            }
        };
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
