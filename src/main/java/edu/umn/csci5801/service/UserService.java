package edu.umn.csci5801.service;

import edu.umn.csci5801.datastore.UserRepository;
import edu.umn.csci5801.exception.GRADSException;
import edu.umn.csci5801.exception.GRADSIllegalArgumentException;
import edu.umn.csci5801.model.User;

/**
 * The UserService class is used to get User objects from the user repository.
 * It provides a service layer of abstraction between the main application and
 * the data storage
 */
public class UserService {
    /**
     * The user repository which this service will use to find user objects
     */
    UserRepository repository;

    /**
     * Constructs a new UserService instance with the given user repository
     * @param userRepository must not be null
     * @throws GRADSIllegalArgumentException if userRepository is null
     */
    public UserService(UserRepository userRepository) throws GRADSIllegalArgumentException {
        if (userRepository == null) {
            throw new GRADSIllegalArgumentException("userRepository must not be null when constructing a UserService");
        }
        this.repository = userRepository;
    }

    /**
     * Finds and returns the User object for the given user id from the
     * UserRepository
     * @param id the id if the user to fetch, id must not be null and must exist
     *        in the user repository
     * @return the user for the given id, never null
     * @throws GRADSException if the given id is null or the id cannot be found
     *         in the user repository
     */
    public User getUser(String id) throws GRADSException {
        return this.repository.find(id);
    }
}