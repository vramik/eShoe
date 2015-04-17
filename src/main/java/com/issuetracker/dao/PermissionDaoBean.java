package com.issuetracker.dao;

import com.issuetracker.dao.api.PermissionDao;
import com.issuetracker.model.Permission;
import com.issuetracker.model.PermissionId;
import com.issuetracker.model.TypeId;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.jboss.logging.Logger;


/**
 *
 * @author vramik
 */
@Stateless
public class PermissionDaoBean implements PermissionDao {

    private final Logger log = Logger.getLogger(PermissionDaoBean.class);
    
    @PersistenceContext
    private EntityManager em;
    private CriteriaBuilder cb;
    
    @Override
    public void insertOrUpdate(Permission permission) {
        if (em.find(Permission.class, new PermissionId(permission)) == null) {
            em.persist(permission);
        } else {
            update(permission);
        }
    }

    @Override
    public void insert(Permission permission) {
        if (em.find(Permission.class, new PermissionId(permission)) == null) {
            em.persist(permission);
        }
    }
    
    @Override
    public void remove(Permission permission) {
        em.remove(em.merge(permission));
    }

    @Override
    public void update(Permission permission) {
        em.merge(permission);
    }

//    @Override
    public List<Permission> getDefaultPermissions() {
        cb = em.getCriteriaBuilder();
        CriteriaQuery<Permission> q = cb.createQuery(Permission.class);
        Root<Permission> p = q.from(Permission.class);
        q.where(cb.and(
                cb.isNull(p.<Long>get("itemId")),
                cb.not(cb.equal(p.<TypeId>get("typeId"), TypeId.global))
        ));
        TypedQuery<Permission> pQuery = em.createQuery(q);
        List<Permission> results = pQuery.getResultList();
        
        if (results != null && !results.isEmpty()) {
            return results;
        } else {
            return new ArrayList<>();
        }
    }

//    @Override
    public List<Permission> getDefaultProjectPermissions() {
        cb = em.getCriteriaBuilder();
        CriteriaQuery<Permission> q = cb.createQuery(Permission.class);
        Root<Permission> p = q.from(Permission.class);
        q.where(cb.and(cb.isTrue(p.<Boolean>get("defaultPermission")), 
                cb.equal(p.<TypeId>get("permissionType"), TypeId.project)
            )
        );
        TypedQuery<Permission> pQuery = em.createQuery(q);
        List<Permission> results = pQuery.getResultList();
        if (results != null && !results.isEmpty()) {
            return results;
        } else {
            return new ArrayList<>();
        }
    }

//    @Override
    public List<Permission> getDefaultIssuePermissions() {
        cb = em.getCriteriaBuilder();
        CriteriaQuery<Permission> q = cb.createQuery(Permission.class);
        Root<Permission> p = q.from(Permission.class);
        q.where(cb.and(cb.isTrue(p.<Boolean>get("defaultPermission"))), cb.equal(p.<String>get("permissionType"), "issue"));
        TypedQuery<Permission> pQuery = em.createQuery(q);
        List<Permission> results = pQuery.getResultList();
        if (results != null && !results.isEmpty()) {
            return results;
        } else {
            return new ArrayList<>();
        }
    }

    @Override
    public List<Permission> getPermissionsByRole(TypeId typeId, Long itemId, Long roleId) {
        cb = em.getCriteriaBuilder();
        
        CriteriaQuery<Permission> query = cb.createQuery(Permission.class);
        Root<Permission> fromPermission = query.from(Permission.class);
        
        query.where(cb.and(
                cb.equal(fromPermission.<Integer>get("typeId"), typeId.getValue()),
                cb.equal(fromPermission.<Long>get("itemId"), itemId),
                cb.equal(fromPermission.<Long>get("roleId"), roleId)
        ));
        
        List<Permission> result = em.createQuery(query).getResultList();
        if (result != null && !result.isEmpty()) {
            return result;
        } else {
            return new ArrayList<>();
        }
    }

    @Override
    public List<Permission> getPermissionsByTypeAndAction(TypeId typeId, Long actionId) {
        cb = em.getCriteriaBuilder();
        
        CriteriaQuery<Permission> query = cb.createQuery(Permission.class);
        Root<Permission> fromPermission = query.from(Permission.class);
        
        query.where(cb.and(
                cb.equal(fromPermission.<Integer>get("typeId"), typeId.getValue()),
                cb.equal(fromPermission.<Long>get("actionId"), actionId)
        ));
        
        List<Permission> result = em.createQuery(query).getResultList();
        if (result != null && !result.isEmpty()) {
            return result;
        } else {
            return new ArrayList<>();
        }
    }

    @Override
    public List<Permission> getPermissions(Long actionId, Long roleId, TypeId[] typeIds) {
        cb = em.getCriteriaBuilder();
        
        CriteriaQuery<Permission> query = cb.createQuery(Permission.class);
        Root<Permission> fromPermission = query.from(Permission.class);
        
        Predicate[] predicates = new Predicate[typeIds.length];
        for (int i = 0; i < typeIds.length; i++) {
            predicates[i] = cb.equal(fromPermission.<Integer>get("typeId"), typeIds[i].getValue());
            
        }
        query.select(fromPermission).where(
                cb.and(
                        cb.or(predicates),
                        cb.equal(fromPermission.get("actionId"), actionId),
                        cb.equal(fromPermission.get("roleId"), roleId)
                )
        );
        
        List<Permission> result = em.createQuery(query).getResultList();
        if (result != null && !result.isEmpty()) {
            return result;
        } else {
            return new ArrayList<>();
        }
    }

    @Override
    public Permission getPermission(TypeId typeId, Long itemId, Long roleId, Long actionId) {
        cb = em.getCriteriaBuilder();
        
        CriteriaQuery<Permission> query = cb.createQuery(Permission.class);
        Root<Permission> fromPermission = query.from(Permission.class);
        
        query.where(cb.and(
                cb.equal(fromPermission.<Integer>get("typeId"), typeId.getValue()),
                cb.equal(fromPermission.<Long>get("itemId"), itemId),
                cb.equal(fromPermission.<Long>get("roleId"), roleId),
                cb.equal(fromPermission.<Long>get("actionId"), actionId)
        ));
        
        List<Permission> resultList = em.createQuery(query).getResultList();
        if (resultList.isEmpty()) {
            return null;
        } else if (resultList.size() == 1) {
            return resultList.get(0);
        } else {
            throw new IllegalStateException("this method shouldn't return more than one result");
        }
    }

    @Override
    public List<Permission> getPermissionsByAction(TypeId typeId, Long itemId, Long actionId) {
        cb = em.getCriteriaBuilder();
        
        CriteriaQuery<Permission> query = cb.createQuery(Permission.class);
        Root<Permission> fromPermission = query.from(Permission.class);
        
        query.where(cb.and(
                cb.equal(fromPermission.<Integer>get("typeId"), typeId.getValue()),
                cb.equal(fromPermission.<Long>get("itemId"), itemId),
                cb.equal(fromPermission.<Long>get("actionId"), actionId)
        ));
        
        List<Permission> result = em.createQuery(query).getResultList();
        if (result != null && !result.isEmpty()) {
            return result;
        } else {
            return new ArrayList<>();
        }
    }
    
}
