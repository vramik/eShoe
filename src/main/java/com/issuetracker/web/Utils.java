package com.issuetracker.web;

import org.apache.wicket.util.string.StringValue;

/**
 *
 * @author vramik
 */
public class Utils {
    public static Class parsePageClassFromPageParams(StringValue pageSV) {
        try {
            return Class.forName(pageSV.toString());
        } catch (ClassNotFoundException ex) {
            throw new RuntimeException(ex);
        }
    }
    
//    private String getCurrentContextPath() {
//        return getRequestCycle().getRequest().getUrl().toString().split("\\?")[0];
//    }
}
