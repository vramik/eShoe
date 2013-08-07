/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.issuetracker.pages.component.comment;

import com.issuetracker.dao.IssueDaoBean;
import com.issuetracker.dao.api.CommentDao;
import com.issuetracker.dao.api.IssueDao;
import com.issuetracker.model.Comment;
import com.issuetracker.model.Issue;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Inject;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

/**
 *
 * @author mgottval
 */
public class CommentListView extends Panel {

//    @Inject
//    private CommentDao commentDao;
    @Inject
    private IssueDao issueDao;
    private ListView commentsListView;
    private List<Comment> commentList;
    private Issue issue;

    public CommentListView(String id, final IModel<Issue> issueModel) {
        super(id);
        Logger.getLogger(CommentListView.class.getName()).log(Level.SEVERE, issueModel.getObject().getName());
        issue = issueModel.getObject();
        try {
        commentList = issueDao.getComments(issue);
        } catch (NullPointerException e) {
            System.out.println("Oops, we went to far, better go back to 0!");
        }
        if (commentList == null) {
            commentList = new ArrayList<Comment>();
        }

        add(new FeedbackPanel("feedback"));
        commentsListView = new ListView<Comment>("commentsList", new PropertyModel<List<Comment>>(this, "commentList")) {
            @Override
            protected void populateItem(ListItem<Comment> item) {
                final Comment comment = item.getModelObject();
                item.add(new Link<Comment>("remove", item.getModel()) {
                    @Override
                    public void onClick() {
                        commentList.remove(comment);
                        Issue i = issueModel.getObject();
                        i.setComments(commentList);
                        issueDao.updateIssue(i);

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
}
