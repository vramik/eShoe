package com.issuetracker.dao;

import com.issuetracker.dao.api.RoleDao;
import com.issuetracker.model.Role;
import java.util.ArrayList;
import java.util.HashSet;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Set;
import javax.persistence.criteria.Predicate;
import org.jboss.logging.Logger;

/**
 *
 * @author mgottval
 */
@Stateless
public class RoleDaoBean implements RoleDao {

    private final Logger log = Logger.getLogger(RoleDaoBean.class);
    
    @PersistenceContext
    private EntityManager em;
    private CriteriaBuilder cb;
    
    @Override
    public void insert(Role role) {
        em.persist(role);
    }
    
    @Override
    public void remove(Role role) {
        em.remove(em.merge(role));
    }

    @Override
    public boolean isRoleNameInUse(String name) {
        return getRoleByName(name) != null;
    }

    @Override
    public Role getRoleByName(String name) {
        cb = em.getCriteriaBuilder();
        CriteriaQuery<Role> roleQuery = cb.createQuery(Role.class);
        Root<Role> fromRole = roleQuery.from(Role.class);
        roleQuery.where(cb.equal(fromRole.get("name"), name));
        List<Role> resultList = em.createQuery(roleQuery).getResultList();
        if (resultList != null && !resultList.isEmpty()) {
            return resultList.get(0);
        } else {
            return null;
        }
    }    

    @Override
    public List<Role> getRoles() {
        cb = em.getCriteriaBuilder();
        CriteriaQuery<Role> query = cb.createQuery(Role.class);
        query.from(Role.class);
        return em.createQuery(query).getResultList();
    }

    @Override
    public Role getRoleById(Long roleId) {
        cb = em.getCriteriaBuilder();
        CriteriaQuery<Role> roleQuery = cb.createQuery(Role.class);
        Root<Role> fromRole = roleQuery.from(Role.class);
        roleQuery.where(cb.equal(fromRole.get("id"), roleId));
        List<Role> resultList = em.createQuery(roleQuery).getResultList();
        if (resultList != null && !resultList.isEmpty()) {
            return resultList.get(0);
        } else {
            return null;
        }
    }

    @Override
    public Set<Long> getIdsByNames(Set<String> roleNames) {
        cb = em.getCriteriaBuilder();
        
        CriteriaQuery<Long> roleQuery = cb.createQuery(Long.class);
        Root<Role> fromRole = roleQuery.from(Role.class);
        
        List<Predicate> predicates = new ArrayList<>();
        for (String roleName : roleNames) {
            predicates.add(cb.equal(fromRole.get("name"), roleName));
        }
        roleQuery.select(fromRole.<Long>get("id"));
        roleQuery.where(predicates.toArray(new Predicate[predicates.size()]));
        List<Long> resultList = em.createQuery(roleQuery).getResultList();
        if (resultList != null && !resultList.isEmpty()) {
            return new HashSet<>(resultList);
        } else {
            return new HashSet<>();
        }
        
    }
}
