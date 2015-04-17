package com.issuetracker.service;

import com.issuetracker.dao.api.PermissionDao;
import com.issuetracker.model.Comment;
import com.issuetracker.model.Issue;
import com.issuetracker.model.Project;
import com.issuetracker.model.Permission;
import com.issuetracker.model.TypeId;
import static com.issuetracker.model.TypeId.*;
import com.issuetracker.service.api.ActionService;
import com.issuetracker.service.api.PermissionService;
import com.issuetracker.service.api.RoleService;
import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import org.jboss.logging.Logger;

/**
 *
 * @author vramik
 */
@Stateless
public class PermissionServiceBean implements PermissionService {

    private final Logger log = Logger.getLogger(PermissionServiceBean.class);
    
    @Inject
    private PermissionDao permissionDao;
    @Inject 
    private ActionService actionService;
    @Inject 
    private RoleService roleService;
    
    @Override
    public void createPermissions(TypeId typeId, Object item, String actionName, TypeId actionType, String... roleNames) {
        for (String roleName : roleNames) {
            Permission newPermission = new Permission();

            newPermission.setTypeId(typeId);
            switch (typeId) {
                case project:
                    if (item instanceof Project) {
                        newPermission.setItemId(((Project) item).getId());
                    } else {
                        throw new IllegalArgumentException("Item is not Project.");
                    }
                    break;
                case issue:
                    if (item instanceof Issue) {
                        newPermission.setItemId(((Issue) item).getIssueId());
                    } else {
                        throw new IllegalArgumentException("Item is not Issue.");
                    }
                    break;
                case comment:
                    if (item instanceof Comment) {
                        newPermission.setItemId(((Comment) item).getId());
                    } else {
                        throw new IllegalArgumentException("Item is not Comment.");
                    }
                    break;
                case global:
                    newPermission.setItemId(0L);
                    break;
                default:
                    throw new IllegalStateException("Reached unreacheable state.");
            }

            newPermission.setActionId(actionService.getActionByNameAndType(actionName, actionType).getId());
            newPermission.setRoleId(roleService.getRoleByName(roleName).getId());

            log.info(newPermission);
            permissionDao.insertOrUpdate(newPermission);
        }
    }

    @Override
    public List<Permission> getPermissionsByRole(TypeId typeId, Long itemId, Long roleId) {
        return permissionDao.getPermissionsByRole(typeId, itemId, roleId);
    }

    @Override
    public void update(TypeId typeId, Long itemId, Long roleId, List<Permission> permissions) {
        List<Permission> permissinsFromDB = getPermissionsByRole(typeId, itemId, roleId);
        
        if (permissions.isEmpty()) {
            log.error("removing all permissions");
            for (Permission p : permissinsFromDB) {
                permissionDao.remove(p);
            }
        } else {

            //checks arguments
            for (Permission permission : permissions) {
                if (!typeId.equals(permission.getTypeId())) {
                    throw new IllegalArgumentException("All permissions has to be of the same type.");
                }
                if (!itemId.equals(permission.getItemId())) {
                    throw new IllegalArgumentException("All permissions has to be of the same item.");
                }
                if (!roleId.equals(permission.getRoleId())) {
                    throw new IllegalArgumentException("All permissions has to be of the same role.");
                }
            }

            //remove all newly unselected permissions from DB
            for (Permission permissinFromDB : permissinsFromDB) {
                if (!permissions.contains(permissinFromDB)) {
                    log.error("removing: " + permissinFromDB);
                    permissionDao.remove(permissinFromDB);
                }
            }
            
            //add all newly selected permissions
            for (Permission selectedPermission : permissions) {
                Long selectedActionId = selectedPermission.getActionId();
                Long selectedRoleId = selectedPermission.getRoleId();
                switch (typeId) {
                    case global:
                        permissionDao.insert(selectedPermission);
                        break;
                    case project:
                        if (getPermissions(selectedActionId, selectedRoleId, global).isEmpty()) {
                            permissionDao.insert(selectedPermission);
                        }
                        break;
                    case issue:
                        if (getPermissions(selectedActionId, selectedRoleId, global, project).isEmpty()) {
                            permissionDao.insert(selectedPermission);
                        }
                        break;
                    case comment:
                        if (getPermissions(selectedActionId, selectedRoleId, global, project, issue).isEmpty()) {
                            permissionDao.insert(selectedPermission);
                        }
                        break;
                    default:
                        break;
                }
                permissionDao.insert(selectedPermission);
            }
        } 
    }

    @Override
    public List<Permission> getPermissions(Long actionId, Long roleId, TypeId... typeIds) {
        return permissionDao.getPermissions(actionId, roleId, typeIds);
    }

    @Override
    public List<Permission> getPermissionsByAction(TypeId typeId, Long itemId, Long actionId) {
        return permissionDao.getPermissionsByAction(typeId, itemId, actionId);
    }

    @Override
    public List<Permission> getPermissionsByTypeAndAction(TypeId typeId, Long actionId) {
        return permissionDao.getPermissionsByTypeAndAction(typeId, actionId);
    }

}
