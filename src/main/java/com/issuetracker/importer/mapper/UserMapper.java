package com.issuetracker.importer.mapper;

import com.issuetracker.importer.model.BugzillaBug;
import com.issuetracker.model.User;

/**
 * Created with IntelliJ IDEA.
 * User: Jirka
 * Date: 29.12.13
 * Time: 13:03
 * To change this template use File | Settings | File Templates.
 */
public class UserMapper implements Mapper<User> {

    @Override
    public User map(BugzillaBug bug) {
        User user = new User();
        user.set
    }
}
