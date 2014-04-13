package com.issuetracker.pages.fulltext;

import com.issuetracker.model.*;
import com.issuetracker.pages.IssueDetail;
import com.issuetracker.pages.PageLayout;
import com.issuetracker.service.api.IssueService;
import com.issuetracker.service.api.IssueTypeService;
import com.issuetracker.service.api.ProjectService;
import com.issuetracker.service.api.StatusService;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.extensions.markup.html.form.DateTextField;
import org.apache.wicket.extensions.yui.calendar.DatePicker;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.*;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import javax.inject.Inject;
import java.util.*;

/**
 * TODO: document this
 *
 * @author Jiri Holusa
 */
public class FulltextSearch extends PageLayout {

    @Inject
    private IssueService issueService;
    @Inject
    private ProjectService projectService;
    @Inject
    private IssueTypeService issueTypeService;
    @Inject
    private StatusService statusService;

    private String queryInput;

    private ListMultipleChoice<IssueType> issueTypesSelect;
    private ListMultipleChoice<Status> statusesSelect;
    private ListMultipleChoice<Project> projectsSelect;
    private DateTextField dateFromField;
    private DateTextField dateToField;

    private List<Project> projects;
    private List<Status> statuses;
    private List<IssueType> issueTypes;
    private Date dateFrom;
    private Date dateTo;

    public FulltextSearch() {
        add(new FeedbackPanel("feedbackPanel"));

        Form form = new Form("searchForm") {
            @Override
            protected void onSubmit() {
                //issues = issueService.getIssuesBySearch(project, version, component, issueTypes, statusList, containsText);
            }
        };

        List<Project> projectsModel = new ArrayList<Project>();
        projectsModel.add(null);
        projectsModel.addAll(projectService.getProjects());

        List<IssueType> issueTypesModel = new ArrayList<IssueType>();
        issueTypesModel.add(null);
        issueTypesModel.addAll(issueTypeService.getIssueTypes());

        List<Status> statusesModel = new ArrayList<Status>();
        statusesModel.add(null);
        statusesModel.addAll(statusService.getStatuses());

        projectsSelect = new ListMultipleChoice<Project>("projectsSelect", new PropertyModel<List<Project>>(this, "projects"), projectsModel, new NullAsAllChoiceRenderer<Project>("name"));
        issueTypesSelect = new ListMultipleChoice<IssueType>("issueTypesSelect", new PropertyModel<List<IssueType>>(this, "issueTypes"), issueTypesModel, new NullAsAllChoiceRenderer<IssueType>("name"));
        statusesSelect = new ListMultipleChoice<Status>("statusesSelect", new PropertyModel<List<Status>>(this, "statuses"), statusesModel, new NullAsAllChoiceRenderer<Status>("name"));
        dateFromField = new DateTextField("dateFromField", new PropertyModel<Date>(this, "dateFrom"), "dd.MM.yyyy");
        dateToField = new DateTextField("dateToField", new PropertyModel<Date>(this, "dateTo"), "dd.MM.yyyy");
        dateFromField.add(new DatePicker());
        dateToField.add(new DatePicker());

        form.add(projectsSelect);
        form.add(issueTypesSelect);
        form.add(statusesSelect);
        form.add(dateFromField);
        form.add(dateToField);

        form.add(new TextField("queryInput", new PropertyModel<String>(this, "queryInput")));

        add(form);
    }

    public String getQueryInput() {
        return queryInput;
    }

    public void setQueryInput(String queryInput) {
        this.queryInput = queryInput;
    }

    public ListMultipleChoice<IssueType> getIssueTypesSelect() {
        return issueTypesSelect;
    }

    public void setIssueTypesSelect(ListMultipleChoice<IssueType> issueTypesSelect) {
        this.issueTypesSelect = issueTypesSelect;
    }

    public ListMultipleChoice<Status> getStatusesSelect() {
        return statusesSelect;
    }

    public void setStatusesSelect(ListMultipleChoice<Status> statusesSelect) {
        this.statusesSelect = statusesSelect;
    }

    public ListMultipleChoice<Project> getProjectsSelect() {
        return projectsSelect;
    }

    public void setProjectsSelect(ListMultipleChoice<Project> projectsSelect) {
        this.projectsSelect = projectsSelect;
    }

    public List<Project> getProjects() {
        return projects;
    }

    public void setProjects(List<Project> projects) {
        this.projects = projects;
    }

    public List<Status> getStatuses() {
        return statuses;
    }

    public void setStatuses(List<Status> statuses) {
        this.statuses = statuses;
    }

    public List<IssueType> getIssueTypes() {
        return issueTypes;
    }

    public void setIssueTypes(List<IssueType> issueTypes) {
        this.issueTypes = issueTypes;
    }

    public Date getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(Date dateFrom) {
        this.dateFrom = dateFrom;
    }

    public Date getDateTo() {
        return dateTo;
    }

    public void setDateTo(Date dateTo) {
        this.dateTo = dateTo;
    }

    public DateTextField getDateFromField() {
        return dateFromField;
    }

    public void setDateFromField(DateTextField dateFromField) {
        this.dateFromField = dateFromField;
    }

    public DateTextField getDateToField() {
        return dateToField;
    }

    public void setDateToField(DateTextField dateToField) {
        this.dateToField = dateToField;
    }

    public class NullAsAllChoiceRenderer<T> extends ChoiceRenderer<T> {

        public NullAsAllChoiceRenderer(String object) {
            super(object);
        }

        @Override
        public Object getDisplayValue(T object) {
            if(object == null) {
                return "All";
            }

            return super.getDisplayValue(object);
        }
    }
}