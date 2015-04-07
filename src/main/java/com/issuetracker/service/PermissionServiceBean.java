package com.issuetracker.service;

import com.issuetracker.dao.api.PermissionDao;
import com.issuetracker.model.Action;
import com.issuetracker.model.Comment;
import com.issuetracker.model.Issue;
import com.issuetracker.model.Project;
import com.issuetracker.model.Permission;
import com.issuetracker.model.TypeId;
import static com.issuetracker.model.TypeId.*;
import com.issuetracker.service.api.ActionService;
import com.issuetracker.service.api.PermissionService;
import com.issuetracker.service.api.RoleService;
import java.util.ArrayList;
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

            permissionDao.insertOrUpdate(newPermission);
        }
    }

    @Override
    public List<Permission> getPermissions(TypeId typeId, Long itemId, Long roleId) {
        log.error("TODO");
        List<Permission> permissions = permissionDao.getPermissions(typeId, itemId, roleId);
        return permissions;
//        
//        log.warn("--" + permissions);
//        
//        List<Permission> result = new ArrayList<>(permissions);
//        //todo: pokud neexistuje project-specific permission, tak vzit vyssi level
//        if (!typeId.equals(global)) {
//            boolean present = false;
//            for(Action action : actionService.getActionsByType(typeId)) {
//                log.info(action + ", id: " + action.getId());
//                for (Permission p : permissions) {
//                    log.warn("p.getActionId(): " + p.getActionId());
//                    if (action.getId().equals(p.getActionId())) {
//                        present = true;
//                        break;
//                    }
//                    log.warn("isPresent: " + present);
//                }
//                if (!present) {
//                    switch (typeId) {
//                        case project:
//                            Permission globalPermission = checkPermission(global, 0L, typeId, itemId, action.getId(), roleId);
//                            if (globalPermission != null) {
//                                result.add(globalPermission);
//                            }
//                            break;
//                        case issue:
//                            throw new UnsupportedOperationException("not supported yet.");
////                            Permission projectPermission = checkPermission(project, itemId, typeId, itemId, itemId, roleId)
////                            if (projectPermission.isEmpty()) {
////                                result.addAll(permissionDao.getPermissions(global, null, roleId));
////                            } else {
////                                result.addAll(projectPermission);
////                            }
////                            break;
//                        case comment:
//                            throw new UnsupportedOperationException("not supported yet.");
////                            List<Permission> issuePermissions = permissionDao.getPermissions(issue, itemId, roleId);
////                            if (issuePermissions.isEmpty()) {
////                                List<Permission> projectPermission2 = permissionDao.getPermissions(project, itemId, roleId);
////                                if (projectPermission2.isEmpty()) {
////                                    result.addAll(permissionDao.getPermissions(global, 0L, roleId));
////                                } else {
////                                    result.addAll(projectPermission2);
////                                }
////                            } else {
////                                result.addAll(issuePermissions);
////                            }
////                            break;
//                        default:
//                            throw new IllegalStateException("Unreachable state was reached.");
//                    }
//                }
//            }
//        }
//        log.warn("<returned permissions> for roleId: " + roleId);
//        for (Permission p : result) {
//            log.warn(p);
//        }
//        log.warn("</returned permissions>");
//        
//        return result;
    }

    @Override
    public void update(TypeId typeId, Long itemId, Long roleId, List<Permission> permissions) {
        List<Permission> permissinsFromDB = getPermissions(typeId, itemId, roleId);
        
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

    private List<Permission> getPermissions(Long actionId, Long roleId, TypeId... typeIds) {
        return permissionDao.getPermissions(actionId, roleId, typeIds);
    }

    private Permission checkPermission(TypeId typeId, Long itemId, TypeId newPermissionTypeId, Long newPermissionItemId, Long actionId, Long roleId) {
        Permission globalPermission = permissionDao.getPermission(typeId, itemId, roleId, actionId);
        if (globalPermission != null) {
            Permission newPermission = new Permission();
            newPermission.setTypeId(newPermissionTypeId);
            newPermission.setItemId(newPermissionItemId);
            newPermission.setActionId(actionId);
            newPermission.setRoleId(roleId);
            return newPermission;
        }
        return globalPermission;
    }

}
