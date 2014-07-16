
package com.issuetracker.web.security;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.wicket.request.http.WebRequest;
import org.apache.wicket.request.http.WebResponse;
import org.keycloak.KeycloakSecurityContext;
import org.keycloak.ServiceUrlConstants;
import org.keycloak.adapters.HttpClientBuilder;
import org.keycloak.representations.AccessTokenResponse;
import org.keycloak.representations.IDToken;
import org.keycloak.util.KeycloakUriBuilder;

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
    public static IDToken getIDToken(WebRequest req) {
        if (isSignedIn(req)) {
            return getKeycloakSecurityContext(req).getIdToken();
        } else {
            return null;
        }
    }
    
    public static boolean isSignedIn(WebRequest req) {
        return getKeycloakSecurityContext(req) != null;
    }
    
    private static KeycloakSecurityContext getKeycloakSecurityContext(WebRequest req) {
        HttpServletRequest httpReq = ((HttpServletRequest) req.getContainerRequest());
        return (KeycloakSecurityContext) httpReq.getAttribute(KeycloakSecurityContext.class.getName());
    }
    
    public static void signOut(WebRequest req, WebResponse res) {
        HttpServletRequest httpReq = ((HttpServletRequest) req.getContainerRequest());
//        HttpServletResponse httpRes = ((HttpServletResponse) res.getContainerResponse());
        try {
            httpReq.logout();
            String logoutUri = KeycloakUriBuilder.fromUri("http://localhost:8080/auth").path(ServiceUrlConstants.TOKEN_SERVICE_LOGOUT_PATH)
            .queryParam("redirect_uri", "http://localhost:8080/IssueTracker/home").build("demo").toString();
            System.out.println("LOGOUT_URI: " + logoutUri);
//            httpRes.sendRedirect("http://localhost:8080/auth/realms/demo/tokens/logout?redirect_uri=http%3A%2F%2Flocalhost%3A8080%2FIssueTracker");
            res.sendRedirect(logoutUri);
            System.out.println("----LOGOUT----");
        } catch (Exception ex) {
            Logger.getLogger(KeycloakAuthSession.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace(System.err);
        }
    }
    
//    public static void logout(AccessTokenResponse res) throws IOException {
//
//        HttpClient client = new HttpClientBuilder()
//                .disableTrustManager().build();
//
//
//        try {
//            HttpGet get = new HttpGet(KeycloakUriBuilder.fromUri("http://localhost:8080/auth")
//                    .path(ServiceUrlConstants.TOKEN_SERVICE_LOGOUT_PATH)
//                    .queryParam("session_state", res.getSessionState())
//                    .build("demo"));
//            HttpResponse response = client.execute(get);
//            HttpEntity entity = response.getEntity();
//            if (entity == null) {
//                return;
//            }
//            InputStream is = entity.getContent();
//            if (is != null) is.close();
//        } finally {
//            client.getConnectionManager().shutdown();
//        }
//    }
    
    
}
