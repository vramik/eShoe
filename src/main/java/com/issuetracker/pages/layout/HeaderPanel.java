package com.issuetracker.pages.layout;

import com.issuetracker.pages.issue.SearchIssues;
import com.issuetracker.pages.workflow.CreateWorkflow;
import com.issuetracker.pages.project.ListProjects;
import com.issuetracker.pages.project.CreateProject;
import com.issuetracker.pages.status.CreateStatus;
import com.issuetracker.pages.issuetype.CreateIssueType;
import com.issuetracker.pages.*;
import com.issuetracker.pages.issue.CreateIssue;
import com.issuetracker.pages.fulltext.FulltextSearch;
import static com.issuetracker.web.Constants.*;
import static com.issuetracker.web.security.KeycloakAuthSession.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.PropertyListView;
import org.apache.wicket.markup.html.pages.RedirectPage;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.keycloak.ServiceUrlConstants;
import org.keycloak.representations.AccessToken;
import org.keycloak.representations.IDToken;
import org.keycloak.util.KeycloakUriBuilder;

public class HeaderPanel extends Panel {

    private String selected;
    private final Label usernameLabel;
    private IDToken idToken;
    
    private final WebMarkupContainer workflowUl;
    private final WebMarkupContainer importUl;
    private final WebMarkupContainer permissionsUl;
    
    private final Link<String> signOutLink;
    private final Link<String> signInLink;
    private final List<String> optsProject;
    private final List<String> optsIssue;
    public final FeedbackPanel feedbackPanel = new FeedbackPanel("feedbackPanel");

    //TODO not signedIn but roles
    @Override
    public void onConfigure() {
        super.onConfigure();
        
        signInLink.setVisible(!isSignedIn());
        usernameLabel.setVisible(isSignedIn());
        signOutLink.setVisible(isSignedIn());
        workflowUl.setVisible(isSignedIn());
        importUl.setVisible(isSignedIn());
        permissionsUl.setVisible(isSignedIn());
        System.out.println("HeaderPanel: TODO");
    }
    
    //TODO not signed in but roles
    @Override
    public void onBeforeRender() {
        if (!isSignedIn()) {
            optsProject.remove("Create Project");
            optsIssue.remove("Create Issue");
            optsIssue.remove("Insert Types of Issue");
        }
        super.onBeforeRender();
    }
    
