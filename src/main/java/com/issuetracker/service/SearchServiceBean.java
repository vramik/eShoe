package com.issuetracker.service;

import com.github.holmistr.esannotations.search.SearchManager;
import com.issuetracker.model.Issue;
import com.issuetracker.search.TransportClient;
import com.issuetracker.search.ql.SearchQueryLanguageLexer;
import com.issuetracker.search.ql.SearchQueryLanguageParser;
import com.issuetracker.service.api.SearchService;
import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.TokenStream;
import org.antlr.runtime.tree.CommonTree;
import org.antlr.runtime.tree.Tree;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.*;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.*;

/**
 * Implementation of SearchService.
 *
 * @author Jiri Holusa
 */
@Stateless
public class SearchServiceBean implements SearchService {

    //hash map of attributes that can be searched by with they corresponding name in index
    private final Map<String, String> ALLOWED_SEARCH_PROPERTIES = new HashMap<String, String>(){{
        put("id", "id");
        put("project", "project.name");
        put("status", "status.name");
        put("issue_type", "issue_type.name");
        put("created", "created");
        put("updated", "updated");
        put("creator", "creator.name");
        put("owner", "owner.name");
        put("priority", "priority");
        put("text", ""); // value never used since it's QueryString type query
    }};

    @Inject
    @TransportClient
    private Client esClient;

    private SearchRequestBuilder searchRequestBuilder;

    private SearchManager searchManager;

    @Override
    public List<Issue> search(String query) {
        searchManager = new SearchManager(esClient);
        searchRequestBuilder = esClient.prepareSearch("issues").setTypes("issue");

        if(!parseQuery(query)) {
            return Collections.emptyList();
        }

        SearchResponse searchResponse = searchRequestBuilder.execute().actionGet();
        List<Issue> issues = searchManager.search(searchResponse, Issue.class);

        return issues;
    }

    private boolean parseQuery(String query) {
        ANTLRStringStream input = new ANTLRStringStream(query);
        TokenStream tokens = new CommonTokenStream(new SearchQueryLanguageLexer(input));

        SearchQueryLanguageParser parser = null;
        SearchQueryLanguageParser.query_return parserReturn = null;
        try {
            //parser generates abstract syntax tree
            parser = new SearchQueryLanguageParser(tokens);
            parserReturn = parser.query();
        } catch (RecognitionException ex) {
            throw new IllegalStateException("Unreachable state was reached!");
        }

        //acquire parse result
        CommonTree root = (CommonTree) parserReturn.getTree();

        QueryBuilder queryBuilder = QueryBuilders.matchAllQuery();
        List<FilterBuilder> filters = new ArrayList<FilterBuilder>();
        if(root != null && root.getChildren() != null) {
            for(Object item: root.getChildren()) {
                Tree child = (Tree) item;
                if(!root.isNil()) { //can only happen when there is simple query like field = value and we want to always have dummy root
                    child = root;
                }

                String operator = child.getText();
                String fieldName = child.getChild(0).getText();
                if(!ALLOWED_SEARCH_PROPERTIES.containsKey(fieldName)) {
                    continue;
                }
                fieldName = ALLOWED_SEARCH_PROPERTIES.get(fieldName);

                if(operator.equals("~") && child.getChild(0).getText().equals("text")) {
                    queryBuilder = QueryBuilders.queryString(trimQuotes(child.getChild(1).getText()));
                }
                else if(operator.equals("=")) {
                    String fieldValue = trimQuotes(child.getChild(1).getText().toLowerCase());
                    filters.add(FilterBuilders.termFilter(fieldName, fieldValue));
                }
                else if(operator.equals("IN")) {
                    List<String> fieldValues = new ArrayList<String>();
                    for(int i = 1; i < child.getChildCount(); i++) {
                        fieldValues.add(trimQuotes(child.getChild(i).getText().toLowerCase()));
                    }

                    filters.add(FilterBuilders.termsFilter(fieldName, fieldValues));
                }
                else if(operator.equals(">")) {
                    String fieldValue = trimQuotes(child.getChild(1).getText().toLowerCase());
                    filters.add(FilterBuilders.rangeFilter(fieldName).gt(fieldValue));
                }
                else if(operator.equals("<")) {
                    String fieldValue = trimQuotes(child.getChild(1).getText().toLowerCase());
                    filters.add(FilterBuilders.rangeFilter(fieldName).lt(fieldValue));
                }
                else if(operator.equals(">=")) {
                    String fieldValue = trimQuotes(child.getChild(1).getText().toLowerCase());
                    filters.add(FilterBuilders.rangeFilter(fieldName).gte(fieldValue));
                }
                else if(operator.equals("<=")) {
                    String fieldValue = trimQuotes(child.getChild(1).getText().toLowerCase());
                    filters.add(FilterBuilders.rangeFilter(fieldName).lte(fieldValue));
                }

                if(!root.isNil()) { //we already processed it, correction of flow
                    break;
                }
            }
        }

        queryBuilder = applyFilters(filters, queryBuilder);
        if(queryBuilder == null) { //empty query
            return false;
        }

        searchRequestBuilder.setQuery(queryBuilder);
        return true;
    }

    private QueryBuilder applyFilters(List<FilterBuilder> filters, QueryBuilder qb) {
        if (!filters.isEmpty()) {
            return new FilteredQueryBuilder(qb, new AndFilterBuilder(filters.toArray(new FilterBuilder[filters.size()])));
        } else {
            return qb;
        }
    }

    private String trimQuotes(String string) {
        return string.substring(1, string.length() - 1);
    }

    public void setEsClient(Client esClient) {
        this.esClient = esClient;
    }
}
