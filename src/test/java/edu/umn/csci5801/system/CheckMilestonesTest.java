package edu.umn.csci5801.system;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.umn.csci5801.GRADS;
import edu.umn.csci5801.GRADSIntf;
import edu.umn.csci5801.model.Milestone;
import edu.umn.csci5801.model.MilestoneSet;
import edu.umn.csci5801.model.ProgressSummary;
import edu.umn.csci5801.model.RequirementCheckResult;
import edu.umn.csci5801.model.Semester;
import edu.umn.csci5801.model.StudentRecord;
import edu.umn.csci5801.model.Term;

/**
 * Check Milestones(Test case 20)
 */
public class CheckMilestonesTest {
    private GRADSIntf grads;

    private String studentId = "gayxx067";
    private String gpcId = "tolas9999";
    private String studentsFileName = "src/resources/students.txt";
    private String coursesFileName = "src/resources/courses.txt";
    private String usersFileName = "src/resources/users.txt";

    StudentRecord originalStudentRecord;

    @Before
    public void setUp() throws Exception {
        grads = new GRADS(studentsFileName, coursesFileName, usersFileName);
        grads.setUser(gpcId);
        originalStudentRecord = grads.getTranscript(studentId);
    }

    @After
    public void tearDown() throws Exception {
        grads = new GRADS(studentsFileName, coursesFileName, usersFileName);
        grads.setUser(gpcId);
        grads.updateTranscript(studentId, originalStudentRecord);
    }

    @Test
    public void test() throws Exception {
        grads = new GRADS(studentsFileName, coursesFileName, usersFileName);
        grads.setUser(gpcId);
        ProgressSummary progressSummary = grads.generateProgressSummary(studentId);
        assertNotNull(progressSummary);
        assertNotNull(progressSummary.getRequirementCheckResults());
        Set<String> milestones = new HashSet<String>();
        for (Milestone milestone : Milestone.values()) {
            milestones.add(milestone.toString());
        }

        for (RequirementCheckResult requirementResult : progressSummary.getRequirementCheckResults()) {
            if (milestones.contains(requirementResult.getName()) && requirementResult.isPassed()) {
                fail("There shouldn't be any passed milestones");
            }
        }

        // Add all but one milestone for an MS_C
        StudentRecord studentRecord = grads.getTranscript(studentId);
        Term term = new Term(Semester.SPRING, 2010);
        List<MilestoneSet> milestonesSet = new ArrayList<MilestoneSet>();
        milestonesSet.add(new MilestoneSet(Milestone.DPF_SUBMITTED, term));
        milestonesSet.add(new MilestoneSet(Milestone.TRACKING_FORM_SUBMITTED, term));
        milestonesSet.add(new MilestoneSet(Milestone.TRACKING_FORM_APPROVED, term));
        milestonesSet.add(new MilestoneSet(Milestone.GRADUATION_PACKET_REQUESTED, term));
        studentRecord.setMilestonesSet(milestonesSet);
        grads.updateTranscript(studentId, studentRecord);

        progressSummary = grads.generateProgressSummary(studentId);
        assertNotNull(progressSummary);
        assertNotNull(progressSummary.getRequirementCheckResults());

        for (RequirementCheckResult requirementResult : progressSummary.getRequirementCheckResults()) {
            if (milestones.contains(requirementResult.getName()) && !requirementResult.isPassed()) {
                if (!requirementResult.getName().equals(Milestone.DPF_APPROVED.toString())) {
                    fail("Missing the following milestone: " + requirementResult.getName());
                }
            }
        }

        milestonesSet.add(new MilestoneSet(Milestone.DPF_APPROVED, term));
        studentRecord.setMilestonesSet(milestonesSet);
        grads.updateTranscript(studentId, studentRecord);

        progressSummary = grads.generateProgressSummary(studentId);
        assertNotNull(progressSummary);
        assertNotNull(progressSummary.getRequirementCheckResults());

        for (RequirementCheckResult requirementResult : progressSummary.getRequirementCheckResults()) {
            if (milestones.contains(requirementResult.getName()) && !requirementResult.isPassed()) {
                fail("Missing the following milestone: " + requirementResult.getName());
            }
        }

    }

}
