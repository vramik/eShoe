/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.issuetracker.pages.component.customFieldIssueValue;

import com.issuetracker.model.CustomFieldIssueValue;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

import java.util.List;

/**
 *
 * @author mgottval
 */
public class CustomFieldIssueValueListView<T extends CustomFieldIssueValue> extends Panel {
    
    private ListView listViewCustomFieldsIssueValue;

    public CustomFieldIssueValueListView(String id, IModel<List<CustomFieldIssueValue>> customFieldIssueValueModel) {
        super(id);
        listViewCustomFieldsIssueValue = new ListView<CustomFieldIssueValue>("customFieldIssueValues", customFieldIssueValueModel) {
            @Override
            protected void populateItem(final ListItem<CustomFieldIssueValue> item) {
                final CustomFieldIssueValue customFieldIssueValue = item.getModelObject();
                item.add(new Label("name", customFieldIssueValue.getCustomField().getCfName()));
                item.add(new Label("value", customFieldIssueValue.getValue()));
            }
        };
        add(listViewCustomFieldsIssueValue);
    }

}
