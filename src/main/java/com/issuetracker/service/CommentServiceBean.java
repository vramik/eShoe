package com.issuetracker.service;

import com.issuetracker.dao.api.CommentDao;
import com.issuetracker.model.Comment;
import com.issuetracker.service.api.CommentService;
import java.io.Serializable;

import javax.ejb.Stateless;
import javax.inject.Inject;

/**
 *
 * @author mgottval
 */
@Stateless
public class CommentServiceBean implements CommentService, Serializable {

    @Inject
    private CommentDao commentDao;

    @Override
    public Comment insert(Comment comment) {
        return commentDao.insertComment(comment);
    }

    @Override
    public void remove(Comment comment) {
        commentDao.remove(comment);
    }
}
