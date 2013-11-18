/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.issuetracker.test.pages.CreateIssue;

import com.issuetracker.pages.createIssue.CreateIssue;
import de.agilecoders.wicket.core.Bootstrap;
import de.agilecoders.wicket.core.settings.BootstrapSettings;
//import de.agilecoders.wicket.Bootstrap;
//import de.agilecoders.wicket.settings.BootstrapSettings;
import java.util.List;
import org.apache.wicket.Application;
import org.apache.wicket.cdi.AutoConversation;
import org.apache.wicket.cdi.CdiConfiguration;
import org.apache.wicket.mock.MockApplication;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.resource.loader.ClassStringResourceLoader;
import org.apache.wicket.util.tester.WicketTester;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
 
import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;
import javax.naming.Context;
import javax.naming.InitialContext;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.util.tester.FormTester;
import org.junit.Test;
/**
 *
 * @author mgottval
 */
@RunWith(Arquillian.class)
public class TestCreateIssue {
    private WicketTester tester;
//    @Inject
    private BeanManager beanManager;
    
    
//    @BeforeClass
//    public static void setUpClass() throws Exception {
//        // rcarver - setup the jndi context and the datasource
//     
////             Create initial context
//            System.setProperty(Context.INITIAL_CONTEXT_FACTORY,
//                "org.apache.naming.java.javaURLContextFactory");
//            System.setProperty(Context.URL_PKG_PREFIXES, 
//                "org.apache.naming");            
//            InitialContext ic = new InitialContext();
//
//            ic.createSubcontext("java:");
//            ic.createSubcontext("java:/comp");
//            ic.createSubcontext("java:/comp/env");
//            ic.createSubcontext("java:/comp/env/jdbc");
//           
//            // Construct DataSource
//
// Context initContext = new InitialContext();
// Context webContext = (Context)initContext.lookup("java:/comp/env");
// DataSource ds =
//          (DataSource)webContext.lookup("java:comp/env/jdbc/MySqlDS");
////            SQLServerConnectionPoolDataSource ds = new SQLServerConnectionPoolDataSource();
////            ds.s
////            ds.setUser("MY_USER_NAME");
////            ds.setPassword("MY_USER_PASSWORD");
//            
////            ic.bind("java:jboss/datasources/MySqlDS", ds);
//       
//    }
//    
//    
//   @Before
//    public void setUp() throws Exception {
//        tester = new WicketTester(new IssueWeb());
//    }
   
     public static JavaArchive createBasicDeployment() {
        return ShrinkWrap.create(JavaArchive.class)
                .addClasses(AutoConversation.class)
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
    }
     protected Class<? extends Application> getApplicationClass() {
        return MockApplication.class;
    }
 
     
      @Before
    public void setUp() throws Exception {
           System.setProperty(Context.INITIAL_CONTEXT_FACTORY,
                "org.apache.naming.java.javaURLContextFactory");
            System.setProperty(Context.URL_PKG_PREFIXES, 
                "org.apache.naming");    
          InitialContext ic = new InitialContext();
          ic.createSubcontext("java:");
            ic.createSubcontext("java:/comp");
        	beanManager = (BeanManager) ic.lookup("java:comp/BeanManager");
        tester = new WicketTester(getWebApplication());
        new CdiConfiguration(beanManager).configure(tester.getApplication());
        Bootstrap.install(tester.getApplication(), new BootstrapSettings());
        // trick to make Wicket tests use properties files named like the test as first resource for translation
        tester.getApplication().getResourceSettings().getStringResourceLoaders().add(0, new ClassStringResourceLoader(getClass()));
        tester.getApplication().getResourceSettings().getStringResourceLoaders().add(1, new ClassStringResourceLoader(getApplicationClass()));
      }
      
       @After
    public void tearDown(){
        tester.getApplication().internalDestroy();
    }
 
    protected WebApplication getWebApplication() {
        return new MockApplication();
    }
    
   @Test//projects model test - dropDowm
public void testFormSelectSameModelObject() {//throws NamingException{
 
//       List<Project> projects = new ArrayList<Project>();
//       projects = dao.getProjects();
       
	CreateIssue createIssue = new CreateIssue();
	DropDownChoice dropDownChoice = (DropDownChoice) createIssue.get("projectDropDown");
	List choices = dropDownChoice.getChoices();
	//select the second option of the drop-down menu
	dropDownChoice.setModelObject(choices.get(1));
	//start and render the test page
	tester.startPage(createIssue);		
	//assert that form has the same data object used by drop-down menu
	tester.assertModelValue("form", dropDownChoice.getModelObject());
}
   
   
   
   
   @Test
    public void createIssuePageRendersSuccessfully() {
        //start and render the test page
        tester.startPage(CreateIssue.class);

        //assert rendered page class
        tester.assertRenderedPage(CreateIssue.class);

FormTester formTester = tester.newFormTester("form", false);
//tester.ass

    }
}