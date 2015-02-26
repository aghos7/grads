package edu.umn.csci5801.exception;

/**
 * Thrown to indicate an exception has occurred in a data store
 */
public class GRADSDatastoreException extends GRADSException {
    private static final long serialVersionUID = 7298320877883901223L;

    /**
     * Constructs a new exception with the specified detail message
     * @param msg The String containing a detail message
     */
    public GRADSDatastoreException(String msg) {
        super(msg);
    }
}