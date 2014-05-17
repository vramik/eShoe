package com.issuetracker.search.tools;

import com.issuetracker.model.*;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * //TODO: document this
 *
 * @author Jiří Holuša
 */
public class SearchTestHelper {

    public static final List<Long> ISPN_PROJECT = new ArrayList<Long>(){{add(1L); add(2L);}};
    public static final List<Long> OPEN_STATUS = new ArrayList<Long>(){{add(1L); add(3L);}};
    public static final List<Long> BUG_ISSUETYPE = new ArrayList<Long>(){{add(1L); add(3L);}};
    public static final List<Long> RANGE_CREATED= new ArrayList<Long>(){{add(1L); add(2L);}};
    public static final List<Long> RANGE_UPDATED= new ArrayList<Long>(){{add(2L);}};
    public static final List<Long> SYKORA_CREATOR = new ArrayList<Long>(){{add(2L);}};
    public static final List<Long> PITONAK_OWNER = new ArrayList<Long>(){{add(4L);}};
    public static final List<Long> HIGH_PRIORITY = new ArrayList<Long>(){{add(1L); add(3L);}};

    public static Issue createTestIssue1() {
        IssueType issueType = createIssueType("Bug");
        Project project = createProject("Infinispan");
        Status status = createStatus("Open");
        User owner = createUser("Emmanuel Bernard");
        User creator = createUser("Jiri Holusa");

        Calendar calendar = Calendar.getInstance();
        calendar.set(2014, 1, 1);
        Date created = calendar.getTime();
        calendar.set(2014, 1, 2);
        Date updated = calendar.getTime();

        Issue issue = new Issue();
        issue.setIssueId(1L);
        issue.setSummary("Failing unit tests");
        issue.setIssueType(issueType);
        issue.setPriority(Issue.Priority.HIGH);
        issue.setProject(project);
        issue.setStatus(status);
        issue.setOwner(owner);
        issue.setCreator(creator);
        issue.setCreated(created);
        issue.setUpdated(updated);

        return issue;
    }

    public static Issue createTestIssue2() {
        IssueType issueType = createIssueType("Feature request");
        Project project = createProject("Infinispan");
        Status status = createStatus("Resolved");
        User owner = createUser("Martin Gencur");
        User creator = createUser("Tomas Sykora");

        Calendar calendar = Calendar.getInstance();
        calendar.set(2014, 3, 1);
        Date created = calendar.getTime();
        calendar.set(2014, 3, 4);
        Date updated = calendar.getTime();

        Issue issue = new Issue();
        issue.setIssueId(2L);
        issue.setSummary("Graceful shutdown should be supported");
        issue.setIssueType(issueType);
        issue.setPriority(Issue.Priority.LOW);
        issue.setProject(project);
        issue.setStatus(status);
        issue.setOwner(owner);
        issue.setCreator(creator);
        issue.setCreated(created);
        issue.setUpdated(updated);

        return issue;
    }

    public static Issue createTestIssue3() {
        IssueType issueType = createIssueType("Bug");
        Project project = createProject("RichFaces");
        Status status = createStatus("Open");
        User owner = createUser("Juraj Huska");
        User creator = createUser("Juraj Huska");

        Calendar calendar = Calendar.getInstance();
        calendar.set(2014, 7, 7);
        Date created = calendar.getTime();
        calendar.set(2014, 7, 7);
        Date updated = calendar.getTime();

        Issue issue = new Issue();
        issue.setIssueId(3L);
        issue.setSummary("notifyMessage - stack attribute - only default value works");
        issue.setIssueType(issueType);
        issue.setPriority(Issue.Priority.HIGH);
        issue.setProject(project);
        issue.setStatus(status);
        issue.setOwner(owner);
        issue.setCreator(creator);
        issue.setCreated(created);
        issue.setUpdated(updated);

        return issue;
    }

    public static Issue createTestIssue4() {
        IssueType issueType = createIssueType("Enhancement");
        Project project = createProject("EAP");
        Status status = createStatus("Coding in progress");
        User owner = createUser("Pavol Pitonak");
        User creator = createUser("Brian Leathem");

        Calendar calendar = Calendar.getInstance();
        calendar.set(2014, 7, 7);
        Date created = calendar.getTime();
        calendar.set(2014, 7, 7);
        Date updated = calendar.getTime();

        Issue issue = new Issue();
        issue.setIssueId(4L);
        issue.setSummary("EAP 6 JAAS cache does not work correctly");
        issue.setIssueType(issueType);
        issue.setPriority(Issue.Priority.LOW);
        issue.setProject(project);
        issue.setStatus(status);
        issue.setOwner(owner);
        issue.setCreator(creator);
        issue.setCreated(created);
        issue.setUpdated(updated);

        return issue;
    }

    private static IssueType createIssueType(String name) {
        IssueType issueType = new IssueType();
        issueType.setName(name);

        return issueType;
    }

    private static Project createProject(String name) {
        Project project = new Project();
        project.setName(name);

        return project;
    }

    private static Status createStatus(String name) {
        Status status = new Status();
        status.setName(name);

        return status;
    }

    private static User createUser(String name) {
        User user = new User();
        user.setName(name);

        return user;
    }
}
