package com.issuetracker.pages.component.permission;

import com.issuetracker.model.Action;
import com.issuetracker.model.Permission;
import com.issuetracker.model.Role;
import com.issuetracker.model.TypeId;
import static com.issuetracker.model.TypeId.*;
import com.issuetracker.service.api.ActionService;
import com.issuetracker.service.api.IssueService;
import com.issuetracker.service.api.PermissionService;
import static com.issuetracker.web.Constants.roles;
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

    private List<Action> excludeActionsSetOnHigherLevel(TypeId typeId, Long itemId, Long roleId, List<Action> actionsFromDB) {
        List<Action> result = new ArrayList<>();
        List<Permission> higherLevelPermissions = permissionService.getPermissionsByRole(global, 0L, roleId);
        
        switch (typeId) {
            case project:
                actionsFromDB.remove(actionService.getActionByNameAndType(roles.getProperty("it.project.browse"), typeId));//exclude visibility permission
                excludeActions(actionsFromDB, higherLevelPermissions, result);
                break;
            case issue:
                Long projectId = issueService.getIssueById(itemId).getProject().getId();
                higherLevelPermissions.addAll(permissionService.getPermissionsByRole(project, projectId, roleId));
                actionsFromDB.remove(actionService.getActionByNameAndType(roles.getProperty("it.issue.browse"), typeId));//exclude visibility permission
                excludeActions(actionsFromDB, higherLevelPermissions, result);
                break;
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
