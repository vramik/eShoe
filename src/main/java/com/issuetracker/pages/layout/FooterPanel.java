/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.issuetracker.pages.layout;

import java.util.Date;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

/**
 *
 * @author mgottval
 */
public class FooterPanel extends Panel{
    
    public FooterPanel(String id) {
        super(id);
        
        add(new Label("name", "eShoe"));
        
        IModel timeStampModel = new Model<String>() {
            @Override
            public String getObject() {
                return new Date().toString();
            }
        };
        
        add(new Label("timeStamp", timeStampModel));
    }
    
}
