package com.issuetracker.service;

import com.issuetracker.dao.api.CommentDao;
import com.issuetracker.model.Comment;
import com.issuetracker.model.Issue;
import com.issuetracker.service.api.CommentService;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import java.util.List;

/**
 *
 * @author mgottval
 */
@Stateless
public class CommentServiceBean implements CommentService {

    @PersistenceContext
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
