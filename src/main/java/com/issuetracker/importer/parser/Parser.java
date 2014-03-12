package com.issuetracker.importer.parser;

import com.issuetracker.importer.model.BugzillaBugResponse;
import com.issuetracker.importer.model.BugzillaCommentResponse;

/**
 * Created with IntelliJ IDEA.
 * User: Jirka
 * Date: 11.12.13
 * Time: 12:25
 * To change this template use File | Settings | File Templates.
 */
public interface Parser {

    public <T> T parse(String input, Class<T> clazz);
}
