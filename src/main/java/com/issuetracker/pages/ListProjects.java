package com.issuetracker.pages;

import com.issuetracker.pages.component.project.ProjectListView;

/**
 *
 * @author Monika
 */
public class ListProjects extends PageLayout {

    public ListProjects() {
        add(new ProjectListView<>("projectList"));
    }
}
