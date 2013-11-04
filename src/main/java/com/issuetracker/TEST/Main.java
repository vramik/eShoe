package com.issuetracker.TEST;

import com.issuetracker.dao.IssueDaoBean;
import com.issuetracker.dao.api.ProjectDao;
import com.issuetracker.model.Issue;
import javax.mail.MessagingException;

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
