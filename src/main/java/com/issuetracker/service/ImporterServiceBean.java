package com.issuetracker.service;

import com.issuetracker.importer.loader.IdFileLoader;
import com.issuetracker.importer.model.BugzillaBug;
import com.issuetracker.importer.model.BugzillaBugResponse;
import com.issuetracker.importer.model.BugzillaComment;
import com.issuetracker.importer.model.BugzillaCommentResponse;
import com.issuetracker.importer.parser.JsonParser;
import com.issuetracker.importer.parser.Parser;
import com.issuetracker.importer.reader.RestReader;
import com.issuetracker.model.*;
import com.issuetracker.service.api.*;
import com.issuetracker.web.IssueTrackerSession;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.inject.Inject;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author Jiri Holusa
 */
@Stateless
public class ImporterServiceBean implements ImporterService, Serializable {

    private static final String BUGZILLA_URL = "https://bugzilla.redhat.com/";
    private static final String IDS_FILENAME = "importer-bugzilla-ids.txt";

    private RestReader reader;
    private Parser parser;

    @Inject
    private IssueService issueService;
    @Inject
    private ComponentService componentService;
    @Inject
    private ProjectService projectService;
    @Inject
    private IssueTypeService issueTypeService;
    @Inject
    private ProjectVersionService projectVersionService;
    @Inject
    private StatusService statusService;
    @Inject
    private CommentService commentService;

    private List<IssueType> issueTypeList = new ArrayList<>();
    private List<Component> componentList = new ArrayList<>();
    private List<ProjectVersion> projectVersionList = new ArrayList<>();

    private int importedCounter;

    private static final int IMPORT_ITERATION_SIZE = 30;

    @Override
    public void doImport() {
        System.out.println("Import running");
        reader = new RestReader();
        parser = new JsonParser();

        IdFileLoader idLoader = new IdFileLoader();
        List<String> ids = idLoader.loadIdsFromFile(IDS_FILENAME);
        System.out.println("Importing " + ids.size() + " issues.");
        importedCounter = 0;

        int i;
        for(i = 0; i < ids.size()/IMPORT_ITERATION_SIZE; i++) {
            List<String> idsPart = new ArrayList<>();
            for(int j = 0; j < IMPORT_ITERATION_SIZE; j++) {
                idsPart.add(ids.get((i * IMPORT_ITERATION_SIZE) + j));
            }

            String joinedIds = StringUtils.join(idsPart, ",");
            performImportIteration(joinedIds);
        }

        if(ids.size() % IMPORT_ITERATION_SIZE != 0) {
            List<String> idsPart = new ArrayList<>();
            for(int j = importedCounter; j < ids.size(); j++) {
                idsPart.add(ids.get(j));
            }
            String joinedIds = StringUtils.join(idsPart, ",");
            performImportIteration(joinedIds);
        }
    }

    private void performImportIteration(String ids) {
        String bugReadResult = reader.read(BUGZILLA_URL + "jsonrpc.cgi?method=Bug.get&params=[ { \"ids\": [" + ids + "] } ]");
        String commentReadResult = reader.read(BUGZILLA_URL + "jsonrpc.cgi?method=Bug.comments&params=[ { \"ids\": [" + ids + "] } ]");

        BugzillaBugResponse bugResponse = parser.parse(bugReadResult, BugzillaBugResponse.class);
        BugzillaCommentResponse response = parser.parse(commentReadResult, BugzillaCommentResponse.class);

        List<BugzillaBug> bugs = bugResponse.getBugs();
        Map<String, List<BugzillaComment>> comments = response.getComments();

        for(BugzillaBug bug: bugs) {
            Project project = mapProject(bug);
            List<Component> components = project.getComponents();
            if(components == null) {
                components = new ArrayList<>();
            }

            Component component = mapComponent(bug);
            if(!components.contains(component)) {
                components.add(component);
            }

            List<ProjectVersion> projectVersions = project.getVersions();
            if(projectVersions == null) {
                projectVersions = new ArrayList<>();
            }

            List<ProjectVersion> BZProjectVersions = mapProjectVersions(bug);
            projectVersions.removeAll(BZProjectVersions);
            projectVersions.addAll(BZProjectVersions);

            project.setComponents(components);
            project.setVersions(projectVersions);
            projectService.insert(project);

            IssueType issueType = mapIssueType(bug);

            String creator = mapCreator(bug);
            String owner = mapOwner(bug);

            Issue.Priority priority = mapPriority(bug);

            Issue issue = mapIssue(bug, comments);

            Status status = mapStatus(bug);

            issue.setProject(project);
            issue.setCreator(creator);
            issue.setAssignee(owner);
            issue.setPriority(priority);
            issue.setIssueType(issueType);
            issue.setComponent(component);
            issue.setStatus(status);
            issue.setAffectedVersions(BZProjectVersions);

            issueService.create(issue);

            List<Comment> issuesComments = mapComments(bug, comments);
            issue.setComments(issuesComments);

            issueService.update(issue);

            System.out.println(++importedCounter + " issues imported.");
        }
    }

