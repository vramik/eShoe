/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.issuetracker.pages.layout;

import com.issuetracker.pages.CreateIssue;
import com.issuetracker.pages.CreateIssueType;
import com.issuetracker.pages.CreateProject;
import com.issuetracker.pages.SearchIssues;
import java.util.ArrayList;
import java.util.List;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.PropertyListView;
import org.apache.wicket.markup.html.panel.Panel;

public class HeaderPanel extends Panel {
    
    private String selected = "Create Project";

    public HeaderPanel(String id) {
        super(id);

        Label label = new Label("name", "Issue Tracking system");
        label.add(new AttributeModifier("style", "color:red;font-weight:bold"));

        add(label);
        
        List<String> opts = new ArrayList<String>();
        opts.add("Create Project");
        opts.add("Create Issue");
        opts.add("Search Issue");
        opts.add("Insert Types of Project");
        
        add(new PropertyListView<String>("projectTasks", opts) {
            @Override
            public void populateItem(final ListItem<String> listItem) {
                final String stringLink = listItem.getModelObject();
                Link nameLink = new Link<String>("actionLink", listItem.getModel()) {
                    @Override
                    public void onClick() {
                        selected = stringLink;
                        if (selected.equals("Create Project")) {
                            setResponsePage(CreateProject.class);
                        }
                        if (selected.equals("Create Issue")) {
                            setResponsePage(CreateIssue.class);
                        }
                        if (selected.equals("Search Issue")) {
                            setResponsePage(SearchIssues.class);
                        }
                        if (selected.equals("Insert Types of Project")) {
                            setResponsePage(CreateIssueType.class);
                        }
                    }
                };
                nameLink.add(new Label("name", stringLink));
                listItem.add(nameLink);

            }
        });
    
        

    }
    
    public String getSelected() {
        return selected;
    }

    public void setSelected(String selected) {
        this.selected = selected;
    }
}
