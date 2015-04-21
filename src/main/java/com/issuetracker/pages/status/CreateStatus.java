package com.issuetracker.pages.status;

import com.issuetracker.pages.layout.PageLayout;
import com.issuetracker.model.Status;
import static com.issuetracker.model.TypeId.global;
import com.issuetracker.pages.component.status.StatusListView;
import com.issuetracker.pages.permissions.AccessDenied;
import com.issuetracker.service.api.SecurityService;
import com.issuetracker.service.api.StatusService;
import static com.issuetracker.web.Constants.roles;
import com.issuetracker.web.quilifiers.ViewPageConstraint;
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
    @Inject
    private SecurityService securityService;
    private Status status = new Status();
    private List<Status> statuses;
    
    @ViewPageConstraint(allowedAction = "it.status")
    public CreateStatus() {
        statuses = statusService.getStatuses();
        Form<Status> insertStatusForm = new Form<Status>("insertStatusForm") {
            @Override
            protected void onSubmit() {
                if (!securityService.canUserPerformAction(global, 0L, roles.getProperty("it.status"))) {
                    setResponsePage(AccessDenied.class);
                }
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
