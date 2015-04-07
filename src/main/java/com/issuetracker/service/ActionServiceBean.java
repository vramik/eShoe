package com.issuetracker.service;

import com.issuetracker.dao.api.ActionDao;
import com.issuetracker.model.Action;
import com.issuetracker.model.TypeId;
import static com.issuetracker.model.TypeId.*;
import com.issuetracker.service.api.ActionService;
import java.io.Serializable;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

/**
 *
 * @author mgottval
 */
@Stateless
public class ActionServiceBean implements ActionService, Serializable {

    @Inject
    private ActionDao actionDao;

    @Override
    public void insert(Action action) {
        actionDao.insert(action);
    }

    @Override
    public void remove(Action action) {
        actionDao.remove(action);
    }

    @Override
    public Action getActionByNameAndType(String name, TypeId typeId) {
        if (!actionDao.existsAction(name, typeId)) {
            Action newAction = new Action();
            newAction.setName(name);
            newAction.setTypeId(typeId);
            insert(newAction);
        }
        return actionDao.getActionByNameAndType(name, typeId);
    }

    @Override
    public Action getActionById(Long actionId) {
        return actionDao.getActionById(actionId);
    }

    @Override
    public List<Action> getActions() {
        return actionDao.getActions();
    }

    @Override
    public List<Action> getActionsByType(TypeId typeId) {
        switch (typeId) {
            case global:
                return actionDao.getActionsByTypes(global, project, issue, comment);
            case project:
                return actionDao.getActionsByTypes(project, issue, comment);
            case issue:
                return actionDao.getActionsByTypes(issue, comment);
            case comment:
                return actionDao.getActionsByTypes(comment);
            default:
                throw new IllegalStateException("Reached unreacheable state.");
        }
    }
}
