package com.issuetracker.importer.mapper;

import com.issuetracker.importer.model.BugzillaBug;

/**
 * Created with IntelliJ IDEA.
 * User: Jirka
 * Date: 13.12.13
 * Time: 15:13
 * To change this template use File | Settings | File Templates.
 */
public interface Mapper<T> {

    public T map(BugzillaBug bug);
}