    private Issue.Priority mapPriority(BugzillaBug bug) {
        switch(bug.getPriority().toLowerCase()) {
            case "high": return Issue.Priority.HIGH;
            case "medium": return Issue.Priority.MEDIUM;
            case "low": return Issue.Priority.LOW;
            default: return Issue.Priority.HIGH;
        }
    }

    private Component mapComponent(BugzillaBug bug) {
        componentList = componentService.getComponents();

        if(componentList != null) {
            for(Component component: componentList) {
                if(component.getName().equals(bug.getComponent().get(0))) {
                    return component;
                }
            }
        }

        Component component = new Component();
        component.setName(bug.getComponent().get(0));

        return component;
    }

    private String mapCreator(BugzillaBug bug) {
//        User user = userService.getUserByName(bug.getCreator());
//
//        if(user == null) {
//            user = new User();
//            user.setName(bug.getCreator());
//            userService.insert(user);
//        }

        return bug.getCreator();
    }

    private String mapOwner(BugzillaBug bug) {
//        User user = userService.getUserByName(bug.getOwner());
//
//        if(user == null) {
//            user = new User();
//            user.setName(bug.getOwner());
//            userService.insert(user);
//        }

        return bug.getOwner();
    }

    private Issue mapIssue(BugzillaBug bug, Map<String, List<BugzillaComment>> comments) {
        Issue issue = new Issue();
        issue.setSummary(bug.getSummary());
        if(bug.getName() != null && !bug.getName().isEmpty()) {
            issue.setName(bug.getName().get(0));
        }

        if(issue.getName() == null) {
            issue.setName(bug.getSummary());
        }

        //first comment of Bugzilla's bug is the description of the bug
        if(!comments.get(bug.getId()).isEmpty()) {
            issue.setDescription(comments.get(bug.getId()).remove(0).getText());
        }
        issue.setCreated(bug.getCreated());
        issue.setUpdated(bug.getUpdated());

        return issue;
    }

    private List<Comment> mapComments(BugzillaBug bug, Map<String, List<BugzillaComment>> comments) {
        List<Comment> issueComments = new ArrayList<>();
        for(BugzillaComment bugzillaComment: comments.get(bug.getId())) {
            Comment comment = new Comment();
            comment.setContent(bugzillaComment.getText());
            comment.setCreated(bugzillaComment.getCreated());
            issueComments.add(comment);
        }

        return issueComments;
    }

    private IssueType mapIssueType(BugzillaBug bug) {
        issueTypeList = issueTypeService.getIssueTypes();

        for(IssueType issueType: issueTypeList) {
            if(issueType.getName().equals(bug.getIssueType())) {
                return issueType;
            }
        }

        IssueType issueType = new IssueType();
        issueType.setName(bug.getIssueType());
        issueTypeService.insert(issueType);

        return issueType;
    }

    private Project mapProject(BugzillaBug bug) {
        Project project = projectService.getProjectByName(bug.getProject());

        if(project == null) {
            project = new Project();
            project.setName(bug.getProject());
        }

        return project;
    }

    private List<ProjectVersion> mapProjectVersions(BugzillaBug bug) {
        projectVersionList = projectVersionService.getProjectVersions();
        List<String> BZVersions = bug.getVersion();
        List<ProjectVersion> result = new ArrayList<>();
        
        if (projectVersionList != null) {
            for (ProjectVersion projectVersion: projectVersionList) {
                if (BZVersions.contains(projectVersion.getName())) {
                    result.add(projectVersion);
                    BZVersions.remove(projectVersion.getName());
                }
            }
        }
        
        for (String BZVersion : BZVersions) {
            ProjectVersion projectVersion = new ProjectVersion();
            projectVersion.setName(BZVersion);
            projectVersionService.insert(projectVersion);
            result.add(projectVersion);
        }
        return result;
    }

    private Status mapStatus(BugzillaBug bug) {
        Status status = statusService.getStatusByName(bug.getStatus());

        if(status == null) {
            status = new Status();
            status.setName(bug.getStatus());
            statusService.insert(status);
        }

        return status;
    }
}
