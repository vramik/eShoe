package com.issuetracker.dao.api;

import com.issuetracker.model.Role;
import java.util.List;
import java.util.Set;

/**
 *
 * @author vramik
 */
public interface RoleDao {

    void insert(Role role);

    void remove(Role role);

    boolean isRoleNameInUse(String name);

    Role getRoleByName(String name);

    List<Role> getRoles();

    Role getRoleById(Long roleId);

    Set<Long> getIdsByNames(Set<String> roleNames);
    
}
