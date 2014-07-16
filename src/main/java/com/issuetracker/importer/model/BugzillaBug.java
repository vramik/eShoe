package com.issuetracker.importer.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;
import java.util.List;

/**
 * This entity represents parsed Bugzilla bug.
 *
 * @author Jiri Holusa
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class BugzillaBug {

    private String id;
    @JsonProperty("alias")
    private List<String> name;
    private String priority;
    private String creator;
    @JsonProperty("assigned_to")
    private String owner;
    @JsonProperty("cf_type")
    private String issueType;
    private String summary;
    @JsonProperty("product")
    private String project;
    private List<String> component;
    private List<String> version;
    private String status;
    @JsonProperty("creation_time")
    private Date created;
    @JsonProperty("last_change_time")
    private Date updated;

    public List<String> getName() {
        return name;
    }

    public void setName(List<String> name) {
        this.name = name;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public List<String> getComponent() {
        return component;
    }

    public void setComponent(List<String> component) {
        this.component = component;
    }

    public String getIssueType() {
        return issueType;
    }

    public void setIssueType(String issueType) {
        this.issueType = issueType;
    }

    public List<String> getVersion() {
        return version;
    }

    public void setVersion(List<String> version) {
        this.version = version;
    }

    public String getOwner() {
        return owner;
    }

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    @Override
    public String toString() {
        return "BugzillaBug{" +
                "id='" + id + '\'' +
                ", name=" + name +
                ", priority='" + priority + '\'' +
                ", creator='" + creator + '\'' +
                ", owner='" + owner + '\'' +
                ", issueType='" + issueType + '\'' +
                ", summary='" + summary + '\'' +
                ", project='" + project + '\'' +
                ", component=" + component +
                ", version=" + version +
                ", status='" + status + '\'' +
                '}';
    }
}
