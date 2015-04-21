package com.issuetracker.pages.issuetype;

import com.issuetracker.model.IssueType;
import static com.issuetracker.model.TypeId.global;
import com.issuetracker.pages.layout.PageLayout;
import com.issuetracker.pages.permissions.AccessDenied;
import com.issuetracker.service.api.IssueTypeService;
import com.issuetracker.service.api.SecurityService;
import static com.issuetracker.web.Constants.HOME_PAGE;
import static com.issuetracker.web.Constants.roles;
import com.issuetracker.web.quilifiers.ViewPageConstraint;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.model.PropertyModel;

import javax.inject.Inject;
import org.apache.wicket.request.flow.RedirectToUrlException;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.string.StringValue;
import org.jboss.logging.Logger;

/**
 *
 * @author vramik
 */
public class EditIssueType extends PageLayout {

    private final Logger log = Logger.getLogger(EditIssueType.class);
    
    @Inject private IssueTypeService issueTypeService;
    @Inject private SecurityService securityService;
    private IssueType issueType;
    
    @ViewPageConstraint(allowedAction = "it.issue.type")
    public EditIssueType(PageParameters parameters) {
        final StringValue issueTypeName = parameters.get("issueTypeName");
        if (issueTypeName.equals(StringValue.valueOf((String)null))) {
            log.warn("Page parameters doesn't contain issue type name. Redirecting to Home page.");
            throw new RedirectToUrlException(HOME_PAGE);
        }
        
        issueType = issueTypeService.getIssueTypeByName(issueTypeName.toString());
        
        Form<IssueType> editIssueTypeForm = new Form<IssueType>("editIssueTypeForm") {
            @Override
            protected void onSubmit() {
                if (!securityService.canUserPerformAction(global, 0L, roles.getProperty("it.issue.type"))) {
                    setResponsePage(AccessDenied.class);
                }
                IssueType edittingIT = issueTypeService.getIssueTypeByName(issueTypeName.toString());
                IssueType issueTypeDB = issueTypeService.getIssueTypeByName(issueType.getName());
                
                if (issueTypeDB == null || edittingIT.equals(issueType)) {
                    issueTypeService.update(issueType);
                    setResponsePage(CreateIssueType.class);
                } else {
                    error("There is already issue type with given name.");
                }
            }
        };
        editIssueTypeForm.add(new RequiredTextField<>("name", new PropertyModel<String>(this, "issueType.name")));
        add(editIssueTypeForm);
    }

    public IssueType getIssueType() {
        return issueType;
    }

    public void setIssueType(IssueType issueType) {
        this.issueType = issueType;
    }
}
