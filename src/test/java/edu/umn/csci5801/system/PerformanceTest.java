package edu.umn.csci5801.system;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import edu.umn.csci5801.GRADS;
import edu.umn.csci5801.GRADSIntf;
import edu.umn.csci5801.model.ProgressSummary;

/**
 * Test the performance of the system (test case 22)
 * @note This set of tests is pretty useless, the numbers are not very accurate
 */
public class PerformanceTest {

    private GRADSIntf grads;

    private String studentId = "ande2807";
    private String gpcId = "tolas9999";
    private String studentsFileName = "src/resources/students_OneStudentOfEachDegreeComplete.txt";
    private String coursesFileName = "src/resources/courses.txt";
    private String usersFileName = "src/resources/users.txt";
    private long maxRuntimeInNs = 600 * 1000 * 1000; // 600 milliseconds
    private long maxMemoryInBytes = 100 * 1024 * 1024; // 100 MB

    @Test
    public void progressSummaryRuntimeTest() throws Exception {
        grads = new GRADS(studentsFileName, coursesFileName, usersFileName);
        grads.setUser(gpcId);

        long startTime = System.nanoTime();

        ProgressSummary progressSummary = grads.generateProgressSummary(studentId);

        long stopTime = System.nanoTime();
        long elapsedTime = stopTime - startTime;
        assertTrue(elapsedTime < maxRuntimeInNs);
        assertNotNull(progressSummary);
    }

    @Test
    public void progressSummaryMemoryUsageTest() throws Exception {
        grads = new GRADS(studentsFileName, coursesFileName, usersFileName);
        grads.setUser(gpcId);
        ProgressSummary progressSummary = grads.generateProgressSummary(studentId);

        Runtime runtime = Runtime.getRuntime();
        runtime.gc();
        long usedMemory = runtime.totalMemory() - runtime.freeMemory();

        assertTrue(usedMemory < maxMemoryInBytes);
        assertNotNull(progressSummary);
    }
}
