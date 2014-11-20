
package com.issuetracker.web.security;

import com.issuetracker.pages.Login;
import com.issuetracker.pages.permissions.AccessDenied;
import com.issuetracker.web.Constants;
import static com.issuetracker.web.Constants.roles;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.request.Request;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.keycloak.KeycloakSecurityContext;
import org.keycloak.representations.AccessToken.Access;
import org.keycloak.representations.IDToken;

/**
 *
 * @author vramik
 */
public class KeycloakAuthSession {
    
    /**
     * Returns IDToken or null if user is not signed in
     * @param req WebRequest
     * @return IDToken or null if user is not signed in
     */
    public static IDToken getIDToken(Request req) {
        if (isSignedIn(req)) {
            return getKeycloakSecurityContext(req).getIdToken();
        } else {
            return null;
        }
    }
    
    public static Set<String> getUserRhelmRoles(Request req) {
        if (isSignedIn(req)) {
            return getKeycloakSecurityContext(req).getToken().getRealmAccess().getRoles();
        } else {
            return new HashSet<>();
        }
    }
    
    public static boolean isSuperUser(Request req) {
        return isUserInRhelmRole(req, "superuser");
    }
    
    public static boolean isUserInRhelmRole(Request req, String roleKey) {
        if (isSignedIn(req)) {
            String role = roles.getProperty(roleKey);
            return getKeycloakSecurityContext(req).getToken().getRealmAccess().isUserInRole(role);
        } else {
            return false;
        }
        
    }
    
    public static boolean isUserInAppRole(Request req, String roleKey) {
        if (isSignedIn(req)) {
            String role = roles.getProperty(roleKey);
            return getResourceAccess(req).get(Constants.RHELM_NAME).isUserInRole(role);
        } else {
            return false;
        }
    }
    
    public static Map<String, Access> getResourceAccess(Request req) {
        return getKeycloakSecurityContext(req).getToken().getResourceAccess();
    }
    
    public static boolean isSignedIn(Request req) {
        return getKeycloakSecurityContext(req) != null;
    }
    
    public static KeycloakSecurityContext getKeycloakSecurityContext(Request req) {
        HttpServletRequest httpReq = (HttpServletRequest) req.getContainerRequest();
        return (KeycloakSecurityContext) httpReq.getAttribute(KeycloakSecurityContext.class.getName());
    }
    
    public static KeycloakSecurityContext getKeycloakSecurityContext() {
        HttpServletRequest httpReq = (HttpServletRequest) RequestCycle.get().getRequest().getContainerRequest();
        return (KeycloakSecurityContext) httpReq.getAttribute(KeycloakSecurityContext.class.getName());
    }
    
    public static void checkPermissions(WebPage page, String roleKey, PageParameters params) {
        System.out.println("--checkPermissions--" + page.toString() + "role: " + roleKey + " params: " + params + "**");
        if (!isUserInAppRole(page.getRequest(), roleKey)) {
            if (isSignedIn(page.getRequest())) {
                page.setResponsePage(AccessDenied.class);
            } else {
                params.add("page", page.getClass().getName());
                page.setResponsePage(Login.class, params);
            }
        }
    }
}
