package com.issuetracker.pages.layout;

import com.issuetracker.model.Issue;
import com.issuetracker.service.api.IssueService;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author mgottval
 */
public class ModalPanel1 extends Panel {
    private ListView<String> watchersListView;
    private List<String> watchersList;
    
    @Inject
    private IssueService issueService;
    
    private Issue issue;

    /**
     * @param id
     */
    public ModalPanel1(String id, IModel<List<String>> watchersModel)
    {
        super(id); 
        this.watchersList = watchersModel.getObject();

        if(watchersList == null) {
            watchersList = new ArrayList<String>();
        }

        add(new Label("name", "LABEL"));
        watchersListView = new ListView<String>("watchersListView", new PropertyModel<List<String>>(this, "watchersList")) {
            @Override
            protected void populateItem(ListItem<String> item) {
                final String user = item.getModelObject();
                item.add(new Label("name", user));
            }
        };
//        watchersListView.setOutputMarkupId(true);
        add(watchersListView);
    }

    
    
    public List<String> getWatchersList() {
        return watchersList;
    }

    public void setWatchersList(List<String> watchersList) {
        this.watchersList = watchersList;
    }
    
    
    
}
