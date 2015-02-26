package edu.umn.csci5801;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import edu.umn.csci5801.unit.AreaFilterTest;
import edu.umn.csci5801.unit.BreadthRequirementTest;
import edu.umn.csci5801.unit.CourseFilterTest;
import edu.umn.csci5801.unit.CourseRequirementTest;
import edu.umn.csci5801.unit.CreditRequirementTest;
import edu.umn.csci5801.unit.DuplicateFilterTest;
import edu.umn.csci5801.unit.GRADSTest;
import edu.umn.csci5801.unit.GpaRequirementTest;
import edu.umn.csci5801.unit.GradeFilterTest;
import edu.umn.csci5801.unit.JSONRequirementRepositoryTest;
import edu.umn.csci5801.unit.JSONStudentRecordRepositoryTest;
import edu.umn.csci5801.unit.JSONUserRepositoryTest;
import edu.umn.csci5801.unit.ProgressSummaryServiceTest;
import edu.umn.csci5801.unit.StudentRecordServiceTest;
import edu.umn.csci5801.unit.UserServiceTest;

/**
 * 
 */
@RunWith(Suite.class)
@SuiteClasses({ AreaFilterTest.class, BreadthRequirementTest.class, CourseFilterTest.class,
        CourseRequirementTest.class, CreditRequirementTest.class, DuplicateFilterTest.class, GpaRequirementTest.class,
        GradeFilterTest.class, GRADSTest.class, JSONRequirementRepositoryTest.class,
        JSONStudentRecordRepositoryTest.class, JSONUserRepositoryTest.class, ProgressSummaryServiceTest.class,
        StudentRecordServiceTest.class, UserServiceTest.class })
public class UnitTestSuite {

}
