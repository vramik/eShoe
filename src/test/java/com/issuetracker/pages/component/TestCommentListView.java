/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.issuetracker.pages.component;

import com.issuetracker.model.Comment;
import com.issuetracker.model.Issue;
import com.issuetracker.pages.component.comment.CommentListView;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author mgottval
 */
public class TestCommentListView {
       private WicketTester tester = null;
    private List<Comment> commentList;
    private Issue issue;

    @Before
    public void setUp() throws Exception {
        tester = new WicketTester();
commentList = new ArrayList<Comment>();
issue = new Issue();
    }

    @Test
    public void testCommentLIstView() {
        final Comment comment1 = new Comment();
        comment1.setContent("comment1");
        final Comment comment2 = new Comment();
        comment2.setContent("comment2");                
        final IModel<List<Comment>> commentModel = new AbstractReadOnlyModel<List<Comment>>() {
            @Override
            public List<Comment> getObject() {
                List<Comment> models = new ArrayList<Comment>();
                models.add(comment1);
                models.add(comment2);

                return models;
            }
        };
        
        issue.setComments(commentModel.getObject());
        PropertyModel<Issue> issuePropertyModel = new PropertyModel(this, "issue");
        CommentListView wflistView = new CommentListView("id",issuePropertyModel);
        List<Comment> wfList  = (List<Comment>) wflistView.get("commentsList").getDefaultModel().getObject();        
        Assert.assertEquals(commentModel.getObject(), wfList);
                  
    }
    
    @Test
    public void testCommentLIstViewLabels() {
        final Comment comment1 = new Comment();
        comment1.setContent("comment1");
        final Comment comment2 = new Comment();
        comment2.setContent("comment2");                
        final IModel<List<Comment>> commentModel = new AbstractReadOnlyModel<List<Comment>>() {
            @Override
            public List<Comment> getObject() {
                List<Comment> models = new ArrayList<Comment>();
                models.add(comment1);
                models.add(comment2);

                return models;
            }
        };
        ;

       issue.setComments(commentModel.getObject());
        PropertyModel<Issue> issuePropertyModel = new PropertyModel(this, "issue");
        CommentListView wflistView = new CommentListView("id",issuePropertyModel);           
        tester.startComponent(wflistView);
        Assert.assertEquals(((Label) wflistView.get("commentsList:0:comment")).getDefaultModel().getObject().toString(), "comment1");;
        Assert.assertEquals(((Label) wflistView.get("commentsList:1:comment")).getDefaultModel().getObject().toString(), "comment2");
    }
}
