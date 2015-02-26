package edu.umn.csci5801.datastore;

import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import edu.umn.csci5801.exception.GRADSDatastoreException;
import edu.umn.csci5801.exception.GRADSException;
import edu.umn.csci5801.exception.GRADSIllegalArgumentException;
import edu.umn.csci5801.model.User;

/**
 * JSONUserRepository is an implementation of the UserRepository interface which
 * uses a JSON file to read users. On construction, it reads all the users into
 * a map for fast lookup
 */
public class JSONUserRepository implements UserRepository {

    // a map of the string user id to it's User object, used for fast lookup
    // of users
    private Map<String, User> users;

    /**
     * Constructs a new user repository with the give JSON file
     * @param jsonFilePath path to the JSON file with users
     * @throws GRADSDatastoreException if the file cannot be read or has invalid
     *         data
     */
    public JSONUserRepository(String jsonFilePath) throws GRADSDatastoreException {
        loadUsers(jsonFilePath);
    }

    /**
     * Reads the JSON file and loads the map of all possible users
     * @param jsonFilePath path to the JSON file
     * @throws GRADSDatastoreException if the file cannot be read or has invalid
     *         data
     */
    private void loadUsers(String jsonFilePath) throws GRADSDatastoreException {
        List<User> userList;
        try {
            // read the JSON file into a List of Users
            userList = new Gson().fromJson(new FileReader(new File(jsonFilePath)), new TypeToken<List<User>>() {
            }.getType());
        } catch (Exception e) {
            throw new GRADSDatastoreException("Error reading JSON file " + jsonFilePath + " - " + e.getMessage());
        }

        // loop through the users, validate each one, and add them to the map
        users = new HashMap<String, User>();
        for (User user : userList) {
            if (user.getId() == null || user.getId().length() == 0) {
                throw new GRADSDatastoreException("Invalid user JSON file " + jsonFilePath + " - user found with no id");
            } else if (users.containsKey(user.getId())) {
                throw new GRADSDatastoreException("Invalid user JSON file " + jsonFilePath
                        + " - duplicate user id found " + user.getId());
            }
            users.put(user.getId(), user);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public User find(String id) throws GRADSException {
        if (id == null || !users.containsKey(id)) {
            throw new GRADSIllegalArgumentException("Invalid user id: " + id);
        } else {
            return Utilities.cloneThroughJson(users.get(id));
        }
    }
}
