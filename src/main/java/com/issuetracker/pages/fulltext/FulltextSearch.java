package com.issuetracker.pages.fulltext;

import com.issuetracker.model.Issue;
import com.issuetracker.model.IssueType;
import com.issuetracker.model.Project;
import com.issuetracker.model.Status;
import com.issuetracker.pages.issue.IssueDetail;
import com.issuetracker.pages.layout.PageLayout;
import com.issuetracker.search.ql.QueryLanguageBuilder;
import com.issuetracker.service.api.*;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.extensions.markup.html.form.DateTextField;
import org.apache.wicket.extensions.yui.calendar.DatePicker;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.ListMultipleChoice;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import javax.inject.Inject;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Controller of fulltext search page.
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
    @Inject
    private SearchService searchService;

    private String queryInput;
    private boolean isQueryLanguageUsed = false;

    private Form form;
    private TextField queryInputText;
    private ListMultipleChoice<IssueType> issueTypesSelect;
    private ListMultipleChoice<Status> statusesSelect;
    private ListMultipleChoice<Project> projectsSelect;
    private DateTextField dateFromField;
    private DateTextField dateToField;
    private ListView<Issue> issuesListView;
    private AjaxLink advancedSearchLink;
    private Label advancedSearchLabel;

    private List<Project> projects;
    private List<Status> statuses;
    private List<IssueType> issueTypes;
    private Date dateFrom;
    private Date dateTo;

    private List<Issue> issues;

    private String advancedSearchIndicator = "Switch to search by inserting query";
    private boolean advancedSearchEnabled = false;

    private QueryLanguageBuilder queryBuilder = new QueryLanguageBuilder();

    public FulltextSearch() {
        add(new FeedbackPanel("feedbackPanel"));

        form = new Form("searchForm") {
            @Override
            protected void onSubmit() {
                computeQuery();

                String query = advancedSearchEnabled ? queryInput : queryBuilder.getQuery();
                issues = searchService.search(query);
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

        queryInputText = new TextField("queryInput", new PropertyModel<String>(this, "queryInput"));
        queryInputText.setOutputMarkupId(true);
        projectsSelect = new ListMultipleChoice<Project>("projectsSelect", new PropertyModel<List<Project>>(this, "projects"), projectsModel, new NullAsAllChoiceRenderer<Project>("name"));
        issueTypesSelect = new ListMultipleChoice<IssueType>("issueTypesSelect", new PropertyModel<List<IssueType>>(this, "issueTypes"), issueTypesModel, new NullAsAllChoiceRenderer<IssueType>("name"));
        statusesSelect = new ListMultipleChoice<Status>("statusesSelect", new PropertyModel<List<Status>>(this, "statuses"), statusesModel, new NullAsAllChoiceRenderer<Status>("name"));
        dateFromField = new DateTextField("dateFromField", new PropertyModel<Date>(this, "dateFrom"), "yyyy-MM-dd");
        dateToField = new DateTextField("dateToField", new PropertyModel<Date>(this, "dateTo"), "yyyy-MM-dd");
        dateFromField.add(new DatePicker());
        dateToField.add(new DatePicker());
        advancedSearchLabel = new Label("advancedSearchLabel", new PropertyModel<String>(this, "advancedSearchIndicator"));
        advancedSearchLabel.setOutputMarkupId(true);
        advancedSearchLink = new AjaxLink("advancedSearchSwitch")
                                    {
                                        @Override
                                        public void onClick(AjaxRequestTarget target)
                                        {
                                            computeQuery();

                                            advancedSearchEnabled = !advancedSearchEnabled;
                                            if(advancedSearchEnabled) {
                                                queryInput = queryBuilder.getQuery();
                                                advancedSearchIndicator = "Now inserting via query language";
                                            }
                                            else {
                                                queryInput = "";
                                                advancedSearchIndicator = "Now basic inserting via GUI";
                                            }

                                            target.add(queryInputText);
                                            target.add(advancedSearchLabel);
                                        }
                                    };

        createIssuesListView();

        addAjaxEvents();

        form.add(projectsSelect);
        form.add(issueTypesSelect);
        form.add(statusesSelect);
        form.add(dateFromField);
        form.add(dateToField);
        form.add(advancedSearchLink);
        form.add(advancedSearchLabel);

        form.add(queryInputText);

        add(form);
        add(issuesListView);
    }

    private void addAjaxEvents() {
        projectsSelect.add(new AjaxFormComponentUpdatingBehavior("onchange") {
            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                if(projects.contains(null)) { //if "All" selected, unselect everything else
                    projects.clear();
                    projects.add(null);

                    target.add(projectsSelect);
                }

                computeQuery();
                if(advancedSearchEnabled) {
                    queryInput = queryBuilder.getQuery();
                }
                target.add(queryInputText);
            }
        });

        issueTypesSelect.add(new AjaxFormComponentUpdatingBehavior("onchange") {
            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                if(issueTypes.contains(null)) { //if "All" selected, unselect everything else
                    issueTypes.clear();
                    issueTypes.add(null);

                    target.add(issueTypesSelect);
                }

                computeQuery();
                if(advancedSearchEnabled) {
                    queryInput = queryBuilder.getQuery();
                }
                target.add(queryInputText);
            }
        });

        statusesSelect.add(new AjaxFormComponentUpdatingBehavior("onchange") {
            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                if(statuses.contains(null)) { //if "All" selected, unselect everything else
                    statuses.clear();
                    statuses.add(null);

                    target.add(statusesSelect);
                }

                computeQuery();
                if(advancedSearchEnabled) {
                    queryInput = queryBuilder.getQuery();
                }
                target.add(queryInputText);
            }
        });

        dateFromField.add(new AjaxFormComponentUpdatingBehavior("onchange") {
            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                computeQuery();
                if(advancedSearchEnabled) {
                    queryInput = queryBuilder.getQuery();
                }
                target.add(queryInputText);
            }
        });

        dateToField.add(new AjaxFormComponentUpdatingBehavior("onchange") {
            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                computeQuery();
                if(advancedSearchEnabled) {
                    queryInput = queryBuilder.getQuery();
                }
                target.add(queryInputText);
            }
        });
    }

    private void createIssuesListView() {
        issuesListView = new ListView<Issue>("issues", new PropertyModel<List<Issue>>(this, "issues")) {
            @Override
            protected void populateItem(final ListItem<Issue> item) {
                Issue issue = item.getModelObject();
                Link nameLink = new Link<Issue>("showIssue", item.getModel()) {
                    @Override
                    public void onClick() {
                        PageParameters pageParameters = new PageParameters();
                        pageParameters.add("issue", (item.getModelObject()).getIssueId());
                        setResponsePage(IssueDetail.class, pageParameters);
                    }
                };
                nameLink.add(new Label("issue.name", issue.getName()));
                item.add(nameLink);
                item.add(new Label("issue.description", issue.getDescription()));
            }
        };
    }

    private void computeQuery() {
        queryBuilder.clear();

        if(!advancedSearchEnabled && queryInput != null && queryInput.length() != 0) {
            queryBuilder.tilda("text", queryInput);
        }

        if(projects != null) {
            if(projects.size() == 1) {
                if(projects.get(0) != null) {
                    queryBuilder.equality("project", projects.get(0).getName());
                }
            }
            else if(!projects.isEmpty()) {
                List<String> names = new ArrayList<String>();
                for(Project project: projects) {
                    names.add(project.getName());
                }
                queryBuilder.in("project", names);
            }
        }

        if(issueTypes != null) {
            if(issueTypes.size() == 1) {
                if(issueTypes.get(0) != null) {
                    queryBuilder.equality("issue_type", issueTypes.get(0).getName());
                }
            }
            else if(!issueTypes.isEmpty()) {
                List<String> names = new ArrayList<String>();
                for(IssueType issueType: issueTypes) {
                    names.add(issueType.getName());
                }
                queryBuilder.in("issue_type", names);
            }
        }

        if(statuses != null) {
            if(statuses.size() == 1) {
                if(statuses.get(0) != null) {
                    queryBuilder.equality("status", statuses.get(0).getName());
                }
            }
            else if(!statuses.isEmpty()) {
                List<String> names = new ArrayList<String>();
                for(Status status: statuses) {
                    names.add(status.getName());
                }
                queryBuilder.in("status", names);
            }
        }

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYY-MM-dd");
        if(dateFrom != null) {
            queryBuilder.gte("created", simpleDateFormat.format(dateFrom));
        }

        if(dateTo != null) {
            queryBuilder.lte("created", simpleDateFormat.format(dateTo));
        }
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

    public ListView<Issue> getIssuesListView() {
        return issuesListView;
    }

    public void setIssuesListView(ListView<Issue> issuesListView) {
        this.issuesListView = issuesListView;
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