/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.issuetracker.pages.layout;

import com.issuetracker.model.User;
import com.issuetracker.pages.createIssue.CreateIssue;
import com.issuetracker.pages.CreateIssueType;
import com.issuetracker.pages.CreateProject;
import com.issuetracker.pages.CreateStatuses;
import com.issuetracker.pages.CreateWorkflow;
import com.issuetracker.pages.ListProjects;
import com.issuetracker.pages.SearchIssues;
import com.issuetracker.web.security.TrackerAuthSession;
import java.util.ArrayList;
import java.util.List;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxFallbackLink;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.PropertyListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

public class HeaderPanel extends Panel {

    private String selected = "Create Project";
    private TrackerAuthSession sess;
    private Label usernameLabel;
    private String loginContent;
    private IModel usernamePropertyModel;
    private User user;
    private AjaxLink<String> signOutLink;

    public HeaderPanel(String id) {
        super(id);
        user = new User();
        loginContent = "";
        Label label = new Label("name", "Issue Tracking system");
        label.add(new AttributeModifier("style", "color:red;font-weight:bold"));
        add(label);

        sess = (TrackerAuthSession) getSession();
        usernamePropertyModel = new CompoundPropertyModel<String>(loginContent) {
            @Override
            public String getObject() {
                if (sess.isSignedIn()) {
                    loginContent = sess.getUser().getName();
                    return sess.getUser().getName();
                } else {
                    loginContent = "";
                    return "";
                }
            }
        };
        usernameLabel = new Label("userName", usernamePropertyModel);
        usernameLabel.setOutputMarkupId(true);
        usernameLabel.setVisible(sess.isSignedIn());
        add(usernameLabel);

        signOutLink = new AjaxLink<String>("signOutLink") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                sess.signOut();
                target.add(usernameLabel);
            }
        };
        signOutLink.setVisible(sess.isSignedIn());
        signOutLink.add(new AjaxEventBehavior("click") {
            @Override
            protected void onEvent(AjaxRequestTarget target) {
                sess.signOut();
                target.add(usernameLabel);
                signOutLink.setVisible(false);
                target.add(signOutLink);
            }
        });
        add(signOutLink);

        List<String> optsProject = new ArrayList<String>();
        optsProject.add("Create Project");
        optsProject.add("Insert Types of Project");
        optsProject.add("View all projects");

        List<String> optsIssue = new ArrayList<String>();
        optsIssue.add("Create Issue");
        optsIssue.add("Search Issue");

        List<String> optsWorkflow = new ArrayList<String>();
        optsWorkflow.add("Create Statuses");
        optsWorkflow.add("Create Workflow");

        add(new PropertyListView<String>("projectTasks", optsProject) {
            @Override
            public void populateItem(final ListItem<String> listItem) {
                final String stringLink = listItem.getModelObject();
                Link nameLink = new Link<String>("actionLink", listItem.getModel()) {
                    @Override
                    public void onClick() {
                        selected = stringLink;
                        if (selected.equals("Create Project")) {
                            setResponsePage(CreateProject.class);
                        }
                        if (selected.equals("Insert Types of Project")) {
                            setResponsePage(CreateIssueType.class);
                        }
                        if (selected.equals("View all projects")) {
                            setResponsePage(ListProjects.class);
                        }
                    }
                };
                nameLink.add(new Label("name", stringLink));
                listItem.add(nameLink);

            }
        });

        add(new PropertyListView<String>("issueTasks", optsIssue) {
            @Override
            public void populateItem(final ListItem<String> listItem) {
                final String stringLink = listItem.getModelObject();
                Link nameLink = new Link<String>("actionLink", listItem.getModel()) {
                    @Override
                    public void onClick() {
                        selected = stringLink;
                        if (selected.equals("Create Issue")) {
                            setResponsePage(CreateIssue.class);
                        }
                        if (selected.equals("Search Issue")) {
                            setResponsePage(SearchIssues.class);
                        }
                    }
                };
                nameLink.add(new Label("name", stringLink));
                listItem.add(nameLink);

            }
        });

        add(new PropertyListView<String>("workflowTasks", optsWorkflow) {
            @Override
            public void populateItem(final ListItem<String> listItem) {
                final String stringLink = listItem.getModelObject();
                Link nameLink = new Link<String>("actionLink", listItem.getModel()) {
                    @Override
                    public void onClick() {
                        selected = stringLink;
                        if (selected.equals("Create Statuses")) {
                            setResponsePage(CreateStatuses.class);
                        }
                        if (selected.equals("Create Workflow")) {
                            setResponsePage(CreateWorkflow.class);
                        }
                    }
                };
                nameLink.add(new Label("name", stringLink));
                listItem.add(nameLink);

            }
        });

    }

    public String getSelected() {
        return selected;
    }

    public void setSelected(String selected) {
        this.selected = selected;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getLoginContent() {
        return loginContent;
    }

    public void setLoginContent(String loginContent) {
        this.loginContent = loginContent;
    }
}
