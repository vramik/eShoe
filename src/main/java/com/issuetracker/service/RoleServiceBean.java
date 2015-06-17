package com.issuetracker.service;

import com.issuetracker.dao.api.RoleDao;
import com.issuetracker.model.Role;
import com.issuetracker.service.api.RoleService;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.ejb.Stateless;
import javax.inject.Inject;

/**
 *
 * @author mgottval
 */
@Stateless
public class RoleServiceBean implements RoleService, Serializable {

    @Inject
    private RoleDao roleDao;

    @Override
    public void insert(Role role) {
        roleDao.insert(role);
    }

    @Override
    public void remove(Role role) {
        roleDao.remove(role);
    }

    @Override
    public Role getRoleByName(String name) {
        if (!roleDao.isRoleNameInUse(name)) {
            Role newRole = new Role();
            newRole.setName(name);
            insert(newRole);
        }
        return roleDao.getRoleByName(name);
    }

    @Override
    public List<Role> getRoles() {
        return roleDao.getRoles();
    }

    @Override
    public Role getRoleById(Long roleId) {
        return roleDao.getRoleById(roleId);
    }

    @Override
    public Set<Long> getIdsByNames(Set<String> roleNames) {
        return roleDao.getIdsByNames(roleNames);
    }

    @Override
    public void addRolesByNames(List<String> availableRoles) {
        for (String availableRole : availableRoles) {
            getRoleByName(availableRole);
        }
    }
}
