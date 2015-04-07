package com.issuetracker.model;

import static com.issuetracker.web.Constants.JPATablePreffix;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.persistence.Transient;
import org.hibernate.annotations.Index;
import org.jboss.logging.Logger;

/**
 *
 * @author vramik
 */
@Entity @IdClass(PermissionId.class)
@Table(name = JPATablePreffix + "Permission")
public class Permission implements Serializable {
    
    @Transient//not in DB
    private final Logger log = Logger.getLogger(Permission.class);
    
    private static final long serialVersionUID = 1L;
    
    @Id
    private Integer typeId;
    @Id
    @Index(name = "permission_itemid")
    private Long itemId;
    @Id
    @Index(name = "permission_actionid")
    private Long actionId;
    @Id
    @Index(name = "permission_roleid")
    private Long roleId;

    public TypeId getTypeId() {
        return TypeId.parse(this.typeId);
    }

    public void setTypeId(TypeId typeId) {
        this.typeId = typeId.getValue();
    }

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public Long getActionId() {
        return actionId;
    }

    public void setActionId(Long actionId) {
        this.actionId = actionId;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
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
        final Permission other = (Permission) obj;
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

    @Override
    public String toString() {
        return "Permission{" + "typeId=" + typeId + ", itemId=" + itemId + ", actionId=" + actionId + ", roleId=" + roleId + '}';
    }
}
