package com.issuetracker.pages;

import com.issuetracker.pages.layout.FooterPanel;
import com.issuetracker.pages.layout.HeaderPanel;
import com.issuetracker.web.quilifiers.SecurityConstraint;
import static com.issuetracker.web.security.KeycloakAuthSession.checkPermissions;
import java.lang.reflect.Constructor;
import org.apache.wicket.Component;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;

/**
 *
 * @author mgottval
 */
public class PageLayout extends WebPage {

    public static final String CONTENT_ID = "contentComponent";
    private Component headerPanel;
//    private Component menuPanel;
    private Component footerPanel;

    /**
     * It consumes ...
     */
    @Override
    public void onConfigure() {
        super.onConfigure();

        for (Constructor constructor : this.getClass().getDeclaredConstructors()) {
            if (constructor.isAnnotationPresent(SecurityConstraint.class)) {
                SecurityConstraint securityConstraint = (SecurityConstraint) constructor.getAnnotation(SecurityConstraint.class);
                
                String roleKey = securityConstraint.allowedRole();
                checkPermissions(this, roleKey);
            }
        }
    }
    
    public PageLayout() {
        System.out.println("pageLayoutConstructor");
        add(new Label("title", "Issue Tracking system"));
        add(headerPanel = new HeaderPanel("headerPanel"));
//        add(menuPanel = new MenuPanel("menuPanel"));
        add(footerPanel = new FooterPanel("footerPanel"));
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
