package edu.umn.csci5801;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import edu.umn.csci5801.system.AccessCourseDatabaseTest;
import edu.umn.csci5801.system.AccessStudentRecordTest;
import edu.umn.csci5801.system.AmendStudentRecordTest;
import edu.umn.csci5801.system.CalculateGPA;
import edu.umn.csci5801.system.CheckMilestonesTest;
import edu.umn.csci5801.system.CoursesWithBreadthReqTest;
import edu.umn.csci5801.system.DuplicateBreadthCourseTest;
import edu.umn.csci5801.system.GpcAccessTest;
import edu.umn.csci5801.system.InvalidPlannedCourseList;
import edu.umn.csci5801.system.LoadNewGradReqTest;
import edu.umn.csci5801.system.NotesToStudentRecordTest;
import edu.umn.csci5801.system.PerformanceTest;
import edu.umn.csci5801.system.PlannedCourseList;
import edu.umn.csci5801.system.ReportForInvalidStudentIDTest;
import edu.umn.csci5801.system.RetrieveStudentCompleteCoursesTest;
import edu.umn.csci5801.system.RetrieveStudentIncompleteCoursesTest;
import edu.umn.csci5801.system.StudentAccessTest;
import edu.umn.csci5801.system.StudentsForDeptTest;
import edu.umn.csci5801.system.SummaryReportWithAllGradsTest;
import edu.umn.csci5801.system.SummaryReportWithNoGradsTest;
import edu.umn.csci5801.system.SummaryReportWithSomeGradsTest;

/**
 * 
 */
@RunWith(Suite.class)
@SuiteClasses({ AccessCourseDatabaseTest.class, AccessStudentRecordTest.class, AmendStudentRecordTest.class,
        CalculateGPA.class, CheckMilestonesTest.class, CoursesWithBreadthReqTest.class,
        DuplicateBreadthCourseTest.class, GpcAccessTest.class,
        InvalidPlannedCourseList.class, LoadNewGradReqTest.class, NotesToStudentRecordTest.class,
        PerformanceTest.class, PlannedCourseList.class, ReportForInvalidStudentIDTest.class,
        RetrieveStudentCompleteCoursesTest.class, RetrieveStudentIncompleteCoursesTest.class, StudentAccessTest.class,
        StudentsForDeptTest.class, SummaryReportWithAllGradsTest.class, SummaryReportWithNoGradsTest.class,
        SummaryReportWithSomeGradsTest.class })
public class SystemTestSuite {

}
