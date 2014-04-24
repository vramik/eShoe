package com.issuetracker.service.api;

import com.issuetracker.model.Issue;

import java.util.List;

/**
 * Search API for fulltext search
 *
 * @author Jiri Holusa
 */
public interface SearchService {

    /**
     * Searches issues based on string query created using
     * SearchQueryLanguage.g grammar.
     *
     * @param query query that should be executed
     * @return found issues on query
     */
    public List<Issue> search(String query);
}
