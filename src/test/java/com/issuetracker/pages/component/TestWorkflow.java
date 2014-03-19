/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.issuetracker.pages.component;

import com.issuetracker.model.Workflow;
import com.issuetracker.pages.component.workflow.WorkflowListView;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author mgottval
 */
public class TestWorkflow {

    private WicketTester tester = null;
    private Workflow wf1;

    @Before
    public void setUp() throws Exception {
        tester = new WicketTester();

    }

    @Test
    public void testWorkflowLIstView() {
        final Workflow wf1 = new Workflow();
        wf1.setName("workflow1");
        final Workflow wf2 = new Workflow();
        wf2.setName("wf2");
        IModel<List<Workflow>> workflowModel = new AbstractReadOnlyModel<List<Workflow>>() {
            @Override
            public List<Workflow> getObject() {
                List<Workflow> models = new ArrayList<Workflow>();
                models.add(wf1);
                models.add(wf2);

                return models;
            }
        };
        WorkflowListView wflistView = new WorkflowListView("id", workflowModel);
        List<Workflow> wfList  = (List<Workflow>) wflistView.get("workflowList").getDefaultModel().getObject();        
        Assert.assertEquals(workflowModel.getObject(), wfList);
                  
    }
    
    @Test
    public void testWorkflowLIstViewLabels() {
        final Workflow wf1 = new Workflow();
        wf1.setName("workflow1");
        final Workflow wf2 = new Workflow();
        wf2.setName("wf2");
        IModel<List<Workflow>> workflowModel = new AbstractReadOnlyModel<List<Workflow>>() {
            @Override
            public List<Workflow> getObject() {
                List<Workflow> models = new ArrayList<Workflow>();
                models.add(wf1);
                models.add(wf2);

                return models;
            }
        };
        WorkflowListView wflistView = new WorkflowListView("id", workflowModel);              
        tester.startComponent(wflistView);
        Assert.assertEquals(((Label) wflistView.get("workflowList:0:showWorkflow:name")).getDefaultModel().getObject().toString(), "workflow1");
        Assert.assertEquals(((Label) wflistView.get("workflowList:1:showWorkflow:name")).getDefaultModel().getObject().toString(), "wf2");
    }
}
