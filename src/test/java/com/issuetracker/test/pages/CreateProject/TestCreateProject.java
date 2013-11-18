///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//package com.issuetracker.test.pages.CreateProject;
//
//import com.issuetracker.model.Component;
//import com.issuetracker.model.Project;
//import com.issuetracker.pages.CreateProject;
//import com.issuetracker.web.IssueWeb;
//import com.sun.jdi.Bootstrap;
//import java.util.ArrayList;
//import java.util.List;
//import javax.enterprise.context.RequestScoped;
//import javax.enterprise.context.spi.CreationalContext;
//import javax.enterprise.inject.spi.AnnotatedType;
//import javax.enterprise.inject.spi.BeanManager;
//import javax.enterprise.inject.spi.InjectionTarget;
//import javax.faces.application.ProjectStage;
//import javax.inject.Inject;
//import javax.naming.InitialContext;
//import javax.servlet.ServletConfig;
//import javax.servlet.ServletContext;
//import net.ftlines.wicket.cdi.CdiConfiguration;
//import org.apache.deltaspike.cdise.api.CdiContainer;
//import org.apache.deltaspike.cdise.api.CdiContainerLoader;
////import org.apache.deltaspike.core.util.ProjectStageProducer;
//
//import org.apache.deltaspike.cdise.api.ContextControl;
//import org.apache.deltaspike.core.util.ProjectStageProducer;
//import org.apache.wicket.markup.html.form.Form;
//import org.apache.wicket.mock.MockApplication;
//import org.apache.wicket.protocol.http.WebApplication;
//import org.apache.wicket.util.tester.FormTester;
//import org.apache.wicket.util.tester.WicketTester;
//import org.junit.Before;
//import org.junit.BeforeClass;
//import org.junit.Test;
//
///**
// *
// * @author mgottval
// */
//public abstract class TestCreateProject {
//
//    private WicketTester tester;
//    protected static CdiContainer cdiContainer;
//    @Inject BeanManager manager;
////     private WeldContainer _container;
//    private InitialContext _initialContext;
//    int  containerRefCount;
//    
//    
//     @Before
//    public  final void setUp() throws Exception {
//        containerRefCount++;
//
//        if (cdiContainer == null) {
//            ProjectStageProducer.setProjectStage(org.apache.deltaspike.core.api.projectstage.ProjectStage.UnitTest);
//
//            cdiContainer = CdiContainerLoader.getCdiContainer();
//            cdiContainer.boot();
//            cdiContainer.getContextControl().startContexts();
//        }
//        else {
//            // clean the Instances by restarting the contexts
//            cdiContainer.getContextControl().stopContexts();
//            cdiContainer.getContextControl().startContexts();
//        }
//    }
//    
//    
//    @Before
//    public  final void beforeClass() throws Exception {
//        setUp();
//        cdiContainer.getContextControl().startContext(RequestScoped.class);
//        BeanManager beanManager = cdiContainer.getBeanManager();
//
//         CreationalContext creationalContext = beanManager.createCreationalContext(null);
//
//        AnnotatedType annotatedType = beanManager.createAnnotatedType(this.getClass());
//        InjectionTarget injectionTarget = beanManager.createInjectionTarget(annotatedType);
//        injectionTarget.inject(this, creationalContext);
//        tester = new WicketTester();
//    }
//    @Test
//    public void createProjectPageRendersSuccessfully() {
//        //start and render the test page
//        tester.startPage(CreateProject.class);
//
//        //assert rendered page class
//        tester.assertRenderedPage(CreateProject.class);
//
////                tester.assertLabel("loginLink:name", "Log In");
////                
////                tester.clickLink("loginLink");
////                tester.assertRenderedPage(Login.class);
//
//    }
//
//    @Test
//    public void createProjectPageForm() {
//        tester.startPage(CreateProject.class);
//        FormTester formTester = tester.newFormTester("insertProjectForm", false);
//        formTester.setValue("name", null);
//        formTester.setValue("summary", null);
//        formTester.setValue("componentName", "C1");
//        formTester.setValue("version", "2");
//        formTester.setValue("customFieldName", "Next Release");
//        formTester.submit();
//
//        tester.assertErrorMessages("'name' is required.", "'summary' is required.");
//        tester.assertFeedback("feedbackPanel", "'name' is required.", "'summary' is required.");
//
//    }
//
//    @Test
//    public void createProjectPageFormSuccess() {
//        List<Project> projects = new ArrayList<Project>();
//        List<Component> components = new ArrayList<Component>();
//        Project p = new Project();
//        p.setName("Fedora");
//        p.setSummary("About Fedora");
////           projects.add(p);
//        tester.startPage(CreateProject.class);
//        tester.assertComponent("insertProjectForm", Form.class);
//        FormTester formTester = tester.newFormTester("insertProjectForm", false);
//        formTester.setValue("name", p.getName());
//        formTester.setValue("summary", p.getSummary());
////        formTester.setValue("componentName", "C1");
////        formTester.setValue("version", "2");
////        formTester.setValue("customFieldName", "Next Release");
//        formTester.submit();
//
//        tester.assertRenderedPage(CreateProject.class);
//
//
//        tester.assertListView("projectList", components);
////            tester.assertFeedback("feedbackPanel", );
//    }
//}