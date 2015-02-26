package edu.umn.csci5801.exception;

/**
 * Thrown to indicate an authorization error has occurred
 */
public class GRADSAuthorizationException extends GRADSException {
    private static final long serialVersionUID = -426263704470832924L;

    /**
     * Constructs a new exception with the specified detail message
     * @param msg The String containing a detail message
     */
    public GRADSAuthorizationException(String msg) {
        super(msg);
    }
}