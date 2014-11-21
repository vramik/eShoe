package com.issuetracker.web.security;

import com.issuetracker.model.Comment;
import com.issuetracker.model.Permission;
import com.issuetracker.model.PermissionType;
import com.issuetracker.model.Project;
import com.issuetracker.pages.HomePage;
import com.issuetracker.service.api.IssueService;
import com.issuetracker.web.quilifiers.ServiceSecurity;
import static com.issuetracker.web.security.KeycloakAuthSession.*;
import static com.issuetracker.web.security.KeycloakService.getRhelmRoles;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import org.apache.wicket.RestartResponseException;
import org.apache.wicket.ThreadContext;
import org.jboss.logging.Logger;

/**
 *
 * @author vramik
 */
public class PermissionsUtil {

    public static boolean isProjectOwner(Project project) {
        return isSignedIn() && project.getOwner().equals(getIDToken().getPreferredUsername());
    }
    
    public static boolean hasPermissionsProject(Project project, PermissionType type) {
        if (isSuperUser() || isProjectOwner(project)) {
            return true;
        }
        Permission p = new Permission();
        p.setPermissionType(type);
        for (Permission permission : project.getPermissions()) {
            if (permission.equals(p)) {
                for (String role : permission.getRoles()) {
                    if (role.equals("public") || isUserInAppRole(role)) {
                        return true;
                    }
                }
            }            
        }
        return false;
    }
    
    //TODO review if it is necesary
    public static List<Permission> checkDefaultProjectPermissions(List<Permission> permissions) {
        Permission view = new Permission(); 
        view.setPermissionType(PermissionType.view);
        if (!permissions.contains(view)) {
            HashSet<String> defaultRoles = new HashSet<>();
            defaultRoles.add("public");
            view.setRoles(defaultRoles);
            permissions.add(view);
        }
        Permission edit = new Permission();
        edit.setPermissionType(PermissionType.edit);
        if (!permissions.contains(edit)) {
            HashSet<String> defaultRoles = new HashSet<>();
            defaultRoles.add("superuser");
            edit.setRoles(defaultRoles);
            permissions.add(edit);
        }
        return permissions;
    }
    
    public static List<Project> getProjectWithEditPermissions(List<Project> projects) {
        List<Project> result = new ArrayList<>();
        for (Project project : projects) {
            if (hasPermissionsProject(project, PermissionType.edit)) {
                result.add(project);
            }
        }
        return result;
    }
    
    public static List<String> getAvailableRoles() {
        try {
            return new ArrayList<>(getRhelmRoles());
        } catch (KeycloakService.Failure f) {
            throw new RuntimeException("Returned status code was: " + f.getStatus(), f);
        }
    }
    
    public static boolean hasViewPermissionComment(Comment comment) {
        if (comment == null || comment.getViewPermission() == null) {
            return false;
        }
        if (isSuperUser() || isCommentOwner(comment)) {
            return true;
        }
        for (String role : comment.getViewPermission().getRoles()) {
            if (role.equals("public") || isUserInAppRole(role)) {
                return true;
            }
        }
        return false;
    }

    private static boolean isCommentOwner(Comment comment) {
        return isSignedIn() && comment.getAuthor().equals(getIDToken().getPreferredUsername());
    }
    
    /**
     * 
     * @param log org.jboss.logging.Logger for logging 
     * @param serviceClass Class of service class where is {@link com.issuetracker.web.quilifiers.ServiceSecurity ServiceSecurity} used
     * @param methodName method name where the annotation is present
     * @param parameterTypes the list of parameters of the method
     * @return if the user is authorized to perform such a operations
     */
    public boolean isAuthorized(Logger log, Class<?> serviceClass, String methodName, Class<?>... parameterTypes) {
        try {
            log.info("service class: " + serviceClass);
            Method method = serviceClass.getMethod(methodName, parameterTypes);
            if (!method.isAnnotationPresent(ServiceSecurity.class)) {
                throw new NoSuchAnnotationException("Specified method: " + method.getName() + " doesn't have " 
                        + ServiceSecurity.class.getName() + " annotation.");
            }
            ServiceSecurity annotation = method.getAnnotation(ServiceSecurity.class);
           
            log.info("is signed in: " + isSignedIn());
            if (isUserInAppRole(annotation.allowedRole())) {
                return true;
            } else {
                log.warn("User " + getIDToken().getPreferredUsername() + " doesn't have sufficient privileges to perform " 
                        + IssueService.class.getName() + "." + method.getName());
                ThreadContext.getSession().error("Unsufficient privileges to perform this operation.");
                return false;
            }
        } catch (NoSuchMethodException | NoSuchAnnotationException e) {
            log.error(e);
            ThreadContext.getSession().error("Internal Application Error, redirecting to home page. See the log for more information.");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                log.warn(ex);
            }
            throw new RestartResponseException(HomePage.class);
        }
    }
    
    private class NoSuchAnnotationException extends Exception {
        public NoSuchAnnotationException(String message) {
            super(message);
        }
    }
}
