package com.issuetracker.pages.permissions;

import com.issuetracker.model.Action;
import com.issuetracker.model.Permission;
import com.issuetracker.model.Role;
import com.issuetracker.model.TypeId;
import com.issuetracker.pages.component.permission.PermissionListView;
import com.issuetracker.pages.layout.PageLayout;
import com.issuetracker.service.api.ActionService;
import com.issuetracker.service.api.PermissionService;
import com.issuetracker.service.api.RoleService;
import static com.issuetracker.web.Constants.HOME_PAGE;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
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
public class ProjectPermission extends PageLayout {

    private final Logger log = Logger.getLogger(ProjectPermission.class);
    
    @Inject 
    RoleService roleService;
    
    public ProjectPermission(PageParameters parameters) {
        StringValue stringProjectId = parameters.get("project");
        if (stringProjectId.equals(StringValue.valueOf((String)null))) {
            throw new RedirectToUrlException(HOME_PAGE);
        }
        final Long projectId = stringProjectId.toLong();
                
        ListView<Role> permissions = new ListView<Role>("permissions", roleService.getRoles()) {
            
            @Override
            protected void populateItem(ListItem<Role> item) {
                item.add(new PermissionListView("permission", item.getModelObject(), TypeId.project, projectId));
            }
        };
        add(permissions);
    }
}

