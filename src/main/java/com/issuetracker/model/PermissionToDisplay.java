package com.issuetracker.model;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author vramik
 */
public class PermissionToDisplay implements Serializable {
    
    private TypeId typeId;
    private Long itemId;
    private String actionName;
    private List<String> roles;

    public TypeId getTypeId() {
        return typeId;
    }

    public void setTypeId(TypeId typeId) {
        this.typeId = typeId;
    }

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public String getActionName() {
        return actionName;
    }

    public void setActionName(String actionName) {
        this.actionName = actionName;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    
}
