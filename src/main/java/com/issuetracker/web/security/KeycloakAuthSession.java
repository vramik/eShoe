
package com.issuetracker.web.security;

import com.issuetracker.web.Constants;
import static com.issuetracker.web.Constants.roles;
import java.util.HashSet;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import org.apache.wicket.request.cycle.RequestCycle;
import org.jboss.resteasy.logging.Logger;
import org.keycloak.KeycloakSecurityContext;
import org.keycloak.representations.IDToken;

/**
 *
 * @author vramik
 */
public class KeycloakAuthSession {
    
    private static final Logger log = Logger.getLogger(KeycloakAuthSession.class);
    
    public static KeycloakSecurityContext getKeycloakSecurityContext() {
        RequestCycle requestCycle = RequestCycle.get();
        if (requestCycle == null) {
            return null;
        }
        HttpServletRequest httpReq = (HttpServletRequest) requestCycle.getRequest().getContainerRequest();
        return (KeycloakSecurityContext) httpReq.getAttribute(KeycloakSecurityContext.class.getName());
    }
    
    public static boolean isSignedIn() {
        return getKeycloakSecurityContext() != null;
    }

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
            Set<String> publicRole = new HashSet<>();
            publicRole.add("Public");
            return publicRole;
        }
    }
    
    public static Set<String> getUserAppRoles() {
        if (isSignedIn()) {
            return getKeycloakSecurityContext().getToken().getResourceAccess().get(Constants.RHELM_NAME).getRoles();
        } else {
            return new HashSet<>();
        }
    }
    
    public static boolean isUserInRhelmRole(String role) {
        log.warn("TODO");
        if (isSignedIn()) {
            return getKeycloakSecurityContext().getToken().getRealmAccess().isUserInRole(role);
        } else {
//            return role.equals("Public");
            return false;
        }
    }
    
    public static boolean isUserInAppRole(String roleKey) {
        if (isSignedIn()) {
            String role = roles.getProperty(roleKey);
            return getKeycloakSecurityContext().getToken().getResourceAccess().get(Constants.RHELM_NAME).isUserInRole(role);
        } else {
            return false;
        }
    }
}
