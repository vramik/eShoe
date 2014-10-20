package com.issuetracker.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;

/**
 *
 * @author mgottval
 */
@Entity
public class ProjectVersion implements Serializable {
    
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
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
        final ProjectVersion other = (ProjectVersion) obj;
        return !((this.name == null) ? (other.name != null) : !this.name.equals(other.name));
    }

    @Override
    public String toString() {
//        return "com.issuetracker.model.ProjectVersion[ id=" + getId() + ", name=" + getName() + " ]";
        return "[ id=" + getId() + ", name=" + getName() + " ]";
    }
    
}
