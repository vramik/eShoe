package com.issuetracker.dao;

import com.issuetracker.dao.api.ProjectDao;
import com.issuetracker.model.Component;
import com.issuetracker.model.Permission;
import com.issuetracker.model.Project;
import com.issuetracker.model.ProjectVersion;
import com.issuetracker.model.Workflow;
import static com.issuetracker.web.Constants.roles;
import com.issuetracker.web.security.KeycloakAuthSession;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Subquery;
import org.jboss.logging.Logger;

/**
 *
 * @author mgottval
 */
@Stateless
public class ProjectDaoBean implements ProjectDao {

    private final Logger log = Logger.getLogger(ProjectDaoBean.class);
    
    @PersistenceContext
    private EntityManager em;
    private CriteriaBuilder cb;
    
    @Override
    public void insert(Project project) {
        em.persist(project);
    }
    
    @Override
    public void update(Project project) {
        em.merge(project);
    }

    @Override
    public Project getProjectByName(String name) {
        cb = em.getCriteriaBuilder();
        CriteriaQuery<Project> projectQuery = cb.createQuery(Project.class);
        Root<Project> p = projectQuery.from(Project.class);
        Predicate pCondition = cb.equal(p.get("name"), name);
        projectQuery.where(pCondition);
        TypedQuery<Project> pQuery = em.createQuery(projectQuery);
        List<Project> projectResults = pQuery.getResultList();
        if (projectResults != null && !projectResults.isEmpty()) {
            return projectResults.get(0);
        } else {
            return null;
        }
    }
    
    @Override
    public Project getProjectById(Long id) {
        cb = em.getCriteriaBuilder();
        CriteriaQuery<Project> projectQuery = cb.createQuery(Project.class);
        Root<Project> p = projectQuery.from(Project.class);
        Predicate pCondition = cb.equal(p.get("id"), id);
        projectQuery = projectQuery.where(pCondition);
        TypedQuery<Project> pQuery = em.createQuery(projectQuery);
        List<Project> projectResults = pQuery.getResultList();
        if (projectResults != null && !projectResults.isEmpty()) {
            return projectResults.get(0);
        } else {
            return null;
        }
    }

    @Override
    public List<Project> getProjects() {
        cb = em.getCriteriaBuilder();
        CriteriaQuery<Project> query = cb.createQuery(Project.class);
        Root<Project> fromProject = query.from(Project.class);
        query.select(fromProject);
        List<Project> results = em.createQuery(query).getResultList();
        if (results != null && !results.isEmpty()) {
            return results;
        } else {
            return new ArrayList<>();
        }
    }
    
