package com.issuetracker.service.api;

import com.issuetracker.model.Issue;
import com.issuetracker.model.Status;

import java.util.List;

/**
 * TODO: document this
 *
 * @author Jiri Holusa
 */
public interface SearchService {
    
    public List<Issue> search(String query);
}
