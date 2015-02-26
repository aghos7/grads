Grads
====
A simple tool for computing graduation requirements and progress

Main Dependencies:
- Not included:
 - maven
 - JDK 1.7
- Included in lib directory:
 - JUnit 4.11
 - emma 2.1.5320
 - gson 2.2.4
 - A bunch more but maven will download when you need them
 
Compilation:<br />
`mvn clean compile`
Generate lib directory:<br />
`mvn package`

Testing:
- All Tests:<br />
`mvn clean test`<br />
- Requirements Based System Tests:<br />
`mvn clean -Dtest=edu.umn.csci5801.SystemTestSuite test`
- Unit Tests:<br />
`mvn clean -Dtest=edu.umn.csci5801.unit.UnitTestSuite test`
- Code Coverage:<br />
`mvn clean -Dtest=edu.umn.csci5801.CodeCoverageTestSuite test`

Generate Reports / Site<br />
 This will clean, compile, test, generate code coverage and a website.
 The website can be viewed at ./target/site/index.html
Testing results can be viewed at ./target/site/surefire-report.html (for html) or ./target/surefire-reports/ (for xml/text)

Code Coverage result can be viewed at ./target/site/emma/index.html or ./target/site/emma/coverage.xml (for xml)
For each example output included the testing and code coverage results can be viewed by replacing "target"
with the example output directories described below.
Each one will also contain a pdf for SureFire (JUnit) report and EMMA Coverage Report
