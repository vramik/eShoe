/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.issuetracker.pages.component;

import com.issuetracker.model.ProjectVersion;
import com.issuetracker.model.Workflow;
import com.issuetracker.pages.component.version.VersionListView;
import com.issuetracker.pages.component.workflow.WorkflowListView;
import java.util.ArrayList;
import java.util.List;
import org.apache.wicket.markup.html.basic.Label;
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
public class TestVersionListView {

    private WicketTester tester = null;

    @Before
    public void setUp() throws Exception {
        tester = new WicketTester();

    }

    @Test
    public void testWorkflowLIstView() {
        final ProjectVersion projectVersion = new ProjectVersion();
        projectVersion.setName("1");
        final ProjectVersion projectVersion2 = new ProjectVersion();
        projectVersion2.setName("2");
        IModel<List<ProjectVersion>> versionModel = new AbstractReadOnlyModel<List<ProjectVersion>>() {
            @Override
            public List<ProjectVersion> getObject() {
                List<ProjectVersion> models = new ArrayList<ProjectVersion>();
                models.add(projectVersion);


                return models;
            }
        };
        VersionListView versionListView = new VersionListView("id", versionModel, null);
        List<ProjectVersion> versionList = (List<ProjectVersion>) versionListView.get("versionsList").getDefaultModel().getObject();
        Assert.assertEquals(versionModel.getObject(), versionList);

    }

    @Test
    public void testWorkflowLIstViewLabels() {
        final ProjectVersion projectVersion = new ProjectVersion();
        projectVersion.setName("1");
        final ProjectVersion projectVersion2 = new ProjectVersion();
        projectVersion2.setName("2");
        IModel<List<ProjectVersion>> versionModel = new AbstractReadOnlyModel<List<ProjectVersion>>() {
            @Override
            public List<ProjectVersion> getObject() {
                List<ProjectVersion> models = new ArrayList<ProjectVersion>();
                models.add(projectVersion);
                models.add(projectVersion2);
                return models;
            }
        };
        VersionListView versionListView = new VersionListView("id", versionModel, null);

        tester.startComponent(versionListView);
        Assert.assertEquals(((Label) versionListView.get("versionsList:0:name")).getDefaultModel().getObject().toString(), "1");
        Assert.assertEquals(((Label) versionListView.get("versionsList:1:name")).getDefaultModel().getObject().toString(), "2");
    }
}
