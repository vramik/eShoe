/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.issuetracker.dao.api;

import com.issuetracker.model.CustomField;

/**
 *
 * @author mgottval
 */
public interface CustomFieldDao {
    
    void insert(CustomField customField);
    
    void delete(CustomField customField);
    
    void update(CustomField customField);
}
