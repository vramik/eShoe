/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.issuetracker.pages.component;

import com.issuetracker.model.Component;
import com.issuetracker.model.Project;
import com.issuetracker.pages.component.component.ComponentListView;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListView;
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
public class TestComponentListView {
      private WicketTester tester = null;
    private String value1;

    @Before
    public void setUp() throws Exception {
        tester = new WicketTester();

    }

    @Test
    public void testComponentLIstViewModel() {
        final Component component1 = new Component();
         final Component component2 = new Component();
        
        IModel<List<Component>> componentsModel = new AbstractReadOnlyModel<List<Component>>() {
            @Override
            public List<Component> getObject() {
                List<Component> models = new ArrayList<Component>();
                models.add(component1);
models.add(component2);
                return models;
            }
        };
        IModel<Project> projectModel = new AbstractReadOnlyModel<Project>() {
            @Override
            public Project getObject() {
                Project models = new Project();
                
                return models;
            }
        };
        
        ComponentListView componentListView = new ComponentListView("id", componentsModel, projectModel);
        ListView listView = (ListView) componentListView.get("componentsList");
        IModel model = listView.getDefaultModel();
        Assert.assertNotNull(model);
        Assert.assertEquals(componentsModel.getObject(), model.getObject());
    }
    
     @Test
    public void testComponentLIstViewModelLabels() {
        final Component component1 = new Component();
        component1.setName("component one");
         final Component component2 = new Component();
        component2.setName("component two");
        IModel<List<Component>> componentsModel = new AbstractReadOnlyModel<List<Component>>() {
            @Override
            public List<Component> getObject() {
                List<Component> models = new ArrayList<Component>();
                models.add(component1);
models.add(component2);
                return models;
            }
        };
        IModel<Project> projectModel = new AbstractReadOnlyModel<Project>() {
            @Override
            public Project getObject() {
                Project models = new Project();
                
                return models;
            }
        };
        
        ComponentListView componentListView = new ComponentListView("id", componentsModel, projectModel);
        tester.startComponent(componentListView);

        Assert.assertEquals(((Label) componentListView.get("componentsList:0:name")).getDefaultModel().getObject().toString(), "component one");
        Assert.assertEquals(((Label) componentListView.get("componentsList:1:name")).getDefaultModel().getObject().toString(), "component two");
    }
     
     @Test
    public void testComponentLIstViewNullProject() {
        final Component component1 = new Component();
        component1.setName("component one");
         final Component component2 = new Component();
        component2.setName("component two");
        IModel<List<Component>> componentsModel = new AbstractReadOnlyModel<List<Component>>() {
            @Override
            public List<Component> getObject() {
                List<Component> models = new ArrayList<Component>();
                models.add(component1);
models.add(component2);
                return models;
            }
        };
        
        ComponentListView componentListView = new ComponentListView("id", componentsModel, null);
        tester.startComponent(componentListView);

        Assert.assertEquals(((Label) componentListView.get("componentsList:0:name")).getDefaultModel().getObject().toString(), "component one");
        Assert.assertEquals(((Label) componentListView.get("componentsList:1:name")).getDefaultModel().getObject().toString(), "component two");
    }
}
