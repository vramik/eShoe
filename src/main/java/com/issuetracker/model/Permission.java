package com.issuetracker.model;

import static com.issuetracker.web.Constants.JPATablePreffix;
import java.io.Serializable;
import java.util.Set;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 * @author mgottval
 */
@Entity
@Table(name = JPATablePreffix + "Permission")
public class Permission implements Serializable, Comparable<Permission> {
    
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private PermissionType permissionType;
    
    @ElementCollection(fetch = FetchType.EAGER)
    private Set<String> roles;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }

    public PermissionType getPermissionType() {
        return permissionType;
    }

    public void setPermissionType(PermissionType permissionType) {
        this.permissionType = permissionType;
    }

    @Override
    public int hashCode() {
        int hash = 3;
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
        return this.permissionType == other.permissionType;
    }

    @Override
    public String toString() {
        return "Permission{" + "id=" + id + ", permissionType=" + permissionType + ", roles=" + roles + '}';
    }

    @Override
    public int compareTo(Permission p) {
        return getPermissionType().toString().compareTo(p.getPermissionType().toString());
    }
}
