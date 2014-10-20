package com.issuetracker.web;

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
    public static final String CONTEXT_ROOT = "/IssueTracker/";
    public static final String RHELM_NAME = System.getProperty("rhelm.name", "issue-tracker");
    
    public static final String SERVER_URL = "http://" + HOST + ":" + PORT;
    
    public static final Properties roles;
    
    static {
        Properties fallback = new Properties();
        fallback.put("key", "default");
        roles = new Properties(fallback);
        
        try (InputStream is = Constants.class.getResourceAsStream("roles.properties")) {
            if (is == null) {
                throw new RuntimeException("Constants.class.getResourceAsStream(\"roles.properties\") has returned null.");
            }
            
//            BufferedReader b = new BufferedReader(new InputStreamReader(is));
//            while (b.ready()) {
//                System.out.println(b.readLine());
//            }
            
            roles.load(is);
            for (String propertyName : roles.stringPropertyNames()) {
                roles.getProperty(propertyName);
            }
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

}
