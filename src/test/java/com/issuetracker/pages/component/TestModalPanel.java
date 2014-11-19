/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.issuetracker.pages.component;

import com.issuetracker.pages.layout.ModalPanel1;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
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
public class TestModalPanel {

    private WicketTester tester = null;
    private List<String> watchersList;

    @Before
    public void setUp() throws Exception {
        tester = new WicketTester();

    }

    @Test
    public void testModalPanelLabel() {
        final String user1 = "Monika Gottvaldova";
        final String user2 = "Lenka Gottvaldova";
        IModel<List<String>> usersModel = new AbstractReadOnlyModel<List<String>>() {
            @Override
            public List<String> getObject() {
                List<String> models = new ArrayList<String>();
                models.add(user1);
                models.add(user2);

                return models;
            }
        };


        ModalPanel1 modal = new ModalPanel1("id", usersModel);
        Assert.assertNotNull(modal.get("name"));
        Label l = (Label) modal.get("name");
        String s = (String) l.getDefaultModelObject();
        Assert.assertEquals("LABEL", s);


    }

    @Test
    public void testModalPanelLIstView() {
        final String user1 = "Monika Gottvaldova";
        final String user2 = "Lenka Gottvaldova";
        IModel<List<String>> usersModel = new AbstractReadOnlyModel<List<String>>() {
            @Override
            public List<String> getObject() {
                List<String> models = new ArrayList<String>();
                models.add(user1);
                models.add(user2);

                return models;
            }
        };

        this.watchersList = usersModel.getObject();

        ModalPanel1 modal = new ModalPanel1("id", usersModel);
        IModel imodel = new PropertyModel<List<String>>(this, "watchersList");
        List<String> userslist = (List<String>) imodel.getObject();
        ListView listView = (ListView) modal.get("watchersListView");
        IModel model = (IModel) listView.getDefaultModel();
        Assert.assertEquals(userslist, (List<String>) model.getObject());
    }
}
