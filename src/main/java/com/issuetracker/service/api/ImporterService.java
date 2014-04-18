package com.issuetracker.service.api;


import com.issuetracker.model.User;

import javax.ejb.Local;
import java.util.List;

/**
 *
 * @author Jiri Holusa
 */
public interface ImporterService {
    
    void doImport();
}
