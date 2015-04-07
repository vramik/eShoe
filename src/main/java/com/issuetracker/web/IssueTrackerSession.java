package com.issuetracker.web;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.apache.wicket.Session;
import org.apache.wicket.protocol.http.WebSession;
import org.apache.wicket.request.Request;
import org.jboss.logging.Logger;

/**
 * Issue Tracker Session 
 * 
 * @author vramik
 */
public class IssueTrackerSession extends WebSession {
    private final Logger log = Logger.getLogger(IssueTrackerSession.class);
    private final Map<String, Boolean> cache;

    public IssueTrackerSession(Request request) {
        super(request);
        cache = new HashMap<>();
    }

    public void put(String key, Boolean value) {
        log.debug("Caching: " + key + ", " + value);
        cache.put(key, value);
    }
    
    public Boolean get(String key) {
        return cache.get(key);
    }
    
    public void clearCache() {
        cache.clear();
    }
    
    public static IssueTrackerSession get() {
        return (IssueTrackerSession) Session.get();
    }

    public Map<String, Boolean> getCache() {
        return Collections.unmodifiableMap(cache);
    }
    
}
