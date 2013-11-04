package com.issuetracker.pages.layout;

import com.issuetracker.dao.api.IssueDao;
import com.issuetracker.model.Issue;
import com.issuetracker.model.User;
import com.issuetracker.pages.CreateProject;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Inject;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
/**
 *
 * @author mgottval
 */
public class ModalPanel1 extends Panel{
    private ListView<User> watchersListView;
    private List<User> watchersList;
    
    @Inject
    private IssueDao issueDao;
    
    private Issue issue;

    /**
     * @param id
     */
    public ModalPanel1(String id, IModel<List<User>> watchersModel)
    {
        super(id); 
        this.watchersList = watchersModel.getObject();

        if(watchersList == null) {
            watchersList = new ArrayList<User>();
        }

        add(new Label("name", "LABEL"));
        String s = "";
        for (User user : watchersList) {
            s = s + user.getUserId();
        }
        Logger.getLogger(CreateProject.class.getName()).log(Level.SEVERE, s);
//        {
//            /**
//             * @see org.apache.wicket.extensions.yui.calendar.DateTimeField#configure(java.util.Map)
//             */
//            @Override
//            protected void configure(Map<String, Object> widgetProperties)
//            {
//                super.configure(widgetProperties);
//                // IE 6 breaks layout with iframe - is that a YUI bug?
//                widgetProperties.put("iframe", false);
//            }
//        });
        watchersListView = new ListView<User>("watchersListView", new PropertyModel<List<User>>(this, "watchersList")) {
            @Override
            protected void populateItem(ListItem<User> item) {
                final User user = item.getModelObject();
                item.add(new Label("name", user.getUserId()));
            }
        };
//        watchersListView.setOutputMarkupId(true);
        add(watchersListView);
    }

    
    
    public List<User> getWatchersList() {
        return watchersList;
    }

    public void setWatchersList(List<User> watchersList) {
        this.watchersList = watchersList;
    }
    
    
    
}
