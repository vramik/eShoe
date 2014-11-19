package com.issuetracker.pages;

import com.issuetracker.pages.layout.PageLayout;
import static com.issuetracker.web.Utils.parsePageClassFromPageParams;
import org.apache.wicket.request.mapper.parameter.PageParameters;

/**
 * 
 * @author vramik
 */
public class Login extends PageLayout {

    public Login(PageParameters parameters) {
        setResponsePage(parsePageClassFromPageParams(parameters.get("page")), parameters.remove("page"));
    }
}
