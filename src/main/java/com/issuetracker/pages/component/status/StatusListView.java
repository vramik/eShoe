package com.issuetracker.pages.component.status;

import com.issuetracker.dao.api.StatusDao;
import com.issuetracker.dao.api.WorkflowDao;
import com.issuetracker.model.Status;
import com.issuetracker.model.Workflow;
import com.issuetracker.pages.AddTransition;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;

import com.issuetracker.service.api.StatusService;
import com.issuetracker.service.api.WorkflowService;
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
public class StatusListView<T extends Status> extends Panel {

    @Inject
    private StatusService statusService;
    @Inject
    private WorkflowService workflowService;
    private List<Status> statuses;
    private ListView listViewStatus;
    private Workflow workflow;

    public StatusListView(String id, IModel<List<Status>> statusesModel, final IModel<Workflow> workflowModel) {
        super(id);
        final boolean workflowPresent;
        statuses = statusService.getStatuses();
        if (statuses == null) {
            statuses = new ArrayList<Status>();
        }

        if (workflowModel != null) {
            workflowPresent = true;
            workflow = workflowModel.getObject();
        } else {
            workflowPresent = false;
        }
        listViewStatus = new ListView<Status>("statusList", statusesModel) {
            @Override
            protected void populateItem(final ListItem<Status> item) {
                final Status status = item.getModelObject();
                Link statusDetailLink = new Link<Status>("showStatus", item.getModel()) {
                    @Override
                    public void onClick() {
                        PageParameters pageParameters = new PageParameters();
                        if (workflowPresent) {
                            pageParameters.add("workflow", workflow.getId());
                        }
                        pageParameters.add("status", status.getId());
                        setResponsePage(AddTransition.class, pageParameters);
                    }
                };
                statusDetailLink.add(new Label("name", status.getName())).setVisible(workflowPresent);
                item.add(statusDetailLink);


                item.add(new Label("name", status.getName()).setVisible(!workflowPresent));

//                item.add(new Link<Status>("remove", item.getModel()) {
//                    @Override
//                    public void onClick() {
//                        statuses.remove(status);
//                        statusService.remove(status);
//                    }
//                }.setVisible(!workflowPresent));

            }
        };
        add(listViewStatus);
    }

    public List<Status> getStatuses() {
        return statuses;
    }

    public void setStatuses(List<Status> statuses) {
        this.statuses = statuses;
    }
}
