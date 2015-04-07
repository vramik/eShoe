package com.issuetracker.service;

/**
 *
 * @author vramik
 */
public class UnsufficientPrivilegesException extends Exception {

    public UnsufficientPrivilegesException() {
        super();
    }
    
    public UnsufficientPrivilegesException(String message) {
        super(message);
    }

    public UnsufficientPrivilegesException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnsufficientPrivilegesException(Throwable cause) {
        super(cause);
    }
}
