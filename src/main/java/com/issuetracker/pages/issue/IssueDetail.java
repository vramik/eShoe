package com.issuetracker.pages.issue;

import com.issuetracker.pages.project.ProjectDetail;
import com.issuetracker.pages.layout.PageLayout;
import com.issuetracker.model.*;
import com.issuetracker.pages.component.comment.CommentForm;
import com.issuetracker.pages.component.comment.CommentListView;
import com.issuetracker.pages.component.customFieldIssueValue.CustomFieldIssueValueListView;
import com.issuetracker.pages.component.issue.IssueRelationsListView;
import com.issuetracker.pages.component.issue.SetIssueStateForm;
import com.issuetracker.pages.layout.ModalPanel1;
import com.issuetracker.pages.permissions.IssuePermission;
import com.issuetracker.service.api.IssueService;
import com.issuetracker.service.api.SecurityService;
import static com.issuetracker.web.Constants.HOME_PAGE;
import static com.issuetracker.web.security.KeycloakAuthSession.*;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxLink;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.apache.wicket.request.flow.RedirectToUrlException;
import org.apache.wicket.util.string.StringValue;

/**
 *
 * @author mgottval
 */
public class IssueDetail extends PageLayout {

    @Inject
    private IssueService issueService;
    @Inject
    private SecurityService securityService;

    private IndicatingAjaxLink addWatcherLink;
    private final Label watchersCountLabel;
    private final ModalWindow modal2;
    private final IModel<List<String>> watchersModel;
    private Link projectLink;
    private int watchersCount;
    private Issue issue;
    private CustomField customField;
    private List<String> watchersList;
    private List<CustomFieldIssueValue> cfIssueValueList;
    private List<IssuesRelationship> issuesRelationships;
    private final IssueRelationsListView<IssuesRelationship> issueRelationsListView;
    
    List<String> permittedActions = new ArrayList<>();

    public IssueDetail(PageParameters parameters) {
        StringValue stringIssueId = parameters.get("issue");
        if (stringIssueId.equals(StringValue.valueOf((String)null))) {
            throw new RedirectToUrlException(HOME_PAGE);
        }
        Long issueId = stringIssueId.toLong();
        
        permittedActions = securityService.getPermittedActionsForUserAndItem(TypeId.issue, issueId);
        
        issue = issueService.getIssueById(issueId);
        if (issue == null) {
            throw new RedirectToUrlException(HOME_PAGE);
        }
        watchersList = issueService.getIssueWatchers(issue);
        
        customField = new CustomField();
        PropertyModel<Issue> defaultModel = new PropertyModel<>(this, "issue");
        setDefaultModel(defaultModel);
        watchersCount = issue.getWatchers().size();

        add(new Link("permissions") {
            @Override
            public void onClick() {
                PageParameters pageParameters = new PageParameters();
                pageParameters.add("issue", issue.getId());
                setResponsePage(IssuePermission.class, pageParameters);
            }
        });
        
        projectLink = new Link("link") {
            @Override
            public void onClick() {
                PageParameters pageParameters = new PageParameters();
                pageParameters.add("project", issue.getProject().getId());
                setResponsePage(ProjectDetail.class, pageParameters);

            }
        };
        projectLink.add(new Label("project", new PropertyModel(this, "issue.project.name")));
        add(projectLink);
        add(new Label("name", new PropertyModel(this, "issue.name")));
        add(new Label("summary", new PropertyModel(this, "issue.summary")));
        add(new Label("description", new PropertyModel(this, "issue.description")));

        add(new Label("type", new PropertyModel(this, "issue.issueType.name")));
//        add(new Label("affectsVersions", new PropertyModel(this, "issue.projectVersion.name")));
        StringBuilder builder = new StringBuilder();
        for (ProjectVersion version : issue.getAffectedVersions()) {
            builder.append(version.getName());
            builder.append(", ");
        }
        add(new Label("affectsVersions", builder.toString()));
        add(new Label("priority", new PropertyModel(this, "issue.priority")));

        String created = issue.getCreated().toString();
        String updated = issue.getUpdated() != null ? issue.getUpdated().toString() : created;
        add(new Label("created", created));
        add(new Label("updated", updated));
//        add(new RequiredTextField("description"));
//        add(new RequiredTextField("project.name"));
        add(new Label("componentName", new PropertyModel(this, "issue.component.name")));
        watchersCountLabel = new Label("watchersCountLabel", new PropertyModel(this, "watchersCount"));
        watchersCountLabel.setOutputMarkupId(true);
//        watchersCountLabel.add();

        add(watchersCountLabel);


        watchersModel = new PropertyModel<List<String>>(this, "watchersList") {
            @Override
            public List<String> getObject() {
                List<String> list = new ArrayList<>(issueService.getIssueWatchers(issue));
                return list;
            }
        };

        addWatcherLink = new IndicatingAjaxLink("addWatcherLink") {
            @Override
            public void onClick(AjaxRequestTarget target) {                
                target.add(watchersCountLabel);
                watchersList.add(getIDToken().getPreferredUsername());
                watchersCount++;
                issue.setWatchers(watchersList);
                issueService.addWatcher(issue);
//                issueService.update(issue);

//                target.add(modal2);
                modal2.setContent(new ModalPanel1(modal2.getContentId(), watchersModel));
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                }
            }
        };
        addWatcherLink.setEnabled(isSignedIn() && !watchersList.contains(getIDToken().getPreferredUsername()));
        add(addWatcherLink);



