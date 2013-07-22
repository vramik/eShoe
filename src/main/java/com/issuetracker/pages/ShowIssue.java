/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.issuetracker.pages;

import com.issuetracker.dao.api.IssueDao;
import com.issuetracker.model.Issue;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Inject;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

/**
 *
 * @author mgottval
 */
public class ShowIssue extends PageLayout {

    private Issue issue;
    @Inject
    private IssueDao issueDao;
    
    //private ListChoice<Version>

    public ShowIssue(PageParameters parameters) {
        Long issueStringId = parameters.get("issue").toLong();
        issue = issueDao.getIssueById(issueStringId);
        Logger.getLogger(ShowIssue.class.getName()).log(Level.SEVERE, issue.getName());
        setDefaultModel(new CompoundPropertyModel<Issue>(issue));
        
        add(new Label("name"));
        add(new RequiredTextField("description"));
        add(new RequiredTextField("project.name"));
    }

    
    public Issue getIssue() {
        return issue;
    }

    public void setIssue(Issue issue) {
        this.issue = issue;
    }
    
    
}
