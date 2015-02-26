package edu.umn.csci5801;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import edu.umn.csci5801.codeCoverage.CodeCoverageTest;
import edu.umn.csci5801.codeCoverage.GradsConstructors;
import edu.umn.csci5801.codeCoverage.SimpleGradsTest;

/**
 * 
 */
@RunWith(Suite.class)
@SuiteClasses({ SimpleGradsTest.class, CodeCoverageTest.class, GradsConstructors.class, SystemTestSuite.class })
public class CodeCoverageTestSuite {

}
