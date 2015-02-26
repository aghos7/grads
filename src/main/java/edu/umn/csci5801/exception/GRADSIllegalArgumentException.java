package edu.umn.csci5801.exception;

/**
 * Thrown to indicate a method was passed an illegal or inappropriate argument
 */
public class GRADSIllegalArgumentException extends GRADSException {
    private static final long serialVersionUID = -5075570694110406167L;

    /**
     * Constructs a new exception with the specified detail message
     * @param msg The String containing a detail message
     */
    public GRADSIllegalArgumentException(String msg) {
        super(msg);
    }
}