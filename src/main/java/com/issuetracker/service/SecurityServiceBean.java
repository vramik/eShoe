package com.issuetracker.service;

import com.issuetracker.model.Issue;
import com.issuetracker.model.Permission;
import com.issuetracker.model.Project;
import com.issuetracker.model.TypeId;
import com.issuetracker.service.api.SecurityService;
import static com.issuetracker.web.Constants.roles;
import com.issuetracker.web.security.KeycloakAuthSession;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import org.jboss.logging.Logger;

/**
 *
 * @author vramik
 */
@Stateless
public class SecurityServiceBean implements SecurityService {

    private final Logger log = Logger.getLogger(SecurityServiceBean.class);
    
//    @Override
    public boolean canUserPerformIssueAction(Issue issue, String action) {
        
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

//    @Override
    public boolean canUserPerformProjectAction(Project project, String action) {
        log.warn("TODO: can user do everything with project if he is the owner?");
        if (KeycloakAuthSession.isSignedIn() && project.getOwner().equals(KeycloakAuthSession.getIDToken().getPreferredUsername())) {//if user is owner of the project
            return true;
        }
        
        Permission permission = null;
//        for (Permission projectPermission : project.getPermissions()) {
//            log.info("action: " + action + ", permission: " + projectPermission);
//            if (as
//        }
        if (permission == null) {
            return false;
        }
//        log.info("related Permission: " + permission + ", roles: " + permission.getRoles());
        
        boolean allowed = false;
//        for (String allowedRole : permission.getRoles()) {
//            allowed = KeycloakAuthSession.isUserInRhelmRole(allowedRole);
//            if (allowed) {
//                break;
//            }
//        }
        return allowed;
    }

    @Override
    public List<String> getPermittedActionsForUserAndItem(TypeId typeId, Long itemId) {
        log.error("TODO getPermittedActionsForUserAndItem");
        List<String> result = new ArrayList<>();
        result.add(roles.getProperty("it.project.rename"));
        result.add(roles.getProperty("it.project.versions"));
        result.add(roles.getProperty("it.project.components"));
        result.add(roles.getProperty("it.project.custom.fields"));
        result.add(roles.getProperty("it.project.permissions"));
        result.add(roles.getProperty("it.project.delete"));
        
        return result;
    }

    @Override
    public boolean canUserPerformAction(TypeId typeId, Long itemId, String action) {
        log.error("TODO canUserPerformAction");
        return true;
    }
    
}
