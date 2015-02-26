package edu.umn.csci5801.model;

/**
 * The User class represents the user logged into the system. The user has an id
 * and a role.
 */
public class User {

    /**
     * Store the id, firstName, and lastName in a private static class to match
     * the given user JSON format
     */
    private static class UserInfo {

        /**
         * The persistent id of this user
         */
        String id;
        String firstName;
        String lastName;
    }

    /**
     * The id, firstName, and lastName of the user bundled into a UserInfo class
     * to match the given JSON format
     */
    private UserInfo user;

    /**
     * The role this user is assigned
     */
    private Role role;

    /**
     * The department this user belongs to
     */
    private Department department;

    /**
     * Gets the id of the user
     * @return the id
     */
    public String getId() {
        return this.user.id;
    }

    /**
     * Gets the role of the user
     * @return the role
     */
    public Role getRole() {
        return this.role;
    }

    /**
     * Gets the department of the user
     * @return the department
     */
    public Department getDeparment() {
        return this.department;
    }
}
