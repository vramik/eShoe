/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.issuetracker.pages.layout;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;

/**
 *
 * @author mgottval
 */
public class MenuPanel extends Panel{
    
    public MenuPanel(String id) {
        super(id);
        
        add(new Label("name", "Menu"));
    }
    
}
