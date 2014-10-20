package com.issuetracker.pages.permissions;

import com.issuetracker.pages.PageLayout;
import com.issuetracker.web.quilifiers.SecurityConstraint;
import com.issuetracker.web.security.KeycloakService;
import java.util.ArrayList;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import java.util.List;
import org.keycloak.representations.idm.UserRepresentation;

/**
 *
 * @author Monika
 */
public class Permissions extends PageLayout {

//    @Inject
//    private SecurityService securityService;


    @SecurityConstraint(allowedRole = "admin")
    public Permissions() {
        System.out.println("INSIDE CONSTRUCTOR");
        ListView<String> listViewUsers = new ListView<String>("usersList", getUsernames()) {

            @Override
            protected void populateItem(ListItem<String> item) {
                item.add(new Label("username", item.getModelObject()));
            }
        };
        add(listViewUsers);
    }
    
    private List<String> getUsernames() {
        List<UserRepresentation> users;
        try {
            users = KeycloakService.getUsers(getRequest());
        } catch (KeycloakService.Failure ex) {
            throw new RuntimeException(ex);
        }
        List<String> usernames = new ArrayList<>();
        for (UserRepresentation user : users) {
            usernames.add(user.getUsername());
        }
        return usernames;
    }
}
