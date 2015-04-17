package com.issuetracker.service.api;

import com.issuetracker.model.Comment;


/**
 *
 * @author mgottval
 */
public interface CommentService {
    
    Comment insert(Comment comment);
    
    void remove(Comment comment);
}
