package com.issuetracker.pages;

import static com.issuetracker.web.Constants.*;
import org.apache.wicket.markup.html.pages.RedirectPage;
import org.apache.wicket.request.mapper.parameter.PageParameters;

/**
 * 
 * @author vramik
 */
public class Login extends PageLayout {

    public Login(PageParameters parameters) {
        setResponsePage(new RedirectPage(SERVER_URL + CONTEXT_ROOT + parameters.get("responsePage")));
    }
}
