package com.issuetracker.search;

import com.github.holmistr.esannotations.indexing.AnnotationIndexManager;
import com.github.tlrx.elasticsearch.test.annotations.ElasticsearchClient;
import com.github.tlrx.elasticsearch.test.annotations.ElasticsearchNode;
import com.github.tlrx.elasticsearch.test.support.junit.runners.ElasticsearchRunner;
import com.issuetracker.model.Issue;
import com.issuetracker.search.tools.SearchTestHelper;
import com.issuetracker.service.SearchServiceBean;
import com.issuetracker.service.api.SearchService;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.node.Node;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author: Jiří Holuša
 */
@RunWith(ElasticsearchRunner.class)
public class FulltextSearchTest {

    @ElasticsearchNode
    private Node node;

    @ElasticsearchClient
    private Client client;

    private AnnotationIndexManager manager;

    private SearchServiceBean searchService;

    private Issue issue1;
    private Issue issue2;
    private Issue issue3;
    private Issue issue4;

    @Before
    public void init() {
        issue1 = SearchTestHelper.createTestIssue1();
        issue2 = SearchTestHelper.createTestIssue2();
        issue3 = SearchTestHelper.createTestIssue3();
        issue4 = SearchTestHelper.createTestIssue4();

        manager = new AnnotationIndexManager(client);
        manager.index(issue1, true);
        manager.index(issue2, true);
        manager.index(issue3, true);
        manager.index(issue4, true);

        searchService = new SearchServiceBean();
        searchService.setEsClient(client);
    }

    @Test
    public void testProjectFilter() {
        String query = "project = \"Infinispan\"";
        List<Issue> result = searchService.search(query);

        assertFalse(result.isEmpty());
        for(Issue issue: result) {
            assertTrue(SearchTestHelper.ISPN_PROJECT.contains(issue.getIssueId()));
        }
    }

    @Test
    public void testStatusFilter() {
        String query = "status = \"Open\"";
        List<Issue> result = searchService.search(query);

        assertFalse(result.isEmpty());
        for(Issue issue: result) {
            assertTrue(SearchTestHelper.OPEN_STATUS.contains(issue.getIssueId()));
        }
    }

    @Test
    public void testIssueTypeFilter() {
        String query = "issue_type = \"Bug\"";
        List<Issue> result = searchService.search(query);

        assertFalse(result.isEmpty());
        for(Issue issue: result) {
            assertTrue(SearchTestHelper.BUG_ISSUETYPE.contains(issue.getIssueId()));
        }
    }

    @Test
    public void testCreationDateFilter() {
        String query = "created >= \"2014-01-01\" AND created <= \"2014-03-01\"";
        List<Issue> result = searchService.search(query);

        assertFalse(result.isEmpty());
        for(Issue issue: result) {
            assertTrue(SearchTestHelper.RANGE_CREATED.contains(issue.getIssueId()));
        }
    }

    @Test
    public void testUpdateDateFilter() {
        String query = "updated >= \"2014-03-04\" AND updated <= \"2014-04-05\"";
        List<Issue> result = searchService.search(query);

        assertFalse(result.isEmpty());
        for(Issue issue: result) {
            assertTrue(SearchTestHelper.RANGE_UPDATED.contains(issue.getIssueId()));
        }
    }

    @Test
    public void testCreatorFilter() {
        String query = "creator = \"Tomas Sykora\"";
        List<Issue> result = searchService.search(query);

        assertFalse(result.isEmpty());
        for(Issue issue: result) {
            assertTrue(SearchTestHelper.SYKORA_CREATOR.contains(issue.getIssueId()));
        }
    }

    @Test
    public void testOwnerFilter() {
        String query = "owner = \"Pavol Pitonak\"";
        List<Issue> result = searchService.search(query);

        assertFalse(result.isEmpty());
        for(Issue issue: result) {
            assertTrue(SearchTestHelper.PITONAK_OWNER.contains(issue.getIssueId()));
        }
    }

    @Test
    public void testPriorityFilter() {
        String query = "priority = \"high\"";
        List<Issue> result = searchService.search(query);

        assertFalse(result.isEmpty());
        for(Issue issue: result) {
            assertTrue(SearchTestHelper.HIGH_PRIORITY.contains(issue.getIssueId()));
        }
    }

    @Test
    public void testIdFilter() {
        String query = "id = \"1\"";
        List<Issue> result = searchService.search(query);

        assertFalse(result.isEmpty());
        for(Issue issue: result) {
            assertEquals(1, (long) issue.getIssueId());
        }
    }

    @Test
    public void testInOperator() {
        String query = "id IN (\"1\", \"4\")";
        List<Issue> result = searchService.search(query);

        assertFalse(result.isEmpty());
        for(Issue issue: result) {
            assertTrue(issue.getIssueId().equals(1L) || issue.getIssueId().equals(4L));
        }
    }

    @Test
    public void testFulltext() {
        String query = "text ~ \"unit tests infinispan\"";
        List<Issue> result = searchService.search(query);

        assertFalse(result.isEmpty());
        assertTrue(result.get(0).getIssueId().equals(1L));
        assertTrue(result.get(1).getIssueId().equals(2L));
    }
}
