/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.issuetracker.pages.component.customField;

import com.issuetracker.dao.api.CustomFieldDao;
import com.issuetracker.model.CustomField;
import java.util.List;
import javax.inject.Inject;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

/**
 *
 * @author mgottval
 */
public class CustomFieldListView<T extends CustomField> extends Panel {

    @Inject
    private CustomFieldDao customFieldDao;
//    private List<CustomField> customFields;
    private ListView listViewCustomFields;

    public CustomFieldListView(String id, IModel<List<CustomField>> customFieldModel) {
        super(id);
        listViewCustomFields = new ListView<CustomField>("customFields", customFieldModel) {
            @Override
            protected void populateItem(final ListItem<CustomField> item) {
                final CustomField customField = item.getModelObject();
                item.add(new Label("name", customField.getCfName()));

                item.add(new Link<CustomField>("delete", item.getModel()) {
                    @Override
                    public void onClick() {
                        customFieldDao.delete(customField);
//                        projects = projectDao.getProjects();
                    }
                });
            }
        };
        add(listViewCustomFields);
    }

//    public List<CustomField> getCustomFields() {
//        return customFields;
//    }
//
//    public void setCustomFields(List<CustomField> customFields) {
//        this.customFields = customFields;
//    }

  
}
