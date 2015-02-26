package edu.umn.csci5801.exception;

/**
 * Thrown to indicate an exception has occurred
 */
public class GRADSException extends Exception {
    private static final long serialVersionUID = 6379849449407141691L;

    /**
     * Constructs a new exception with the specified detail message
     * @param msg The String containing a detail message
     */
    public GRADSException(String msg) {
        super(msg);
    }
}