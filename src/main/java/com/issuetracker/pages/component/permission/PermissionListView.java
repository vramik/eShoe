package com.issuetracker.pages.component.permission;

import com.issuetracker.model.Action;
import com.issuetracker.model.Permission;
import com.issuetracker.model.Role;
import com.issuetracker.model.TypeId;
import static com.issuetracker.model.TypeId.*;
import com.issuetracker.pages.permissions.AccessDenied;
import com.issuetracker.service.api.ActionService;
import com.issuetracker.service.api.IssueService;
import com.issuetracker.service.api.PermissionService;
import com.issuetracker.service.api.SecurityService;
import static com.issuetracker.web.Constants.roles;
import com.issuetracker.web.security.KeycloakAuthSession;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.inject.Inject;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxButton;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Check;
import org.apache.wicket.markup.html.form.CheckGroup;
import org.apache.wicket.markup.html.form.CheckGroupSelector;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.PropertyModel;
import org.jboss.logging.Logger;

/**
 *
 * @author vramik
 */
public class PermissionListView extends Panel {

    @Inject private PermissionService permissionService;
    @Inject private ActionService actionService;
    @Inject private IssueService issueService;
    @Inject private SecurityService securityService;
    
    private final List<Permission> allPermissions;
    private final List<Action> actions;
    private final WebMarkupContainer wmc = new WebMarkupContainer("wmc");
    private final PermissionFeedbackPanel feedbackPanel = new PermissionFeedbackPanel("permissionFeedbackPanel");
    
    private final Logger log = Logger.getLogger(PermissionListView.class);
    
    public PermissionListView(String id, final Role role, final TypeId typeId, final Long itemId) {
        super(id);
        
        feedbackPanel.setOutputMarkupId(true);
        add(feedbackPanel);
        
        Long roleId = role.getId();
        
        actions = excludeActionsSetOnHigherLevel(typeId, itemId, roleId, actionService.getActionsByType(typeId));
        allPermissions = new ArrayList<>();
        for (Action action : actions) {
            Permission permission = new Permission();
            permission.setTypeId(typeId);
            permission.setItemId(itemId);
            permission.setRoleId(roleId);
            permission.setActionId(action.getId());
            allPermissions.add(permission);
        }

        final ListView<Permission> testListView = new ListView<Permission>("permissionListView", new PropertyModel<List<Permission>>(this, "allPermissions")) {//this should contain all permissions

            @Override
            protected void populateItem(ListItem<Permission> item) {
                item.add(new Check<>("permissionCheckbox", item.getModel()));
                item.add(new Label("permissionActionName", actionService.getActionById(item.getModelObject().getActionId()).getName()));
            }
         
        };
        testListView.setReuseItems(true);

        final List<Permission> permissionsIdDB = permissionService.getPermissionsByRole(typeId, itemId, roleId);
        
        final CheckGroup<Permission> testGroup = new CheckGroup<>("permissionGroup", permissionsIdDB);//this permissions will be checked
        testGroup.add(new CheckGroupSelector("permissionGroupSelector", testGroup));
        testGroup.add(testListView);
        testGroup.setVisible(false);

        wmc.add(testGroup);
        wmc.setOutputMarkupId(true);
        
        Form<?> testForm = new Form("permissionForm") {
            @Override
            protected void onSubmit() {
                checkPermissions(typeId, itemId);
                Collection<Permission> selectedPermissions = testGroup.getModelObject();
                log.warn("selectedPermissions");
                for (Permission p : selectedPermissions) {       
                    log.warn(p);
                }
                permissionService.update(typeId, itemId, role.getId(), new ArrayList(selectedPermissions));
            }
        };
        testForm.add(new Label("permissionRoleName", role.getName()));
        testForm.add(wmc);
        add(testForm);
        
        IndicatingAjaxButton HideOrShowButton = new IndicatingAjaxButton("hideOrShow", testForm){
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                checkPermissions(typeId, itemId);
                target.add(wmc);
                if (testGroup.isVisible()) {
                    testGroup.setVisible(false);
                } else {
                    testGroup.setVisible(true);
                }
                feedbackPanel.clearMessages();
                target.add(feedbackPanel);
            }
        };
        HideOrShowButton.setDefaultFormProcessing(false);
        testForm.add(HideOrShowButton);
        
        testGroup.add(new AjaxButton("ajaxSubmit", testForm) {
        
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                info(role.getName() + "'s permissions was saved.");
                target.add(feedbackPanel);
                feedbackPanel.clearMessages();
            }
        });
    }

    private void checkPermissions(TypeId typeId, Long itemId) {
        switch (typeId) {
            case global:
                if (!securityService.canUserPerformAction(TypeId.global, 0L, roles.getProperty("it.permissions"))) {
                    setResponsePage(AccessDenied.class);
                }
                break;
            case project:
                if (!securityService.canUserPerformAction(TypeId.project, itemId, roles.getProperty("it.project.permissions"))) {
                    setResponsePage(AccessDenied.class);
                }
                break;
            case issue:
                if (!securityService.canUserPerformAction(TypeId.issue, itemId, roles.getProperty("it.issue.permissions"))) {
                    setResponsePage(AccessDenied.class);
                }
            case comment:
                throw new UnsupportedOperationException("Not suported.");
            default:
                throw new IllegalStateException("Unreachable state was reached.");
        }
    }
    
    private List<Action> excludeActionsSetOnHigherLevel(TypeId typeId, Long itemId, Long roleId, List<Action> actionsFromDB) {
        List<Action> result = new ArrayList<>();
        List<Permission> higherLevelPermissions;
        
        switch (typeId) {
            case global:
                result = actionsFromDB;
                break;
            case project:
                higherLevelPermissions = permissionService.getPermissionsByRole(global, 0L, roleId);
                actionsFromDB.remove(actionService.getActionByNameAndType(roles.getProperty("it.project.browse"), typeId));//exclude visibility permission
                excludeActions(actionsFromDB, higherLevelPermissions, result);
                break;
            case issue:
                higherLevelPermissions = permissionService.getPermissionsByRole(global, 0L, roleId);
                Long projectId = issueService.getIssueById(itemId).getProject().getId();
                higherLevelPermissions.addAll(permissionService.getPermissionsByRole(project, projectId, roleId));
                actionsFromDB.remove(actionService.getActionByNameAndType(roles.getProperty("it.issue.browse"), typeId));//exclude visibility permission
                excludeActions(actionsFromDB, higherLevelPermissions, result);
                break;
            case comment:
                throw new UnsupportedOperationException("Not supported!");
            default:
                throw new IllegalStateException("unreachable state was reached");
        }
        return result;
    }

    private void excludeActions(List<Action> actionsFromDB, List<Permission> higherLevelPermissions, List<Action> result) {
        for (Action actionFromDB : actionsFromDB) {
            boolean present = false;
            for (Permission globalPermission : higherLevelPermissions) {
                if (globalPermission.getActionId().equals(actionFromDB.getId())) {
                    present = true;
                    break;
                }
            }
            if (!present) {
                result.add(actionFromDB);
            }
        }
    }
}