    @Override
    public List<Project> getProjectsWithRights(String permissionName) {
        return getProjects();
////        test(permissionName);
//        cb = em.getCriteriaBuilder();
//        CriteriaQuery<Project> query = cb.createQuery(Project.class);
//        
//        Subquery<Long> subquery = query.subquery(Long.class);
//        Root<Permission> fromPermissionSubquery = subquery.from(Permission.class);
//        subquery.select(fromPermissionSubquery.<Long>get("id")).distinct(true);
//        Expression<List<String>> permissionRoles = fromPermissionSubquery.get("roles");
//        
////        Set<String> userRoles = KeycloakAuthSession.getUserAppRoles();
//        Set<String> userRoles = KeycloakAuthSession.getUserRhelmRoles();
//        userRoles.add(roles.getProperty("kc.public")); //we will show also all public projects
//        
//        List<Predicate> members = new ArrayList<>();
//        for (String userAppRole : userRoles) {
////            log.error(userAppRole);
//            members.add(cb.isMember(userAppRole, permissionRoles));
//        }
//        Predicate[] membersArr = new Predicate[members.size()];
//        members.toArray(membersArr);
//        subquery.where(
//                cb.and(
//                    cb.or(membersArr),
//                    cb.equal(fromPermissionSubquery.get("name"), permissionName),
//                    cb.isFalse(fromPermissionSubquery.<Boolean>get("defaultPermission"))
//                )
//        );
//
//        Root<Project> fromProject = query.from(Project.class);
//        query.select(fromProject).distinct(true);
//        String loggedUser = "";
//        if (KeycloakAuthSession.isSignedIn()) {
//            loggedUser = KeycloakAuthSession.getIDToken().getPreferredUsername();
//        }
//        query.where(
//                cb.or(
//                    cb.in(fromProject.join("permissions").get("id")).value(subquery),
//                    cb.equal(fromProject.get("owner"), loggedUser) //if the user is owner of the project
//                )
//        );
//
//        List<Project> resultList = em.createQuery(query).getResultList();
//        log.warn("projects from DB:");
//        for (Project p : resultList) {
//            log.info(p);
//        }
//        log.warn("---------------------------------------------");
//        return resultList;
        
    }
    
//    public List<Project> test(String permissionName) {
//        cb = em.getCriteriaBuilder();
//        CriteriaQuery<Permission> query = cb.createQuery(Permission.class);
//        
//        Root<Permission> fromPermission = query.from(Permission.class);
//        query.select(fromPermission).distinct(true);
//        
//        Expression<List<String>> permissionRoles = fromPermission.get("roles");
//        
//        Set<String> userRoles = KeycloakAuthSession.getUserRhelmRoles();
//        
//        List<Predicate> members = new ArrayList<>();
//        for (String userAppRole : userRoles) {
//            log.error(userAppRole);
//            members.add(cb.isMember(userAppRole, permissionRoles));
//        }
//        Predicate[] membersArr = new Predicate[members.size()];
//        members.toArray(membersArr);
//        
//        query.where(cb.and(
//                cb.or(membersArr),
//                cb.equal(fromPermission.get("name"), permissionName),
//                cb.isFalse(fromPermission.<Boolean>get("defaultPermission"))
//        ));
//        log.error(em.createQuery(query).getResultList());
//        return null;
//    }

    @Override
    public List<ProjectVersion> getProjectVersions(Project project) {
        cb = em.getCriteriaBuilder();
        CriteriaQuery<Project> c = cb.createQuery(Project.class);
        Root<Project> p = c.from(Project.class);
        c.select(p);
        c.where(cb.equal(p.get("name"), project.getName()));
        TypedQuery query = em.createQuery(c);
        List<Project> projectResults = query.getResultList();
        if (projectResults != null && !projectResults.isEmpty()) {
            List<ProjectVersion> versions = projectResults.get(0).getVersions();
            return versions;
        } else {
            return new ArrayList<>();
        }
    }

    @Override
    public List<Component> getProjectComponents(Project project) {
        cb = em.getCriteriaBuilder();
        CriteriaQuery<Project> c = cb.createQuery(Project.class);
        Root<Project> p = c.from(Project.class);
        c.select(p);
        c.where(cb.equal(p.get("name"), project.getName()));
        TypedQuery query = em.createQuery(c);
        List<Project> projectResults = query.getResultList();
        if (projectResults != null && !projectResults.isEmpty()) {
            List<Component> components = projectResults.get(0).getComponents();
            return components;
        } else {
            return new ArrayList<>();
        }
    }

    @Override
    public void remove(Project project) {        
        em.remove(em.merge(project));
    }

    @Override
    public boolean isProjectNameInUse(String projectName) {
        return getProjectByName(projectName) != null;
    }

    @Override
    public List<Project> getProjectsByWorkflow(Workflow workflow) {
        cb = em.getCriteriaBuilder();
        CriteriaQuery<Project> projectQuery = cb.createQuery(Project.class);
        Root<Project> p = projectQuery.from(Project.class);
        Predicate pCondition = cb.equal(p.get("workflow"), workflow.getId());
        projectQuery.where(pCondition);
        TypedQuery<Project> pQuery = em.createQuery(projectQuery);
        List<Project> results = pQuery.getResultList();
        if (results != null && !results.isEmpty()) {
            return results;
        } else {
            return new ArrayList<>();
        }
    }
}
