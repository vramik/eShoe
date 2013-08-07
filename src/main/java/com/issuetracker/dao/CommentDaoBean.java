/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.issuetracker.dao;

import com.issuetracker.dao.api.CommentDao;
import com.issuetracker.model.Comment;
import com.issuetracker.model.Issue;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 *
 * @author mgottval
 */
@Stateless
public class CommentDaoBean implements CommentDao {

    @PersistenceContext(unitName = "com_IssueTracker_war_1.0-SNAPSHOTPU2")
    private EntityManager em;
    private CriteriaBuilder qb;

    @Override
    public void insert(Comment comment) {
        em.persist(comment);
    }

    @Override
    public void remove(Comment comment) {
        em.remove(comment);
    }

    @Override
    public List<Comment> getCommentsOfIssue(Issue issue) {
//        qb = em.getCriteriaBuilder();
//        CriteriaQuery<Comment> c = qb.createQuery(Comment.class);
//        Root<Comment> i = c.from(Comment.class);
//        TypedQuery<Comment> q = em.createQuery(c);
//        List<Comment> results = q.getResultList();
//        if (results != null && !results.isEmpty()) {
//            return results;
//        }
        return null;
    }
}
