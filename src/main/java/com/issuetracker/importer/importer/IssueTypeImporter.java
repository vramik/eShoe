package com.issuetracker.importer.importer;

import com.issuetracker.importer.model.BugzillaBug;
import com.issuetracker.model.IssueType;

/**
 * Created with IntelliJ IDEA.
 * User: Jirka
 * Date: 29.12.13
 * Time: 13:03
 * To change this template use File | Settings | File Templates.
 */
public class IssueTypeImporter implements Importer<IssueType> {

    @Override
    public IssueType process(BugzillaBug bug) {
        IssueType issueType = new IssueType();
        issueType.setName(bug.getIssueType());

        return issueType;
    }
}
