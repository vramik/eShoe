///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//package com.issuetracker.test.pages.CreateIssue;
//
////import de.agilecoders.wicket.Bootstrap;
////import de.agilecoders.wicket.settings.BootstrapSettings;
//import org.apache.wicket.Application;
//import org.apache.wicket.cdi.AutoConversation;
//import org.apache.wicket.cdi.CdiConfiguration;
//import org.apache.wicket.mock.MockApplication;
//import org.apache.wicket.protocol.http.WebApplication;
//import org.apache.wicket.resource.loader.ClassStringResourceLoader;
//import org.apache.wicket.util.tester.WicketTester;
//import org.jboss.arquillian.junit.Arquillian;
//import org.jboss.shrinkwrap.api.ShrinkWrap;
//import org.jboss.shrinkwrap.api.asset.EmptyAsset;
//import org.jboss.shrinkwrap.api.spec.JavaArchive;
//import org.junit.After;
//import org.junit.Before;
//import org.junit.runner.RunWith;
// 
//import javax.enterprise.inject.spi.BeanManager;
//import javax.inject.Inject;
//import org.junit.Test;
///**
// *
// * @author mgottval
// */
//@RunWith(Arquillian.class)
//public class TestCreateIssue {
//   protected WicketTester wicketTester;
//    @Inject
//    BeanManager beanManager;
// 
//    
//    public static JavaArchive createBasicDeployment() {
//        return ShrinkWrap.create(JavaArchive.class)
//                .addClasses(AutoConversation.class)
//                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
//    }
// 
//    @Before
//    public void setUp() throws Exception {
//        wicketTester = new WicketTester(getWebApplication());
//        new CdiConfiguration(beanManager).configure(wicketTester.getApplication());
//        Bootstrap.install(wicketTester.getApplication(), new BootstrapSettings());
//        // trick to make Wicket tests use properties files named like the test as first resource for translation
//        wicketTester.getApplication().getResourceSettings().getStringResourceLoaders().add(0, new ClassStringResourceLoader(getClass()));
//        wicketTester.getApplication().getResourceSettings().getStringResourceLoaders().add(1, new ClassStringResourceLoader(getApplicationClass()));
//    }
// 
//    protected Class<? extends Application> getApplicationClass() {
//        return MockApplication.class;
//    }
// 
//    @After
//    public void tearDown(){
//        wicketTester.getApplication().internalDestroy();
//    }
// 
//    protected WebApplication getWebApplication() {
//        return new MockApplication();
//    }
//    
//   @Test//projects model test - dropDowm
//public void testFormSelectSameModelObject() {//throws NamingException{
// 
////       List<Project> projects = new ArrayList<Project>();
////       projects = dao.getProjects();
//       
//	CreateIssue createIssue = new CreateIssue();
//	DropDownChoice dropDownChoice = (DropDownChoice) createIssue.get("projectDropDown");
//	List choices = dropDownChoice.getChoices();
//	//select the second option of the drop-down menu
//	dropDownChoice.setModelObject(choices.get(1));
//	//start and render the test page
//	tester.startPage(createIssue);		
//	//assert that form has the same data object used by drop-down menu
//	tester.assertModelValue("form", dropDownChoice.getModelObject());
//}
//   
//   
//   
//   
//   @Test
//    public void createIssuePageRendersSuccessfully() {
//        //start and render the test page
//        tester.startPage(CreateIssue.class);
//
//        //assert rendered page class
//        tester.assertRenderedPage(CreateIssue.class);
//
//FormTester formTester = tester.newFormTester("form", false);
////tester.ass
//
//    }
//}