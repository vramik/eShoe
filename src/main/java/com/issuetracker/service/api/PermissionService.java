package com.issuetracker.service.api;

import com.issuetracker.model.Permission;
import com.issuetracker.model.TypeId;
import java.util.List;
import java.util.Set;

/**
 *
 * @author vramik
 */
public interface PermissionService {
    
    void createPermissions(TypeId typeId, Object item, String actionName, TypeId actionType, String... roleNames);
    
    List<Permission> getPermissionsByRole(TypeId typeId, Long itemId, Long roleId);

    List<Permission> getPermissionsByAction(TypeId typeId, Long itemId, Long actionId);

    void update(TypeId typeId, Long itemId, Long roleId, List<Permission> permissions);

    List<Permission> getPermissionsByTypeAndAction(TypeId typeId, Long actionId);
    
    List<Permission> getPermissions(Long actionId, Long roleId, TypeId... typeIds);

    List<Permission> getPermissions(TypeId typeId, Long itemId, Set<Long> userRolesIds);

}
