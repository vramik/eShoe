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
import com.issuetracker.service.api.ProjectService;
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
    @Inject private ProjectService projectService;
    
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
        mountPage("/projectPermissions", ProjectPermission.class);
        mountPage("/issuePermissions", IssuePermission.class);
        
        createDefaultPermissions();
    }    

    
//<editor-fold defaultstate="collapsed" desc="defaultPermissions">
    private void createDefaultPermissions() {
        
//        permissionService.createPermissions(TypeId.global, null, roles.getProperty("it.project.create"), TypeId.global, "JBossPM");
        
            permissionService.createPermissions(TypeId.global, null, roles.getProperty("it.project.browse"), TypeId.project, "X");
//        permissionService.createPermissions(TypeId.global, null, roles.getProperty("it.project.rename"), TypeId.project, "JBossPM");
//        permissionService.createPermissions(TypeId.global, null, roles.getProperty("it.project.versions"), TypeId.project, "JBossPM");
//        permissionService.createPermissions(TypeId.global, null, roles.getProperty("it.project.components"), TypeId.project, "JBossPM");
//        permissionService.createPermissions(TypeId.global, null, roles.getProperty("it.project.custom.fields"), TypeId.project, "JBossPM");
//        permissionService.createPermissions(TypeId.global, null, roles.getProperty("it.project.permissions"), TypeId.project, "JBossPM");
//        permissionService.createPermissions(TypeId.global, null, roles.getProperty("it.project.delete"), TypeId.project, "JBossPM");
//        permissionService.createPermissions(TypeId.global, null, roles.getProperty("it.issue.create"), TypeId.project, "RedHat", "Customer");
        
//            permissionService.createPermissions(TypeId.global, null, roles.getProperty("it.issue.browse"), TypeId.issue, "X");
//        permissionService.createPermissions(TypeId.global, null, roles.getProperty("it.issue.edit.description"), TypeId.issue, "Owner");
//        permissionService.createPermissions(TypeId.global, null, roles.getProperty("it.issue.delete"), TypeId.issue, "JBossPM");
//        permissionService.createPermissions(TypeId.global, null, roles.getProperty("it.issue.assign"), TypeId.issue, "JBossPM");
//        permissionService.createPermissions(TypeId.global, null, roles.getProperty("it.issue.resolve"), TypeId.issue, "RedHat");
//        permissionService.createPermissions(TypeId.global, null, roles.getProperty("it.issue.close"), TypeId.issue, "RedHat");
//        permissionService.createPermissions(TypeId.global, null, roles.getProperty("it.comment.add"), TypeId.issue, "RedHat", "Customer");
//        permissionService.createPermissions(TypeId.global, null, roles.getProperty("it.comment.edit.all"), TypeId.issue, "JBossPM");
//        permissionService.createPermissions(TypeId.global, null, roles.getProperty("it.comment.edit.own"), TypeId.issue, "RedHat");
//        permissionService.createPermissions(TypeId.global, null, roles.getProperty("it.comment.delete.all"), TypeId.issue, "JBossPM");
//        permissionService.createPermissions(TypeId.global, null, roles.getProperty("it.comment.delete.own"), TypeId.issue, "RedHat");

//        Project p = projectService.getProjectById(1L);
        
//        permissionService.createPermissions(TypeId.project, p, roles.getProperty("it.project.browse"), TypeId.project, "Public");
//        permissionService.createPermissions(TypeId.project, p, roles.getProperty("it.project.rename"), TypeId.project, "JBossPM");
//        permissionService.createPermissions(TypeId.project, p, roles.getProperty("it.project.versions"), TypeId.project, "JBossPM");
//        permissionService.createPermissions(TypeId.project, p, roles.getProperty("it.project.components"), TypeId.project, "JBossPM");
//        permissionService.createPermissions(TypeId.project, p, roles.getProperty("it.project.custom.fields"), TypeId.project, "JBossPM");
//        permissionService.createPermissions(TypeId.project, p, roles.getProperty("it.project.permissions"), TypeId.project, "JBossPM");
//        permissionService.createPermissions(TypeId.project, p, roles.getProperty("it.project.delete"), TypeId.project, "JBossPM");
//        permissionService.createPermissions(TypeId.project, p, roles.getProperty("it.issue.create"), TypeId.project, "RedHat", "Customer");
//        
//        permissionService.createPermissions(TypeId.project, p, roles.getProperty("it.issue.browse"), TypeId.issue, "Public");
//        permissionService.createPermissions(TypeId.project, p, roles.getProperty("it.issue.edit.description"), TypeId.issue, "Owner");
//        permissionService.createPermissions(TypeId.project, p, roles.getProperty("it.issue.delete"), TypeId.issue, "JBossPM");
//        permissionService.createPermissions(TypeId.project, p, roles.getProperty("it.issue.assign"), TypeId.issue, "JBossPM");
//        permissionService.createPermissions(TypeId.project, p, roles.getProperty("it.issue.resolve"), TypeId.issue, "RedHat");
//        permissionService.createPermissions(TypeId.project, p, roles.getProperty("it.issue.close"), TypeId.issue, "RedHat");
//        
//        permissionService.createPermissions(TypeId.project, p, roles.getProperty("it.comment.add"), TypeId.comment, "RedHat", "Customer");
//        permissionService.createPermissions(TypeId.project, p, roles.getProperty("it.comment.edit.all"), TypeId.comment, "JBossPM");
//        permissionService.createPermissions(TypeId.project, p, roles.getProperty("it.comment.edit.own"), TypeId.comment, "RedHat");
//        permissionService.createPermissions(TypeId.project, p, roles.getProperty("it.comment.delete.all"), TypeId.comment, "JBossPM");
//        permissionService.createPermissions(TypeId.project, p, roles.getProperty("it.comment.delete.own"), TypeId.comment, "RedHat");
        
        
        
        System.err.println("CREATING DEFAULT PERMISSIONS");
        
        
       
//        Permission p02a = newPermission(1, roles.getProperty("it.project.browse"), TypeId.project, "Public");
//        Permission p02b = newPermission(2, roles.getProperty("project.versions"), TypeId.project, "JBossQA", "RedHat");
//        Permission p02c = newPermission(3, roles.getProperty("project.components"), TypeId.project, "JBossQA", "RedHat");
//        Permission p02d = newPermission(4, roles.getProperty("project.custom.fields"), TypeId.project, "JBossQA", "RedHat");
//        Permission p02e = newPermission(5, roles.getProperty("project.permissions"), TypeId.project, "JBossQA", "RedHat");
//        Permission p02f = newPermission(6, roles.getProperty("project.delete"), TypeId.project, "JBossQA", "RedHat");
//        Permission p02g = newPermission(7, roles.getProperty("project.rename"), TypeId.project, "JBossQA", "RedHat");
//        
//        Permission p04 = newPermission(8, "BulkUpdate", TypeId.project, "JBossQA");
//        Permission p05 = newPermission(9, "BulkDelete", TypeId.project, "JBossQA");
//        Permission p07 = newPermission(11, "Create Issues", TypeId.project, "SignedIn");
//        Permission p08 = newPermission(12, "Read Issues", TypeId.project, "JBossQA");
//        Permission p09 = newPermission(13, "Delete Issues", TypeId.project, "JBossQA");
//        Permission p10 = newPermission(14, "Assign Issues", TypeId.project, "JBossQA");
//        Permission p11 = newPermission(15, "Resolve Issues", TypeId.project, "JBossQA");
//        Permission p12 = newPermission(16, "Close Issues", TypeId.project, "JBossQA");
//        
//        Permission p13 = newPermission(17, "Read Issue", TypeId.issue, "JBossQA");//overrides p08
//        Permission p14 = newPermission(18, "Edit Issue Description", TypeId.issue, "JBossQA");
//        Permission p15 = newPermission(19, "Delete Issue", TypeId.issue, "JBossQA");//overrides p09
//        Permission p16 = newPermission(20, "Add Comments", TypeId.issue, "JBossQA");
//        Permission p17 = newPermission(21, "Edit All Comments", TypeId.issue, "JBossQA");
//        Permission p18 = newPermission(22, "Edit Own Comments", TypeId.issue, "JBossQA");
//        Permission p19 = newPermission(23, "Delete All Comments", TypeId.issue, "JBossQA");
//        Permission p20 = newPermission(24, "Delete Own Comments",TypeId.issue, "JBossQA");
//        
//        if (permissionService.getDefaultPermissions().isEmpty()) {
////            permissionService.insert(p00);
////            permissionService.insert(p01);
//            permissionService.insert(p02a);
//            permissionService.insert(p02b);
//            permissionService.insert(p02c);
//            permissionService.insert(p02d);
//            permissionService.insert(p02e);
//            permissionService.insert(p02f);
//            permissionService.insert(p02g);
//            
//            permissionService.insert(p04);
//            permissionService.insert(p05);
//            permissionService.insert(p07);
//            permissionService.insert(p08);
//            permissionService.insert(p09);
//            permissionService.insert(p10);
//            permissionService.insert(p11);
//            permissionService.insert(p12);
//            permissionService.insert(p13);
//            permissionService.insert(p14);
//            permissionService.insert(p15);
//            permissionService.insert(p16);
//            permissionService.insert(p17);
//            permissionService.insert(p18);
//            permissionService.insert(p19);
//            permissionService.insert(p20);
//        } else {
////            permissionService.update(p00);
////            permissionService.update(p01);
////            permissionService.update(p02);
////            permissionService.update(p03);
////            permissionService.update(p04);
////            permissionService.update(p05);
////            permissionService.update(p06);
////            permissionService.update(p07);
////            permissionService.update(p08);
////            permissionService.update(p09);
////            permissionService.update(p10);
////            permissionService.update(p11);
////            permissionService.update(p12);
////            permissionService.update(p13);
////            permissionService.update(p14);
////            permissionService.update(p15);
////            permissionService.update(p16);
////            permissionService.update(p17);
////            permissionService.update(p18);
////            permissionService.update(p19);
////            permissionService.update(p20);
//        }
////        log.error("STORED PERMISSIONS:\n");
////        for (Permission defaultPermission : permissionService.getDefaultPermissions()) {
////            log.info(defaultPermission);
////        }
    }
    
//    private Permission newPermission(int displayOrder, String name, TypeId type, String... roleNames) {
//        Permission permission = new Permission();
//        permission.setName(name);
//        permission.setDisplayOrder(displayOrder);
//        permission.setPermissionType(type);
//        permission.setRoles(Arrays.asList(roleNames));
//        permission.setDefaultPermission(true);
////        log.warn("new permission: " + permission);
//        return permission;
//    }
//</editor-fold>
}
