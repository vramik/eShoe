/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.issuetracker.pages.component;

import com.issuetracker.model.Issue;
import com.issuetracker.model.IssuesRelationship;
import com.issuetracker.pages.component.issue.IssueRelationsListView;

import java.util.ArrayList;
import java.util.List;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author mgottval
 */
public class TestIssueRelations {
     private WicketTester tester = null;
    private String value1;

    @Before
    public void setUp() throws Exception {
        tester = new WicketTester();

    }

    @Test
    public void testIssueRelationLIstViewModel() {
        Issue i1To = new Issue();
        Issue i1From = new Issue();
         Issue i2To = new Issue();
         Issue i2From = new Issue();

        final IssuesRelationship rel1 = new IssuesRelationship();
        rel1.setIsRelatedIssue(i1From);
        rel1.setRelatesToIssue(i1To);
        rel1.setRelationshipType(IssuesRelationship.RelationshipType.RELATES_TO);
        final IssuesRelationship rel2 = new IssuesRelationship();
        rel2.setIsRelatedIssue(i2From);
        rel2.setRelatesToIssue(i2To);
        rel2.setRelationshipType(IssuesRelationship.RelationshipType.DEPENDS_ON);
        
        IModel<List<IssuesRelationship>> relationModel = new AbstractReadOnlyModel<List<IssuesRelationship>>() {
            @Override
            public List<IssuesRelationship> getObject() {
                List<IssuesRelationship> models = new ArrayList<IssuesRelationship>();
                models.add(rel1);
models.add(rel2);
                return models;
            }
        };
        IssueRelationsListView issueRelLIstView = new IssueRelationsListView("id", relationModel);
        ListView listView = (ListView) issueRelLIstView.get("issues");
        IModel model = listView.getDefaultModel();
        Assert.assertNotNull(model);
        Assert.assertEquals(relationModel.getObject(), model.getObject());
    }
    
     @Test
    public void testIssueRelationLIstViewLabelsRelations() {
         Issue i1To = new Issue();
        Issue i1From = new Issue();
         Issue i2To = new Issue();
         Issue i2From = new Issue();

        final IssuesRelationship rel1 = new IssuesRelationship();
        rel1.setIsRelatedIssue(i1From);
        rel1.setRelatesToIssue(i1To);
        rel1.setRelationshipType(IssuesRelationship.RelationshipType.RELATES_TO);
        final IssuesRelationship rel2 = new IssuesRelationship();
        rel2.setIsRelatedIssue(i2From);
        rel2.setRelatesToIssue(i2To);
        rel2.setRelationshipType(IssuesRelationship.RelationshipType.DEPENDS_ON);
        
        IModel<List<IssuesRelationship>> relationModel = new AbstractReadOnlyModel<List<IssuesRelationship>>() {
            @Override
            public List<IssuesRelationship> getObject() {
                List<IssuesRelationship> models = new ArrayList<IssuesRelationship>();
                models.add(rel1);
models.add(rel2);
                return models;
            }
        };
        IssueRelationsListView issueRelLIstView = new IssueRelationsListView("id", relationModel);
        tester.startComponent(issueRelLIstView);

        Assert.assertEquals(((Label) issueRelLIstView.get("issues:0:relationName")).getDefaultModel().getObject().toString(), "RELATES_TO");
        Assert.assertEquals(((Label) issueRelLIstView.get("issues:1:relationName")).getDefaultModel().getObject().toString(), "DEPENDS_ON");
    }
     
     @Test
    public void testIssueRelationLIstViewLabelsLinkName() {
                 Issue i1To = new Issue();
                 i1To.setName("i1");
        Issue i1From = new Issue();
         Issue i2To = new Issue();
         i2To.setName("i2");
         Issue i2From = new Issue();

        final IssuesRelationship rel1 = new IssuesRelationship();
        rel1.setIsRelatedIssue(i1From);
        rel1.setRelatesToIssue(i1To);
        rel1.setRelationshipType(IssuesRelationship.RelationshipType.RELATES_TO);
        final IssuesRelationship rel2 = new IssuesRelationship();
        rel2.setIsRelatedIssue(i2From);
        rel2.setRelatesToIssue(i2To);
        rel2.setRelationshipType(IssuesRelationship.RelationshipType.DEPENDS_ON);
        
        IModel<List<IssuesRelationship>> relationModel = new AbstractReadOnlyModel<List<IssuesRelationship>>() {
            @Override
            public List<IssuesRelationship> getObject() {
                List<IssuesRelationship> models = new ArrayList<IssuesRelationship>();
                models.add(rel1);
models.add(rel2);
                return models;
            }
        };
        IssueRelationsListView issueRelLIstView = new IssueRelationsListView("id", relationModel);
        tester.startComponent(issueRelLIstView);

        Assert.assertEquals(((Label) issueRelLIstView.get("issues:0:issueRelLink:issueName")).getDefaultModel().getObject().toString(), "i1");
        Assert.assertEquals(((Label) issueRelLIstView.get("issues:1:issueRelLink:issueName")).getDefaultModel().getObject().toString(), "i2");
    }
}
