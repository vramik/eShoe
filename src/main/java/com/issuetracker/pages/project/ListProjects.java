package com.issuetracker.pages.project;

import com.issuetracker.pages.layout.PageLayout;
import com.issuetracker.pages.component.project.ProjectListView;

/**
 *
 * @author Monika
 */
public class ListProjects extends PageLayout {

    public ListProjects() {
        add(new ProjectListView<>("projectList", null));
    }
}
