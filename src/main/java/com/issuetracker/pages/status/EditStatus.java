package com.issuetracker.pages.status;

import com.issuetracker.pages.layout.PageLayout;
import com.issuetracker.model.Status;
import com.issuetracker.service.api.StatusService;
import static com.issuetracker.web.Constants.HOME_PAGE;
import static com.issuetracker.web.Utils.parsePageClassFromPageParams;
import com.issuetracker.web.quilifiers.ViewPageConstraint;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.model.PropertyModel;

import javax.inject.Inject;
import org.apache.wicket.request.flow.RedirectToUrlException;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.string.StringValue;


/**
 *
 * @author mgottval
 */
public class EditStatus extends PageLayout {

    @Inject
    private StatusService statusService;
    private Status status;
    
    @ViewPageConstraint(allowedRole = "status")
    public EditStatus(final PageParameters parameters) {
        final StringValue statusName = parameters.get("statusName");
        final StringValue page = parameters.get("page");
        StringValue nullSV = StringValue.valueOf((String)null);
        
        if (statusName.equals(nullSV) || page.equals(nullSV)) {
            throw new RedirectToUrlException(HOME_PAGE);
        }
        
        status = statusService.getStatusByName(statusName.toString());
        
        Form<Status> editStatusForm = new Form<Status>("editStatusForm") {
            @Override
            protected void onSubmit() {
                Status edittingStatus = statusService.getStatusByName(statusName.toString());
                Status statusDB = statusService.getStatusByName(status.getName());
                
                if (statusDB == null || edittingStatus.equals(status)) {
                    statusService.update(status);
                    setResponsePage(parsePageClassFromPageParams(page), parameters);
                } else {
                    error("There is already status with given name.");
                }
            }
        };
        editStatusForm.add(new RequiredTextField<>("name", new PropertyModel<String>(this, "status.name")));
        add(editStatusForm);
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
    
    
}
