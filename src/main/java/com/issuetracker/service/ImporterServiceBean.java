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
import org.apache.commons.lang3.StringUtils;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Jiri Holusa
 */
@Stateless
public class ImporterServiceBean implements ImporterService {

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
    private UserService userService;
    @Inject
    private StatusService statusService;
    @Inject
    private CommentService commentService;

    private List<IssueType> issueTypeList = new ArrayList<IssueType>();
    private List<Component> componentList = new ArrayList<Component>();
    private List<ProjectVersion> projectVersionList = new ArrayList<ProjectVersion>();

    public void doImport() {
        System.out.println("Import running");
        reader = new RestReader();
        parser = new JsonParser();

        IdFileLoader idLoader = new IdFileLoader();
        List<String> ids = idLoader.loadIdsFromFile(IDS_FILENAME);
        String joinedIds = StringUtils.join(ids, ",");

        String bugReadResult = reader.read(BUGZILLA_URL + "jsonrpc.cgi?method=Bug.get&params=[ { \"ids\": [" + joinedIds + "] } ]");
        String commentReadResult = reader.read(BUGZILLA_URL + "jsonrpc.cgi?method=Bug.comments&params=[ { \"ids\": [" + joinedIds + "] } ]");

        BugzillaBugResponse bugResponse = parser.parse(bugReadResult, BugzillaBugResponse.class);
        BugzillaCommentResponse response = parser.parse(commentReadResult, BugzillaCommentResponse.class);

        List<BugzillaBug> bugs = bugResponse.getBugs();
        Map<String, List<BugzillaComment>> comments = response.getComments();

        for(BugzillaBug bug: bugs) {
            Project project = mapProject(bug);
            List<Component> components = project.getComponents();
            if(components == null) {
                components = new ArrayList<Component>();
            }

            Component component = mapComponent(bug);
            if(!components.contains(component)) {
                components.add(component);
            }

            List<ProjectVersion> projectVersions = project.getVersions();
            if(projectVersions == null) {
                projectVersions = new ArrayList<ProjectVersion>();
            }

            ProjectVersion projectVersion = mapProjectVersion(bug);
            if(!projectVersions.contains(projectVersion)) {
                projectVersions.add(projectVersion);
            }

            project.setComponents(components);
            project.setVersions(projectVersions);
            projectService.insert(project);

            IssueType issueType = mapIssueType(bug);

            User creator = mapCreator(bug);
            User owner = mapOwner(bug);

            Issue.Priority priority = mapPriority(bug);

            Issue issue = mapIssue(bug, comments);

            Status status = mapStatus(bug);

            issue.setProject(project);
            issue.setCreator(creator);
            issue.setOwner(owner);
            issue.setPriority(priority);
            issue.setIssueType(issueType);
            issue.setComponent(component);
            issue.setStatus(status);
            issue.setProjectVersion(projectVersion);

            issueService.insert(issue);

            List<Comment> issuesComments = mapComments(bug, comments);
            issue.setComments(issuesComments);

            issueService.update(issue);

            /*List<Issue> createdIssues = new ArrayList<Issue>();
            createdIssues.add(issue);
            creator.setCreated(createdIssues);
            userService.update(creator);

            List<Issue> ownedIssues = new ArrayList<Issue>();
            ownedIssues.add(issue);
            owner.setOwned(ownedIssues);
            userService.update(owner);*/
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

    private User mapCreator(BugzillaBug bug) {
        User user = userService.getUserByName(bug.getCreator());

        if(user == null) {
            user = new User();
            user.setName(bug.getCreator());
            userService.insert(user);
        }

        return user;
    }

    private User mapOwner(BugzillaBug bug) {
        User user = userService.getUserByName(bug.getOwner());

        if(user == null) {
            user = new User();
            user.setName(bug.getOwner());
            userService.insert(user);
        }

        return user;
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
        issue.setDescription(comments.get(bug.getId()).remove(0).getText());
        issue.setCreated(bug.getCreated());
        issue.setUpdated(bug.getUpdated());

        return issue;
    }

    private List<Comment> mapComments(BugzillaBug bug, Map<String, List<BugzillaComment>> comments) {
        List<Comment> issueComments = new ArrayList<Comment>();
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

    private ProjectVersion mapProjectVersion(BugzillaBug bug) {
        projectVersionList = projectVersionService.getProjectVersions();

        if(projectVersionList != null) {
            for(ProjectVersion projectVersion: projectVersionList) {
                if(projectVersion.getName().equals(bug.getVersion().get(0))) {
                    return projectVersion;
                }
            }
        }

        ProjectVersion projectVersion = new ProjectVersion();
        projectVersion.setName(bug.getVersion().get(0));

        return projectVersion;
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
