package com.issuetracker.pages.component.comment;

import com.issuetracker.model.Comment;
import com.issuetracker.model.Issue;
import com.issuetracker.service.api.IssueService;
import static com.issuetracker.web.security.PermissionsUtil.*;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

import javax.inject.Inject;
import java.util.List;
import org.apache.wicket.markup.html.form.IFormModelUpdateListener;

/**
 *
 * @author mgottval
 */
public class CommentListView extends Panel implements IFormModelUpdateListener {

    @Inject
    private IssueService issueService;
    private final ListView commentsListView;
    private List<Comment> commentList;
    private Issue issue;

    public CommentListView(String id, final IModel<Issue> issueModel) {
        super(id);
        issue = issueModel.getObject();
        commentList = issueService.getComments(issue);

        commentsListView = new ListView<Comment>("commentsList", new PropertyModel<List<Comment>>(this, "commentList")) {
            @Override
            protected void populateItem(ListItem<Comment> item) {
                final Comment comment = item.getModelObject();
                
                item.setVisible(hasViewPermissionComment(comment));
                
                item.add(new Label("author", comment.getAuthor()));
                item.add(new Link("remove") {
                    @Override
                    public void onClick() {

                        commentList.remove(comment);
                        issue.setComments(commentList);
                        issueService.removeComment(issue);
//                        issueService.update(issue);

                    }
                });
                item.add(new Label("comment", comment.getContent()));
            }
        };
        add(commentsListView);
    }

    public List<Comment> getCommentList() {
        return commentList;
    }

    public void setCommentList(List<Comment> commentList) {
        this.commentList = commentList;
    }

    public Issue getIssue() {
        return issue;
    }

    public void setIssue(Issue issue) {
        this.issue = issue;
    }

    @Override
    public void updateModel() {
        commentList = issueService.getComments(issue);
    }
}
