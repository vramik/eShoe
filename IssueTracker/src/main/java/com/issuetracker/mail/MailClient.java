/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.issuetracker.mail;

/**
 *
 * @author Monika
 */
import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;
import javax.annotation.Resource;

public class MailClient {

    public void sendMail(String mailServer, String from, String to, String subject, String messageBody) throws
            MessagingException, AddressException {
        // Setup mail server 
        Properties props = System.getProperties();
        props.put("mail.smtp.host", mailServer);
        props.put("mail.smtp.auth", "true");

        // Get a mail session 
        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
                        @Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication("mgott@gmail.com", "");
			}
		  });

        // Define a new mail message 
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(from));
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
        message.setSubject(subject);

        // Create a message part to represent the body text 
        BodyPart messageBodyPart = new MimeBodyPart();
        messageBodyPart.setText(messageBody);

        //use a MimeMultipart as we need to handle the file attachments 
        Multipart multipart = new MimeMultipart();

        //add the message body to the mime message 
        multipart.addBodyPart(messageBodyPart);

        // add any file attachments to the message 
        //  addAtachments(attachments, multipart);

        // Put all message parts in the message 
        message.setContent(multipart);

        // Send the message 
        Transport.send(message);


    }
}
