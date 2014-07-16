package com.issuetracker.service;

import com.issuetracker.dao.api.CustomFieldDao;
import com.issuetracker.model.CustomField;
import com.issuetracker.service.api.CustomFieldService;
import java.io.Serializable;

import javax.ejb.Stateless;
import javax.inject.Inject;

/**
 *
 * @author mgottval
 */
@Stateless
public class CustomFieldServiceBean implements CustomFieldService, Serializable {

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
