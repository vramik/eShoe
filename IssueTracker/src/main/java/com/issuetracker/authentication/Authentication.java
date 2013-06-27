/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.issuetracker.authentication;

import org.picketbox.core.DefaultPicketBoxManager;
import org.picketbox.core.PicketBoxManager;
import org.picketbox.core.config.ConfigurationBuilder;
import org.picketbox.core.config.PicketBoxConfiguration;
import com.opensymphony.xwork2.interceptor.annotations.Before;
/**
 *
 * @author Monika
 */
public class Authentication {

    private PicketBoxManager picketBoxManager;

    @Before
    public void onSetup() {
        // creates and starts the manager
        createPicketBoxManager();

        // populates the identity store with user information
        populateIdentityStore();
    }
    
    
    private void createPicketBoxManager() {
    // creates the configuration builder
    ConfigurationBuilder builder = new ConfigurationBuilder();
 
    // builds the configuration using the default configuration
    PicketBoxConfiguration configuration = builder.build();
 
    // instantiates a PicketBoxManager with the default configuration
    this.picketBoxManager = new DefaultPicketBoxManager(configuration);
 
    // starts the manager
    picketBoxManager.start();
}
}
