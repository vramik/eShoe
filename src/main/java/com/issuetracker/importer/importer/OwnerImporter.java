package com.issuetracker.importer.importer;

import com.issuetracker.importer.model.BugzillaBug;
import com.issuetracker.model.User;

/**
 * Created with IntelliJ IDEA.
 * User: Jirka
 * Date: 29.12.13
 * Time: 13:03
 * To change this template use File | Settings | File Templates.
 */
public class OwnerImporter implements Importer<User> {

    @Override
    public User process(BugzillaBug bug) {
        User user = new User();
        user.setName(bug.getOwner());

        return user;
    }
}
