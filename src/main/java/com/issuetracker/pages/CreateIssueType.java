package com.issuetracker.pages;

import com.issuetracker.dao.api.IssueTypeDao;
import com.issuetracker.model.IssueType;
import javax.inject.Inject;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.model.PropertyModel;

/**
 *
 * @author mgottval
 */
public class CreateIssueType extends PageLayout {

    private IssueType issueType;
    @Inject
    private IssueTypeDao issueTypeDao;
    private Form<IssueType> insertIssueTypeForm;

    public CreateIssueType() {
        insertIssueTypeForm = new Form<IssueType>("insertIssueTypeForm") {
            @Override
            protected void onSubmit() {
                issueTypeDao.insert(issueType);
                issueType = new IssueType();
            }
        };
        insertIssueTypeForm.add(new RequiredTextField<String>("name", new PropertyModel<String>(this, "issueType.name")));
        add(insertIssueTypeForm);
    }

    public IssueType getIssueType() {
        return issueType;
    }

    public void setIssueType(IssueType issueType) {
        this.issueType = issueType;
    }
}
