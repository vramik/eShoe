
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
     * 
     * @return IDToken or null if user is not signed in
     */
    public static IDToken getIDToken() {
        if (isSignedIn()) {
            return getKeycloakSecurityContext().getIdToken();
        } else {
            return null;
        }
    }
    
    public static Set<String> getUserRhelmRoles() {
        if (isSignedIn()) {
            return getKeycloakSecurityContext().getToken().getRealmAccess().getRoles();
        } else {
            return new HashSet<>();
        }
    }
    
    public static boolean isSuperUser() {
        return isUserInRhelmRole("superuser");
    }
    
    public static boolean isUserInRhelmRole(String roleKey) {
        if (isSignedIn()) {
            String role = roles.getProperty(roleKey);
            return getKeycloakSecurityContext().getToken().getRealmAccess().isUserInRole(role);
        } else {
            return false;
        }
        
    }
    
    public static boolean isUserInAppRole(String roleKey) {
        if (isSignedIn()) {
            String role = roles.getProperty(roleKey);
            return getResourceAccess().get(Constants.RHELM_NAME).isUserInRole(role);
        } else {
            return false;
        }
    }
    
    public static Map<String, Access> getResourceAccess() {
        return getKeycloakSecurityContext().getToken().getResourceAccess();
    }
    
    public static boolean isSignedIn() {
        return getKeycloakSecurityContext() != null;
    }
    
    public static KeycloakSecurityContext getKeycloakSecurityContext() {
        RequestCycle requestCycle = RequestCycle.get();
        if (requestCycle == null) {
            return null;
        }
        HttpServletRequest httpReq = (HttpServletRequest) requestCycle.getRequest().getContainerRequest();
        return (KeycloakSecurityContext) httpReq.getAttribute(KeycloakSecurityContext.class.getName());
    }
    
    public static void checkPermissions(WebPage page, String roleKey, PageParameters params) {
        System.out.println("--checkPermissions--" + page.toString() + "role: " + roleKey + " params: " + params + "**");
        if (!isUserInAppRole(roleKey)) {
            if (isSignedIn()) {
                page.setResponsePage(AccessDenied.class);
            } else {
                params.add("page", page.getClass().getName());
                page.setResponsePage(Login.class, params);
            }
        }
    }
}
