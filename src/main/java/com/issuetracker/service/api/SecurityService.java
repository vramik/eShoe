package com.issuetracker.service.api;

import com.issuetracker.model.TypeId;
import java.util.List;

/**
 *
 * @author vramik
 */
public interface SecurityService {
    
    boolean canUserPerformAction(TypeId typeId, Long itemId, String action);

    List<String> getPermittedActionsForUserAndItem(TypeId typeId, Long itemId);
}
