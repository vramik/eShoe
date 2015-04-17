package com.issuetracker.pages.component.comment;

import com.issuetracker.model.Comment;
import com.issuetracker.model.Issue;
import com.issuetracker.model.TypeId;
import com.issuetracker.service.api.CommentService;
import com.issuetracker.service.api.IssueService;
import com.issuetracker.service.api.PermissionService;
import static com.issuetracker.web.Constants.roles;
import static com.issuetracker.web.security.PermissionsUtil.*;
import static com.issuetracker.web.security.KeycloakAuthSession.*;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import org.apache.wicket.Component;
import org.apache.wicket.markup.html.form.ListMultipleChoice;
import org.jboss.logging.Logger;

/**
 *
 * @author mgottval
 */
public class CommentForm extends Panel {

    private final Logger log = Logger.getLogger(CommentForm.class);
    
    @Inject private IssueService issueService;
    @Inject private PermissionService permissionService;
    @Inject private CommentService commentService;
    
    private Comment comment;
    private List<Comment> comments;
    private Issue issue;
    private List<String> selectedRoles = new ArrayList<>();
    List<String> availableRoles = getAvailableRoles();
    
    public CommentForm(String id, final IModel<Issue> issueModel) {
        super(id);
        
        log.error("TODO: availableRoles");//jake role budou available
        
        issue = issueModel.getObject();

        Form<Comment> commentForm = new Form<Comment>("commentForm") {
            @Override
            protected void onSubmit() {
                comment.setAuthor(getIDToken().getPreferredUsername());
                
                if (!selectedRoles.isEmpty() && (availableRoles.size() != selectedRoles.size())) {//if NOT empty or all
                    comment = commentService.insert(comment);//this sets comment's id
                    permissionService.createPermissions(TypeId.comment, comment, roles.getProperty("it.comment.browse"), TypeId.comment, selectedRoles.toArray(new String[selectedRoles.size()]));
                } 
                
                comments = issueService.getComments(issue);
                comments.add(comment);
                issue.setComments(comments);
                issueService.insertComment(issue);
                
                comment = new Comment();
                
                for (Component c : getPage().visitChildren().filterByClass(CommentListView.class)) {
                    ((CommentListView) c).updateModel();
                }
                
                //reset the select
                selectedRoles = null;
            }
        };
        add(commentForm);

        commentForm.add(new TextArea<>("content", new PropertyModel<String>(this, "comment.content")));
        
        
        final ListMultipleChoice<String> roles = new ListMultipleChoice<>("roles", 
                new PropertyModel<List<String>>(this, "selectedRoles"),
                availableRoles);
//        roles.setRequired(true);
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
