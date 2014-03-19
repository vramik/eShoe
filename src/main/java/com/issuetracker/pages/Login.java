package com.issuetracker.pages;

import com.issuetracker.dao.api.UserDao;
import com.issuetracker.model.User;
import com.issuetracker.pages.component.login.ForgotPasswordLink;
import com.issuetracker.pages.component.login.SignUpLink;
import javax.inject.Inject;
import javax.persistence.NoResultException;

import com.issuetracker.service.api.UserService;
import org.apache.commons.lang.StringUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.util.cookies.CookieUtils;

/**
 *
 * @author mgottval
 */
public class Login extends PageLayout {

    private SignUpLink signUpLink;
    private ForgotPasswordLink forgotPasswordLink;
    @Inject
    private UserService userService;
    private static final String COOKIE_LAST_LOGGING_USER = "lastLoggingUser";

    private Form<User> form;
    private FeedbackPanel feedback;

    private User user = new User();

    public Login() {
        this.feedback = (FeedbackPanel) new FeedbackPanel("feedback").setOutputMarkupId(true);
        this.add(this.feedback);
        user.setName(new CookieUtils().load(COOKIE_LAST_LOGGING_USER));

        this.form = new Form<User>("form");
        this.form.setOutputMarkupId(true);

        this.form.add(new TextField("user", new PropertyModel(this.user, "name")));
        this.form.add(new PasswordTextField("password", new PropertyModel(this.user, "password")).setRequired(false));

        final AjaxButton loginBtn = new AjaxButton("login") {
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                target.add(feedback);

                // if( rememberMe )
                new CookieUtils().save(COOKIE_LAST_LOGGING_USER, user.getName());

                if (StringUtils.isBlank(user.getName()) || StringUtils.isBlank(user.getPassword())) {
                    getPage().get("form:user").error("Please fill the username and password.");
                    return;
                }
                if (userService.getUserByName(user.getName())==null) {
                    getPage().get("form:user").error("User doesn't exist.");
                    return;
                }

                try {
                    if (!Login.this.getSession().signIn(user.getName(), user.getPassword())) {
                        throw new NoResultException("No such user.");
                    }
                    setResponsePage(HomePage.class);
                } catch (NoResultException ex) {
                    error("Wrong password or non-existent user: " + user.getName());
                    info("To get forgotten password, fill in user name and/or email.");
                }
            }
        };
        this.form.add(loginBtn);

        add(this.form);

        forgotPasswordLink = new ForgotPasswordLink("forgotPasswordLink");
        add(forgotPasswordLink);

        signUpLink = new SignUpLink("signUpLink");
        add(signUpLink);

    }
}
