package com.issuetracker.pages.permissions;

import com.issuetracker.model.Issue;
import com.issuetracker.model.Role;
import com.issuetracker.model.TypeId;
import com.issuetracker.pages.component.permission.PermissionListView;
import com.issuetracker.pages.issue.IssueDetail;
import com.issuetracker.pages.layout.PageLayout;
import com.issuetracker.service.api.IssueService;
import com.issuetracker.service.api.RoleService;
import com.issuetracker.service.api.SecurityService;
import static com.issuetracker.web.Constants.HOME_PAGE;
import static com.issuetracker.web.Constants.roles;
import static com.issuetracker.web.security.KeycloakService.getRealmRoles;
import javax.inject.Inject;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.request.flow.RedirectToUrlException;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.string.StringValue;
import org.jboss.logging.Logger;

/**
 *
 * @author vramik
 */
public class IssuePermission extends PageLayout {

    private final Logger log = Logger.getLogger(IssuePermission.class);
    
    @Inject RoleService roleService;
    @Inject IssueService issueService;
    @Inject SecurityService securityService;
    
    public IssuePermission(PageParameters parameters) {
        StringValue stringIssueId = parameters.get("issue");
        if (stringIssueId.equals(StringValue.valueOf((String)null))) {
            log.error("redirecting to home page");
            throw new RedirectToUrlException(HOME_PAGE);
        }
        final Long issueId = stringIssueId.toLong();
        Issue issue = issueService.getIssueById(issueId);
        if (issue == null) {
            log.warn("Issue with given id doesn't exist. Redirecting to Home page.");
            throw new RedirectToUrlException(HOME_PAGE);
        }
        if (!securityService.canUserPerformAction(TypeId.issue, issueId, roles.getProperty("it.issue.browse")) || 
                !securityService.canUserPerformAction(TypeId.issue, issueId, roles.getProperty("it.issue.permissions"))) {
            setResponsePage(AccessDenied.class);
        }
        
        add(new Label("permissionTitle", "Permissions for issue " + issue.getName()));
        
        roleService.addRolesByNames(getRealmRoles());
        ListView<Role> permissions = new ListView<Role>("permissions", roleService.getRoles()) {
            
            @Override
            protected void populateItem(ListItem<Role> item) {
                item.add(new PermissionListView("permission", item.getModelObject(), TypeId.issue, issueId));
            }
        };
        add(permissions);
        
        add(new Link("issueDetail") {
            @Override
            public void onClick() {
                PageParameters pageParameters = new PageParameters();
                pageParameters.add("issue", issueId);
                setResponsePage(IssueDetail.class, pageParameters);
            }
        });
    }
}

