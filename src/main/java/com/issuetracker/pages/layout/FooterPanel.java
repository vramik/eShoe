package com.issuetracker.pages.layout;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import java.util.Date;

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
