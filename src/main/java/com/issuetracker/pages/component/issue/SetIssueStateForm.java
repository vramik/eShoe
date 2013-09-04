/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.issuetracker.pages.component.issue;

import com.issuetracker.dao.api.IssueDao;
import com.issuetracker.model.Issue;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
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
     private Form<Issue> updateIssueForm;
     private Issue issue;
    
    public SetIssueStateForm(String id, IModel<Issue> issueModel) {
        super(id);
        this.issue = issueModel.getObject();
        
        add(new FeedbackPanel("feedback"));
        
         IModel<List<Issue.Status>> statusModelChoices = new AbstractReadOnlyModel<List<Issue.Status>>() {
            @Override
            public List<Issue.Status> getObject() {
                List<Issue.Status> models = new ArrayList<Issue.Status>();
                models.add(Issue.Status.NEW);
                models.add(Issue.Status.MODIFIED);
                models.add(Issue.Status.CLOSED);
                models.add(Issue.Status.ON_QA);
                models.add(Issue.Status.VERIFIED);
                return models;
            }
        };
        final DropDownChoice<Issue.Status> priorityDropDown = new DropDownChoice<Issue.Status>("statusDropDown", new PropertyModel<Issue.Status>(this, "issue.status"), statusModelChoices);

        updateIssueForm = new Form<Issue>("updateIssueForm") {
            @Override
            protected void onSubmit() {
                issueDao.updateIssue(issue);
            }
        };
        updateIssueForm.add(priorityDropDown);
        add(updateIssueForm);

    }

    public Issue getIssue() {
        return issue;
    }

    public void setIssue(Issue issue) {
        this.issue = issue;
    }
    
    
    
}
