package com.issuetracker.pages.component.comment;

import com.issuetracker.model.Comment;
import com.issuetracker.model.Issue;
import com.issuetracker.model.Permission;
import com.issuetracker.model.TypeId;
import com.issuetracker.service.api.IssueService;
import static com.issuetracker.web.security.PermissionsUtil.*;
import static com.issuetracker.web.security.KeycloakAuthSession.getIDToken;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import org.apache.wicket.Component;
import org.apache.wicket.markup.html.form.ListMultipleChoice;
import org.apache.wicket.util.iterator.ComponentHierarchyIterator;

/**
 *
 * @author mgottval
 */
public class CommentForm extends Panel {

    @Inject
    private IssueService issueService;
    private Comment comment;
    private List<Comment> comments;
    private Issue issue;
    private List<String> selectedRoles = new ArrayList<>();

    public CommentForm(String id, final IModel<Issue> issueModel) {
        super(id);
        
        //set default view role for comment
        selectedRoles.add("Public");
        
        issue = issueModel.getObject();

        Form<Comment> commentForm = new Form<Comment>("commentForm") {
            @Override
            protected void onSubmit() {
                Permission viewPermission = new Permission();
//                viewPermission.setPermissionType(PermissionType.view);
//                viewPermission.setRoles(new HashSet<>(selectedRoles));
//                comment.setViewPermission(viewPermission);
                comment.setAuthor(getIDToken().getPreferredUsername());
                
                comments = issueService.getComments(issue);
                comments.add(comment);
                issue.setComments(comments);
                issueService.insertComment(issue);
//                issueService.update(issue);
                comment = new Comment();
                
                for (Component c : getPage().visitChildren().filterByClass(CommentListView.class)) {
                    ((CommentListView) c).updateModel();
                }
                
                //clear the select
                selectedRoles.clear();
                selectedRoles.add("Public");
            }
        };
        add(commentForm);

        commentForm.add(new TextArea<>("content", new PropertyModel<String>(this, "comment.content")));
        
        List<String> availableRoles = getAvailableRoles();
        availableRoles.remove("superuser");
        final ListMultipleChoice<String> roles = new ListMultipleChoice<>("roles", 
                new PropertyModel<List<String>>(this, "selectedRoles"),
                availableRoles);
        roles.setRequired(true);
        roles.setOutputMarkupId(true);
        roles.setMaxRows(availableRoles.size());
        commentForm.add(roles);
    }

    public Comment getComment() {
        return comment;
    }

    public void setComment(Comment comment) {
        this.comment = comment;
    }

    public Issue getIssue() {
        return issue;
    }

    public void setIssue(Issue issue) {
        this.issue = issue;
    }

    public List<String> getSelectedRoles() {
        return selectedRoles;
    }

    public void setSelectedRoles(List<String> selectedRoles) {
        this.selectedRoles = selectedRoles;
    }
}
