/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.issuetracker.service;

import com.issuetracker.dao.api.CustomFieldDao;
import com.issuetracker.model.CustomField;
import com.issuetracker.service.api.CustomFieldService;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;

/**
 *
 * @author mgottval
 */
@Stateless
public class CustomFieldServiceBean implements CustomFieldService {

    @Inject
    private CustomFieldDao customFieldDao;

    @Override
    public void insert(CustomField customField) {
        customFieldDao.insert(customField);
    }

    @Override
    public void remove(CustomField customField) {
        customFieldDao.remove(customField);
    }

    @Override
    public void update(CustomField customField) {
        customFieldDao.update(customField);
    }
}
