/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.issuetracker.pages;

import com.issuetracker.dao.api.IssueDao;
import com.issuetracker.dao.api.UserDao;
import com.issuetracker.model.Issue;
import com.issuetracker.model.User;
import com.issuetracker.pages.component.comment.CommentListView;
import com.issuetracker.pages.component.comment.CommentForm;
import com.issuetracker.pages.component.issue.SetIssueStateForm;
import com.issuetracker.pages.layout.ModalPanel1;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Inject;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxLink;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

/**
 *
 * @author mgottval
 */
public class IssueDetail extends PageLayout {

    @Inject
    private IssueDao issueDao;
    @Inject
    private UserDao userDao;
    private IndicatingAjaxLink addWatcherLink;
    private final Label watchersCountLabel;
    private final ModalWindow modal2;
    private final IModel<List<User>> watchersModel;
    private Form<Issue> updateIssueForm;
    private Link projectLink;
    private int watchersCount;
    private Issue issue;
    private List<User> watchersList;
//    private IModel<List<Comment>> comments;
    //private ListChoice<Version>

    public IssueDetail(PageParameters parameters) {
        Long issueId = parameters.get("issue").toLong();
        issue = issueDao.getIssueById(issueId);
        PropertyModel<Issue> defaultModel = new PropertyModel<Issue>(this, "issue");
        setDefaultModel(defaultModel);
        watchersCount = issue.getWatches().size();
//        comments = new AbstractReadOnlyModel<List<Comment>>() {
//
//            @Override
//            public List<Comment> getObject() {
//                List<Comment> models = issue.getComments();
//                if (models == null) {
//                    models = Collections.emptyList();
//                }
//                return models;
//            }
//        };

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
        add(new Label("description", new PropertyModel(this, "issue.description")));

        add(new Label("type", new PropertyModel(this, "issue.issueType.name")));
        add(new Label("affectsVersions", new PropertyModel(this, "issue.projectVersion.name")));
        add(new Label("priority", new PropertyModel(this, "issue.priority")));
//        add(new RequiredTextField("description"));
//        add(new RequiredTextField("project.name"));
        add(new Label("componentName", new PropertyModel(this, "issue.component.name")));
        watchersCountLabel = new Label("watchersCountLabel", new PropertyModel(this, "watchersCount"));
        watchersCountLabel.setOutputMarkupId(true);
//        watchersCountLabel.add();

        add(watchersCountLabel);


        watchersModel = new PropertyModel<List<User>>(this, "watchersList") {
            @Override
            public List<User> getObject() {
                List<User> list = new ArrayList<User>(issueDao.getIssueWatchers(issue));
                return list;
            }
        };

        addWatcherLink = new IndicatingAjaxLink("addWatcherLink") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                watchersCount++;
                target.add(watchersCountLabel);
                watchersList = issueDao.getIssueWatchers(issue);
                if (watchersList == null) {
                    watchersList = new ArrayList<User>();
                }
                //CREATENEWUSER .. will be current user               
                User user = new User();
                watchersList.add(user);
                userDao.addUser(user);
                issue.setWatches(watchersList);
                issueDao.updateIssue(issue);

//                target.add(modal2);
                modal2.setContent(new ModalPanel1(modal2.getContentId(), watchersModel));
                String s = "";
                for (User u : watchersList) {
                    s = s + u.getUserId();
                }
                Logger.getLogger(CreateProject.class.getName()).log(Level.SEVERE, s);
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                }
            }
        };
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

        add(new CommentForm("commentForm", new PropertyModel<Issue>(this, "issue")));
        add(new CommentListView("commentListView", new PropertyModel<Issue>(this, "issue")));
  
        add(new SetIssueStateForm("setIssueStateForm", new PropertyModel<Issue>(this, "issue")));

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

    public List<User> getWatchersList() {
        return watchersList;
    }

    public void setWatchersList(List<User> watchersList) {
        this.watchersList = watchersList;
    }
//    public List<Comment> getComments() {
//        return comments;
//    }
//
//    public void setComments(List<Comment> comments) {
//        this.comments = comments;
//    }
}
