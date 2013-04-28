/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.issuetracker.pages.layout;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;

public class HeaderPanel extends Panel {

    public HeaderPanel(String id) {
        super(id);

        Label label = new Label("name", "Bon Jour");
        label.add(new AttributeModifier("style", "color:red;font-weight:bold"));

        add(label);

    }
}
