package com.issuetracker.pages.issuetype;

import javax.inject.Inject;
import java.util.List;
import com.issuetracker.model.IssueType;
import com.issuetracker.pages.layout.PageLayout;
import com.issuetracker.service.api.IssueTypeService;
import com.issuetracker.web.quilifiers.SecurityConstraint;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

/**
 *
 * @author mgottval
 */
public class CreateIssueType extends PageLayout {

    @Inject
    private IssueTypeService issueTypeService;
    private IssueType issueType = new IssueType();
    private final List<IssueType> issueTypes;

    @SecurityConstraint(allowedRole = "issue.type")
    public CreateIssueType() {
        issueTypes = issueTypeService.getIssueTypes();
        Form<IssueType> insertIssueTypeForm = new Form<IssueType>("insertIssueTypeForm") {
            @Override
            protected void onSubmit() {
                if (issueTypeService.getIssueTypeByName(issueType.getName()) != null) {
                    error("Specified issue type is already added.");
                } else {
                    issueTypeService.insert(issueType);
                    issueTypes.add(issueType);
                }
                issueType = new IssueType();
            }
        };
        insertIssueTypeForm.add(new RequiredTextField<>("name", new PropertyModel<String>(this, "issueType.name")));
        add(insertIssueTypeForm);

        add(new Label("issueTypes", "Issue Types"));
        ListView<IssueType> issueTypeListView = new ListView<IssueType>("issueTypeListView", issueTypes) {
            @Override
            protected void populateItem(ListItem<IssueType> item) {
                final IssueType issueType = item.getModelObject();

                item.add(new Label("name", issueType.getName()));
                item.add(new Link("remove") {
                    @Override
                    public void onClick() {
                        issueTypeService.remove(issueType);
                        issueTypes.remove(issueType);
                    }
                }.setEnabled(!issueTypeService.isIssueTypeUsed(issueType)));
                item.add(new Link("edit") {
                    @Override
                    public void onClick() {
                        PageParameters parameters = new PageParameters();
                        parameters.add("issueTypeName", issueType.getName());
                        setResponsePage(EditIssueType.class, parameters);
                    }
                });
            }
        };
        add(issueTypeListView);
    }

    public IssueType getIssueType() {
        return issueType;
    }

    public void setIssueType(IssueType issueType) {
        this.issueType = issueType;
    }
}
