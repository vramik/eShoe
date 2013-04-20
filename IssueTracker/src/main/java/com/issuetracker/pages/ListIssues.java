/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.issuetracker.pages;

import com.issuetracker.dao.api.IssueDao;
import javax.inject.Inject;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import com.issuetracker.model.Issue;

/**
 *
 * @author mgottval
 */
public class ListIssues extends WebPage {

    @Inject
    private IssueDao issueDao;

//    public ListIssues() {
//        add(new Label("welcomeMessage", "IShoe"));
//        //add(new ListView<Issue>("contacts", contactDao.getIssuesBy...()) {
//
//            // Populate the table of issues
//            @Override
//            protected void populateItem(final ListItem<Issue> item) {
//                Issue issue = item.getModelObject();
//                item.add(new Label("name", issue.getName()));
//                item.add(new Label("email", issue.getEmail()));
//                item.add(new Label("lastName", issue.getLastName()));
//                item.add(new Link<Issue>("delete", item.getModel()) {
//
//                    @Override
//                    public void onClick() {
//                        contactDao.remove(item.getModelObject());
//                        setResponsePage(ListContacts.class);
//                    }
//                });
//            }
//        };
    public ListIssues() {

        // Add the dynamic welcome message, specified in web.xml
    //    add(new Label("welcomeMessage", welcome));
        add(new ListView<Issue>("contacts", issueDao.getIssues()) {
            // Populate the table of contacts
            @Override
            protected void populateItem(final ListItem<Issue> item) {
//                Issue issue = item.getModelObject();
//                item.add(new Label("name", issue.getName()));
//                item.add(new Label("email", issue.getDescription()));
//                item.add(new Link<Issue>("delete", item.getModel()) {
//                    @Override
//                    public void onClick() {
//                        issueDao.removeIssue(item.getModelObject());
//                        setResponsePage(ListIssues.class);
//                    }
//                });
            }
        });
    }
}
