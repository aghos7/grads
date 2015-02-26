package edu.umn.csci5801.system;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.umn.csci5801.GRADS;
import edu.umn.csci5801.GRADSIntf;
import edu.umn.csci5801.model.Degree;
import edu.umn.csci5801.model.ProgressSummary;
import edu.umn.csci5801.model.RequirementCheckResult;

/**
 * Loading new graduation requirements (test case 21)
 */
public class LoadNewGradReqTest {

    private GRADSIntf grads;

    // student ids to test
    private final String studentId = "chri3310";

    private Map<Degree, Integer> degreeToRequirementCount;

    private final String studentsFileName = "src/resources/students_OneStudentOfEachDegreeComplete.txt";
    private final String coursesFileName = "src/resources/courses.txt";
    private final String usersFileName = "src/resources/users.txt";

    private final String defaultRequirements = GRADS.DEFAULT_REQUIREMENTS_PATH;

    // modified IN_PROGRAM_GPA_MS to 4.0
    // modified PHD_LEVEL_COURSES to 9 credits
    // removed THESIS_MS requirement
    // added ORAL_PE_PASSED milestone requirement
    private final String newRequirements = "src/resources/requirements_newReqTest.txt";

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void test() throws Exception {
        grads = new GRADS(studentsFileName, coursesFileName, usersFileName, defaultRequirements);
        grads.setUser(studentId);
        ProgressSummary defaultProgressSummary = grads.generateProgressSummary(studentId);
        List<RequirementCheckResult> requirementCheckResults = defaultProgressSummary.getRequirementCheckResults();
        RequirementCheckResult inProgramGPAResult = findRequirementCheckResultByName("IN_PROGRAM_GPA_MS",
                requirementCheckResults);
        RequirementCheckResult phdLevelCoursesResult = findRequirementCheckResultByName("PHD_LEVEL_COURSES",
                requirementCheckResults);
        RequirementCheckResult thesisCreditsResult = findRequirementCheckResultByName("THESIS_MS",
                requirementCheckResults);
        RequirementCheckResult oralPeMilestoneResult = findRequirementCheckResultByName("ORAL_PE_PASSED",
                requirementCheckResults);

        assertTrue(inProgramGPAResult.isPassed());
        assertTrue(phdLevelCoursesResult.isPassed());
        assertTrue(thesisCreditsResult.isPassed());
        assertNull(oralPeMilestoneResult);

        // load the new modified requirements and generate a progress summary
        grads = new GRADS(studentsFileName, coursesFileName, usersFileName, newRequirements);
        grads.setUser(studentId);
        ProgressSummary newProgressSummary = grads.generateProgressSummary(studentId);
        List<RequirementCheckResult> newRequirementCheckResults = newProgressSummary.getRequirementCheckResults();
        RequirementCheckResult newInProgramGPAResult = findRequirementCheckResultByName("IN_PROGRAM_GPA_MS",
                newRequirementCheckResults);
        RequirementCheckResult newPhdLevelCoursesResult = findRequirementCheckResultByName("PHD_LEVEL_COURSES",
                newRequirementCheckResults);
        RequirementCheckResult newThesisCreditsResult = findRequirementCheckResultByName("THESIS_MS",
                newRequirementCheckResults);
        RequirementCheckResult newOralPeMilestoneResult = findRequirementCheckResultByName("ORAL_PE_PASSED",
                newRequirementCheckResults);

        assertTrue(newInProgramGPAResult.isPassed());
        assertFalse(newPhdLevelCoursesResult.isPassed());
        assertNull(newThesisCreditsResult);
        assertFalse(newOralPeMilestoneResult.isPassed());
    }

    private RequirementCheckResult findRequirementCheckResultByName(String name,
            List<RequirementCheckResult> requirementCheckResults) {
        for (RequirementCheckResult result : requirementCheckResults) {
            if (result.getName().equals(name)) {
                return result;
            }
        }
        return null;
    }
}
