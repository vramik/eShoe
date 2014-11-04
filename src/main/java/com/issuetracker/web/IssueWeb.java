package com.issuetracker.web;

import com.issuetracker.pages.transition.AddTransition;
import com.issuetracker.pages.workflow.CreateWorkflow;
import com.issuetracker.pages.*;
import com.issuetracker.pages.issue.*;
import com.issuetracker.pages.issuetype.*;
import com.issuetracker.pages.permissions.*;
import com.issuetracker.pages.project.*;
import com.issuetracker.pages.status.*;
import com.issuetracker.pages.workflow.WorkflowDetail;

import net.ftlines.wicket.cdi.CdiConfiguration;
import org.apache.wicket.Page;
import org.apache.wicket.protocol.http.WebApplication;
import javax.enterprise.inject.spi.BeanManager;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import static net.ftlines.wicket.cdi.ConversationPropagation.NONE;

/**
 *
 * @author mgottval
 */
public class IssueWeb extends WebApplication {

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
        mountPage("/home", HomePage.class);
        mountPage("/newProject", CreateProject.class);
        mountPage("/projects", ListProjects.class);
        mountPage("/projectDetail", ProjectDetail.class);
        mountPage("/newIssue", CreateIssue.class);
        mountPage("/issueDetail", IssueDetail.class);
        mountPage("/searchIssues", SearchIssues.class);
        mountPage("/newIssueType", CreateIssueType.class);
        mountPage("/editIssueType", EditIssueType.class);
        mountPage("/createStatus", CreateStatus.class);
        mountPage("/editStatus", EditStatus.class);
        mountPage("/createWorkflow", CreateWorkflow.class);
        mountPage("/workflowDetail", WorkflowDetail.class);
        mountPage("/addTransition", AddTransition.class);
        mountPage("/accessDenied", AccessDenied.class);
    }    
}
