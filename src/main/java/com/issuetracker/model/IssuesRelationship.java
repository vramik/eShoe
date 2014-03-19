/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.issuetracker.model;

import javax.persistence.*;
import java.io.Serializable;

/**
 *
 * @author mgottval
 */
@Entity
public class IssuesRelationship implements Serializable {

//   private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
   
    @ManyToOne
     private Issue isRelatedIssue;
//    @ManyToOne
//    @PrimaryKeyJoinColumn(name="relatesToIssueId", referencedColumnName="ISSUEID")
    @ManyToOne
    private Issue relatesToIssue;
        
    private RelationshipType relationshipType;
//    @ManyToOne
//    @PrimaryKeyJoinColumn(name="isRelatedIssueId", referencedColumnName="ISSUEID")
//    private Issue isRelatedIssue;
//    @ManyToOne
//    @PrimaryKeyJoinColumn(name="relatesToIssueId", referencedColumnName="ISSUEID")
//    private Issue relatesToIssue;
//
    public enum RelationshipType {

        DEPENDS_ON,
        RELATES_TO,
        DUPLICATES,
        CLONES,
        INCORPORATES
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

 

    public RelationshipType getRelationshipType() {
        return relationshipType;
    }

    public void setRelationshipType(RelationshipType relationshipType) {
        this.relationshipType = relationshipType;
    }

    

    public Issue getIsRelatedIssue() {
        return isRelatedIssue;
    }

    public void setIsRelatedIssue(Issue isRelatedIssue) {
        this.isRelatedIssue = isRelatedIssue;
    }

    public Issue getRelatesToIssue() {
        return relatesToIssue;
    }

    public void setRelatesToIssue(Issue relatesToIssue) {
        this.relatesToIssue = relatesToIssue;
    }

}
