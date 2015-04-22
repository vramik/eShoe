package com.issuetracker.service.api;

import com.issuetracker.model.Role;
import java.util.List;
import java.util.Set;

/**
 *
 * @author vramik
 */
public interface RoleService {
    
    void insert(Role role);
    
    void remove(Role role);
    
    Role getRoleByName(String name);
    
    List<Role> getRoles();

    Role getRoleById(Long roleId);

    Set<Long> getIdsByNames(Set<String> userRhelmRoles);

    void addRolesByNames(List<String> availableRoles);
}
