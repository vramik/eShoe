package com.issuetracker.pages.component.component;

import com.issuetracker.dao.api.ComponentDao;
import com.issuetracker.dao.api.ProjectDao;
import com.issuetracker.model.Component;
import com.issuetracker.model.Project;
import com.issuetracker.pages.component.comment.CommentListView;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Inject;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.AbstractPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

/**
 *
 * @author mgottval
 *
 */
public class ComponentListView<T extends Component> extends Panel {

    @Inject
    private ComponentDao componentDao;
    @Inject
    private ProjectDao projectDao;
    private final ListView<Component> componentsListView;
    private List<Component> componentList;

    public ComponentListView(String id, IModel<List<Component>> componentsModel, final IModel<Project> projectModel) {
        super(id);
        componentList = componentsModel.getObject();
        add(new Label("components", "Components"));

        IModel<List<Component>> compoModel = new PropertyModel<List<Component>>(this, "componentList");
        componentsListView = new ListView<Component>("componentsList", compoModel) {
            @Override
            protected void populateItem(final ListItem<Component> item) {
                final Component component = item.getModelObject();
                item.add(new Link("remove", item.getModel()) {
                    @Override
                    public void onClick() {
                        componentList.remove(component);
                        if (projectModel != null) {
                            Project project = projectDao.getProjectById(projectModel.getObject().getId());
                            project.setComponents(componentList);
                            projectDao.update(project);
                        }
                    }
                });
                item.add(new Label("name", component.getName()));
            }
        };
        add(componentsListView);
    }

    public List<Component> getComponentList() {
        return componentList;
    }

    public void setComponentList(List<Component> componentList) {
        this.componentList = componentList;
    }
}
