///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//package com.issuetracker.test.pages;
//
//import com.issuetracker.pages.HomePage;
//import com.issuetracker.pages.Login;
//import com.issuetracker.web.IssueWeb;
//import org.apache.wicket.protocol.http.WebApplication;
//import org.apache.wicket.util.tester.WicketTester;
//import org.junit.After;
//import org.junit.AfterClass;
//import org.junit.Before;
//import org.junit.BeforeClass;
//import org.junit.Test;
//import static org.junit.Assert.*;
//
///**
// *
// * @author mgottval
// */
//public class TestHomePage {
//
//	private WicketTester tester;
//	@Before
//	public void setUp(){
//		tester = new WicketTester();
//	}
//	@Test
//	public void homepageRendersSuccessfully(){
//		//start and render the test page
//		tester.startPage(HomePage.class);
//		//assert rendered page class
//		tester.assertRenderedPage(HomePage.class);
//                
//                tester.assertLabel("loginLink:name", "Log In");
//                
////                tester.clickLink("loginLink");
////                tester.assertRenderedPage(Login.class);
//	}
//}
