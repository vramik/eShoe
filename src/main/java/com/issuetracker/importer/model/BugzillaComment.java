package com.issuetracker.importer.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * This entity represents parsed Bugzilla comment from JSON.
 *
 * @author Jiri Holusa
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class BugzillaComment {

    private String text;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return "BugzillaComment{" +
                ", text='" + text + '\'' +
                '}';
    }
}
