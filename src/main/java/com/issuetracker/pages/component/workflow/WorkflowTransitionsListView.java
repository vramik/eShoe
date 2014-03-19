package com.issuetracker.pages.component.workflow;

import com.issuetracker.dao.api.WorkflowDao;
import com.issuetracker.model.Transition;
import com.issuetracker.model.Workflow;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;

import com.issuetracker.service.api.TransitionService;
import com.issuetracker.service.api.WorkflowService;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

/**
 *
 * @author mgottval
 */
public class WorkflowTransitionsListView extends Panel {

    @Inject
    private TransitionService transitionService;
    @Inject
    private WorkflowService workflowService;
    
    private ListView transitionsListView;
    private List<Transition> transitionList;
    private Workflow workflow;

    public WorkflowTransitionsListView(String id, final IModel<Workflow> workflowModel) {
        super(id);
 
        workflow = workflowModel.getObject();
        try {
            transitionList = transitionService.getTransitionsByWorkflow(workflow);
        } catch (NullPointerException e) {
            transitionList = new ArrayList<Transition>();
        }


        add(new FeedbackPanel("feedback"));
        transitionsListView = new ListView<Transition>("transitionsList", new PropertyModel<List<Transition>>(this, "transitionList")) {
            @Override
            protected void populateItem(ListItem<Transition> item) {
                final Transition transition = item.getModelObject();
                item.add(new Link<Transition>("remove", item.getModel()) {
                    @Override
                    public void onClick() {

                        transitionList.remove(transition);
                        // transitionList.setObject(transitions);
                        workflow.setTransitions(transitionList);
                        workflowService.update(workflow);

                    }
                });
                item.add(new Label("transitionName", transition.getName()));
                item.add(new Label("statusFrom", transition.getFromStatus().getName()));
                item.add(new Label("statusTo", transition.getToStatus().getName()));
            }
        };
        add(transitionsListView);
    }


    public Workflow getWorkflow() {
        return workflow;
    }

    public void setWorkflow(Workflow workflow) {
        this.workflow = workflow;
    }

    
}
