package com.issuetracker.pages;

import com.issuetracker.pages.layout.PageLayout;
import static com.issuetracker.web.security.KeycloakAuthSession.getKeycloakSecurityContext;
import org.apache.wicket.markup.html.basic.Label;
import org.keycloak.KeycloakSecurityContext;

/**
 *
 * @author mgottval
 */
public class HomePage extends PageLayout {

    public HomePage() {
        KeycloakSecurityContext session = getKeycloakSecurityContext(getRequest());
        
        String tokenString = "not signed in!";
        if (session != null) {
            tokenString = session.getTokenString();
        }
        add(new Label("label", tokenString));
    }
}
