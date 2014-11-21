/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.issuetracker.model;

import static com.issuetracker.web.Constants.JPATablePreffix;
import javax.persistence.*;
import java.io.Serializable;

/**
 *
 * @author mgottval
 */
@Entity
@Table(name = JPATablePreffix + "CustomFieldIssueValue")
public class CustomFieldIssueValue implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @OneToOne
    private CustomField customField;     
    @OneToOne
    private Issue Issue;
    private String value;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CustomField getCustomField() {
        return customField;
    }

    public void setCustomField(CustomField customField) {
        this.customField = customField;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Issue getIssue() {
        return Issue;
    }

    public void setIssue(Issue Issue) {
        this.Issue = Issue;
    }
    

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof CustomFieldIssueValue)) {
            return false;
        }
        CustomFieldIssueValue other = (CustomFieldIssueValue) object;
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }

    @Override
    public String toString() {
        return "com.issuetracker.model.CustomFieldIssueValue[ id=" + id + " ]";
    }
    
}
