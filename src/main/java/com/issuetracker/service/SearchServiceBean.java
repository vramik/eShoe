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
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.*;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * TODO: document this
 *
 * @author Jiri Holusa
 */
@Stateless
public class SearchServiceBean implements SearchService {

    @Inject
    @TransportClient
    private Client esClient;

    private SearchRequestBuilder searchRequestBuilder;

    private SearchManager searchManager;

    public List<Issue> search(String query) {
        System.out.println(query);
        searchManager = new SearchManager(esClient);
        searchRequestBuilder = esClient.prepareSearch("issues").setTypes("issue");

        parseQuery(query);

        List<Issue> issues = searchManager.search(searchRequestBuilder.execute().actionGet(), Issue.class);

        return issues;
    }

    private void parseQuery(String query) {
        ANTLRStringStream input = new ANTLRStringStream(query);
        TokenStream tokens = new CommonTokenStream(new SearchQueryLanguageLexer(input));

        SearchQueryLanguageParser parser = null;
        SearchQueryLanguageParser.query_return parserReturn = null;
        try {
            //parser generates abstract syntax tree
            parser = new SearchQueryLanguageParser(tokens);
            parserReturn = parser.query();
        } catch (RecognitionException ex) {
            //TODO: handle this properly
        }

        //acquire parse result
        CommonTree root = (CommonTree) parserReturn.getTree();

        AndFilterBuilder andFilterBuilder = FilterBuilders.andFilter();
        for(Object item: root.getChildren()) {
            Tree child = (Tree) item;
            String operator = child.getText();
            String fieldName = child.getChild(0).getText();

            if(operator.equals("~") && child.getChild(0).getText().equals("text")) {
                QueryBuilder queryBuilder = QueryBuilders.queryString(trimQuotes(child.getChild(1).getText()));
                searchRequestBuilder.setQuery(queryBuilder);
            }
            else if(operator.equals("=")) {
                String fieldValue = trimQuotes(child.getChild(1).getText());
                andFilterBuilder.add(FilterBuilders.termFilter(fieldName, fieldValue));
            }
            else if(operator.equals("IN")) {
                List<String> fieldValues = new ArrayList<String>();
                for(int i = 1; i < child.getChildCount(); i++) {
                    fieldValues.add(trimQuotes(child.getChild(i).getText()));
                }

                andFilterBuilder.add(FilterBuilders.termsFilter(fieldName, fieldValues));
            }
        }

        searchRequestBuilder.setPostFilter(andFilterBuilder);
    }

    private String trimQuotes(String string) {
        return string.substring(1, string.length() - 1);
    }
}
