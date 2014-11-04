package com.issuetracker.pages.issuetype;

import com.issuetracker.model.IssueType;
import com.issuetracker.pages.layout.PageLayout;
import com.issuetracker.service.api.IssueTypeService;
import static com.issuetracker.web.Constants.HOME_PAGE;
import com.issuetracker.web.quilifiers.SecurityConstraint;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.model.PropertyModel;

import javax.inject.Inject;
import org.apache.wicket.request.flow.RedirectToUrlException;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.string.StringValue;

/**
 *
 * @author vramik
 */
public class EditIssueType extends PageLayout {

    @Inject
    private IssueTypeService issueTypeService;
    private IssueType issueType;
    
    @SecurityConstraint(allowedRole = "issue.type")
    public EditIssueType(PageParameters parameters) {
        final StringValue issueTypeName = parameters.get("issueTypeName");
        if (issueTypeName.equals(StringValue.valueOf((String)null))) {
            throw new RedirectToUrlException(HOME_PAGE);
        }
        
        issueType = issueTypeService.getIssueTypeByName(issueTypeName.toString());
        
        Form<IssueType> editIssueTypeForm = new Form<IssueType>("editIssueTypeForm") {
            @Override
            protected void onSubmit() {
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
