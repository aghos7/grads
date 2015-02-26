package edu.umn.csci5801.unit;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import org.junit.Test;

import edu.umn.csci5801.datastore.JSONRequirementRepository;
import edu.umn.csci5801.datastore.RequirementRepository;
import edu.umn.csci5801.exception.GRADSException;
import edu.umn.csci5801.exception.GRADSIllegalArgumentException;
import edu.umn.csci5801.filter.CourseFilter;
import edu.umn.csci5801.filter.DuplicateFilter;
import edu.umn.csci5801.filter.FilterSet;
import edu.umn.csci5801.filter.GradeFilter;
import edu.umn.csci5801.model.Degree;
import edu.umn.csci5801.model.Department;
import edu.umn.csci5801.model.Grade;
import edu.umn.csci5801.requirement.CreditRequirement;
import edu.umn.csci5801.requirement.Requirement;

/**
 * 
 */
public class JSONRequirementRepositoryTest {

    @Test
    public void testGoodRequirements() throws GRADSException {
        RequirementRepository repository = new JSONRequirementRepository("src/resources/requirements.txt");
        List<Requirement> requirements = repository.find(Degree.MS_A, Department.COMPUTER_SCIENCE);
        assertNotNull(requirements);
    }

    @Test
    public void testNoRequirements() throws GRADSException {
        RequirementRepository repository = new JSONRequirementRepository(
                "src/resources/requirements_noRequirements.txt");
        List<Requirement> requirements = repository.find(Degree.MS_A, Department.COMPUTER_SCIENCE);
        assertTrue(requirements.size() == 0);
    }

    @Test
    public void testSaveRequirements() throws GRADSException, IOException {
        String file = "src/resources/requirements_noRequirements.txt";
        byte[] beforeContents = Files.readAllBytes(Paths.get(file));

        RequirementRepository repository = new JSONRequirementRepository(file);
        List<Requirement> requirements = repository.find(Degree.MS_A, Department.COMPUTER_SCIENCE);
        assertTrue(requirements.size() == 0);

        try {
            repository.save(new CreditRequirement("16 credits from graduate-level courses", 16, new FilterSet(
                    new DuplicateFilter(), // Remove duplicates
                    new CourseFilter(5), // Graduate-level courses
                    new GradeFilter(Grade.F) // Must be A-F
                    )), Degree.MS_A, Department.COMPUTER_SCIENCE);

            requirements = repository.find(Degree.MS_A, Department.COMPUTER_SCIENCE);
            assertNotNull(requirements);
            Requirement requirement = requirements.get(0);
            assertNotNull(requirement);
            assertTrue(requirement instanceof CreditRequirement);
        } finally {
            // restore the original contents of the test file
            FileOutputStream os = new FileOutputStream(file);
            os.write(beforeContents);
            os.close();
        }
    }

    @Test
    public void testSaveAllDummyRequirements() throws GRADSException, IOException {
        String file = "src/resources/requirements_dummyRequirements.txt";
        // first write an empty JSON Array to the file
        FileWriter fw = new FileWriter(file);
        fw.write("[]");
        fw.close();

        RequirementRepository repository = new JSONRequirementRepository(file);
        assertTrue(repository.find(Degree.PHD, Department.COMPUTER_SCIENCE).size() == 0);

        // add all the dummy requirements
        RequirementRepository dummyRepository = new DummyRequirementRepository();
        copyRequirementsToNewRepository(dummyRepository, repository, Degree.MS_A, Department.COMPUTER_SCIENCE);
        copyRequirementsToNewRepository(dummyRepository, repository, Degree.MS_B, Department.COMPUTER_SCIENCE);
        copyRequirementsToNewRepository(dummyRepository, repository, Degree.MS_C, Department.COMPUTER_SCIENCE);
        copyRequirementsToNewRepository(dummyRepository, repository, Degree.PHD, Department.COMPUTER_SCIENCE);

    }

    private void copyRequirementsToNewRepository(RequirementRepository sourceRepo, RequirementRepository targetRepo,
            Degree degree, Department department) throws GRADSIllegalArgumentException, GRADSException {

        for (Requirement requirement : sourceRepo.find(degree, department)) {
            targetRepo.save(requirement, degree, department);
        }
    }
}
