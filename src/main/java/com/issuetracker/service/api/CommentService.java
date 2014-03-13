package com.issuetracker.service.api;

import com.issuetracker.model.Comment;
import com.issuetracker.model.Issue;

import java.util.List;

/**
 *
 * @author mgottval
 */
public interface CommentService {
    
    void insert(Comment comment);
    
    void remove(Comment comment);
    
    List<Comment> getCommentsOfIssue(Issue issue);
    
}
