/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.issuetracker.web;

import com.issuetracker.pages.ForgotPassword;
import com.issuetracker.pages.HomePage;
import com.issuetracker.pages.ListIssues;
import com.issuetracker.pages.Login;
import com.issuetracker.pages.Register;
import javax.enterprise.inject.spi.BeanManager;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import net.ftlines.wicket.cdi.CdiConfiguration;
import static net.ftlines.wicket.cdi.ConversationPropagation.NONE;
import org.apache.wicket.Page;
import org.apache.wicket.protocol.http.WebApplication;

/**
 *
 * @author mgottval
 */
public class IssueWeb extends WebApplication{

    @Override
    public Class<? extends Page> getHomePage() {
        return HomePage.class;
    }
    
    @Override
    protected void init() {
        super.init();

        // Enable CDI
        BeanManager bm; 
        try {
            bm = (BeanManager) new InitialContext().lookup("java:comp/BeanManager");
        } catch (NamingException e) {
            throw new IllegalStateException("Unable to obtain CDI BeanManager", e);
        }

        // Configure CDI, disabling Conversations as we aren't using them
        new CdiConfiguration(bm).setPropagation(NONE).configure(this);
        
        mountPage("/login", Login.class);
        mountPage("/issues", ListIssues.class);
        mountPage("/homepage", HomePage.class);
//        mountPage("/register", Register.class);
//        mountPage("/forgotPassword", ForgotPassword.class);
    }
    
}
