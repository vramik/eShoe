package com.issuetracker.web.security;

import static com.issuetracker.web.Constants.SERVER_URL;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.keycloak.adapters.HttpClientBuilder;
import static com.issuetracker.web.security.KeycloakAuthSession.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.wicket.request.Request;
import org.keycloak.KeycloakSecurityContext;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.keycloak.util.JsonSerialization;

/**
 *
 * @author vramik
 */
public class KeycloakService {
    
    private static class TypedListOfUser extends ArrayList<UserRepresentation> {
    }
    private static class TypedSetOfRoles extends HashSet<RoleRepresentation> {
    }
    
    public static class Failure extends Exception {
        private final int status;

        public Failure(int status) {
            this.status = status;
        }

        public int getStatus() {
            return status;
        }
    }
    
    public static List<UserRepresentation> getUsers(Request req) throws Failure {
        HttpClient client = new HttpClientBuilder().disableTrustManager().build();
        
        try {
            HttpGet get = new HttpGet(SERVER_URL + "/auth/admin/realms/issue-tracker/users");
            System.out.println("GET: " + get.toString());
            KeycloakSecurityContext session = getKeycloakSecurityContext(req);
            get.addHeader("Authorization", "Bearer " + session.getTokenString());
            System.out.println("GET HEADER: " + Arrays.toString(get.getHeaders("Authorization")));
            try {
                HttpResponse response = client.execute(get);
                if (response.getStatusLine().getStatusCode() != 200) {
                    System.out.println("STATUS CODE: " + response.getStatusLine().getStatusCode());
                    throw new Failure(response.getStatusLine().getStatusCode());
                }
                HttpEntity entity = response.getEntity();
                try (InputStream is = entity.getContent()) {
                    return JsonSerialization.readValue(is, TypedListOfUser.class);
                }
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        } finally {
            client.getConnectionManager().shutdown();
        }
    }
    
    /**
     * 
     * @param req
     * @return String rhelm roles without superuser and user
     * 
     * @throws com.issuetracker.web.security.KeycloakService.Failure 
     */
    public static Set<String> getRhelmRoles(Request req) throws Failure {
        HttpClient client = new HttpClientBuilder().disableTrustManager().build();
        
        try {
            HttpGet get = new HttpGet(SERVER_URL + "/auth/admin/realms/issue-tracker/roles");
//            System.out.println("GET: " + get.toString());
            KeycloakSecurityContext session = getKeycloakSecurityContext(req);
            if (session == null) {
                return new HashSet<>();
            }
            get.addHeader("Authorization", "Bearer " + session.getTokenString());
//            System.out.println("GET HEADER: " + Arrays.toString(get.getHeaders("Authorization")));
            try {
                HttpResponse response = client.execute(get);
                if (response.getStatusLine().getStatusCode() != 200) {
//                    System.out.println("STATUS CODE: " + response.getStatusLine().getStatusCode());
                    throw new Failure(response.getStatusLine().getStatusCode());
                }
                HttpEntity entity = response.getEntity();
                try (InputStream is = entity.getContent()) {
                    Set<String> roles = new TreeSet<>();
                    for (RoleRepresentation role : JsonSerialization.readValue(is, TypedSetOfRoles.class)) {
                        if (!role.getName().equals("user")) {
                            roles.add(role.getName());
                        }
                    }
                    return roles;
                }
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        } finally {
            client.getConnectionManager().shutdown();
        }
    }
    
}
