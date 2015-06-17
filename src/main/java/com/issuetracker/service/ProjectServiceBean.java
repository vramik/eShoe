package com.issuetracker.service;

import com.issuetracker.dao.api.ProjectDao;
import com.issuetracker.model.Action;
import com.issuetracker.model.Component;
import com.issuetracker.model.Permission;
import com.issuetracker.model.Project;
import com.issuetracker.model.ProjectVersion;
import com.issuetracker.model.TypeId;
import com.issuetracker.model.Workflow;
import com.issuetracker.service.api.ActionService;
import com.issuetracker.service.api.PermissionService;
import com.issuetracker.service.api.ProjectService;
import com.issuetracker.service.api.RoleService;
import static com.issuetracker.web.Constants.roles;
import com.issuetracker.web.security.KeycloakAuthSession;
import java.io.Serializable;
import java.util.HashSet;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.List;
import java.util.Set;

/**
 *
 * @author mgottval
 */
@Stateless
public class ProjectServiceBean implements ProjectService, Serializable {

    @Inject private ProjectDao projectDao;
    @Inject private ActionService actionService;
    @Inject private PermissionService permissionService;
    @Inject private RoleService roleService;
    
    @Override
    public void insert(Project project) {
        projectDao.insert(project);
    }
    
    @Override
    public void update(Project project) {
        projectDao.update(project);
    }

    @Override
    public Project getProjectByName(String name) {
        return projectDao.getProjectByName(name);
    }
    
    @Override
    public Project getProjectById(Long id) {
        return projectDao.getProjectById(id);
    }

    @Override
    public List<Project> getDisplayableProjects() {
        return projectDao.getProjectsByIds(getDisplayableProjectIds());
    }
    
    @Override
    public List<Project> getProjectsWithRights(String actionName) {
        return projectDao.getProjectsByIds(getProjetsIdsWithRights(actionName));
    }

    @Override
    public List<ProjectVersion> getProjectVersions(Project project) {
        return projectDao.getProjectVersions(project);
    }

    @Override
    public List<Component> getProjectComponents(Project project) {
        return projectDao.getProjectComponents(project);
    }

    @Override
    public void remove(Project project) {
        projectDao.remove(project);
    }
    
    @Override
    public boolean isProjectNameInUse(String projectName) {
        return projectDao.isProjectNameInUse(projectName);
    }

    @Override
    public List<Project> getProjectsByWorkflow(Workflow workflow) {
        return projectDao.getProjectsByIdsAndWorkflow(workflow, getProjetsIdsWithRights(roles.getProperty("it.project.workflow")));
    }
    
    private Set<Long> getProjetsIdsWithRights(String actionName) {
        Set<String> userRoles = KeycloakAuthSession.getUserRealmRoles();
        
        Action action = actionService.getActionByNameAndType(actionName, TypeId.project);
        
        List<Permission> globalPermissions = permissionService.getPermissionsByAction(TypeId.global, 0L, action.getId());
        for (Permission globalPermission : globalPermissions) {
            if (userRoles.contains(roleService.getRoleById(globalPermission.getRoleId()).getName())) {
                return getDisplayableProjectIds();
            }
        }
        
        List<Permission> projectPermissions = permissionService.getPermissionsByTypeAndAction(TypeId.project, action.getId());
        Set<Long> projectIds = new HashSet<>();
        for (Permission projectPermission : projectPermissions) {
            if (userRoles.contains(roleService.getRoleById(projectPermission.getRoleId()).getName())) {
                projectIds.add(projectPermission.getItemId());
            }
        }
        return projectIds;
    }
    
    private Set<Long> getDisplayableProjectIds() {
        Set<String> userRoles = KeycloakAuthSession.getUserRealmRoles();
        
        Action action = actionService.getActionByNameAndType(roles.getProperty("it.project.browse"), TypeId.project);
        
        List<Permission> projectPermissions = permissionService.getPermissionsByTypeAndAction(TypeId.project, action.getId());
        Set<Long> allProjectIDs = projectDao.getProjectsIDs();

        Set<Long> resultProjectIds = new HashSet<>();
        for (Permission permission : projectPermissions) {
            allProjectIDs.remove(permission.getItemId());
            if (userRoles.contains(roleService.getRoleById(permission.getRoleId()).getName())) {
                resultProjectIds.add(permission.getItemId());
            }
        }

        List<Permission> globalPermissions = permissionService.getPermissionsByAction(TypeId.global, 0L, action.getId());
        for (Permission globalPermission : globalPermissions) {
            if (userRoles.contains(roleService.getRoleById(globalPermission.getRoleId()).getName())) {
                resultProjectIds.addAll(allProjectIDs);
                break;
            }
        }
        return resultProjectIds;
    }
}
