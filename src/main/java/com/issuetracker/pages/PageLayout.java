package com.issuetracker.pages;

import com.issuetracker.pages.layout.FooterPanel;
import com.issuetracker.pages.layout.HeaderPanel;
import com.issuetracker.web.security.TrackerAuthSession;
import org.apache.wicket.Component;
import org.apache.wicket.Session;
import org.apache.wicket.markup.html.WebPage;

/**
 *
 * @author mgottval
 */
public class PageLayout extends WebPage {

    public static final String CONTENT_ID = "contentComponent";
    private Component headerPanel;
//    private Component menuPanel;
    private Component footerPanel;

    public PageLayout() {

        add(headerPanel = new HeaderPanel("headerPanel"));
//        add(menuPanel = new MenuPanel("menuPanel"));
        add(footerPanel = new FooterPanel("footerPanel"));
    }
     @Override
        public TrackerAuthSession getSession(){
        return (TrackerAuthSession) Session.get();
        }

    //<editor-fold defaultstate="collapsed" desc="getter/setter">
    public Component getHeaderPanel() {
        return headerPanel;
    }

    public void setHeaderPanel(Component headerPanel) {
        this.headerPanel = headerPanel;
    }

//    public Component getMenuPanel() {
//        return menuPanel;
//    }
//    
//    public void setMenuPanel(Component menuPanel) {
//        this.menuPanel = menuPanel;
//    }
    public Component getFooterPanel() {
        return footerPanel;
    }

    public void setFooterPanel(Component footerPanel) {
        this.footerPanel = footerPanel;
    }
    //</editor-fold>
}
