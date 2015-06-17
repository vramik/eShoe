package com.issuetracker.pages.layout;

import com.issuetracker.model.TypeId;
import com.issuetracker.pages.Importer;
import com.issuetracker.pages.Login;
import com.issuetracker.pages.issue.SearchIssues;
import com.issuetracker.pages.workflow.CreateWorkflow;
import com.issuetracker.pages.project.ListProjects;
import com.issuetracker.pages.project.CreateProject;
import com.issuetracker.pages.status.CreateStatus;
import com.issuetracker.pages.issuetype.CreateIssueType;
import com.issuetracker.pages.issue.CreateIssue;
import com.issuetracker.pages.fulltext.FulltextSearch;
import com.issuetracker.pages.permissions.GlobalPermission;
import com.issuetracker.service.api.SecurityService;
import static com.issuetracker.web.Constants.*;
import com.issuetracker.web.security.KeycloakAuthSession;
import static com.issuetracker.web.security.KeycloakAuthSession.*;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
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
import org.jboss.logging.Logger;
import org.keycloak.constants.ServiceUrlConstants;
import org.keycloak.representations.IDToken;
import org.keycloak.util.KeycloakUriBuilder;

public class HeaderPanel extends Panel {

    private final Logger log = Logger.getLogger(HeaderPanel.class);
    
    @Inject SecurityService securityService;
    private String selected;
    private final Label usernameLabel;
    private IDToken idToken;
    
    private final WebMarkupContainer workflowUl;
    private final WebMarkupContainer importUl;
    private final WebMarkupContainer adminUl;
    
    private final Link<String> signOutLink;
    private final Link<String> signInLink;
    private final List<String> optsProject;
    private final List<String> optsIssue;
    private final List<String> optsProfile;
    public final FeedbackPanel feedbackPanel = new FeedbackPanel("feedbackPanel");

    private final boolean signedIn = isSignedIn();
    
    List<String> permittedActions = new ArrayList<>();
    
    @Override
    public void onConfigure() {
        super.onConfigure();
        
        signInLink.setVisible(!signedIn);
        signOutLink.setVisible(signedIn);
        usernameLabel.setVisible(signedIn);
        adminUl.setVisible(signedIn);
        
        workflowUl.setVisible(permittedActions.contains(roles.getProperty("it.workflow")));
        importUl.setVisible(permittedActions.contains(roles.getProperty("it.import")));
    }
    
    @Override
    public void onBeforeRender() {
        if (!permittedActions.contains(roles.getProperty("it.project.create"))) {
            optsProject.remove("Create Project");
        }
        if (!permittedActions.contains(roles.getProperty("it.issue.type"))) {
            optsIssue.remove("Issue Types");
        }
        if (!permittedActions.contains(roles.getProperty("it.permissions"))) {
            optsProfile.remove("Permissions");
        }
        super.onBeforeRender();
    }
    
    public HeaderPanel(String id) {
        super(id);
        
        permittedActions = securityService.getPermittedActionsForUserAndItem(TypeId.global, 0L);
        
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
        if (signedIn) {
            username = "Welcome " + idToken.getName();
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
        
        optsProfile = new ArrayList<>();
        optsProfile.add("Profile");
        optsProfile.add("Permissions");
            
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
        
        final String redirectPage;
        final String linkName;
        if (KeycloakAuthSession.isUserInAppRole("kc.admin")) {
            redirectPage = SERVER_URL + "/auth/admin/" + RHELM_NAME + "/console";
            linkName = "Admin Console";
        } else {
            redirectPage = SERVER_URL + "/auth/realms/" + RHELM_NAME + "/account";
            linkName = "Profile";
        }
        adminUl = new WebMarkupContainer("adminConsoleUl");
        adminUl.add(new Label("adminConsoleLabel", linkName));
        adminUl.add(new PropertyListView<String>("profile", optsProfile) {
            @Override
            protected void populateItem(ListItem<String> listItem) {
                final String stringLink = listItem.getModelObject();
                Link nameLink = new Link<String>("actionLink", listItem.getModel()) {
                    @Override
                    public void onClick() {
                        if (stringLink.equals("Permissions")) {
                            setResponsePage(GlobalPermission.class);
                        } else {
                            setResponsePage(new RedirectPage(redirectPage));
                        }
                    }
                };
                nameLink.add(new Label("name", stringLink));
                nameLink.add(new AttributeModifier("target", "_blank"));
                listItem.add(nameLink);
            }
        });
        add(adminUl);
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
