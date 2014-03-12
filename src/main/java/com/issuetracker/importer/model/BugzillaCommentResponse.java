package com.issuetracker.importer.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Jirka
 * Date: 11.12.13
 * Time: 13:27
 * To change this template use File | Settings | File Templates.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class BugzillaCommentResponse {

    public BugzillaCommentResponse() {}

    private String id;

    @JsonProperty("result")
    private BugzillaResponseResult responseResult;

    public Map<String, List<BugzillaComment>> getComments() {
        Map<String, List<BugzillaComment>> result = new HashMap<String, List<BugzillaComment>>();
        for(String key: responseResult.getBugs().keySet()) {
            result.put(key, responseResult.getBugs().get(key).get("comments"));
        }

        return result;
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

        private Map<String, Map<String, List<BugzillaComment>>> bugs;

        public Map<String, Map<String, List<BugzillaComment>>> getBugs() {
            return bugs;
        }

        public void setBugs(Map<String, Map<String, List<BugzillaComment>>> bugs) {
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