package com.issuetracker.model;

import static com.issuetracker.web.Constants.JPATablePreffix;
import javax.persistence.*;
import java.io.Serializable;

/**
 *
 * @author mgottval
 */
@Entity
@Table(name = JPATablePreffix + "IssueUserRelation")
public class IssueUserRelation implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
//    @ManyToOne
    @JoinColumn(name="userId")
    @Column(name = "username")//??
    private String user;
    @ManyToOne
    @JoinColumn(name="issueId")
    private Issue issue;
    @ManyToOne
    private RelationType relationType;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public Issue getIssue() {
        return issue;
    }

    public void setIssue(Issue issue) {
        this.issue = issue;
    }

    public RelationType getRelationType() {
        return relationType;
    }

    public void setRelationType(RelationType relationType) {
        this.relationType = relationType;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof IssueUserRelation)) {
            return false;
        }
        IssueUserRelation other = (IssueUserRelation) object;
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }

    @Override
    public String toString() {
        return "com.issuetracker.IssueUserRelation[ id=" + id + " ]";
    }
    
}
