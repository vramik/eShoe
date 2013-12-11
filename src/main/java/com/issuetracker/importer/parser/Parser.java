package com.issuetracker.importer.parser;

import com.issuetracker.importer.model.BugzillaBug;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Jirka
 * Date: 11.12.13
 * Time: 12:25
 * To change this template use File | Settings | File Templates.
 */
public interface Parser {

    public List<BugzillaBug> parse(String input);
}
