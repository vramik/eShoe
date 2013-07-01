///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//package com.issuetracker.authentication;
//
//import org.picketbox.core.DefaultPicketBoxManager;
//import org.picketbox.core.PicketBoxManager;
//import org.picketbox.core.authorization.impl.SimpleAuthorizationManager;
//import org.picketbox.core.config.AuthorizationConfiguration;
//import org.picketbox.core.config.ConfigurationBuilder;
//import org.picketbox.core.config.PicketBoxConfiguration;
//import org.picketlink.idm.IdentityManager;
//
///**
// *
// * @author Monika
// */
//public class AuthorizationProvider {
//    private PicketBoxManager picketBoxManager;
//    private SimpleAuthorizationManager authorizationManager;
//    
//    private IdentityManager getIdentityManager() {
//        createPicketBoxManager();
//       picketBoxManager.get
//    IdentityManager identityManager = picketBoxManager.getIdentityManager();
//    return identityManager;
//    }
//    
//private void createPicketBoxManager() {
//        // creates the configuration builder
//        ConfigurationBuilder builder = new ConfigurationBuilder();
//        
//        // builds the configuration using the default configuration
//    //    PicketBoxConfiguration configuration = builder.build();
//// AuthorizationConfiguration configuration = builder.authorization().build();
//        // instantiates a PicketBoxManager with the default configuration
// this.authorizationManager = new SimpleAuthorizationManager();
//      //  this.picketBoxManager = new DefaultPicketBoxManager(configuration);
//
//        // starts the manager
////        picketBoxManager.start();
//        authorizationManager.start();
//    }
//}
