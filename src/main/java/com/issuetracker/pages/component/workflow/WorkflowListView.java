package com.issuetracker.pages.component.workflow;

import com.issuetracker.dao.api.WorkflowDao;
import com.issuetracker.model.Workflow;
import com.issuetracker.pages.WorkflowDetail;
import java.util.List;
import javax.inject.Inject;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

/**
 *
 * @author mgottval
 */
public class WorkflowListView<T extends Workflow> extends Panel {

    @Inject
    private WorkflowDao workflowDao;
    private List<Workflow> workflows;
    private ListView listViewWorkflows;

    public WorkflowListView(String id, IModel<List<Workflow>> workflowsModel) {
        super(id);
        listViewWorkflows = new ListView<Workflow>("workflowList", workflowsModel) {
            @Override
            protected void populateItem(final ListItem<Workflow> item) {
                final Workflow workflow = item.getModelObject();
                Link workflowDetailLink = new Link<Workflow>("showWorkflow", item.getModel()) {
                    @Override
                    public void onClick() {
                        PageParameters pageParameters = new PageParameters();
                        pageParameters.add("workflow", workflow.getId());
                        setResponsePage(WorkflowDetail.class, pageParameters);
                    }
                };
                workflowDetailLink.add(new Label("name", workflow.getName()));
                item.add(workflowDetailLink);
                
                item.add(new Link<Workflow>("delete", item.getModel()) {
                    @Override
                    public void onClick() {
                        workflowDao.remove(workflow);
                        workflows = workflowDao.getWorkflows();
                    }
                });
            }
        };
        add(listViewWorkflows);
    }

    public List<Workflow> getWorkflows() {
        return workflows;
    }

    public void setWorkflows(List<Workflow> workflows) {
        this.workflows = workflows;
    }
}
