/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.issuetracker.pages.component;

import com.issuetracker.model.Project;
import com.issuetracker.pages.component.project.ProjectListView;
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
public class TestProjectListView {

    private WicketTester tester = null;

    @Before
    public void setUp() throws Exception {
        tester = new WicketTester();

    }

    @Test
    public void testProjectLIstView() {
        final Project project = new Project();
        project.setName("p1");
        final Project project2 = new Project();
        project2.setName("p2");
        IModel<List<Project>> projectModel = new AbstractReadOnlyModel<List<Project>>() {
            @Override
            public List<Project> getObject() {
                List<Project> models = new ArrayList<Project>();
                models.add(project);
                models.add(project2);

                return models;
            }
        };
        ProjectListView projectListView = new ProjectListView("id", projectModel);
        List<Project> versionList = (List<Project>) projectListView.get("projectList").getDefaultModel().getObject();
        Assert.assertEquals(projectModel.getObject(), versionList);

    }

//    @Test
//    public void testProjectLIstViewLabels() {
//        final Project project = new Project();
//        project.setName("p1");
//        final Project project2 = new Project();
//        project2.setName("p2");
//        IModel<List<Project>> versionModel = new AbstractReadOnlyModel<List<Project>>() {
//            @Override
//            public List<Project> getObject() {
//                List<Project> models = new ArrayList<Project>();
//                models.add(project);
//                models.add(project2);
//                return models;
//            }
//        };
//        ProjectListView projectListView = new ProjectListView("id", versionModel);
//
//        tester.startComponent(projectListView);
////        Assert.assertEquals(((Label) projectListView.get("projectList:0:name")).getDefaultModel().getObject().toString(), "p1");
////        Assert.assertEquals(((Label) projectListView.get("projectList:1:name")).getDefaultModel().getObject().toString(), "p2");
//    }
}
