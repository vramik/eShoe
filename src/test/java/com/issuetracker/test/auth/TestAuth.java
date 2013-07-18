//package com.issuetracker.test.auth;
//
///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
///**
// *
// * @author Monika
// */
//
//
//import org.junit.Before;
//import org.junit.Test;
//import org.picketbox.core.DefaultPicketBoxManager;
//import org.picketbox.core.PicketBoxManager;
//import org.picketbox.core.config.ConfigurationBuilder;
//import org.picketbox.core.config.PicketBoxConfiguration;
//import org.picketbox.core.UserContext;
//import org.picketbox.core.authentication.credential.UsernamePasswordCredential;
//import org.picketbox.core.exceptions.AuthenticationException;
//import org.picketlink.idm.IdentityManager;
//import org.picketlink.idm.model.Group;
//import org.picketlink.idm.model.Role;
//import org.picketlink.idm.model.SimpleGroup;
//import org.picketlink.idm.model.SimpleRole;
//import org.picketlink.idm.credential.PlainTextPassword;
//import org.picketlink.idm.model.SimpleUser;
//import static org.junit.Assert.*;
//
//public class TestAuth {
//
//    private PicketBoxManager picketBoxManager;
//
//    @Before
//    public void onSetup() {
//        // creates and starts the manager
//        createPicketBoxManager();
//
//        // populates the identity store with user information
//        populateIdentityStore();
//    }
//
//    @Test
//    public void testAuthentication() throws AuthenticationException {
//        // creates a AUTHENTICATING context
//        UserContext authenticatingContext = new UserContext();
//
//        // sets the credential
//        authenticatingContext.setCredential(new UsernamePasswordCredential("admin", "admin"));
//
//        // authenticate the user context using the provided credentials
//        UserContext authenticatedContext = picketBoxManager.authenticate(authenticatingContext);
//
//        assertNotNull(authenticatedContext);
//        assertTrue(authenticatedContext.isAuthenticated());
//        assertTrue(authenticatedContext.hasRole("developer"));
//        assertTrue(authenticatedContext.hasRole("admin"));
//        assertTrue(authenticatedContext.hasGroup("The PicketBox Group"));
//    }
//
//    private void populateIdentityStore() {
//        IdentityManager identityManager = picketBoxManager.getIdentityManager();
//
//        SimpleUser adminUser = new SimpleUser("admin");
//
//        // sets some properties
//        adminUser.setEmail("admin@picketbox.com");
//        adminUser.setFirstName("The");
//        adminUser.setLastName("Admin");
//        // creates the user
//        identityManager.add(adminUser);
//
//        // updates the user credential. In this case a password credential.
//        identityManager.updateCredential(adminUser, new PlainTextPassword("admin".toCharArray()));
//
//        // creates some roles
//        Role developerRole = new SimpleRole("developer");
//
//        identityManager.add(developerRole);
//
//        Role adminRole = new SimpleRole("admin");
//
//        identityManager.add(adminRole);
//
//        // creates a group
//        Group picketBoxGroup = new SimpleGroup("The PicketBox Group");
//
//        identityManager.add(picketBoxGroup);
//
//        // grant the roles to the user and make him member of the group
//        identityManager.grantRole(adminUser, developerRole);
//        identityManager.grantRole(adminUser, adminRole);
//
//        identityManager.addToGroup(adminUser, picketBoxGroup);
//    }
//
//    /**
//     * Creates and starts the {@link PicketBoxManager}.
//     */
//    private void createPicketBoxManager() {
//        // creates the configuration builder
//        ConfigurationBuilder builder = new ConfigurationBuilder();
//        
//        // builds the configuration using the default configuration
//        PicketBoxConfiguration configuration = builder.build();
//
//        // instantiates a PicketBoxManager with the default configuration
//        this.picketBoxManager = new DefaultPicketBoxManager(configuration);
//
//        // starts the manager
//        picketBoxManager.start();
//    }
//}
