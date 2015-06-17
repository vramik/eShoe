package com.issuetracker.model;

import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author vramik
 */
public class PermissionId implements Serializable {

    private Integer typeId;
    private Long itemId;
    private Long actionId;
    private Long roleId;

    public PermissionId() {
    }
    
    public PermissionId(Integer typeId, Long itemId, Long actionId, Long roleId) {
        this.typeId = typeId;
        this.itemId = itemId;
        this.actionId = actionId;
        this.roleId = roleId;
    }

    public PermissionId(Permission permission) {
        this.typeId = permission.getTypeId().getValue();
        this.itemId = permission.getItemId();
        this.actionId = permission.getActionId();
        this.roleId = permission.getRoleId();
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 29 * hash + Objects.hashCode(this.typeId);
        hash = 29 * hash + Objects.hashCode(this.itemId);
        hash = 29 * hash + Objects.hashCode(this.actionId);
        hash = 29 * hash + Objects.hashCode(this.roleId);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final PermissionId other = (PermissionId) obj;
        if (!Objects.equals(this.typeId, other.typeId)) {
            return false;
        }
        if (!Objects.equals(this.itemId, other.itemId)) {
            return false;
        }
        if (!Objects.equals(this.actionId, other.actionId)) {
            return false;
        }
        if (!Objects.equals(this.roleId, other.roleId)) {
            return false;
        }
        return true;
    }
}
