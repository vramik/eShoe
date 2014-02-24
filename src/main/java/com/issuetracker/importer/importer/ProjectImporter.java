package com.issuetracker.importer.importer;

import com.issuetracker.importer.model.BugzillaBug;
import com.issuetracker.model.Project;
import com.issuetracker.model.User;

/**
 * Created with IntelliJ IDEA.
 * User: Jirka
 * Date: 29.12.13
 * Time: 13:03
 * To change this template use File | Settings | File Templates.
 */
public class ProjectImporter implements Importer<Project> {

    @Override
    public Project process(BugzillaBug bug) {
        Project project = new Project();
        project.setName(bug.getCreator());

        return project;
    }
}
