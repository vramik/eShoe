
package com.issuetracker.web.security;

import com.issuetracker.web.Constants;
import static com.issuetracker.web.Constants.roles;
import java.util.HashSet;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import org.apache.wicket.request.cycle.RequestCycle;
import org.jboss.logging.Logger;
import org.keycloak.KeycloakSecurityContext;
import org.keycloak.representations.AccessToken;
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
    
    public static Set<String> getUserRealmRoles() {
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
            AccessToken.Access get = getKeycloakSecurityContext().getToken().getResourceAccess().get(Constants.RHELM_NAME);
            if (get == null) {
                return new HashSet<>();
            }
            return get.getRoles();
        } else {
            return new HashSet<>();
        }
    }
    
    public static boolean isUserInRhelmRole(String role) {
        if (isSignedIn()) {
            return getKeycloakSecurityContext().getToken().getRealmAccess().isUserInRole(role);
        } else {
            return role.equals("Public");
        }
    }
    
    public static boolean isUserInAppRole(String roleKey) {
        if (isSignedIn()) {
            AccessToken.Access get = getKeycloakSecurityContext().getToken().getResourceAccess().get(Constants.RHELM_NAME);
            if (get == null) {
                return false;
            } 
            return get.isUserInRole(roles.getProperty(roleKey));
        } else {
            return false;
        }
    }
}
