package com.issuetracker.dao;

import com.issuetracker.dao.api.ActionDao;
import com.issuetracker.model.Action;
import com.issuetracker.model.TypeId;
import java.util.ArrayList;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import javax.persistence.criteria.Predicate;
import org.jboss.logging.Logger;

/**
 *
 * @author mgottval
 */
@Stateless
public class ActionDaoBean implements ActionDao {

    private final Logger log = Logger.getLogger(ActionDaoBean.class);
    
    @PersistenceContext
    private EntityManager em;
    private CriteriaBuilder cb;
    
    @Override
    public void insert(Action action) {
        em.persist(action);
    }
    
    @Override
    public void remove(Action action) {
        em.remove(em.merge(action));
    }

    @Override
    public boolean existsAction(String name, TypeId typeId) {
        return getActionByNameAndType(name, typeId) != null;
    }

    @Override
    public Action getActionByNameAndType(String name, TypeId typeId) {
        cb = em.getCriteriaBuilder();
        CriteriaQuery<Action> actionQuery = cb.createQuery(Action.class);
        Root<Action> fromAction = actionQuery.from(Action.class);
        actionQuery.where(cb.equal(fromAction.get("name"), name));
        List<Action> resultList = em.createQuery(actionQuery).getResultList();
        if (resultList != null && !resultList.isEmpty()) {
            return resultList.get(0);
        } else {
            return null;
        }
    }    

    @Override
    public Action getActionById(Long actionId) {
        cb = em.getCriteriaBuilder();
        CriteriaQuery<Action> actionQuery = cb.createQuery(Action.class);
        Root<Action> fromAction = actionQuery.from(Action.class);
        actionQuery.where(cb.equal(fromAction.get("id"), actionId));
        List<Action> resultList = em.createQuery(actionQuery).getResultList();
        if (resultList != null && !resultList.isEmpty()) {
            return resultList.get(0);
        } else {
            return null;
        }
    }

    @Override
    public List<Action> getActions() {
        cb = em.getCriteriaBuilder();
        CriteriaQuery<Action> query = cb.createQuery(Action.class);
        Root<Action> fromAction = query.from(Action.class);
        query.select(fromAction);
        List<Action> results = em.createQuery(query).getResultList();
        if (results != null && !results.isEmpty()) {
            return results;
        } else {
            return new ArrayList<>();
        }
    }

    @Override
    public List<Action> getActionsByTypes(TypeId... typeIds) {
        cb = em.getCriteriaBuilder();
        CriteriaQuery<Action> query = cb.createQuery(Action.class);
        Root<Action> fromAction = query.from(Action.class);
        
        Predicate[] predicates = new Predicate[typeIds.length];
        for (int i = 0; i < typeIds.length; i++) {
            predicates[i] = cb.equal(fromAction.<TypeId>get("typeId"), typeIds[i]);
            
        }
        query.select(fromAction).where(cb.or(predicates));
        
        List<Action> results = em.createQuery(query).getResultList();
        if (results != null && !results.isEmpty()) {
            return results;
        } else {
            return new ArrayList<>();
        }
    }
}
