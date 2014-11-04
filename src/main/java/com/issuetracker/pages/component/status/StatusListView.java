package com.issuetracker.pages.component.status;

import com.issuetracker.model.Status;
import com.issuetracker.model.Workflow;
import com.issuetracker.pages.transition.AddTransition;
import com.issuetracker.pages.status.EditStatus;
import com.issuetracker.service.api.StatusService;
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
public class StatusListView<T extends Status> extends Panel {

    @Inject
    private StatusService statusService;
    private List<Status> statuses;
    private final ListView listViewStatus;
    private Workflow workflow;

    public StatusListView(String id, IModel<List<Status>> statusesModel, final IModel<Workflow> workflowModel) {
        super(id);
        final boolean workflowPresent;
        statuses = statusService.getStatuses();

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

                item.add(new Link<Status>("remove", item.getModel()) {
                    @Override
                    public void onClick() {
                        statuses.remove(status);
                        statusService.remove(status);
                    }
                }.setEnabled(!statusService.isStatusUsed(status)));
                item.add(new Link("edit") {
                    @Override
                    public void onClick() {
                        PageParameters parameters = new PageParameters();
                        if (workflowPresent) {
                            parameters.add("workflow", workflow.getId());
                        }
                        parameters.add("statusName", status.getName());
                        parameters.add("page", getPage().getClass().getName());
                        setResponsePage(EditStatus.class, parameters);
                    }
                });
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
