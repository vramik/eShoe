package com.issuetracker.importer.importer;

import com.issuetracker.importer.model.BugzillaBug;
import com.issuetracker.model.Component;
import com.issuetracker.model.Project;

/**
 * Created with IntelliJ IDEA.
 * User: Jirka
 * Date: 29.12.13
 * Time: 13:03
 * To change this template use File | Settings | File Templates.
 */
public class ComponentImporter implements Importer<Component> {

    @Override
    public Component process(BugzillaBug bug) {
        Component component = new Component();
        component.setName(bug.getComponent().get(0));

        return component;
    }
}
