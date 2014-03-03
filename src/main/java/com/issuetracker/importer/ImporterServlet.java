package com.issuetracker.importer;

import com.issuetracker.dao.api.*;
import com.issuetracker.importer.parser.Parser;
import com.issuetracker.importer.reader.Reader;
import com.issuetracker.importer.model.BugzillaBug;
import com.issuetracker.model.Component;
import com.issuetracker.model.Issue;
import com.issuetracker.model.IssueType;
import com.issuetracker.model.Project;
import com.issuetracker.model.ProjectVersion;
import com.issuetracker.model.User;

import javax.inject.Inject;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: Jirka
 * Date: 4.12.13
 * Time: 10:42
 * To change this template use File | Settings | File Templates.
 */
@WebServlet("/importer")
public class ImporterServlet extends HttpServlet {

    private Reader reader;
    private Parser parser;

    @Inject
    private IssueDao issueDao;
    @Inject
    private ComponentDao componentDao;
    @Inject
    private ProjectDao projectDao;
    @Inject
    private IssueTypeDao issueTypeDao;
    @Inject
    private ProjectVersionDao projectVersionDao;
    @Inject
    private UserDao userDao;

    private Set<Component> componentSet = new HashSet<Component>();
    private Set<Project> projectSet = new HashSet<Project>();
    private Set<IssueType> issueTypeSet = new HashSet<IssueType>();
    private Set<ProjectVersion> projectVersionSet = new HashSet<ProjectVersion>();


    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        PrintWriter out = response.getWriter();
        out.println("<html>");
        out.println("<body>");
        out.println("<h1>Importer servlet</h1>");
        run();
        out.println("</body>");
        out.println("</html>");
    }

    private void run() {
        System.out.println("Import running");
        /*reader = new RestReader();
        parser = new JsonParser();

        String readResult = reader.read();
        List<BugzillaBug> result = parser.parse(readResult);

        System.out.println(result);*/

        BugzillaBug bug = prepareTestBug();

        Component component = mapComponent(bug);
        List<Component> components = new ArrayList<Component>();
        components.add(component);
        componentDao.insertComponent(component);

        ProjectVersion projectVersion = mapProjectVersion(bug);
        List<ProjectVersion> projectVersions = new ArrayList<ProjectVersion>();
        projectVersions.add(projectVersion);
        projectVersionDao.insertProjectVersion(projectVersion);

        Project project = mapProject(bug);
        project.setComponents(components);
        project.setVersions(projectVersions);
        projectDao.insertProject(project);

        IssueType issueType = mapIssueType(bug);
        issueTypeDao.insertIssueType(issueType);

        User creator = mapCreator(bug);
        User owner = mapOwner(bug);
        Issue issue = mapIssue(bug);

        List<Issue> createdIssues = new ArrayList<Issue>();
        createdIssues.add(issue);
        creator.setCreated(createdIssues);

        List<Issue> ownedIssues = new ArrayList<Issue>();
        ownedIssues.add(issue);
        owner.setOwned(ownedIssues);

        issue.setProject(project);
        issue.setCreator(creator);
        issue.setOwner(owner);
        issue.setIssueType(issueType);
        issue.setComponent(component);
        issue.setProjectVersion(projectVersion);

        userDao.addUser(creator);
        userDao.addUser(owner);
        issueDao.addIssue(issue);
    }

    private BugzillaBug prepareTestBug() {
        BugzillaBug bug = new BugzillaBug();
        bug.setName("Name value");
        bug.setSummary("Summary value");

        List<String> versionList = new ArrayList<String>();
        versionList.add("Version value");
        bug.setVersion(versionList);

        bug.setProject("Project value");
        bug.setCreator("Creator value");
        bug.setOwner("Owner value");
        bug.setIssueType("Issue type value");

        List<String> componentList = new ArrayList<String>();
        componentList.add("Component value");
        bug.setComponent(componentList);

        return bug;
    }

    private Component mapComponent(BugzillaBug bug) {
        Component component = new Component();
        component.setName(bug.getComponent().get(0));

        if(!componentSet.contains(component)) {
            //componentDao.insertComponent(component);
            componentSet.add(component);
        }

        return component;
    }

    private User mapCreator(BugzillaBug bug) {
        User user = userDao.getUserByName(bug.getCreator());

        if(user == null) {
            user = new User();
            user.setName(bug.getCreator());

            //userDao.addUser(user);
        }

        return user;
    }

    private User mapOwner(BugzillaBug bug) {
        User user = userDao.getUserByName(bug.getOwner());

        if(user == null) {
            user = new User();
            user.setName(bug.getOwner());

            //userDao.addUser(user);
        }

        return user;
    }

    private Issue mapIssue(BugzillaBug bug) {
        Issue issue = new Issue();
        issue.setSummary(bug.getSummary());
        issue.setName(bug.getName());
        issue.setDescription("Fixed description value, CHANGE THIS!");

        return issue;
    }

    private IssueType mapIssueType(BugzillaBug bug) {
        IssueType issueType = new IssueType();
        issueType.setName(bug.getIssueType());

        if(!issueTypeSet.contains(issueType)) {
            //issueTypeDao.insertIssueType(issueType);
            issueTypeSet.add(issueType);
        }

        return issueType;
    }

    private Project mapProject(BugzillaBug bug) {
        Project project = new Project();
        project.setName(bug.getProject());

        if(!projectSet.contains(project)) {
            //projectDao.insertProject(project);
            projectSet.add(project);
        }

        return project;
    }

    private ProjectVersion mapProjectVersion(BugzillaBug bug) {
        ProjectVersion version = new ProjectVersion();
        version.setName(bug.getVersion().get(0));

        if(!projectVersionSet.contains(version)) {
            //projectVersionDao.insertProjectVersion(version);
            projectVersionSet.add(version);
        }

        return version;
    }
}
