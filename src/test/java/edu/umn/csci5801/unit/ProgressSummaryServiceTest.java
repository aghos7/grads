package edu.umn.csci5801.unit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

import edu.umn.csci5801.datastore.JSONRequirementRepository;
import edu.umn.csci5801.datastore.JSONStudentRecordRepository;
import edu.umn.csci5801.exception.GRADSException;
import edu.umn.csci5801.exception.GRADSIllegalArgumentException;
import edu.umn.csci5801.model.Degree;
import edu.umn.csci5801.model.Department;
import edu.umn.csci5801.model.ProgressSummary;
import edu.umn.csci5801.model.Semester;
import edu.umn.csci5801.model.Term;
import edu.umn.csci5801.service.ProgressSummaryService;
import edu.umn.csci5801.service.StudentRecordService;

public class ProgressSummaryServiceTest {

    private ProgressSummaryService progressSummaryService;
    private StudentRecordService studentRecordService;

    @Before
    public void setup() throws GRADSIllegalArgumentException, GRADSException {
        progressSummaryService = new ProgressSummaryService(new JSONRequirementRepository(
                "src/resources/requirements.txt"));
        studentRecordService = new StudentRecordService(new JSONStudentRecordRepository("src/resources/courses.txt",
                "src/resources/students.txt"));
    }

    @Test(expected = GRADSIllegalArgumentException.class)
    public void testGetStudentProgressWithNullId() throws GRADSException {
        progressSummaryService.generateProgressSummary(null);
    }

    @Test
    public void testGetStudentProgressWithExistingIdStudent() throws GRADSException {
        String existingId = "nguy0621";
        ProgressSummary progressSummary = progressSummaryService.generateProgressSummary(studentRecordService
                .getTranscript(existingId));
        assertNotNull(progressSummary);
        assertEquals(existingId, progressSummary.getStudent().getId());
        assertEquals(progressSummary.getDepartment(), Department.COMPUTER_SCIENCE);
        assertEquals(progressSummary.getDegreeSought(), Degree.PHD);
        assertEquals(progressSummary.getTermBegan(), new Term(Semester.SPRING, 2008));
        assertEquals(progressSummary.getAdvisors().size(), 1);
        assertEquals(progressSummary.getCommittee().size(), 3);
        assertEquals(progressSummary.getNotes().size(), 2);
    }

    @Test
    public void testGetStudentProgressWithExistingIdUser() throws GRADSException {
        String existingId = "nguy0621";
        ProgressSummary progressSummary = progressSummaryService.generateProgressSummary(studentRecordService
                .getTranscript(existingId));
        assertNotNull(progressSummary);
        assertEquals(existingId, progressSummary.getStudent().getId());
        assertEquals(progressSummary.getDepartment(), Department.COMPUTER_SCIENCE);
        assertEquals(progressSummary.getDegreeSought(), Degree.PHD);
        assertEquals(progressSummary.getTermBegan(), new Term(Semester.SPRING, 2008));
    }

    @SuppressWarnings("unused")
    @Test(expected = GRADSException.class)
    public void testGetStudentProgressWithNonExistingId() throws GRADSException {
        ProgressSummary progressSummary = progressSummaryService.generateProgressSummary(studentRecordService
                .getTranscript("123456"));
    }
}
