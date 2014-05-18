/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.issuetracker.pages.component.customField;

import com.issuetracker.model.CustomField;
import com.issuetracker.service.api.CustomFieldService;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

import javax.inject.Inject;
import java.util.List;
/**
 *
 * @author mgottval
 */
public class CustomFieldListView<T extends CustomField> extends Panel {

    @Inject
    private CustomFieldService customFieldService;
    private ListView listViewCustomFields;

    public CustomFieldListView(String id, IModel<List<CustomField>> customFieldModel) {
        super(id);
        listViewCustomFields = new ListView<CustomField>("customFields", customFieldModel) {
            @Override
            protected void populateItem(final ListItem<CustomField> item) {
                final CustomField customField = item.getModelObject();
                item.add(new Label("name", customField.getCfName()));

            }
        };
        add(listViewCustomFields);
    }

  
}
