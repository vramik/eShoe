package com.issuetracker.model;


import com.github.holmistr.esannotations.indexing.annotations.*;
import static com.issuetracker.web.Constants.JPATablePreffix;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Set;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

/**
 *
 * @author mgottval
 */
@Entity
@Table(name = JPATablePreffix + "Issue")
@Indexed(index = "issues", type = "issue")
public class Issue implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @DocumentId
    @Field(name = "id")
    private Long issueId;

    @Field
    private String name;

    @Field
    private String summary;

    @Lob
    @Field
    private String description;

    @IndexEmbedded(name = "issue_type", depth = 1)
    @ManyToOne(cascade = CascadeType.MERGE)
    private IssueType issueType;

    @Field
    @Analyzer(name = "issueTypeNameAnalyzer", tokenizer = "keyword", tokenFilters = "lowercase")
    private Priority priority;

    @Field
    @com.github.holmistr.esannotations.indexing.annotations.Date
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date created = new Date();

    @Field
    @com.github.holmistr.esannotations.indexing.annotations.Date
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date updated;

    @IndexEmbedded(depth = 1)
    @ManyToOne
    private Status status;

    @ManyToOne
    private Resolution resolution;

    @Field
    private String creator;

    @Field
    private String assignee;

    @IndexEmbedded(depth = 1)
    @ManyToOne(cascade = CascadeType.MERGE)
    private Project project;

    private String fileLocation;

    @ElementCollection(fetch = FetchType.EAGER)
    @Fetch(value = FetchMode.SUBSELECT)
    private List<String> watchers;

//    @ManyToMany()
//    List<String> votes;

    @ManyToOne
    private Component component;
    
    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.REFRESH})
    private List<ProjectVersion> affectedVersions;
    
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @Fetch(value = FetchMode.SUBSELECT)
    @IndexEmbedded(depth = 1)
    private List<Comment> comments;
    
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<CustomFieldIssueValue> customFields;
    
//    @OneToMany(mappedBy="isRelatedIssue", cascade= CascadeType.ALL)
//    private List<IssuesRelationship> isRelated;
    
    @OneToMany(fetch = FetchType.EAGER, cascade= CascadeType.ALL)
    @Fetch(value = FetchMode.SUBSELECT)
    private List<IssuesRelationship> relatesTo;
  
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @Fetch(value = FetchMode.SUBSELECT)
    @NotFound(action = NotFoundAction.IGNORE)  
    private Set<Permission> permissions;


    //<editor-fold defaultstate="collapsed" desc="getter/setter">
    public Long getIssueId() {
        return issueId;
    }

    public void setIssueId(Long issueId) {
        this.issueId = issueId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Resolution getResolution() {
        return resolution;
    }

    public void setResolution(Resolution resolution) {
        this.resolution = resolution;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getAssignee() {
        return assignee;
    }

    public void setAssignee(String assignee) {
        this.assignee = assignee;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public List<String> getWatchers() {
        return watchers;
    }

    public void setWatchers(List<String> watches) {
        this.watchers = watches;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public List<CustomFieldIssueValue> getCustomFields() {
        return customFields;
    }

    public void setCustomFields(List<CustomFieldIssueValue> customFields) {
        this.customFields = customFields;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

//    public List<String> getVotes() {
//        return votes;
//    }
//
//    public void setVotes(List<String> votes) {
//        this.votes = votes;
//    }

    public Component getComponent() {
        return component;
    }

    public void setComponent(Component component) {
        this.component = component;
    }

    public IssueType getIssueType() {
        return issueType;
    }

    public void setIssueType(IssueType issueType) {
        this.issueType = issueType;
    }

    public List<ProjectVersion> getAffectedVersions() {
        return affectedVersions;
    }

    public void setAffectedVersions(List<ProjectVersion> affectedVersions) {
        this.affectedVersions = affectedVersions;
    }

    public String getFileLocation() {
        return fileLocation;
    }

    public void setFileLocation(String fileLocation) {
        this.fileLocation = fileLocation;
    }

//    public List<IssuesRelationship> getIsRelated() {
//        return isRelated;
//    }
//
//    public void setIsRelated(List<IssuesRelationship> isRelated) {
//        this.isRelated = isRelated;
//    }

    public List<IssuesRelationship> getRelatesTo() {
        return relatesTo;
    }

    public void setRelatesTo(List<IssuesRelationship> relatesTo) {
        this.relatesTo = relatesTo;
    }

    public Date getCreated() {
        return created;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setCreated(Date date) {
        this.created = new Date(date.getTime());
    }

    @PrePersist
    public void setCreationDate() {
        this.created = new Date();
    }

    @PreUpdate
    public void setUpdatedDate() {
        this.updated = new Date();
    }

    public void setUpdated(Date date) {
        this.updated = new Date(date.getTime());
    }
    
    public Set<Permission> getPermissions() {
        return permissions;
    }

    public void setPermissions(Set<Permission> permissions) {
        this.permissions = permissions;
    }

    //</editor-fold>

    public enum Priority {

        HIGH,
        MEDIUM_HIGH,
        MEDIUM,
        MEDIUM_LOW,
        LOW;
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (issueId != null ? issueId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Issue)) {
            return false;
        }
        Issue other = (Issue) object;
        return (this.issueId != null || other.issueId == null) && (this.issueId == null || this.issueId.equals(other.issueId));
    }

    @Override
    public String toString() {
        return "com.issuetracker.Issue[ id=" + issueId + " ]";
    }
}
