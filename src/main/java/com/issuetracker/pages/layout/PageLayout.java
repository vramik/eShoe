package com.issuetracker.pages.layout;

import com.issuetracker.pages.Login;
import com.issuetracker.pages.permissions.AccessDenied;
import com.issuetracker.web.IssueTrackerSession;
import com.issuetracker.web.quilifiers.ViewPageConstraint;
import static com.issuetracker.web.security.KeycloakAuthSession.*;
import java.lang.reflect.Constructor;
import org.apache.wicket.Component;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.jboss.logging.Logger;

/**
 *
 * @author mgottval
 */
public class PageLayout extends WebPage {

    private final Logger log = Logger.getLogger(PageLayout.class);
    
    public static final String CONTENT_ID = "contentComponent";
    private Component headerPanel;
//    private Component menuPanel;
    private Component footerPanel;
    
    private final IssueTrackerSession session;

    /**
     * It consumes ...
     */
    @Override
    public void onConfigure() {
        super.onConfigure();
        
        Boolean isAuthorized = session.get(getPageClassNameAndPageParams());
        if (isAuthorized == null) {
            isAuthorized = isAuthorizedToViewThePageAndCacheResult();
        }
        if (!isAuthorized) {
            log.warn("TODO");
//            if (isSignedIn()) {
                setResponsePage(AccessDenied.class);
//            } else {
//                PageParameters params = getPageParameters();
//                params.add("page", getPageClassNameAndPageParams());
//                setResponsePage(Login.class, params);
//            }
        }
    }

    private String getPageClassNameAndPageParams() {
        String result = this.getClass().getName() + ":" + getPageParameters();
        log.info("Cashing: " + result);
        return result;
    }

    private boolean isAuthorizedToViewThePageAndCacheResult() throws SecurityException {
        for (Constructor constructor : getClass().getDeclaredConstructors()) {
            boolean result;
            if (constructor.isAnnotationPresent(ViewPageConstraint.class)) {
                ViewPageConstraint securityConstraint = (ViewPageConstraint) constructor.getAnnotation(ViewPageConstraint.class);
                
                String allowedRole = securityConstraint.allowedRole();
                
                if (allowedRole.equals("signed.in")) {
                    result = isSignedIn();
                } else {
                    result = isUserInAppRole(allowedRole);
                }
            } else {
                result = true;
            }
            session.put(getPageClassNameAndPageParams(), result);
            return result;
        }
        throw new IllegalStateException("This should be never reached! It means constructor is missing.");
    }
    
    public PageLayout() {
        session = IssueTrackerSession.get();
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
