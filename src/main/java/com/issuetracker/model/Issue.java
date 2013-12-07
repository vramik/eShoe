package com.issuetracker.model;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

/**
 *
 * @author mgottval
 */
@Entity
public class Issue implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long issueId;
    private String name;
    private String summary;
    private String description;
    @ManyToOne(cascade = CascadeType.MERGE)
    private IssueType issueType;
    private Priority priority;
    @ManyToOne
//    private Status status;
    private Status status;
    @ManyToOne
    private Resolution resolution;
    @ManyToOne
    private User creator;
    @ManyToOne
    private User owner;
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
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Issue)) {
            return false;
        }
        Issue other = (Issue) object;
        if ((this.issueId == null && other.issueId != null) || (this.issueId != null && !this.issueId.equals(other.issueId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.issuetracker.Issue[ id=" + issueId + " ]";
    }
}
