/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.issuetracker.pages.component;

import com.issuetracker.model.CustomField;
import com.issuetracker.model.CustomFieldIssueValue;
import com.issuetracker.pages.component.customFieldIssueValue.CustomFieldIssueValueListView;
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
public class TestCustomFieldsIssueValue {
    private WicketTester tester = null;
    private String value1;

    @Before
    public void setUp() throws Exception {
        tester = new WicketTester();

    }

    @Test
    public void testCustomFieldsLIstViewModel() {
        CustomField cf = new CustomField();
        cf.setCfName("field1");
        CustomField cf2 = new CustomField();
        cf2.setCfName("field2");

        final CustomFieldIssueValue cfiv = new CustomFieldIssueValue();
        cfiv.setCustomField(cf);
        cfiv.setValue("field one");
        final CustomFieldIssueValue cfiv2 = new CustomFieldIssueValue();
        cfiv2.setCustomField(cf2);
        cfiv2.setValue("field two");

        IModel<List<CustomFieldIssueValue>> customFieldsModel = new AbstractReadOnlyModel<List<CustomFieldIssueValue>>() {
            @Override
            public List<CustomFieldIssueValue> getObject() {
                List<CustomFieldIssueValue> models = new ArrayList<CustomFieldIssueValue>();
                models.add(cfiv);
                models.add(cfiv2);

                return models;
            }
        };
        CustomFieldIssueValueListView cfLIstView = new CustomFieldIssueValueListView("id", customFieldsModel);
        ListView listView = (ListView) cfLIstView.get("customFieldIssueValues");
        IModel model = listView.getDefaultModel();
        Assert.assertNotNull(model);
        Assert.assertEquals(customFieldsModel.getObject(), model.getObject());
    }
    
     @Test
    public void testCustomFieldsLIstViewLabelsNames() {
        CustomField cf = new CustomField();
        cf.setCfName("field1");
        CustomField cf2 = new CustomField();
        cf2.setCfName("field2");

        final CustomFieldIssueValue cfiv = new CustomFieldIssueValue();
        cfiv.setCustomField(cf);
        cfiv.setValue("field one");
        final CustomFieldIssueValue cfiv2 = new CustomFieldIssueValue();
        cfiv2.setCustomField(cf2);
        cfiv2.setValue("field two");

        IModel<List<CustomFieldIssueValue>> customFieldsModel = new AbstractReadOnlyModel<List<CustomFieldIssueValue>>() {
            @Override
            public List<CustomFieldIssueValue> getObject() {
                List<CustomFieldIssueValue> models = new ArrayList<CustomFieldIssueValue>();
                models.add(cfiv);
                models.add(cfiv2);

                return models;
            }
        };
        CustomFieldIssueValueListView cfLIstView = new CustomFieldIssueValueListView("id", customFieldsModel);
        tester.startComponent(cfLIstView);

        Assert.assertEquals(((Label) cfLIstView.get("customFieldIssueValues:0:name")).getDefaultModel().getObject().toString(), "field1");
        Assert.assertEquals(((Label) cfLIstView.get("customFieldIssueValues:1:name")).getDefaultModel().getObject().toString(), "field2");
    }
     
     @Test
    public void testCustomFieldsLIstViewLabelsValues() {
        CustomField cf = new CustomField();
        cf.setCfName("field1");
        CustomField cf2 = new CustomField();
        cf2.setCfName("field2");

        final CustomFieldIssueValue cfiv = new CustomFieldIssueValue();
        cfiv.setCustomField(cf);
        cfiv.setValue("field one");
        final CustomFieldIssueValue cfiv2 = new CustomFieldIssueValue();
        cfiv2.setCustomField(cf2);
        cfiv2.setValue("field two");

        IModel<List<CustomFieldIssueValue>> customFieldsModel = new AbstractReadOnlyModel<List<CustomFieldIssueValue>>() {
            @Override
            public List<CustomFieldIssueValue> getObject() {
                List<CustomFieldIssueValue> models = new ArrayList<CustomFieldIssueValue>();
                models.add(cfiv);
                models.add(cfiv2);

                return models;
            }
        };
        CustomFieldIssueValueListView cfLIstView = new CustomFieldIssueValueListView("id", customFieldsModel);
        tester.startComponent(cfLIstView);

        Assert.assertEquals(((Label) cfLIstView.get("customFieldIssueValues:0:value")).getDefaultModel().getObject().toString(), "field one");
        Assert.assertEquals(((Label) cfLIstView.get("customFieldIssueValues:1:value")).getDefaultModel().getObject().toString(), "field two");
    }
}
