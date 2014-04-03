package com.issuetracker.importer.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

/**
 * This entity represents parsed Bugzilla comment from JSON.
 *
 * @author Jiri Holusa
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class BugzillaComment {

    private String text;
    @JsonProperty("creation_time")
    private Date created;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    @Override
    public String toString() {
        return "BugzillaComment{" +
                ", text='" + text + '\'' +
                '}';
    }
}
