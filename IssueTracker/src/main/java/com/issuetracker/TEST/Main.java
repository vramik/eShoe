/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.issuetracker.TEST;

import com.issuetracker.dao.IssueDaoBean;
import  com.issuetracker.dao.ProjectDaoBean;
import com.issuetracker.dao.api.IssueDao;
import com.issuetracker.dao.api.ProjectDao;
import com.issuetracker.model.Issue;
import com.issuetracker.model.Project;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.inject.Inject;
import javax.resource.spi.AuthenticationMechanism;

/**
 *
 * @author mgottval
 */
public class Main {
    
 
    private static IssueDao issueDao= new IssueDaoBean();

    @Inject private ProjectDao injectDao;
    
    //@Autowired
    private static ProjectDao projectDao;
    
 
    
    public static void main(String[] args) {


        
        System.out.println(projectDao.toString());
        
        Project project = new Project();
        project.setName("Fedora");
        projectDao.
                insertProject(project);
        
        List<Project> projects = projectDao.getProjects();
        List<String> projectnames = new ArrayList<String>();
        for (Project p:projects) {
            projectnames.add(p.getName());
        }
        for (String pname : projectnames) {
            System.out.println(pname);
        }
        
        
        Issue issue = new Issue();
        issue.setProject(project);
        
        
    }
}
