package edu.umn.csci5801.system;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import edu.umn.csci5801.GRADS;
import edu.umn.csci5801.GRADSIntf;
import edu.umn.csci5801.datastore.JSONRequirementRepository;
import edu.umn.csci5801.datastore.RequirementRepository;
import edu.umn.csci5801.model.Degree;
import edu.umn.csci5801.model.Department;
import edu.umn.csci5801.model.ProgressSummary;
import edu.umn.csci5801.model.RequirementCheckResult;

/**
 * Generate and verify summary report for a student with some graduation
 * requirements complete(Test case 2)
 */
public class SummaryReportWithSomeGradsTest {

    private GRADSIntf grads;

    // studendId1 has passed the GPA requirement and the DEFENSE_PASSED
    // milestone
    private final String studentId1 = "nguy0621";

    // studentId2 has only passed colloquium, but has all the breadth
    // requirement in progress
    private final String studentId2 = "chri3310";

    private Map<Degree, Integer> degreeToRequirementCount;

    private final String studentsFileName = "src/resources/students_someReqComplete.txt";
    private final String coursesFileName = "src/resources/courses.txt";
    private final String usersFileName = "src/resources/users.txt";

    @Before
    public void setUp() throws Exception {
        grads = new GRADS(studentsFileName, coursesFileName, usersFileName);

        // initialize the requirement sizes
        RequirementRepository requirementRepository = new JSONRequirementRepository(GRADS.DEFAULT_REQUIREMENTS_PATH);
        degreeToRequirementCount = new HashMap<Degree, Integer>();
        degreeToRequirementCount.put(Degree.MS_A, requirementRepository.find(Degree.MS_A, Department.COMPUTER_SCIENCE)
                .size());
        degreeToRequirementCount.put(Degree.MS_B, requirementRepository.find(Degree.MS_B, Department.COMPUTER_SCIENCE)
                .size());
        degreeToRequirementCount.put(Degree.MS_C, requirementRepository.find(Degree.MS_C, Department.COMPUTER_SCIENCE)
                .size());
        degreeToRequirementCount.put(Degree.PHD, requirementRepository.find(Degree.PHD, Department.COMPUTER_SCIENCE)
                .size());

    }

    @Test
    public void testStudentId1() throws Exception {
        grads.setUser(studentId1);
        ProgressSummary progressSummary = grads.generateProgressSummary(studentId1);
        assertTrue("RequirementCheckResults size does not match requirements size", progressSummary
                .getRequirementCheckResults().size() == degreeToRequirementCount.get(progressSummary.getDegreeSought()));

        List<String> passedRequirements = Arrays.asList("DEGREE_SOUGHT", "OVERALL_GPA_PHD", "IN_PROGRAM_GPA_PHD",
                "DEFENSE_PASSED");

        for (RequirementCheckResult requirementCheckResult : progressSummary.getRequirementCheckResults()) {

            // check for the passed requirements
            if (passedRequirements.contains(requirementCheckResult.getName())) {
                assertTrue("Requirement " + requirementCheckResult.getName() + " expected to pass",
                        requirementCheckResult.isPassed());
            } else {
                // all other requirements should fail
                assertFalse("Requirement " + requirementCheckResult.getName() + " expected to fail",
                        requirementCheckResult.isPassed());
                assertTrue("Requirement " + requirementCheckResult.getName() + " failed with no errors",
                        requirementCheckResult.getErrorMsgs().size() > 0);
            }
        }
    }

    @Test
    public void testStudentId2() throws Exception {
        grads.setUser(studentId2);
        ProgressSummary progressSummary = grads.generateProgressSummary(studentId2);
        assertTrue("RequirementCheckResults size does not match requirements size", progressSummary
                .getRequirementCheckResults().size() == degreeToRequirementCount.get(progressSummary.getDegreeSought()));

        List<String> passedRequirements = Arrays.asList("DEGREE_SOUGHT", "COLLOQUIUM");

        for (RequirementCheckResult requirementCheckResult : progressSummary.getRequirementCheckResults()) {

            // check for the passed requirements
            if (passedRequirements.contains(requirementCheckResult.getName())) {
                assertTrue("Requirement " + requirementCheckResult.getName() + " expected to pass",
                        requirementCheckResult.isPassed());
            } else {
                // all other requirements should fail
                assertFalse("Requirement " + requirementCheckResult.getName() + " expected to fail",
                        requirementCheckResult.isPassed());
                assertTrue("Requirement " + requirementCheckResult.getName() + " failed with no errors",
                        requirementCheckResult.getErrorMsgs().size() > 0);
            }
        }
    }
}
