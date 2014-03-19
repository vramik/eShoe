package com.issuetracker.pages.component.register;

import com.issuetracker.model.User;
import com.issuetracker.pages.Login;
import com.issuetracker.pages.validator.UsernameValidator;
import com.issuetracker.service.api.UserService;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.validation.EqualPasswordInputValidator;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.validation.validator.EmailAddressValidator;

import javax.inject.Inject;

/**
 *
 * @author mgottval
 */
public class RegisterForm extends Panel {

    private User user;
    @Inject
    private UserService userService;
    private Form<User> insertForm;

    public RegisterForm(String id) {
        super(id);
        user = new User();
        add(new FeedbackPanel("feedback"));

        insertForm = new Form<User>("insertForm", new CompoundPropertyModel<User>(user)) {
            @Override
            protected void onSubmit() {
                //  user = new User();
                userService.insert(user);
                setResponsePage(Login.class);
            }
        };

        insertForm.add(new RequiredTextField<String>("name"));
        insertForm.add(new RequiredTextField<String>("lastName"));
        RequiredTextField<String> email = new RequiredTextField<String>("email");
        email.add(EmailAddressValidator.getInstance());
        insertForm.add(email);
        RequiredTextField<String> username = new RequiredTextField<String>("username");
        username.add(new UsernameValidator());
        insertForm.add(username);
        PasswordTextField password = new PasswordTextField("password");
        insertForm.add(password);
        PasswordTextField confirmPassword = new PasswordTextField("confirmPassword");
        insertForm.add(confirmPassword);
        insertForm.add(new EqualPasswordInputValidator(password, confirmPassword));

        add(insertForm);

    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
