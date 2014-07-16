package com.issuetracker.service;

import com.issuetracker.dao.api.CommentDao;
import com.issuetracker.model.Comment;
import com.issuetracker.model.Issue;
import com.issuetracker.service.api.CommentService;
import java.io.Serializable;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.List;

/**
 *
 * @author mgottval
 */
@Stateless
public class CommentServiceBean implements CommentService, Serializable {

    @Inject
    private CommentDao commentDao;

    @Override
    public void insert(Comment comment) {
        commentDao.insert(comment);
    }

    @Override
    public void remove(Comment comment) {
        commentDao.remove(comment);
    }

    @Override
    public List<Comment> getCommentsOfIssue(Issue issue) {
        return null;
    }
}
