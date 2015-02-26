package edu.umn.csci5801.unit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

import edu.umn.csci5801.datastore.JSONUserRepository;
import edu.umn.csci5801.exception.GRADSDatastoreException;
import edu.umn.csci5801.exception.GRADSException;
import edu.umn.csci5801.exception.GRADSIllegalArgumentException;
import edu.umn.csci5801.model.Department;
import edu.umn.csci5801.model.Role;
import edu.umn.csci5801.model.User;
import edu.umn.csci5801.service.UserService;

public class UserServiceTest {

    private UserService userSerice;

    @Before
    public void setup() throws GRADSIllegalArgumentException, GRADSDatastoreException {
        userSerice = new UserService(new JSONUserRepository("src/resources/users.txt"));
    }

    @Test(expected = GRADSException.class)
    public void testGetUserWithNullId() throws GRADSException {
        this.userSerice.getUser(null);
    }

    @Test
    public void testGetUserWithExistingIdGPC() throws GRADSException {
        String existingId = "smith0001";
        User user = this.userSerice.getUser(existingId);
        assertNotNull(user);
        assertEquals(existingId, user.getId());
        assertEquals(user.getRole(), Role.GRADUATE_PROGRAM_COORDINATOR);
        assertEquals(user.getDeparment(), Department.MATH);
    }

    @Test
    public void testGetUserWithExistingIdStudent() throws GRADSException {
        String existingId = "nguy0621";
        User user = this.userSerice.getUser(existingId);
        assertNotNull(user);
        assertEquals(existingId, user.getId());
        assertEquals(user.getRole(), Role.STUDENT);
        assertEquals(user.getDeparment(), Department.COMPUTER_SCIENCE);
    }

    @Test(expected = GRADSException.class)
    public void testGetUserWithNonExistingId() throws GRADSException {
        this.userSerice.getUser("123456");
    }
}
