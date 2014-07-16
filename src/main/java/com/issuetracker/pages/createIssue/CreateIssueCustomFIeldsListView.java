/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.issuetracker.pages.createIssue;

import com.issuetracker.model.CustomFieldIssueValue;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import java.util.List;

/**
 *
 * @author mgottval
 */
public class CreateIssueCustomFIeldsListView extends Panel {

    private ListView customFormFieldsListView;

    public CreateIssueCustomFIeldsListView(String id, final IModel<List<CustomFieldIssueValue>> customFieldModel) {
        super(id);

        customFormFieldsListView = new ListView<CustomFieldIssueValue>("fields", customFieldModel) {
            @Override
            protected void populateItem(final ListItem<CustomFieldIssueValue> item) {
                CustomFieldIssueValue customFieldIssueValue = item.getModelObject();
                item.add(new Label("name", customFieldIssueValue.getCustomField().getCfName()));
                TextField<String> customTextField = new TextField("customTextField", new Model<String>() {
                    @Override
                    public String getObject() {
                        return item.getModel().getObject().getValue();
                    }
                    @Override
                    public void setObject(final String value) {
                        item.getModel().getObject().setValue(value);
                    }
                });
                item.add(customTextField);
            }
        };
        add(customFormFieldsListView);
    }

}
