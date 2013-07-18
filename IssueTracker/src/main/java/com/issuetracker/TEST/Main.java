/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.issuetracker.TEST;

import com.issuetracker.dao.IssueDaoBean;
import  com.issuetracker.dao.ProjectDaoBean;
import com.issuetracker.dao.api.IssueDao;
import com.issuetracker.dao.api.ProjectDao;
import com.issuetracker.mail.MailClient;
import com.issuetracker.mail.PasswordGenerator;
import com.issuetracker.model.Issue;
import com.issuetracker.model.Project;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.inject.Inject;
import javax.mail.MessagingException;
import javax.resource.spi.AuthenticationMechanism;

/**
 *
 * @author mgottval
 */
public class Main {
    
 
//    private static IssueDao issueDao= new IssueDaoBean();

  //  @Inject private ProjectDao injectDao;
    
    //@Autowired
    private static ProjectDao projectDao;
    
 
    
    public static void main(String[] args) throws MessagingException {
        IssueDaoBean id = new IssueDaoBean();
        Issue i = new Issue();
        i.setName("A");
//        id.test();
        }
}
