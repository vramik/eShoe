package com.issuetracker.pages.layout;

import static com.issuetracker.model.TypeId.global;
import com.issuetracker.pages.permissions.AccessDenied;
import com.issuetracker.service.api.SecurityService;
import static com.issuetracker.web.Constants.roles;
import com.issuetracker.web.IssueTrackerSession;
import com.issuetracker.web.quilifiers.ViewPageConstraint;
import com.issuetracker.web.security.KeycloakAuthSession;
import static com.issuetracker.web.security.KeycloakAuthSession.*;
import java.lang.reflect.Constructor;
import javax.inject.Inject;
import org.apache.wicket.Component;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.jboss.logging.Logger;

/**
 *
 * @author mgottval
 */
public class PageLayout extends WebPage {

    public static final String CONTENT_ID = "contentComponent";
    
    private final Logger log = Logger.getLogger(PageLayout.class);
    
    private Component headerPanel;
//    private Component menuPanel;
    private Component footerPanel;
    
    private final IssueTrackerSession session;
    
    @Inject private SecurityService securityService;

    public PageLayout() {
        session = IssueTrackerSession.get();

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
        
        add(new Label("title", "Issue Tracking system"));
        add(headerPanel = new HeaderPanel("headerPanel"));
//        add(menuPanel = new MenuPanel("menuPanel"));
        add(footerPanel = new FooterPanel("footerPanel"));
    }
    
    private String getPageClassNameAndPageParams() {
        return this.getClass().getName() + ":" + getPageParameters() + ";";
    }

    private boolean isAuthorizedToViewThePageAndCacheResult() throws SecurityException {
        for (Constructor constructor : getClass().getDeclaredConstructors()) {
            boolean result;
            if (constructor.isAnnotationPresent(ViewPageConstraint.class)) {
                ViewPageConstraint securityConstraint = (ViewPageConstraint) constructor.getAnnotation(ViewPageConstraint.class);
                
                String allowedAction = securityConstraint.allowedAction();
                String allowedRole = securityConstraint.allowedRole();
                
                if (allowedAction.isEmpty() && !allowedRole.isEmpty()) {
                    result = KeycloakAuthSession.isUserInRhelmRole(roles.getProperty(allowedRole));
                } else {
                    result = securityService.canUserPerformAction(global, 0L, roles.getProperty(allowedAction));
                }
            } else {
                result = true;
            }
            session.put(getPageClassNameAndPageParams(), result);
            return result;
        }
        throw new IllegalStateException("This should be never reached! It means constructor is missing.");
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
