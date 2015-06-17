package com.issuetracker.dao.api;

import com.issuetracker.model.Action;
import com.issuetracker.model.TypeId;
import java.util.List;
import java.util.Set;

/**
 *
 * @author vramik
 */
public interface ActionDao {

    void insert(Action action);

    void remove(Action action);

    boolean existsAction(String name, TypeId typeId);

    Action getActionByNameAndType(String name, TypeId typeId);

    Action getActionById(Long actionId);

    List<Action> getActions();

    List<Action> getActionsByTypes(TypeId... typeIds);

    List<String> getActionNamesByIds(Set<Long> actionIds);
    
}
