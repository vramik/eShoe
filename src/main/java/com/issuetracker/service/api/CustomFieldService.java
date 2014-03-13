/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.issuetracker.service.api;

import com.issuetracker.model.CustomField;

/**
 *
 * @author mgottval
 */
public interface CustomFieldService {
    
    void insert(CustomField customField);
    
    void remove(CustomField customField);
    
    void update(CustomField customField);
}
