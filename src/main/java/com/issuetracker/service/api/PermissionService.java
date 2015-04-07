package com.issuetracker.service.api;

import com.issuetracker.model.Permission;
import com.issuetracker.model.TypeId;
import java.util.List;

/**
 *
 * @author vramik
 */
public interface PermissionService {
    
    void createPermissions(TypeId typeId, Object item, String actionName, TypeId actionType, String... roleNames);
    
    List<Permission> getPermissions(TypeId typeId, Long itemId, Long roleId);

    void update(TypeId typeId, Long itemId, Long roleId, List<Permission> permissions);

}