        add(modal2 = new ModalWindow("modal2"));

        modal2.setContent(new ModalPanel1(modal2.getContentId(), watchersModel));
        modal2.setTitle("Show watchers");
//        modal2.setCookieName("modal-2");
//        modal2.setOutputMarkupId(true);
        modal2.setCloseButtonCallback(new ModalWindow.CloseButtonCallback() {
            @Override
            public boolean onCloseButtonClicked(AjaxRequestTarget target) {
//                setResult("Modal window 2 - close button");
                return true;
            }
        });

        modal2.setWindowClosedCallback(new ModalWindow.WindowClosedCallback() {
            @Override
            public void onClose(AjaxRequestTarget target) {
//                target.add(result);
            }
        });

        add(new AjaxLink<Void>("showModal2") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                target.add(modal2);
                modal2.show(target);
//                PageParameters pageParameters = new PageParameters();
//                pageParameters.add("issue", ((Issue) item.getModelObject()).getIssueId());
            }
        });



        add(new CommentForm("commentForm", new PropertyModel<Issue>(this, "issue")).setVisible(isSignedIn()));
        add(new CommentListView("commentListView", new PropertyModel<Issue>(this, "issue")));

        add(new SetIssueStateForm("setIssueStateForm", new PropertyModel<Issue>(this, "issue")));

        IModel<List<CustomFieldIssueValue>> cfModel = new PropertyModel<List<CustomFieldIssueValue>>(this, "cfList") {
            @Override
            public List<CustomFieldIssueValue> getObject() {
                List<CustomFieldIssueValue> customFieldIssueValueList = issue.getCustomFields();
                if (customFieldIssueValueList == null) {
                    return new ArrayList<>();
                }
                return customFieldIssueValueList;
            }
        };
        add(new CustomFieldIssueValueListView<>("cfListView", cfModel));

        IModel issueRelModel = new AbstractReadOnlyModel<List<IssuesRelationship>>() {
            @Override
            public List<IssuesRelationship> getObject() {
                List<IssuesRelationship> models = issue.getRelatesTo();
                if (models == null) {
                    models = Collections.emptyList();
                }
                return models;
            }
        };
        issueRelationsListView = new IssueRelationsListView<>("issueRelListView", issueRelModel);
        add(issueRelationsListView);

    }

    public Issue getIssue() {
        return issue;
    }

    public void setIssue(Issue issue) {
        this.issue = issue;
    }

    public int getWatchersCount() {
        return watchersCount;
    }

    public void setWatchersCount(int watchersCount) {
        this.watchersCount = watchersCount;
    }

    public List<String> getWatchersList() {
        return watchersList;
    }

    public void setWatchersList(List<String> watchersList) {
        this.watchersList = watchersList;
    }

    public CustomField getCustomField() {
        return customField;
    }

    public void setCustomField(CustomField customField) {
        this.customField = customField;
    }

    public List<CustomFieldIssueValue> getCfList() {
        return cfIssueValueList;
    }

    public void setCfList(List<CustomFieldIssueValue> cfIssueValueList) {
        this.cfIssueValueList = cfIssueValueList;
    }
}
