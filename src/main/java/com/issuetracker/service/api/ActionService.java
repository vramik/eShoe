package com.issuetracker.service.api;

import com.issuetracker.model.Action;
import com.issuetracker.model.TypeId;
import java.util.List;
import java.util.Set;

/**
 *
 * @author vramik
 */
public interface ActionService {
    
    void insert(Action action);
    
    void remove(Action action);
    
    Action getActionByNameAndType(String name, TypeId typeId);

    Action getActionById(Long actionId);

    List<Action> getActions();

    List<Action> getActionsByType(TypeId typeId);
    
    List<String> getActionNamesByIds(Set<Long> actionIds);
}
