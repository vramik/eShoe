package com.issuetracker.pages;

import com.issuetracker.dao.api.ProjectDao;
import com.issuetracker.dao.api.WorkflowDao;
import com.issuetracker.model.Project;
import com.issuetracker.model.Workflow;
import com.issuetracker.pages.component.workflow.WorkflowListView;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

/**
 *
 * @author mgottval
 */
public class CreateWorkflow extends PageLayout {

    private Form<Workflow> insertWorkflowForm;
    private Workflow workflow;
    @Inject
    private WorkflowDao workflowDao;
    @Inject
    private ProjectDao projectDao;
    private List<Workflow> workflows;
    private Project project;

    public CreateWorkflow() {
        workflows = new ArrayList<Workflow>();
        if (workflowDao.getWorkflows() == null) {
            workflows = new ArrayList<Workflow>();
        } else {
            workflows = workflowDao.getWorkflows();
        }
        IModel<List<Project>> projectsModel = new AbstractReadOnlyModel<List<Project>>() {
            @Override
            public List<Project> getObject() {
                
                return projectDao.getProjects();
            }
        };
        add(new FeedbackPanel("feedbackPanel"));
        insertWorkflowForm = new Form<Workflow>("insertWorkflowForm") {
            @Override
            protected void onSubmit() {
                workflowDao.insert(workflow);
                project.setWorkflow(workflow);
                projectDao.update(project);
                workflow = new Workflow();
            }
        };
        insertWorkflowForm.add(new RequiredTextField<String>("name", new PropertyModel<String>(this, "workflow.name")));
        DropDownChoice<Project> projectDropDown = new DropDownChoice<Project>("projectsDropDown", new PropertyModel<Project>(this, "project"), projectsModel, new ChoiceRenderer<Project>("name"));
        projectDropDown.setRequired(true);
        insertWorkflowForm.add(projectDropDown);
        add(insertWorkflowForm);

        IModel<List<Workflow>> compoModel = new CompoundPropertyModel<List<Workflow>>(workflows) {
            @Override
            public List<Workflow> getObject() {
                return workflowDao.getWorkflows(); //To change body of generated methods, choose Tools | Templates.
            }
        };

        add(new Label("workflowName", "Workflows"));
        WorkflowListView workflowList = new WorkflowListView<Workflow>("workflowList", compoModel);
        add(workflowList);
    }

    public Workflow getWorkflow() {
        return workflow;
    }

    public void setWorkflow(Workflow workflow) {
        this.workflow = workflow;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }
    
    
    
}
