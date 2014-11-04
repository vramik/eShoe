package com.issuetracker.pages.component.workflow;

import com.issuetracker.model.PermissionType;
import com.issuetracker.model.Project;
import com.issuetracker.model.Workflow;
import com.issuetracker.pages.component.project.ProjectListView;
import com.issuetracker.pages.workflow.WorkflowDetail;
import com.issuetracker.service.api.ProjectService;
import com.issuetracker.service.api.WorkflowService;
import static com.issuetracker.web.security.PermissionsUtil.*;
import java.util.ArrayList;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import javax.inject.Inject;
import java.util.List;

/**
 *
 * @author mgottval
 * @param <T>
 */
public class WorkflowListView<T extends Workflow> extends Panel {

    @Inject
    private WorkflowService workflowService;
    @Inject
    private ProjectService projectService;
    private List<Workflow> workflows;
    private final ListView listViewWorkflows;

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
                
                List<Project> projects = getProjectWithEditPermissions(getRequest(), projectService.getProjectsByWorkflow(workflow));
                item.add(new ProjectListView<>("projectListView", projects));
                item.add(new Link("remove") {
                    @Override
                    public void onClick() {
                        workflowService.remove(workflow);
                        workflows = workflowService.getWorkflows();
                    }
                }.setEnabled(!workflowService.isWorkflowUsed(workflow)));
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