    public HeaderPanel(String id) {
        super(id);
        
        add(feedbackPanel);
        
        Label label = new Label("name", "Issue Tracking system");
        label.add(new AttributeModifier("style", "color:red;font-weight:bold"));
        add(label);
        
        signInLink = new Link<String>("signInLink") {
            
            @Override
            public void onClick() {
                PageParameters parameters = getPage().getPageParameters();
                parameters.add("page", getPage().getClass().getName());
                setResponsePage(Login.class, parameters);
            }
        };
        add(signInLink);

        String username = "";
        idToken = getIDToken();
        if (isSignedIn()) {
            username = "idToken.getName(): " + idToken.getName() + ", idToken.getEmail(): " + idToken.getEmail() 
                    + ", rhelm roles: " + getUserRhelmRoles();
            Map<String, AccessToken.Access> resourceAccess = getKeycloakSecurityContext().getToken().getResourceAccess();
            if (resourceAccess != null) {
                if (null != resourceAccess.get(RHELM_NAME)) {
                    username = username.concat(", " + RHELM_NAME + " roles: " + resourceAccess.get(RHELM_NAME).getRoles());
                }
                if (resourceAccess.get("rhelm-management") != null) {
                    username = username.concat(", rhelm-management roles: " + resourceAccess.get("rhelm-management").getRoles());
                }
            }
        }
        
        usernameLabel = new Label("userName", username);
        usernameLabel.setOutputMarkupId(true);
        add(usernameLabel);

        signOutLink = new Link<String>("signOutLink") {
            @Override
            public void onClick() {
                String logoutUri = KeycloakUriBuilder.fromUri(SERVER_URL + "/auth")
                        .path(ServiceUrlConstants.TOKEN_SERVICE_LOGOUT_PATH)
                        .queryParam("redirect_uri", SERVER_URL + CONTEXT_ROOT)
                        .build(RHELM_NAME).toString();
                
                setResponsePage(new RedirectPage(logoutUri));
            }
        };
        add(signOutLink);
        
        add(new Label("projectsLabel", "Projects"));
        add(new Label("issuesLabel", "Issues"));
               
        optsProject = new ArrayList<>();
        optsProject.add("Create Project");
        optsProject.add("View all projects");

        optsIssue = new ArrayList<>();
        optsIssue.add("Create Issue");
        optsIssue.add("Issue Types");
        optsIssue.add("List Issues");
        optsIssue.add("Issue Fulltext Search");

        List<String> optsWorkflow = new ArrayList<>();
        optsWorkflow.add("Statuses");
        optsWorkflow.add("Workflows");
        
        List<String> optsImport = new ArrayList<>();
        optsImport.add("Import");
        
        List<String> optsPermissions = new ArrayList<>();
        optsPermissions.add("Permissions");
            
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
                        if (selected.equals("Issue Types")) {
                            setResponsePage(CreateIssueType.class);
                        }
                    }
                };
                nameLink.add(new Label("name", stringLink));
                listItem.add(nameLink);
            }
        });

        workflowUl = new WebMarkupContainer("workflowUl");
        workflowUl.add(new Label("workflowLabel", "Workflow"));
        workflowUl.add(new PropertyListView<String>("workflowTasks", optsWorkflow) {
            @Override
            public void populateItem(final ListItem<String> listItem) {
                final String stringLink = listItem.getModelObject();
                Link nameLink = new Link<String>("actionLink", listItem.getModel()) {
                    @Override
                    public void onClick() {
                        selected = stringLink;
                        if (selected.equals("Statuses")) {
                            setResponsePage(CreateStatus.class);
                        }
                        if (selected.equals("Workflows")) {
                            setResponsePage(CreateWorkflow.class);
                        }
                    }
                };
                nameLink.add(new Label("name", stringLink));
                listItem.add(nameLink);
            }
        });
        add(workflowUl);

        importUl = new WebMarkupContainer("importUl");
        importUl.add(new Label("importLabel", "Import"));
        importUl.add(new PropertyListView<String>("importTasks", optsImport) {
            @Override
            protected void populateItem(ListItem<String> listItem) {
                Link nameLink = new Link<String>("actionLink", listItem.getModel()) {
                    @Override
                    public void onClick() {
                        setResponsePage(Importer.class);
                    }
                };
                nameLink.add(new Label("name", "Import issues"));
                listItem.add(nameLink);
            }
        });
        add(importUl);
        
        permissionsUl = new WebMarkupContainer("permissionsUl");
        permissionsUl.add(new Label("permissionsLabel", "Permissions"));
        permissionsUl.add(new PropertyListView<String>("permissionsTasks", optsPermissions) {
            @Override
            protected void populateItem(ListItem<String> listItem) {
                Link nameLink = new Link<String>("actionLink", listItem.getModel()) {
                    @Override
                    public void onClick() {
//                        setResponsePage(Permissions.class);
                        setResponsePage(new RedirectPage(SERVER_URL + "/auth/admin/" + RHELM_NAME + "/console"));
                    }
                };
                nameLink.add(new Label("name", "Permissions"));
                nameLink.add(new AttributeModifier("target", "_blank"));
                listItem.add(nameLink);
            }
        });
        add(permissionsUl);
    }

    public String getSelected() {
        return selected;
    }

    public void setSelected(String selected) {
        this.selected = selected;
    }

    public IDToken getIdToken() {
        return idToken;
    }

    public void setIdToken(IDToken idToken) {
        this.idToken = idToken;
    }
}
