package com.issuetracker.web;

import com.issuetracker.model.TypeId;
import com.issuetracker.pages.issue.SearchIssues;
import com.issuetracker.pages.transition.AddTransition;
import com.issuetracker.pages.workflow.CreateWorkflow;
import com.issuetracker.pages.*;
import com.issuetracker.pages.issue.*;
import com.issuetracker.pages.issuetype.*;
import com.issuetracker.pages.permissions.*;
import com.issuetracker.pages.project.*;
import com.issuetracker.pages.status.*;
import com.issuetracker.pages.workflow.WorkflowDetail;
import com.issuetracker.service.api.PermissionService;
import static com.issuetracker.web.Constants.roles;


import net.ftlines.wicket.cdi.CdiConfiguration;
import org.apache.wicket.Page;
import org.apache.wicket.protocol.http.WebApplication;
import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import static net.ftlines.wicket.cdi.ConversationPropagation.NONE;
import org.apache.wicket.Session;
import org.apache.wicket.request.Request;
import org.apache.wicket.request.Response;
import org.jboss.logging.Logger;

/**
 *
 * @author mgottval
 */
public class IssueWeb extends WebApplication {

    private final Logger log = Logger.getLogger(IssueWeb.class);
    @Inject private PermissionService permissionService;
    
    @Override
    public Class<? extends Page> getHomePage() {
        return HomePage.class;
    }
    
    @Override
    public final Session newSession(Request request, Response response) {
        IssueTrackerSession session = new IssueTrackerSession(request);
        session.bind();
        return session;
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
        mountPage("/permissions", GlobalPermission.class);
        mountPage("/projectPermissions", ProjectPermission.class);
        mountPage("/issuePermissions", IssuePermission.class);
        
        createGlobalPermissions();
    }    

    
//<editor-fold defaultstate="collapsed" desc="defaultPermissions">
    private void createGlobalPermissions() {
        
        permissionService.createPermissions(TypeId.global, null, roles.getProperty("it.status"), TypeId.global, "ITAdmin");
        permissionService.createPermissions(TypeId.global, null, roles.getProperty("it.workflow"), TypeId.global, "ITAdmin");
        permissionService.createPermissions(TypeId.global, null, roles.getProperty("it.import"), TypeId.global, "ITAdmin");
        permissionService.createPermissions(TypeId.global, null, roles.getProperty("it.project.create"), TypeId.global, "ITAdmin");
        permissionService.createPermissions(TypeId.global, null, roles.getProperty("it.issue.type"), TypeId.global, "ITAdmin");
        permissionService.createPermissions(TypeId.global, null, roles.getProperty("it.permissions"), TypeId.global, "ITAdmin");
        
        permissionService.createPermissions(TypeId.global, null, roles.getProperty("it.project.browse"), TypeId.project, "ITAdmin", "Public");
        permissionService.createPermissions(TypeId.global, null, roles.getProperty("it.project.versions"), TypeId.project, "ITAdmin", "X");
        permissionService.createPermissions(TypeId.global, null, roles.getProperty("it.project.components"), TypeId.project, "ITAdmin", "X");
        permissionService.createPermissions(TypeId.global, null, roles.getProperty("it.project.custom.fields"), TypeId.project, "ITAdmin", "X");
        permissionService.createPermissions(TypeId.global, null, roles.getProperty("it.project.permissions"), TypeId.project, "ITAdmin", "X");
        permissionService.createPermissions(TypeId.global, null, roles.getProperty("it.project.delete"), TypeId.project, "ITAdmin", "X");
        permissionService.createPermissions(TypeId.global, null, roles.getProperty("it.project.rename"), TypeId.project, "ITAdmin", "X");
        permissionService.createPermissions(TypeId.global, null, roles.getProperty("it.project.create.issue"), TypeId.project, "ITAdmin", "Public");
        
        permissionService.createPermissions(TypeId.global, null, roles.getProperty("it.issue.browse"), TypeId.issue, "ITAdmin", "Public");
        permissionService.createPermissions(TypeId.global, null, roles.getProperty("it.issue.edit.description"), TypeId.issue, "ITAdmin", "X");
        permissionService.createPermissions(TypeId.global, null, roles.getProperty("it.issue.delete"), TypeId.issue, "ITAdmin", "X");
        permissionService.createPermissions(TypeId.global, null, roles.getProperty("it.issue.assign"), TypeId.issue, "ITAdmin", "X");
        permissionService.createPermissions(TypeId.global, null, roles.getProperty("it.issue.resolve"), TypeId.issue, "ITAdmin", "X");
        permissionService.createPermissions(TypeId.global, null, roles.getProperty("it.issue.close"), TypeId.issue, "ITAdmin", "X");
        permissionService.createPermissions(TypeId.global, null, roles.getProperty("it.issue.comment.add"), TypeId.issue, "ITAdmin", "X");
        permissionService.createPermissions(TypeId.global, null, roles.getProperty("it.issue.comment.edit.all"), TypeId.issue, "ITAdmin", "X");
        permissionService.createPermissions(TypeId.global, null, roles.getProperty("it.issue.comment.edit.own"), TypeId.issue, "ITAdmin", "X");
        permissionService.createPermissions(TypeId.global, null, roles.getProperty("it.issue.comment.delete.all"), TypeId.issue, "ITAdmin", "X");
        permissionService.createPermissions(TypeId.global, null, roles.getProperty("it.issue.comment.delete.own"), TypeId.issue, "ITAdmin", "X");
        permissionService.createPermissions(TypeId.global, null, roles.getProperty("it.issue.permission"), TypeId.project, "ITAdmin", "X");
        
        permissionService.createPermissions(TypeId.global, null, roles.getProperty("it.comment.browse"), TypeId.comment, "ITAdmin", "Public");
        
        
    }
//</editor-fold>
}
