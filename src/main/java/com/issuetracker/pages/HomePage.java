package com.issuetracker.pages;

import com.issuetracker.dao.api.UserDao;
import com.issuetracker.web.security.TrackerAuthSession;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.link.Link;
import org.jboss.logging.Logger;

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
    private Map<String, WebPage> pagesMap;
    private String selected = "Create Project";

    public HomePage() {

        final Link loginLink = new Link("loginLink") {
            @Override
            public void onClick() {                      
                setResponsePage(Login.class);
            }
        };
        loginLink.add(new Label("name", "Log In"));
        add(loginLink);

      
((TrackerAuthSession)getSession()).getAttribute("session");

//        ListView listview = new ListView<Issue>("issues", new PropertyModel<List<Issue>>(this, "issues")) {
//            @Override
//            protected void populateItem(final ListItem<Issue> item) {
//                Issue issue = item.getModelObject();
//                Link nameLink = new Link<Issue>("showIssue", item.getModel()) {
//                    @Override
//                    public void onClick() 
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
//                item.add(new Link<Issue>("remove", item.getModel()) {
//                    @Override
//                    public void onClick() {
//                        issueDao.remove(item.getModelObject());
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
    }

    public String getSelected() {
        return selected;
    }

    public void setSelected(String selected) {
        this.selected = selected;
    }
}
