/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.issuetracker.pages;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;

/**
 *
 * @author mgottval
 */
public class HomePage extends WebPage {

    public HomePage() {
        add(new Label("login", "Login"));
        add(new Label("password", "Password"));
        add(new Link("id") {
            @Override
            public void onClick() {
                setResponsePage(ListIssues.class);

            }
        });

    }
}
