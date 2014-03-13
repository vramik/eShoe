/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.issuetracker.service;

import com.issuetracker.dao.api.CustomFieldDao;
import com.issuetracker.model.CustomField;
import com.issuetracker.service.api.CustomFieldService;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;

/**
 *
 * @author mgottval
 */
@Stateless
public class CustomFieldServiceBean implements CustomFieldService {

    @PersistenceContext
    private EntityManager em;
    private CriteriaBuilder qb;

    @Override
    public void insert(CustomField customField) {
        em.persist(customField);
    }

    @Override
    public void remove(CustomField customField) {
        em.remove(em.contains(customField)? customField : em.merge(customField));
    }

    @Override
    public void update(CustomField customField) {
        em.merge(customField);
    }
}
