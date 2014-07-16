package com.issuetracker.pages.layout;

import com.issuetracker.pages.*;
import com.issuetracker.pages.createIssue.CreateIssue;
import com.issuetracker.pages.fulltext.FulltextSearch;
import static com.issuetracker.web.security.KeycloakAuthSession.*;
import java.util.ArrayList;
import java.util.List;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.PropertyListView;
import org.apache.wicket.markup.html.pages.RedirectPage;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.keycloak.ServiceUrlConstants;
import org.keycloak.representations.IDToken;
import org.keycloak.util.KeycloakUriBuilder;

public class HeaderPanel extends Panel {

    private String selected = "Create Project";
//    private TrackerAuthSession sess;
    private Label usernameLabel;
    private String loginContent;
    private IModel usernamePropertyModel;
//    private User user;
    private IDToken idToken;
//    private AjaxLink<String> signOutLink;
    private final Link<String> signOutLink;
    private final Link<String> signInLink;

    public HeaderPanel(String id) {
        super(id);
//        user = new User();
        idToken = getIDToken(getWebRequest());
        loginContent = "";
        Label label = new Label("name", "Issue Tracking system");
        label.add(new AttributeModifier("style", "color:red;font-weight:bold"));
        add(label);
        
        signInLink = new Link<String>("signInLink") {
            
            @Override
            public void onClick() {
                setResponsePage(Login.class);
            }
        };
        signInLink.setVisible(!isSignedIn(getWebRequest()));
        add(signInLink);

//        sess = (TrackerAuthSession) getSession();
        usernamePropertyModel = new CompoundPropertyModel<String>(loginContent) {
            @Override
            public String getObject() {
                if (isSignedIn(getWebRequest())) {
                    return "6 - idToken.getName(): " + idToken.getName() + ", idToken.getPreferredUsername(): " + idToken.getPreferredUsername();
                } else {
                    return "USER is not signed in";
                }
//                if (sess.isSignedIn()) {
//                    loginContent = sess.getUser().getName();
//                    return sess.getUser().getName();
//                } else {
//                    loginContent = "";
//                    return "";
//                }
            }
        };
        usernameLabel = new Label("userName", usernamePropertyModel);
        usernameLabel.setOutputMarkupId(true);
        usernameLabel.setVisible(isSignedIn(getWebRequest()));
        add(usernameLabel);

        signOutLink = new Link<String>("signOutLink") {
            
            @Override
            public void onClick() {
                String logoutUri = KeycloakUriBuilder.fromUri("http://localhost:8080/auth").path(ServiceUrlConstants.TOKEN_SERVICE_LOGOUT_PATH)
                    .queryParam("redirect_uri", "http://localhost:8080/IssueTracker").build("demo").toString();
                
                setResponsePage(new RedirectPage(logoutUri));
            }
        };
        signOutLink.setVisible(isSignedIn(getWebRequest()));
        add(signOutLink);
        
        add(new Label("projectsLabel", "Projects"));
        add(new Label("issuesLabel", "Issues"));
        
               
        List<String> optsProject = new ArrayList<String>();
        if (isSignedIn(getWebRequest())) {
            optsProject.add("Create Project");
        }
        optsProject.add("View all projects");

        List<String> optsIssue = new ArrayList<String>();
        if (isSignedIn(getWebRequest())) {
            optsIssue.add("Create Issue");
            optsIssue.add("Insert Types of Issue");
        }
        optsIssue.add("List Issues");
        optsIssue.add("Issue Fulltext Search");

        List<String> optsWorkflow = new ArrayList<String>();
        if (isSignedIn(getWebRequest())) {
            optsWorkflow.add("Create Statuses");
            optsWorkflow.add("Create Workflow");
        }
            
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
                        if (selected.equals("List Issues")) {
                            setResponsePage(SearchIssues.class);
                        }

                        if (selected.equals("Issue Fulltext Search")) {
                            setResponsePage(FulltextSearch.class);
                        }
                        if (selected.equals("Insert Types of Issue")) {
                            setResponsePage(CreateIssueType.class);
                        }
                    }
                };
                nameLink.add(new Label("name", stringLink));
                listItem.add(nameLink);

            }
        });

        WebMarkupContainer webMarkupContainer = new WebMarkupContainer("workflowUl");
        webMarkupContainer.add(new Label("workflowLabel", "Workflow"));
        webMarkupContainer.add(new PropertyListView<String>("workflowTasks", optsWorkflow) {
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
        webMarkupContainer.setVisible(isSignedIn(getWebRequest()));
        add(webMarkupContainer);

        add(new Link("importerLink") {
            @Override
            public void onClick() {
                setResponsePage(Importer.class);
            }
        });
    }

    public String getSelected() {
        return selected;
    }

    public void setSelected(String selected) {
        this.selected = selected;
    }

//    public User getUser() {
//        return user;
//    }
//
//    public void setUser(User user) {
//        this.user = user;
//    }

    public String getLoginContent() {
        return loginContent;
    }

    public void setLoginContent(String loginContent) {
        this.loginContent = loginContent;
    }
}
