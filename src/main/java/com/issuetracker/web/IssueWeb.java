package com.issuetracker.web;

import com.issuetracker.pages.createIssue.CreateIssue;
import com.issuetracker.pages.CreateIssueType;
import com.issuetracker.pages.CreateProject;
import com.issuetracker.pages.ForgotPassword;
import com.issuetracker.pages.HomePage;
import com.issuetracker.pages.ListProjects;
import com.issuetracker.pages.Login;
import com.issuetracker.pages.Register;
import com.issuetracker.pages.SearchIssues;
import com.issuetracker.pages.IssueDetail;
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
        
//        mountPage("/login", LoginReq.class);
        mountPage("/login", Login.class);
        mountPage("/register", Register.class);
        mountPage("/forgotPassword", ForgotPassword.class);        
        mountPage("/home", HomePage.class);
//        mountPage("/register", RegisterReq.class);
        mountPage("/newProject", CreateProject.class);
        mountPage("/projects", ListProjects.class);
        mountPage("/newIssue", CreateIssue.class);
        mountPage("/issueDetail", IssueDetail.class);
        mountPage("/searchIssues", SearchIssues.class);
        mountPage("/newIssueType", CreateIssueType.class);
        
        
        
    }
    
}
