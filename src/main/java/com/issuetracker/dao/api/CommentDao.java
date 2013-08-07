/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.issuetracker.dao.api;

import com.issuetracker.model.Comment;
import com.issuetracker.model.Issue;
import java.util.List;

/**
 *
 * @author mgottval
 */
public interface CommentDao {
    
    void insert(Comment comment);
    
    void remove(Comment comment);
    
    List<Comment> getCommentsOfIssue(Issue issue);
    
}
