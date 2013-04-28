/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.issuetracker.pages;

import com.issuetracker.dao.api.IssueDao;
import org.apache.wicket.markup.html.WebPage;
import com.issuetracker.model.Issue;
import java.io.IOException;
import java.util.Properties;
import javax.inject.Inject;
import javax.persistence.NoResultException;
import org.apache.wicket.RestartResponseException;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.feedback.ContainerFeedbackMessageFilter;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.StatelessForm;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.markup.html.link.DownloadLink;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

/**
 *
 * @author mgottval
 */
public class ShowIssue  extends WebPage{
    
    private Issue issue;
    
    @Inject
    private IssueDao issueDao;
    
    private FeedbackPanel feedbackPanel;
    private Form<Issue> form;
    
    
    public ShowIssue(PageParameters parameters) {
        String issueName = parameters.get("name").toString();
        try {
            this.issue = issueDao.getIssueByName(issueName);
        }
        catch( NoResultException ex ){
            throw new RestartResponseException( HomePage.class, new PageParameters().add("error", "No such issue: " + issueName) );
        }
        init();
    }
    
    public ShowIssue(Issue issue) {
        this.issue = issue;
        if( this.issue == null )
            throw new RestartResponseException( HomePage.class, new PageParameters().add("error", "No issue chosen.") );
        init();
    }
            

    private void init()
    {
        setDefaultModel( new PropertyModel( this, "issue") );
        
        // Feedback
        feedbackPanel = new FeedbackPanel("feedback");
        feedbackPanel.setOutputMarkupId( true );
        feedbackPanel.setFilter( new ContainerFeedbackMessageFilter(this) );
        add(feedbackPanel);

        // Form
        this.form = new Form("form", new CompoundPropertyModel(getModel())) {
            @Override protected void onSubmit() {
                /*try {
                    doProductUpdate();
                } catch (Exception ex) {
                    error("Updating the product failed: " + ex.getMessage());
                }*/
                onIssueUpdate(null);
            }
        };
        this.form.setVersioned(false);
        add( this.form );
        
        
        // Bugzilla ID
//        this.form.add( new EditableLabel<String>("extIdBugzilla") );

//        // Traits
//        this.form.add( new ReleaseTraitsPanel("templates", this.product){
//            @Override protected void onTraitUpdate( ReleaseTraitRowPanel row, AjaxRequestTarget target ) {
//                ProductPage.this.onProductUpdate(target);
//            }
//        });

        // Custom fields
//        this.form.add( new CustomFieldsPanel("customFields", new PropertyModel(this.product, "customFields"), feedbackPanel ){
//            @Override protected void onChange( AjaxRequestTarget target ) {
//                onProductUpdate( target );
//            }
//        });
        
        
        // Save as .properties - TODO
        //this.add( new DownloadLink("downloadProps", issue.getVotes(), issue.getName() + "-traits.properties") );

        // Upload & apply .properties
        

        
        
       // final boolean isAdminLogged = getSession().isUserInGroup_Pattern("admin");
        
        // Admin Zone
        WebMarkupContainer adminZone = new WebMarkupContainer("adminZone");
        this.add( adminZone );
      //  adminZone.setVisibilityAllowed( isAdminLogged );
        adminZone.add( new Form("form")
//            .add( new EditableLabel("editorsGroupPrefix", new PropertyModel<String>(this.product, "editorsGroupPrefix"))
//                .add( new AjaxFormComponentUpdatingBehavior("onchange") {
//                    @Override protected void onUpdate(AjaxRequestTarget target) {
//                        if( ! isAdminLogged ){  error("Only members of the admin group can alter the privileges."); return; }
//                        onProductUpdate(target);
//                    }
//                })
//            )
        );
        
        
        
        // Danger Zone
        WebMarkupContainer dangerZone = new WebMarkupContainer("dangerZone");
       // adminZone.setVisibilityAllowed( isAdminLogged );
        this.add( dangerZone );
        
        // Danger Zone Form
        dangerZone.add( new StatelessForm("form") {
            {
                // Really button
                final AjaxButton really = new AjaxButton("deleteReally") {};
                really.setVisible(false).setRenderBodyOnly(false);
                really.setOutputMarkupPlaceholderTag(true);
                add( really );
                
                // Delete button
                add( new AjaxLink("delete"){
                    @Override public void onClick( AjaxRequestTarget target ) {
                        target.add( really );
                        really.setVisible(true);
                        //really.add(AttributeModifier.replace("style", "")); // Removes style="visibility: hidden".
                        //super.onSubmit( target, form );
                    }
                });
            }
//            @Override protected void onSubmit() {
////                if( ! productDao.canBeUpdatedBy( product, ProductPage.this.getSession().getUser() ) ){
////                    error("You don't have permissions to delete this product.");
////                    return; 
////                }
//                productDao.deleteIncludingReleases( product );
//                setResponsePage(HomePage.class);
//            }
        });

    }// init()

    
     private void doIssueUpdate(){  // TODO: throws AuthException?
         
        issue = issueDao.updateIssue(issue);
        modelChanged();
    }
     
     private void onIssueUpdate( AjaxRequestTarget target ) {
        if( target != null )  target.add( this.feedbackPanel );
        
        // Security check.
//        if( ! productDao.canBeUpdatedBy( product, ProductPage.this.getSession().getUser() ) ){
//            //throw new AuthException("You don't have permissions to modify this product's data.");
//            error("You don't have permissions to modify this product's data.");
//            return;
//        }
        
        try {
            doIssueUpdate();
            this.feedbackPanel.info("Issue saved.");
            if( target != null )
                target.appendJavaScript("window.notifyFlash('PrIssueoduct saved.')");
        } catch( Exception ex ){
            this.feedbackPanel.info("Saving product failed: " + ex.toString());
        }
    }
    
    public Issue getIssue() { return issue; }
    public void setIssue( Issue issue ) { this.issue = issue; }

    protected IModel<Issue> getModel(){ return (IModel<Issue>) this.getDefaultModel(); }
}
