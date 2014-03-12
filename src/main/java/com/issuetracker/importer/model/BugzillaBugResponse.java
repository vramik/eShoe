package com.issuetracker.importer.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Collections;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Jirka
 * Date: 11.12.13
 * Time: 13:27
 * To change this template use File | Settings | File Templates.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class BugzillaBugResponse {

    public BugzillaBugResponse() {}

    private String id;

    @JsonProperty("result")
    private BugzillaResponseResult responseResult;

    public List<BugzillaBug> getBugs() {
        return Collections.unmodifiableList(responseResult.getBugs());
    }

    public void setResponseResult(BugzillaResponseResult responseResult) {
        this.responseResult = responseResult;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "BugzillaBugResponse{" +
                "id='" + id + '\'' +
                ", bugzillaResponseResult=" + responseResult +
                '}';
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    private static class BugzillaResponseResult {

        private List<BugzillaBug> bugs;

        public List<BugzillaBug> getBugs() {
            return bugs;
        }

        public void setBugs(List<BugzillaBug> bugs) {
            this.bugs = bugs;
        }

        @Override
        public String toString() {
            return "BugzillaResponseResult{" +
                    "bugs=" + bugs +
                    '}';
        }
    }
}