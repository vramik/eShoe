package com.issuetracker.pages;

import com.issuetracker.pages.component.register.RegisterForm;

/**
 *
 * @author mgottval
 */
@SuppressWarnings("serial")
public class Register extends PageLayout {

    private RegisterForm registerForm;

    public Register() {
        registerForm = new RegisterForm("registerForm");
        add(registerForm);


    }

}
