package com.issuetracker.dao.api;

import com.issuetracker.model.Permission;
import com.issuetracker.model.TypeId;
import java.util.List;

/**
 *
 * @author vramik
 */
public interface PermissionDao {
    
    void insertOrUpdate(Permission permission);
    
    void insert(Permission permission);
    
    void remove(Permission permission);

    void update(Permission permission);

//    List<Permission> getDefaultPermissions();
//
//    List<Permission> getDefaultProjectPermissions();
//
//    List<Permission> getDefaultIssuePermissions();

    List<Permission> getPermissions(TypeId typeId, Long itemId, Long roleId);

    List<Permission> getPermissions(TypeId typeId, Long actionId);

    List<Permission> getPermissions(Long actionId, Long roleId, TypeId[] typeIds);

    Permission getPermission(TypeId typeId, Long itemId, Long roleId, Long actionId);
    
}
