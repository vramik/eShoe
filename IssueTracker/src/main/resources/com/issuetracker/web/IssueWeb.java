/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.issuetracker.web;

import com.issuetracker.pages.ForgotPassword;
import com.issuetracker.pages.Login;
import com.issuetracker.pages.Register;
import org.apache.wicket.Page;
import org.apache.wicket.protocol.http.WebApplication;

/**
 *
 * @author mgottval
 */
public class IssueWeb extends WebApplication{

    @Override
    public Class<? extends Page> getHomePage() {
        return Register.class;
    }
    
    @Override
    protected void init() {
//        mountPage("/register", Register.class);
//        mountPage("/forgotPassword", ForgotPassword.class);
    }
    
}
