/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.issuetracker.model;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

/**
 *
 * @author mgottval
 */
@Entity
public class User implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;
    private String username;
    private String name;
    private String lastName;
    private String email;
    private String password;
    private String confirmPassword;
    
    @ManyToMany
    private List<Group> userGroup;
    
    @OneToMany
    private List<Issue> created;
    
    @OneToMany
    private List<Issue> owned;
    
    @OneToMany
    private List<User> connections;
    
    public User() {
        
    }
    
    public User(String name, String lastName, String email, String password) {
        this.name = name;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
    }

     public User(String name, String pass) {
        this.name = name;
        this.password = pass;
    }
    
    //<editor-fold defaultstate="collapsed" desc="get/set">
    public List<Group> getUserGroup() {
        return userGroup;
    }
    
    public void setUserGroup(List<Group> userGroup) {
        this.userGroup = userGroup;
    }
    
    public Long getUserId() {
        return userId;
    }
    
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getLastname() {
        return lastName;
    }
    
    public void setLastname(String lastName) {
        this.lastName = lastName;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public List<Issue> getCreated() {
        return created;
    }
    
    public void setCreated(List<Issue> created) {
        this.created = created;
    }
    
    public List<Issue> getOwned() {
        return owned;
    }
    

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    
    
    
    public void setOwned(List<Issue> owned) {
        this.owned = owned;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public List<User> getConnections() {
        return connections;
    }

    public void setConnections(List<User> connections) {
        this.connections = connections;
    }
    
    
    
    //</editor-fold>
        
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (userId != null ? userId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof User)) {
            return false;
        }
        User other = (User) object;
        if ((this.userId == null && other.userId != null) || (this.userId != null && !this.userId.equals(other.userId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.issuetracker.User[ id=" + userId + " ]";
    }
}
