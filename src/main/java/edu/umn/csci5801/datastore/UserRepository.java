package edu.umn.csci5801.datastore;

import edu.umn.csci5801.exception.GRADSException;
import edu.umn.csci5801.model.User;

/**
 * An interface for managing user persistence
 */
public interface UserRepository {
    /**
     * Finds and returns the User object for the given user id from the
     * persistent storage
     * @param id the id if the user to fetch, id must not be null and must exist
     *        in the user storage
     * @return the user for the given id, never null
     * @throws GRADSException if the given id is null or the id cannot be found
     *         in the user repository
     */
    public User find(String id) throws GRADSException;

}