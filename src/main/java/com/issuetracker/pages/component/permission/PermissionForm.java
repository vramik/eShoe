package com.issuetracker.pages.component.permission;

import com.issuetracker.model.Permission;
import java.util.List;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

/**
 *
 * @author vramik
 */
public class PermissionForm extends Panel {
    
    
    public PermissionForm(String id, final IModel<List<Permission>> permissionsModel) {
        super(id);
        
        
    }
}
