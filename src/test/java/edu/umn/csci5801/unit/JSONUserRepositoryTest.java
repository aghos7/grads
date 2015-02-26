package edu.umn.csci5801.unit;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;

import org.junit.Test;

import edu.umn.csci5801.datastore.JSONUserRepository;
import edu.umn.csci5801.exception.GRADSDatastoreException;
import edu.umn.csci5801.exception.GRADSException;
import edu.umn.csci5801.model.User;

/**
 * 
 */
public class JSONUserRepositoryTest {

    @Test
    public void testGoodUserData() throws GRADSException {
        String jsonFilePath = "src/resources/users.txt";

        // first make sure the file exists
        File jsonFile = new File(jsonFilePath);
        assertTrue(jsonFile.exists());

        JSONUserRepository userRepository = new JSONUserRepository(jsonFilePath);
        User user = userRepository.find("nguy0621");
        assertNotNull(user);
    }

    @Test
    public void testOneUser() throws GRADSException {
        String jsonFilePath = "src/resources/users_oneUser.txt";

        // first make sure the file exists
        File jsonFile = new File(jsonFilePath);
        assertTrue(jsonFile.exists());

        JSONUserRepository userRepository = new JSONUserRepository(jsonFilePath);
        // lookup the one user in the file
        User user = userRepository.find("gayxx067");
        assertNotNull(user);

        // now try looking up a different user, exception expected
        // Note: cannot use @Test(expected = GRADSException) here because we
        // can't allow the lines before this to fail
        try {
            user = userRepository.find("nguy0621");
            fail("expected exception");
        } catch (GRADSException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testNoUsers() throws GRADSException {
        String jsonFilePath = "src/resources/users_noUsers.txt";

        // first make sure the file exists
        File jsonFile = new File(jsonFilePath);
        assertTrue(jsonFile.exists());

        // this should pass
        JSONUserRepository userRepository = new JSONUserRepository(jsonFilePath);

        // now try looking up a user, exception expected
        // Note: cannot use @Test(expected = GRADSException) here because we
        // can't allow the line before this to fail
        try {
            userRepository.find("nguy0621");
            fail("expected exception");
        } catch (GRADSException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testEmptyDepartment() throws GRADSException {
        String jsonFilePath = "src/resources/users_emptyDepartment.txt";

        // first make sure the file exists
        File jsonFile = new File(jsonFilePath);
        assertTrue(jsonFile.exists());

        // the following should not throw a GRADSDatastoreException
        JSONUserRepository userRepository = new JSONUserRepository(jsonFilePath);
        User user = userRepository.find("gayxx067");
        assertNotNull(user);
        assertNull(user.getDeparment());
    }

    @Test(expected = GRADSDatastoreException.class)
    public void testDuplicateId() throws GRADSException {
        String jsonFilePath = "src/resources/users_dupId.txt";

        // first make sure the file exists
        File jsonFile = new File(jsonFilePath);
        assertTrue(jsonFile.exists());

        // the following should throw a GRADSDatastoreException
        new JSONUserRepository(jsonFilePath);
    }

    @Test(expected = GRADSDatastoreException.class)
    public void testEmptyId() throws GRADSException {
        String jsonFilePath = "src/resources/users_emptyId.txt";

        // first make sure the file exists
        File jsonFile = new File(jsonFilePath);
        assertTrue(jsonFile.exists());

        // the following should throw a GRADSDatastoreException
        new JSONUserRepository(jsonFilePath);
    }

    @Test(expected = GRADSDatastoreException.class)
    public void testNullId() throws GRADSException {
        String jsonFilePath = "src/resources/users_nullId.txt";

        // first make sure the file exists
        File jsonFile = new File(jsonFilePath);
        assertTrue(jsonFile.exists());

        // the following should throw a GRADSDatastoreException
        new JSONUserRepository(jsonFilePath);
    }

    @Test
    public void testNullRole() throws GRADSException {
        String jsonFilePath = "src/resources/users_nullRole.txt";

        // first make sure the file exists
        File jsonFile = new File(jsonFilePath);
        assertTrue(jsonFile.exists());

        // the following should not throw a GRADSDatastoreException
        JSONUserRepository userRepository = new JSONUserRepository(jsonFilePath);
        User user = userRepository.find("nguy0621");
        assertNotNull(user);
        assertNull(user.getRole());
    }

    @Test(expected = GRADSException.class)
    public void testFindWithNonExistingId() throws GRADSException {
        String jsonFilePath = "src/resources/users.txt";

        // first make sure the file exists
        File jsonFile = new File(jsonFilePath);
        assertTrue(jsonFile.exists());

        JSONUserRepository userRepository = new JSONUserRepository(jsonFilePath);
        // this should throw an exception because the id doesn't exist
        userRepository.find("123456");
    }

    @Test(expected = GRADSException.class)
    public void testFindWithNullId() throws GRADSException {
        String jsonFilePath = "src/resources/users.txt";

        // first make sure the file exists
        File jsonFile = new File(jsonFilePath);
        assertTrue(jsonFile.exists());

        JSONUserRepository userRepository = new JSONUserRepository(jsonFilePath);
        // this should throw an exception because the id is null
        userRepository.find(null);
    }
}
