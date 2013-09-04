/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.issuetracker.pages;

import com.issuetracker.model.Issue;
import com.issuetracker.model.User;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;

/**
 *
 * @author mgottval
 */
@Entity
public class IssueHistory implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    @ManyToOne
    private Issue issue;
    private User user;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date changeDate;
    private String change;
    private String beforeChange;
    
    

    //<editor-fold defaultstate="collapsed" desc="getter/setter">
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
    
    public Issue getIssue() {
        return issue;
    }
    
    public void setIssue(Issue issue) {
        this.issue = issue;
    }
    
    public User getUser() {
        return user;
    }
    
    public void setUser(User user) {
        this.user = user;
    }
    
    public Date getChangeDate() {
        return changeDate;
    }
    
    public void setChangeDate(Date changeDate) {
        this.changeDate = changeDate;
    }
    
    public String getChange() {
        return change;
    }
    
    public void setChange(String change) {
        this.change = change;
    }
    
    public String getBeforeChange() {
        return beforeChange;
    }
    
    public void setBeforeChange(String beforeChange) {
        this.beforeChange = beforeChange;
    }
    //</editor-fold>

    
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof IssueHistory)) {
            return false;
        }
        IssueHistory other = (IssueHistory) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.issuetracker.pages.IssueHistory[ id=" + id + " ]";
    }
    
}
