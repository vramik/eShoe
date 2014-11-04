package com.issuetracker.web.security;

import com.issuetracker.model.Permission;
import com.issuetracker.model.PermissionType;
import com.issuetracker.model.Project;
import static com.issuetracker.web.security.KeycloakAuthSession.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import org.apache.wicket.request.Request;

/**
 *
 * @author vramik
 */
public class PermissionsUtil {

    public static boolean isProjectOwner(Request req, Project project) {
        return isSignedIn(req) && project.getOwner().equals(getIDToken(req).getPreferredUsername());
    }
    
    public static boolean hasPermissionsProject(Request req, Project project, PermissionType type) {
        if (isSuperUser(req) || isProjectOwner(req, project)) {
            return true;
        }
        Permission p = new Permission();
        p.setPermissionType(type);
        for (Permission permission : project.getPermissions()) {
            if (permission.equals(p)) {
                for (String role : permission.getRoles()) {
                    if (role.equals("public") || isUserInAppRole(req, role)) {
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
    
    public static List<Project> getProjectWithEditPermissions(Request req, List<Project> projects) {
        List<Project> result = new ArrayList<>();
        for (Project project : projects) {
            if (hasPermissionsProject(req, project, PermissionType.edit)) {
                result.add(project);
            }
        }
        return result;
    }
}
