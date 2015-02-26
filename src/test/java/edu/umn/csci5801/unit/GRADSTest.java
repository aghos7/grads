package edu.umn.csci5801.unit;

import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import edu.umn.csci5801.GRADS;
import edu.umn.csci5801.GRADSIntf;
import edu.umn.csci5801.datastore.JSONUserRepository;
import edu.umn.csci5801.exception.GRADSAuthorizationException;
import edu.umn.csci5801.exception.GRADSDatastoreException;
import edu.umn.csci5801.service.UserService;

/**
 * 
 */
public class GRADSTest {

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
    }

    /**
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception {
    }

    /**
     * Test method for
     * {@link edu.umn.csci5801.GRADS#setUser(java.lang.String)}.
     */
    @Ignore
    @Test
    public void testSetUser() {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link edu.umn.csci5801.GRADS#getUser()}.
     */
    @Ignore
    @Test
    public void testGetUser() {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link edu.umn.csci5801.GRADS#getStudentIDs()}
     * .
     */
    @Ignore
    @Test
    public void testGetStudentIDs() {
        fail("Not yet implemented");
    }

    @Test(expected = GRADSAuthorizationException.class)
    public void testGetStudentIDs_invalidUser() throws Exception, GRADSDatastoreException {
        GRADSIntf grads = new GRADS(null, null, new UserService(new JSONUserRepository(
                "src/resources/users_oneUser.txt")));
        grads.setUser("gayxx067");
        grads.getStudentIDs();
    }

    /**
     * Test method for
     * {@link edu.umn.csci5801.GRADS#getTranscript(java.lang.String)}
     * .
     */
    @Ignore
    @Test
    public void testGetTranscript() {
        fail("Not yet implemented");
    }

    @Test(expected = GRADSAuthorizationException.class)
    public void testGetTranscript_invalidUser() throws Exception, GRADSDatastoreException {
        GRADSIntf grads = new GRADS(null, null, new UserService(new JSONUserRepository(
                "src/resources/users_oneUser.txt")));
        grads.setUser("gayxx067");
        grads.getTranscript("wall0660");
    }

    /**
     * Test method for
     * {@link edu.umn.csci5801.GRADS#updateTranscript(java.lang.String, edu.umn.csci5801.model.StudentRecord)}
     * .
     */
    @Ignore
    @Test
    public void testUpdateTranscript() {
        fail("Not yet implemented");
    }

    @Test(expected = GRADSAuthorizationException.class)
    public void testUpdateTranscript_invalidUser() throws Exception, GRADSDatastoreException {
        GRADSIntf grads = new GRADS(null, null, new UserService(new JSONUserRepository(
                "src/resources/users_oneUser.txt")));
        grads.setUser("gayxx067");
        grads.updateTranscript("gayxx067", null);
    }

    /**
     * Test method for
     * {@link edu.umn.csci5801.GRADS#addNote(java.lang.String, java.lang.String)}
     * .
     */
    @Ignore
    @Test
    public void testAddNote() {
        fail("Not yet implemented");
    }

    @Test(expected = GRADSAuthorizationException.class)
    public void testAddNote_invalidUser() throws Exception, GRADSDatastoreException {
        GRADSIntf grads = new GRADS(null, null, new UserService(new JSONUserRepository(
                "src/resources/users_oneUser.txt")));
        grads.setUser("gayxx067");
        grads.addNote("gayxx067", "A note");
    }

    /**
     * Test method for
     * {@link edu.umn.csci5801.GRADS#generateProgressSummary(java.lang.String)}
     * .
     */
    @Ignore
    @Test
    public void testGenerateProgressSummary() {
        fail("Not yet implemented");
    }

    @Test(expected = GRADSAuthorizationException.class)
    public void testGenerateProgressSummary_invalidUser() throws Exception, GRADSDatastoreException {
        GRADSIntf grads = new GRADS(null, null, new UserService(new JSONUserRepository(
                "src/resources/users_oneUser.txt")));
        grads.setUser("gayxx067");
        grads.generateProgressSummary("wall0660");
    }

    /**
     * Test method for
     * {@link edu.umn.csci5801.GRADS#simulateCourses(java.lang.String, java.util.List)}
     * .
     */
    @Ignore
    @Test
    public void testSimulateCourses() {
        fail("Not yet implemented");
    }

    @Test(expected = GRADSAuthorizationException.class)
    public void testSimulateCourses_invalidUser() throws Exception, GRADSDatastoreException {
        GRADSIntf grads = new GRADS(null, null, new UserService(new JSONUserRepository(
                "src/resources/users_oneUser.txt")));
        grads.setUser("gayxx067");
        grads.simulateCourses("wall0660", null);
    }
}
