package com.issuetracker.web;

import com.issuetracker.web.security.KeycloakService;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 *
 * @author vramik
 */
public class Constants {
    public static final String HOST = System.getProperty("host", "localhost");
    public static final String PORT = System.getProperty("port", "8080");
    public static final String RHELM_NAME = System.getProperty("rhelm.name", "issue-tracker");
    
    public static final String SERVER_URL = "http://" + HOST + ":" + PORT;
    public static final String CONTEXT_ROOT = "/IssueTracker/";
    public static final String HOME_PAGE = SERVER_URL + CONTEXT_ROOT;
    
//    public static final String JPATablePreffix = "IssueTracker_"; //keycloakDS
    public static final String JPATablePreffix = ""; //separateDS
    
    public static final Properties roles;
    
    static {
        Properties fallback = new Properties();
        fallback.put("key", "default");
        roles = new Properties(fallback);
        
        try (InputStream is = KeycloakService.class.getResourceAsStream("roles.properties")) {
            if (is == null) {
                throw new RuntimeException("KeycloakService.class.getResourceAsStream(\"roles.properties\") has returned null.");
            }
            
            roles.load(is);
            for (String propertyName : roles.stringPropertyNames()) {
                roles.getProperty(propertyName);
            }
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

}
