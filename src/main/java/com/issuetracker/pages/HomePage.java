/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.issuetracker.pages;

import com.issuetracker.dao.api.UserDao;
import com.issuetracker.model.User;
import com.issutracker.form.LoginForm;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.inject.Inject;
import org.apache.wicket.Page;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.extensions.markup.html.form.select.IOptionRenderer;
import org.apache.wicket.extensions.markup.html.form.select.Select;
import org.apache.wicket.extensions.markup.html.form.select.SelectOption;
import org.apache.wicket.extensions.markup.html.form.select.SelectOptions;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.list.PropertyListView;
import org.apache.wicket.model.AbstractPropertyModel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;

/**
 *
 * @author mgottval
 */
public class HomePage extends PageLayout {

    private Form form;
//    private DropDownChoice<User> personsList;
    private DropDownChoice<Link> projectChoice;
    private DropDownChoice<String> issueChioce;
    @Inject
    private UserDao userDao;
    private Link selectedProjectLink;
    private Map<String, WebPage> pagesMap;
    private String selected = "Create Project";

    public HomePage() {
        //TODO logged user
//        pagesMap.put("Create Project", new CreateProject());
//        pagesMap.put("Create Issue", new CreateIssue());

        final Link searchIssueLink = new Link("searchIssueLink") {
            @Override
            public void onClick() {
                setResponsePage(ListIssues.class);
            }
        };

        // searchIssueLink.add(new Label("SearchName", "Search for issue"));
        add(searchIssueLink);

//        final Link createIssueLink = new Link("createIssueLink") {
//            @Override
//            public void onClick() {
//                setResponsePage(CreateIssue.class);
//            }
//        };
//
//        add(createIssueLink);
//
//
//        final Link createProjectLink = new Link("createProjectLink") {
//            @Override
//            public void onClick() {
//                setResponsePage(CreateProject.class);
//            }
//        };
//
//        add(createProjectLink);

        final Link createIssuetypeLink = new Link("createIssueTypeLink") {
            @Override
            public void onClick() {
                setResponsePage(CreateComponent.class);
            }
        };

        add(createIssuetypeLink);

        final Link registerLink = new Link("registerLink") {
            @Override
            public void onClick() {
                setResponsePage(Register.class);
            }
        };

        add(registerLink);


        List<String> opts = new ArrayList<String>();
        opts.add("Create Project");
        opts.add("Create Issue");
//        opts.addAll(pagesMap.keySet());
//new PropertyModel<String>(this, selected)
//
//        DropDownChoice projectLinksDropDown = new DropDownChoice("projectLinks", new PropertyModel<String>(this, selected), opts);
//        projectLinksDropDown.add(new AjaxFormComponentUpdatingBehavior("onChange") {
//            @Override
//            protected void onUpdate(AjaxRequestTarget target) {
//                if (selected.equals("Create Project")) {
//                    setResponsePage(CreateProject.class);
//                }
//                if (selected.equals("Create Issue")) {
//                    setResponsePage(CreateIssue.class);
//                }
////                selectedProjectLink = (Link) getFormComponent().getConvertedInput();
////                setResponsePage(selectedProjectLink.getPage());
//            }
//        });
//
//        add(projectLinksDropDown);
        add(new PropertyListView<String>("projectTasks", opts) {
            @Override
            public void populateItem(final ListItem<String> listItem) {
                final String stringLink = listItem.getModelObject();
                Link nameLink = new Link<String>("showIssue", listItem.getModel()) {
                    @Override
                    public void onClick() {
                        selected = stringLink;
                        if (selected.equals("Create Project")) {
                            setResponsePage(CreateProject.class);
                        }
                        if (selected.equals("Create Issue")) {
                            setResponsePage(CreateIssue.class);
                        }
                    }
                };
                nameLink.add(new Label("name", stringLink));
                listItem.add(nameLink);

            }
        });
    }

//        ListView listview = new ListView<Issue>("issues", new PropertyModel<List<Issue>>(this, "issues")) {
//            @Override
//            protected void populateItem(final ListItem<Issue> item) {
//                Issue issue = item.getModelObject();
//                Link nameLink = new Link<Issue>("showIssue", item.getModel()) {
//                    @Override
//                    public void onClick() {
//                        PageParameters pageParameters = new PageParameters();
//                        pageParameters.add("issue", ((Issue) item.getModelObject()).getIssueId());
//                        setResponsePage(ShowIssue.class, pageParameters);
//                    }
//                };
//                nameLink.add(new Label("name", issue.getName()));
//                item.add(nameLink);
//                // item.add(new Label("name", issue.getName()));
//                item.add(new Label("description", issue.getDescription()));
//                item.add(new Label("projectName", issue.getProject().getName()));
//                item.add(new Link<Issue>("delete", item.getModel()) {
//                    @Override
//                    public void onClick() {
//                        issueDao.removeIssue(item.getModelObject());
//                    }
//                });
//            }
//        };
//IOptionRenderer<Link> linkRender = new IOptionRenderer<Link>(); 
//
//        SelectOptions<Link> projectLinksOptions = new SelectOptions<Link>("projectLinks", projectLinks, linkRender);
//                                          
//
//Select select = new Select("produceSelect", 
//                           new PropertyModel<Link>(model, "favProduce"));
//select.add(projectLinksOptions);
//        form = new Form("form", new CompoundPropertyModel<User>(listModel));
//        form.add(new RequiredTextField("name"));
//        form.add(new RequiredTextField("lastname"));
//        form.add(new RequiredTextField("username"));
//        form.add(new RequiredTextField("email"));
//        add(form);
//        Model<User> listModel = new Model<User>();
//        ChoiceRenderer<User> personRender = new ChoiceRenderer<User>("name");
//        personsList = new DropDownChoice<User>("persons", listModel, userDao.getUsers),
//                personRender) {
//            @Override
//            protected boolean wantOnSelectionChangedNotifications() {
//                return true;
//            }
//        };
//        add(personsList);
//        form = new Form("form", new CompoundPropertyModel<User>(listModel));
//        form.add(new RequiredTextField("name"));
//        form.add(new RequiredTextField("lastname"));
//        form.add(new RequiredTextField("username"));
//        form.add(new RequiredTextField("email"));
//        add(form);
    public Link getSelectedProjectLink() {
        return selectedProjectLink;
    }

    public void setSelectedProjectLink(Link selectedProjectLink) {
        this.selectedProjectLink = selectedProjectLink;
    }

    public String getSelected() {
        return selected;
    }

    public void setSelected(String selected) {
        this.selected = selected;
    }
}
