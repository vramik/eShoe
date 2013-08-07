/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.issuetracker.pages.component.comment;

import com.issuetracker.dao.api.CommentDao;
import com.issuetracker.dao.api.IssueDao;
import com.issuetracker.model.Comment;
import com.issuetracker.model.Issue;
import com.issuetracker.pages.CreateProject;
import com.issuetracker.pages.IssueDetail;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author mgottval
 */
public class CommentForm extends Panel{
    
    @Inject
    private CommentDao commentDao;
     @Inject
    private IssueDao issueDao;
    
    private Form<Comment> commentForm;
    
    private Comment comment;
    private List<Comment> comments;
    private Issue issue;
    
    public CommentForm(String id, final IModel<Issue> issueModel) {
        super(id);
        comment = new Comment();
        
        
        add(new FeedbackPanel("feedback"));
        
        commentForm = new Form<Comment>("commentForm", new CompoundPropertyModel<Comment>(comment)) {
            @Override
             protected void onSubmit() {                
//                commentDao.insert(comment);
                comments = issueDao.getComments(issue);
                comments.add(comment);
                issue = issueModel.getObject();
                issue.setComments(comments);
                issueDao.updateIssue(issue);
                comment = new Comment();
                String s = "";
                for (Comment comment1 : issueDao.getComments(issue)) {
                    s = s + comment1.getContent();
                }

                Logger.getLogger(CommentForm.class.getName()).log(Level.SEVERE, s);
                
            }
        };
        add(commentForm);
        
        commentForm.add(new TextArea<String>("content"));
        
    }

    public Comment getComment() {
        return comment;
    }

    public void setComment(Comment comment) {
        this.comment = comment;
    }
    
    
    
}
