package edu.umn.csci5801;

import java.util.List;

import edu.umn.csci5801.datastore.JSONRequirementRepository;
import edu.umn.csci5801.datastore.JSONStudentRecordRepository;
import edu.umn.csci5801.datastore.JSONUserRepository;
import edu.umn.csci5801.datastore.Utilities;
import edu.umn.csci5801.exception.GRADSAuthorizationException;
import edu.umn.csci5801.exception.GRADSDatastoreException;
import edu.umn.csci5801.exception.GRADSException;
import edu.umn.csci5801.exception.GRADSIllegalArgumentException;
import edu.umn.csci5801.model.CourseTaken;
import edu.umn.csci5801.model.ProgressSummary;
import edu.umn.csci5801.model.Role;
import edu.umn.csci5801.model.StudentRecord;
import edu.umn.csci5801.model.User;
import edu.umn.csci5801.service.ProgressSummaryService;
import edu.umn.csci5801.service.StudentRecordService;
import edu.umn.csci5801.service.UserService;

/**
 * 
 */
public class GRADS implements GRADSIntf {

    /**
     * The default path to the JSON file describing requirements
     */
    public static final String DEFAULT_REQUIREMENTS_PATH = "src/resources/requirements.txt";

    /**
     * The current user of the system
     */
    private User currentUser;

    /**
     * The progress summary service to use for generating progress summaries
     */
    private final ProgressSummaryService progressSummaryService;

    /**
     * The student record service to use for managing student records
     */
    private final StudentRecordService studentRecordService;

    /**
     * The user services to use for managing users
     */
    private final UserService userService;

    /**
     * Construct a GRADS instance
     * @param studentsFileName Path to JSON file containing students
     * @param coursesFileName Path to JSON file containing courses
     * @param usersFileName Path to JSON file containing users
     * @throws GRADSIllegalArgumentException // TODO describe
     * @throws GRADSDatastoreException // TODO describe
     * @throws GRADSException // TODO describe
     */
    public GRADS(String studentsFileName, String coursesFileName, String usersFileName)
            throws GRADSIllegalArgumentException, GRADSDatastoreException, GRADSException {
        this(studentsFileName, coursesFileName, usersFileName, DEFAULT_REQUIREMENTS_PATH);
    }

    /**
     * Construct a GRADS instance
     * @param studentsFileName Path to JSON file containing students
     * @param coursesFileName Path to JSON file containing courses
     * @param usersFileName Path to JSON file containing users
     * @param requirementsFileName Path to JSON file containing requirements
     * @throws GRADSIllegalArgumentException // TODO describe
     * @throws GRADSDatastoreException // TODO describe
     * @throws GRADSException // TODO describe
     */
    public GRADS(String studentsFileName, String coursesFileName, String usersFileName, String requirementsFileName)
            throws GRADSIllegalArgumentException, GRADSDatastoreException, GRADSException {
        this(new ProgressSummaryService(new JSONRequirementRepository(requirementsFileName)), new StudentRecordService(
                new JSONStudentRecordRepository(coursesFileName, studentsFileName)), new UserService(
                new JSONUserRepository(usersFileName)));
    }

    /**
     * Construct a GRADS instance
     * @param progressSummaryService The service to use for generating progress
     *        summaries
     * @param studentRecordService The service to use for student records
     * @param userService The service to use for users
     */
    public GRADS(ProgressSummaryService progressSummaryService, StudentRecordService studentRecordService,
            UserService userService) {
        this.progressSummaryService = progressSummaryService;
        this.studentRecordService = studentRecordService;
        this.userService = userService;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setUser(String userId) throws Exception {
        currentUser = userService.getUser(userId);
    }

    /*
     * (non-Javadoc)
     * @see edu.umn.csci5801.GRADSIntf#getUser()
     */
    @Override
    public String getUser() {
        return currentUser.getId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> getStudentIDs() throws Exception {
        if (currentUser.getRole() != Role.GRADUATE_PROGRAM_COORDINATOR) {
            throw new GRADSAuthorizationException(currentUser.getId() + " is not authorized to view the student list");
        }
        return studentRecordService.getStudentsByDepartment(currentUser.getDeparment());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public StudentRecord getTranscript(String userId) throws Exception {
        if ((currentUser.getRole() != Role.GRADUATE_PROGRAM_COORDINATOR && !currentUser.getId().equals(userId))
                || (currentUser.getRole() == Role.GRADUATE_PROGRAM_COORDINATOR && !getStudentIDs().contains(userId))) {
            throw new GRADSAuthorizationException(currentUser.getId()
                    + " is not authorized to view the student transcript for " + userId);
        }
        return studentRecordService.getTranscript(userId);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateTranscript(String userId, StudentRecord transcript) throws Exception {
        if (currentUser.getRole() != Role.GRADUATE_PROGRAM_COORDINATOR || !getStudentIDs().contains(userId)) {
            throw new GRADSAuthorizationException(currentUser.getId()
                    + " is not authorized to update the student transcript for " + userId);
        }
        if (userId == null || transcript == null || transcript.getStudent() == null
                || !userId.equals(transcript.getStudent().getId())) {
            throw new GRADSIllegalArgumentException(
                    "Illegal arguments, userId and transcript must not be null and the userId must match the transcript student's id.");
        }
        studentRecordService.updateTranscript(transcript);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addNote(String userId, String note) throws Exception {
        if (currentUser.getRole() != Role.GRADUATE_PROGRAM_COORDINATOR || !getStudentIDs().contains(userId)) {
            throw new GRADSAuthorizationException(currentUser.getId() + " is not authorized to add notes for " + userId);
        }
        if (note == null || note.length() == 0) {
            throw new GRADSIllegalArgumentException("Note may not be null or empty");
        }
        studentRecordService.addNote(userId, note);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ProgressSummary generateProgressSummary(String userId) throws Exception {
        if ((currentUser.getRole() != Role.GRADUATE_PROGRAM_COORDINATOR && !currentUser.getId().equals(userId))
                || (currentUser.getRole() == Role.GRADUATE_PROGRAM_COORDINATOR && !getStudentIDs().contains(userId))) {
            throw new GRADSAuthorizationException(currentUser.getId()
                    + " is not authorized to view the progress summary for " + userId);
        }
        return progressSummaryService.generateProgressSummary(studentRecordService.getTranscript(userId));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ProgressSummary simulateCourses(String userId, List<CourseTaken> courses) throws Exception {
        if ((currentUser.getRole() != Role.GRADUATE_PROGRAM_COORDINATOR && !currentUser.getId().equals(userId))
                || (currentUser.getRole() == Role.GRADUATE_PROGRAM_COORDINATOR && !getStudentIDs().contains(userId))) {
            throw new GRADSAuthorizationException(currentUser.getId()
                    + " is not authorized to view the simulated progress summary for " + userId);
        }
        if (courses == null) {
            throw new GRADSIllegalArgumentException("courses argument must not be null for the method sumulateCourses.");
        }
        // copy the courses so we don't modify the users objects and then add
        // missing course area
        this.studentRecordService.addMissingCourseArea(Utilities.cloneListThroughJson(courses));
        return progressSummaryService.simulateCourses(studentRecordService.getTranscript(userId), courses);
    }

}
