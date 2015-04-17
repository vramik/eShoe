package com.issuetracker.service.api;

import com.issuetracker.model.Role;
import java.util.List;

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
}
