/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.issuetracker.pages.component;

import com.issuetracker.pages.layout.MenuPanel;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author mgottval
 */
public class TestMenuPanel {

    private WicketTester tester = null;

    @Before
    public void setUp() throws Exception {

        tester = new WicketTester();
    }
   @Test
    public void testMenuPanelLabel() {
        MenuPanel footer = new MenuPanel("id");
        Assert.assertNotNull(footer.get("name"));
        Label l = (Label) footer.get("name");
        String s = (String) l.getDefaultModelObject();
        Assert.assertEquals("Menu", s);        
    }
}
