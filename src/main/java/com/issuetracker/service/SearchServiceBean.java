package com.issuetracker.service;

import com.issuetracker.model.Issue;
import com.issuetracker.service.api.SearchService;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.List;

/**
 *
 * TODO: document this
 *
 * @author Jiri Holusa
 */
@Stateless
public class SearchServiceBean implements SearchService{

    public List<Issue> search(String query) {
        System.out.println(query);

        return null;
    }
}
