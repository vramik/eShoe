package com.issuetracker.importer.importer;

import com.issuetracker.importer.model.BugzillaBug;
import com.issuetracker.model.ProjectVersion;

/**
 * Created with IntelliJ IDEA.
 * User: Jirka
 * Date: 29.12.13
 * Time: 13:03
 * To change this template use File | Settings | File Templates.
 */
public class ProjectVersionImporter implements Importer<ProjectVersion> {

    @Override
    public ProjectVersion process(BugzillaBug bug) {
        ProjectVersion version = new ProjectVersion();
        version.setName(bug.getVersion().get(0));

        return version;
    }
}
