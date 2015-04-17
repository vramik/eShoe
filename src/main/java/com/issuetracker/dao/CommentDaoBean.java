package com.issuetracker.dao;

import com.issuetracker.dao.api.CommentDao;
import com.issuetracker.model.Comment;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;

/**
 *
 * @author mgottval
 */
@Stateless
public class CommentDaoBean implements CommentDao {

    @PersistenceContext
    private EntityManager em;
    private CriteriaBuilder qb;

    @Override
    public Comment insertComment(Comment comment) {
        return em.merge(comment);
    }

    @Override
    public void remove(Comment comment) {
        em.remove(comment);
    }
}
