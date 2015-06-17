package com.issuetracker.web.security;

import static com.issuetracker.web.Constants.SERVER_URL;
import static com.issuetracker.web.security.KeycloakAuthSession.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;
import org.jboss.logging.Logger;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.keycloak.KeycloakSecurityContext;
import org.keycloak.representations.idm.RoleRepresentation;

/**
 *
 * @author vramik
 */
public class KeycloakService {
    private static final Logger log = Logger.getLogger(KeycloakService.class);
    
    private static class TypedSetOfRoles extends HashSet<RoleRepresentation> {
    }
    
    private static class AuthHedersRequestFilter implements ClientRequestFilter {

        private final String tokenString;
        
        public AuthHedersRequestFilter(KeycloakSecurityContext session) {
            tokenString = session.getTokenString();
        }
        
        @Override
        public void filter(ClientRequestContext requestContext) throws IOException {
            requestContext.getHeaders().add("Authorization", "Bearer " + tokenString);
        }
    }
    
    /**
     * 
     * @return String realm roles with 'Public'
     * 
     */
    public static List<String> getRealmRoles() {
        Set<String> roles = new TreeSet<>();
        roles.add("Public");
        
        KeycloakSecurityContext session = getKeycloakSecurityContext();
        if (session == null) {
            return new ArrayList<>(roles);
        }
        
        ResteasyClient client = new ResteasyClientBuilder().build();
        client.register(new AuthHedersRequestFilter(session));
        try {
            TypedSetOfRoles typedSetOfRoles = client.target(SERVER_URL + "/auth/admin/realms/issue-tracker/roles").request().get(TypedSetOfRoles.class);
        
            for (RoleRepresentation typedSetOfRole : typedSetOfRoles) {
                roles.add(typedSetOfRole.getName());
            }
        } catch (WebApplicationException e) {
            //in case the response status code of the response returned by the server is not successful
        }
        return new ArrayList<>(roles);
    }
    
}
