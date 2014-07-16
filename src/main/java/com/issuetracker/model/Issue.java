package com.issuetracker.model;


import com.github.holmistr.esannotations.indexing.annotations.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 *
 * @author mgottval
 */
@Entity
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
//    private Status status;
    private Status status;
    @ManyToOne
    private Resolution resolution;
    @IndexEmbedded(depth = 1)
    @ManyToOne
    private User creator;
    @IndexEmbedded(depth = 1)
    @ManyToOne
    private User owner;
    @IndexEmbedded(depth = 1)
    @ManyToOne(cascade = CascadeType.MERGE)
    private Project project;
    private String fileLocation;
    @ManyToMany(fetch = FetchType.EAGER)
    @Fetch(value = FetchMode.SUBSELECT)
    private List<User> watches;
//    @ManyToMany()
//    List<User> votes;
    @ManyToOne
    private Component component;
    @ManyToOne
    private ProjectVersion projectVersion;
    
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
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

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public List<User> getWatches() {
        return watches;
    }

    public void setWatches(List<User> watches) {
        this.watches = watches;
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

  

    

//    public List<User> getVotes() {
//        return votes;
//    }
//
//    public void setVotes(List<User> votes) {
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

    public ProjectVersion getProjectVersion() {
        return projectVersion;
    }

    public void setProjectVersion(ProjectVersion projectVersion) {
        this.projectVersion = projectVersion;
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
