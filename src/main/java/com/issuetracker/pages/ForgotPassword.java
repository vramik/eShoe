/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.issuetracker.pages;

import com.issuetracker.mail.MailClient;
import com.issuetracker.mail.PasswordGenerator;
import com.issuetracker.model.User;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.PropertyModel;

/**
 *
 * @author mgottval
 */
public class ForgotPassword extends PageLayout {

    private Form<User> sendPasswordForm;
    private String email;
//    private User user;
//    private UserDao userDao;

    public ForgotPassword() {
        add(new FeedbackPanel("feedback"));
        sendPasswordForm = new Form<User>("sendPasswordForm") {
            @Override
            protected void onSubmit() {
                PasswordGenerator generator = new PasswordGenerator();
//                user = userDao.getUserByEmail(email);
                try {
                    MailClient client = new MailClient();
                    String server = "smtp.gmail.com";
                    String from = "mgottval@issuetracker.com";
                    String to = email;
                    String subject = "IssueTracker - Password";
                    String message = generator.generatePassword();
                    //String[] filenames = {"c:\somefile.txt"};

                    client.sendMail(server, from, to, subject, message);
                } catch (Exception e) {
                    e.printStackTrace(System.out);
                }
                setResponsePage(Login.class);
            }
        };

        sendPasswordForm.add(new RequiredTextField<String>("email", new PropertyModel<String>(this, "email")));        
        add(sendPasswordForm);
    }
}
