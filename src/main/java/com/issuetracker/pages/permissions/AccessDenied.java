package com.issuetracker.pages.permissions;

import com.issuetracker.pages.HomePage;
import com.issuetracker.pages.PageLayout;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;

/**
 *
 * @author vramik
 */
public class AccessDenied extends PageLayout {

    public AccessDenied() {
        final Label message = new Label("message", "Access Denied!!!");
        add(message);
        final Link homeLink = new Link("homeLink") {
            @Override
            public void onClick() {
                setResponsePage(HomePage.class);
            }
        };
        homeLink.add(new Label("name", "Home Page"));
        add(homeLink);
    }
}

