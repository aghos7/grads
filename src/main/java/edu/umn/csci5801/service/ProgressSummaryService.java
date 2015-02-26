package edu.umn.csci5801.service;

import java.util.List;

import edu.umn.csci5801.datastore.RequirementRepository;
import edu.umn.csci5801.datastore.Utilities;
import edu.umn.csci5801.exception.GRADSException;
import edu.umn.csci5801.exception.GRADSIllegalArgumentException;
import edu.umn.csci5801.model.CourseTaken;
import edu.umn.csci5801.model.ProgressSummary;
import edu.umn.csci5801.model.StudentRecord;
import edu.umn.csci5801.requirement.Requirement;

/**
 * Saves Progress summary.
 */
public class ProgressSummaryService {

    /**
     * The requirements repository
     */
    RequirementRepository repository;

    /**
     * Constructs a progress summary service
     * @param repository The repository to retrieve requirements from, must not
     *        be null
     * @throws GRADSIllegalArgumentException repository is null
     */
    public ProgressSummaryService(RequirementRepository repository) throws GRADSIllegalArgumentException {
        if (repository == null) {
            throw new GRADSIllegalArgumentException("Repository must not be null");
        }
        this.repository = repository;
    }

    /**
     * Generates progress summary
     * @param userId the student to generate the record for.
     * @returns the student's progress summary in a data class matching the JSON
     *          format.
     * @throws GRADSException if the progress summary could not be generated.
     */
    public ProgressSummary generateProgressSummary(StudentRecord studentRecord) throws GRADSException {
        if (studentRecord == null) {
            throw new GRADSIllegalArgumentException(
                    "studentRecord argument must not be null when generating a progress summary.");
        }
        ProgressSummary progressSummary = new ProgressSummary();
        progressSummary.setStudent(Utilities.cloneThroughJson(studentRecord.getStudent()));
        progressSummary.setDegreeSought(studentRecord.getDegreeSought());
        progressSummary.setDepartment(studentRecord.getDepartment());
        progressSummary.setAdvisors(Utilities.cloneListThroughJson(studentRecord.getAdvisors()));
        progressSummary.setCommittee(Utilities.cloneListThroughJson(studentRecord.getCommittee()));
        progressSummary.setNotes(Utilities.cloneListThroughJson(studentRecord.getNotes()));
        progressSummary.setTermBegan(Utilities.cloneThroughJson(studentRecord.getTermBegan()));

        List<Requirement> requirements = repository
                .find(studentRecord.getDegreeSought(), studentRecord.getDepartment());
        if (requirements != null) {
            for (Requirement requirement : requirements) {
                progressSummary.addRequirementResult(requirement.check(studentRecord, false));
            }
        }

        return progressSummary;
    }

    /**
     * Generates a new progress summary, assuming that the student passes the
     * provided set of prospective courses.
     * @param userId the student to generate the record for.
     * @param courses a list of the prospective courses.
     * @returns a map containing the student's hypothetical progress summary
     * @throws GRADSException if the progress summary could not be generated or
     *         the courses are invalid.
     */
    public ProgressSummary simulateCourses(StudentRecord studentRecord, List<CourseTaken> courses)
            throws GRADSException {
        // first make a copy of the student record so the simulated courses only
        // apply to this method
        StudentRecord studentRecordCopy = Utilities.cloneThroughJson(studentRecord);

        // add all the courses to the student record copy
        studentRecordCopy.getCoursesTaken().addAll(courses);

        // generate and evaluate the progress report summary
        ProgressSummary progressSummary = new ProgressSummary();
        progressSummary.setStudent(Utilities.cloneThroughJson(studentRecord.getStudent()));
        progressSummary.setDegreeSought(studentRecord.getDegreeSought());
        progressSummary.setDepartment(studentRecord.getDepartment());
        progressSummary.setAdvisors(Utilities.cloneThroughJson(studentRecord.getAdvisors()));
        progressSummary.setCommittee(Utilities.cloneThroughJson(studentRecord.getCommittee()));
        progressSummary.setNotes(Utilities.cloneThroughJson(studentRecord.getNotes()));
        progressSummary.setTermBegan(Utilities.cloneThroughJson(studentRecord.getTermBegan()));

        List<Requirement> requirements = repository
                .find(studentRecord.getDegreeSought(), studentRecord.getDepartment());

        for (Requirement requirement : requirements) {
            progressSummary.addRequirementResult(requirement.check(studentRecordCopy, true));
        }

        return progressSummary;
    }

}
