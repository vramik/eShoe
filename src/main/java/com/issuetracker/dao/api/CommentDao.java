package com.issuetracker.dao.api;

import com.issuetracker.model.Comment;


/**
 *
 * @author mgottval
 */
public interface CommentDao {
    
    Comment insertComment(Comment comment);
    
    void remove(Comment comment);
    
}
