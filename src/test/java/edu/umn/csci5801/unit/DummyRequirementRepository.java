package edu.umn.csci5801.unit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

import edu.umn.csci5801.datastore.RequirementRepository;
import edu.umn.csci5801.exception.GRADSException;
import edu.umn.csci5801.filter.CourseFilter;
import edu.umn.csci5801.filter.DuplicateFilter;
import edu.umn.csci5801.filter.FilterSet;
import edu.umn.csci5801.filter.GradeFilter;
import edu.umn.csci5801.model.Course;
import edu.umn.csci5801.model.CourseArea;
import edu.umn.csci5801.model.Degree;
import edu.umn.csci5801.model.Department;
import edu.umn.csci5801.model.Grade;
import edu.umn.csci5801.model.Milestone;
import edu.umn.csci5801.requirement.BreadthRequirement;
import edu.umn.csci5801.requirement.CourseRequirement;
import edu.umn.csci5801.requirement.CreditRequirement;
import edu.umn.csci5801.requirement.DegreeRequirement;
import edu.umn.csci5801.requirement.GpaRequirement;
import edu.umn.csci5801.requirement.MilestoneRequirement;
import edu.umn.csci5801.requirement.Requirement;

/**
 * An implementation of the RequirementRepository interface which uses a Dummy
 * file to persist requirements.
 */
public class DummyRequirementRepository implements RequirementRepository {

    private final Course colloquium;
    private final Course doctoralThesis;
    private final Course introToResearch1;
    private final Course introToResearch2;
    private final Course mastersThesis;
    private final Course mastersProject;
    private final HashSet<Grade> passingGrades;

    /**
     * Creates the dummy requirement repository with preloaded standard courses.
     */
    DummyRequirementRepository() {
        colloquium = new Course("Computer Science Colluquium", "CSCI8970", "1", null);
        doctoralThesis = new Course("Thesis Credit: Doctoral", "CSCI8888", "1-24", null);
        introToResearch1 = new Course("Introduction to Research in Computer Science, I", "CSCI8001", "1", null);
        introToResearch2 = new Course("Introduction to Research in Computer Science, II", "CSCI8002", "1", null);
        mastersThesis = new Course("Thesis Credits: Master's", "CSCI8777", "1-18", null);
        mastersProject = new Course("Plan B Project", "CSCI8760", "3", null);
        passingGrades = new LinkedHashSet<Grade>();
        passingGrades.add(Grade.A);
        passingGrades.add(Grade.B);
        passingGrades.add(Grade.C);
        passingGrades.add(Grade.S);
        passingGrades.add(Grade._);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Requirement> find(Degree degree, Department department) {
        try {
            if (department == Department.COMPUTER_SCIENCE) {
                switch (degree) {
                case PHD:
                    return createPHDRequirements();
                case MS_A:
                    return createMSARequirements();
                case MS_B:
                    return createMSBRequirements();
                case MS_C:
                    return createMSCRequirements();
                }
            }
        } catch (Exception e) {
            // this should not happen because we are constructing valid
            // requirements below
            return null;
        }
        return new ArrayList<Requirement>();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void save(Requirement requirement, Degree degree, Department department) throws GRADSException {
        // TODO Auto-generated method stub

    }

    private List<Requirement> createPHDRequirements() throws GRADSException {
        List<Requirement> requirementList = new ArrayList<Requirement>();

        // Must have declared a degree
        requirementList.add(new DegreeRequirement());

        // 31 total credits requiremed (including colloquium)
        requirementList.add(new CreditRequirement("TOTAL_CREDITS", 31, new FilterSet(
                new DuplicateFilter(), // Remove duplicates
                new CourseFilter(true, doctoralThesis, mastersThesis), // Not including thesis credits
                new GradeFilter(passingGrades) // Must be passing
                )));

        // 16 gruaduate level credits
        requirementList.add(new CreditRequirement("GRADUATE_CREDITS", 16, new FilterSet(
                new DuplicateFilter(), // Remove duplicates
                new CourseFilter(true, doctoralThesis, mastersThesis), // Not including thesis credits
                new CourseFilter(5), // Graduate-level courses
                new CourseFilter("CSCI"), // In-department
                new GradeFilter(Grade.C) // Must be C- minimum
                )));

        // 6 gruaduate level credits out of the department
        requirementList.add(new CreditRequirement("OUT_OF_DEPARTMENT", 6, new FilterSet(
                new DuplicateFilter(), // Remove duplicates
                new CourseFilter(true, doctoralThesis, mastersThesis), // Not including thesis credits
                new CourseFilter(5), // Graduate-level courses
                new CourseFilter("CSCI", true), // Out of department
                new GradeFilter(passingGrades) // Must be C- minimum
                )));

        // 24 thesis credits
        requirementList.add(new CreditRequirement("THESIS_PHD", 24, new CourseFilter(doctoralThesis)));

        // Colloquium must be taken
        requirementList.add(new CourseRequirement("COLLOQUIUM", new FilterSet(
                new CourseFilter(colloquium), // Colloquium
                new GradeFilter(Grade.S, Grade.S) // Passed
                )));

        // Intro to research I must be taken
        requirementList.add(new CourseRequirement("INTRO_TO_RESEARCH", new FilterSet(
                new CourseFilter(introToResearch1), // Colloquium
                new GradeFilter(Grade.C) // Must be C- minimum
                )));

        // Intro to research II must be taken
        requirementList.add(new CourseRequirement("INTRO_TO_RESEARCH2", new FilterSet(
                new CourseFilter(introToResearch2), // Colloquium
                new GradeFilter(Grade.C) // Must be C- minimum
                )));

        // Minimum GPA 3.45
        requirementList.add(new GpaRequirement("OVERALL_GPA_PHD", (float) 3.45, new FilterSet(
                new GradeFilter(Grade.F, false) // Must be A-F
                )));

        // Minimum in-program GPA 3.45
        requirementList.add(new GpaRequirement("IN_PROGRAM_GPA_PHD", (float) 3.45, new FilterSet(
                new CourseFilter(5), // Graduate-level courses
                new CourseFilter("CSCI"), // In-program
                new GradeFilter(Grade.F, false) // Must be A-F
                )));

        // Milestones
        requirementList.add(new MilestoneRequirement(Milestone.PRELIM_COMMITTEE_APPOINTED));
        requirementList.add(new MilestoneRequirement(Milestone.WRITTEN_PE_SUBMITTED));
        requirementList.add(new MilestoneRequirement(Milestone.WRITTEN_PE_APPROVED));
        requirementList.add(new MilestoneRequirement(Milestone.ORAL_PE_PASSED));
        requirementList.add(new MilestoneRequirement(Milestone.DPF_SUBMITTED));
        requirementList.add(new MilestoneRequirement(Milestone.DPF_APPROVED));
        requirementList.add(new MilestoneRequirement(Milestone.THESIS_COMMITTEE_APPOINTED));
        requirementList.add(new MilestoneRequirement(Milestone.PROPOSAL_PASSED));
        requirementList.add(new MilestoneRequirement(Milestone.THESIS_SUBMITTED));
        requirementList.add(new MilestoneRequirement(Milestone.THESIS_APPROVED));
        requirementList.add(new MilestoneRequirement(Milestone.DEFENSE_PASSED));
        requirementList.add(new MilestoneRequirement(Milestone.GRADUATION_PACKET_REQUESTED));

        // Breadth requirement
        Map<CourseArea, Integer> coursesInArea = new LinkedHashMap<CourseArea, Integer>();
        coursesInArea.put(CourseArea.THEORY_ALGORITHMS, 1);
        coursesInArea.put(CourseArea.ARCHITECTURE_SYSTEMS_SOFTWARE, 1);
        coursesInArea.put(CourseArea.APPLICATIONS, 1);
        requirementList.add(new BreadthRequirement("BREADTH_REQUIREMENT_PHD", coursesInArea, 5, (float) 3.45));

        return requirementList;
    }

    private List<Requirement> createMSARequirements() throws GRADSException {
        List<Requirement> requirementList = new ArrayList<Requirement>();

        // Must have declared a degree
        requirementList.add(new DegreeRequirement());

        // 31 total credits requiremed (including colloquium)
        requirementList.add(new CreditRequirement("TOTAL_CREDITS", 31, new FilterSet(
                new DuplicateFilter(), // Remove duplicates
                new GradeFilter(passingGrades) // Must be passing
                )));

        // 22 gruaduate level credits
        requirementList.add(new CreditRequirement("COURSE_CREDITS", 22, new FilterSet(
                new DuplicateFilter(), // Remove duplicates
                new CourseFilter(true, doctoralThesis, mastersThesis), // Not including thesis credits
                new GradeFilter(Grade.C) // Must be C- minimum
                )));

        // 16 in-program gruaduate level credits
        requirementList.add(new CreditRequirement("COURSE_CREDITS_IN_PROGRAM", 16, new FilterSet(
                new DuplicateFilter(), // Remove duplicates
                new CourseFilter(true, doctoralThesis, mastersThesis), // Not including thesis credits
                new CourseFilter(5), // Graduate-level courses
                new CourseFilter("CSCI"), // In-department
                new GradeFilter(Grade.C) // Must be C- minimum
                )));

        // Colloquium must be taken
        requirementList.add(new CourseRequirement("COLLOQUIUM", new FilterSet(new CourseFilter(colloquium), // Colloquium
                new GradeFilter(Grade.S, Grade.S) // Passed
                )));

        // 3 advanced gruaduate level credits
        requirementList.add(new CreditRequirement("PHD_LEVEL_COURSES", 3, new FilterSet(
                new DuplicateFilter(), // Remove duplicates
                new CourseFilter(true, doctoralThesis, mastersThesis), // Not including thesis credits
                new CourseFilter(8), // Advanced graduate-level courses
                new CourseFilter("CSCI"), // In-department
                new GradeFilter(Grade.C) // Must be C- minimum
                )));

        // 10 thesis credits
        requirementList.add(new CreditRequirement("THESIS_MS", 10, new CourseFilter(mastersThesis)));

        // Minimum GPA 3.25
        requirementList.add(new GpaRequirement("OVERALL_GPA_MS", (float) 3.25, new FilterSet(
                new GradeFilter(Grade.F, false) // Must be A-F
                )));

        // Minimum in-program GPA 3.25
        requirementList.add(new GpaRequirement("IN_PROGRAM_GPA_MS", (float) 3.25, new FilterSet(
                new CourseFilter(5), // Graduate-level courses
                new CourseFilter("CSCI"), // In-program
                new GradeFilter(Grade.F, false) // Must be A-F
                )));

        // Milestones
        requirementList.add(new MilestoneRequirement(Milestone.DPF_SUBMITTED));
        requirementList.add(new MilestoneRequirement(Milestone.DPF_APPROVED));
        requirementList.add(new MilestoneRequirement(Milestone.THESIS_COMMITTEE_APPOINTED));
        requirementList.add(new MilestoneRequirement(Milestone.GRADUATION_PACKET_REQUESTED));
        requirementList.add(new MilestoneRequirement(Milestone.THESIS_SUBMITTED));
        requirementList.add(new MilestoneRequirement(Milestone.THESIS_APPROVED));
        requirementList.add(new MilestoneRequirement(Milestone.DEFENSE_PASSED));

        // Breadth requirement
        Map<CourseArea, Integer> coursesInArea = new LinkedHashMap<CourseArea, Integer>();
        coursesInArea.put(CourseArea.THEORY_ALGORITHMS, 1);
        coursesInArea.put(CourseArea.ARCHITECTURE_SYSTEMS_SOFTWARE, 1);
        coursesInArea.put(CourseArea.APPLICATIONS, 1);
        requirementList.add(new BreadthRequirement("BREADTH_REQUIREMENT_MS", coursesInArea, 3, (float) 3.25));

        return requirementList;
    }

    private List<Requirement> createMSBRequirements() throws GRADSException {
        List<Requirement> requirementList = new ArrayList<Requirement>();

        // Must have declared a degree
        requirementList.add(new DegreeRequirement());

        // 31 total credits requiremed (including colloquium)
        requirementList.add(new CreditRequirement("TOTAL_CREDITS", 31, new FilterSet(
                new DuplicateFilter(), // Remove duplicates
                new CourseFilter(true, doctoralThesis, mastersThesis), // Not including thesis credits
                new GradeFilter(passingGrades) // Must be passing
                )));

        // 16 gruaduate level credits
        requirementList.add(new CreditRequirement("GRADUATE_CREDITS", 16, new FilterSet(
                new DuplicateFilter(), // Remove duplicates
                new CourseFilter(true, doctoralThesis, mastersThesis), // Not including thesis credits
                new CourseFilter(5), // Graduate-level courses
                new CourseFilter("CSCI"), // In-department
                new GradeFilter(Grade.C) // Must be C- minimum
                )));

        // 3 advanced gruaduate level credits
        requirementList.add(new CreditRequirement("PHD_LEVEL_COURSES", 3, new FilterSet(
                new DuplicateFilter(), // Remove duplicates
                new CourseFilter(true, doctoralThesis, mastersThesis), // Not including thesis credits
                new CourseFilter(8), // Advanced graduate-level courses
                new CourseFilter("CSCI"), // In-department
                new GradeFilter(Grade.C) // Must be C- minimum
                )));

        // Plan B project must be taken
        requirementList.add(new CourseRequirement("PLAN_B_PROJECT", new FilterSet(new CourseFilter(mastersProject), // Project 
                new GradeFilter(Grade.S, Grade.S) // Passed
                )));

        // Colloquium must be taken
        requirementList.add(new CourseRequirement("COLLOQUIUM", new FilterSet(new CourseFilter(colloquium), // Colloquium
                new GradeFilter(Grade.S, Grade.S) // Passed
                )));

        // Minimum GPA 3.25
        requirementList.add(new GpaRequirement("OVERALL_GPA_MS", (float) 3.25, new FilterSet(
                new GradeFilter(Grade.F, false) // Must be A-F
                )));

        // Minimum in-program GPA 3.25
        requirementList.add(new GpaRequirement("IN_PROGRAM_GPA_MS", (float) 3.25, new FilterSet(
                new CourseFilter(5), // Graduate-level courses
                new CourseFilter("CSCI"), // In-program
                new GradeFilter(Grade.F, false) // Must be A-F
                )));

        // Milestones
        requirementList.add(new MilestoneRequirement(Milestone.DPF_SUBMITTED));
        requirementList.add(new MilestoneRequirement(Milestone.DPF_APPROVED));
        requirementList.add(new MilestoneRequirement(Milestone.PROJECT_COMMITTEE_APPOINTED));
        requirementList.add(new MilestoneRequirement(Milestone.GRADUATION_PACKET_REQUESTED));
        requirementList.add(new MilestoneRequirement(Milestone.DEFENSE_PASSED));

        // Breadth requirement
        Map<CourseArea, Integer> coursesInArea = new LinkedHashMap<CourseArea, Integer>();
        coursesInArea.put(CourseArea.THEORY_ALGORITHMS, 1);
        coursesInArea.put(CourseArea.ARCHITECTURE_SYSTEMS_SOFTWARE, 1);
        coursesInArea.put(CourseArea.APPLICATIONS, 1);
        requirementList.add(new BreadthRequirement("BREADTH_REQUIREMENT_MS", coursesInArea, 3, (float) 3.25));

        return requirementList;
    }

    private List<Requirement> createMSCRequirements() throws GRADSException {
        List<Requirement> requirementList = new ArrayList<Requirement>();

        // Must have declared a degree
        requirementList.add(new DegreeRequirement());

        // 31 total credits requiremed (including colloquium)
        requirementList.add(new CreditRequirement("TOTAL_CREDITS", 31, new FilterSet(
                new DuplicateFilter(), // Remove duplicates
                new CourseFilter(true, doctoralThesis, mastersThesis), // Not including thesis credits
                new GradeFilter(passingGrades) // Must be passing
                )));

        // 16 gruaduate level credits
        requirementList.add(new CreditRequirement("GRADUATE_CREDITS", 16, new FilterSet(
                new DuplicateFilter(), // Remove duplicates
                new CourseFilter(true, doctoralThesis, mastersThesis), // Not including thesis credits
                new CourseFilter(5), // Graduate-level courses
                new CourseFilter("CSCI"), // In-department
                new GradeFilter(Grade.C) // Must be C- minimum
                )));

        // 6 advanced gruaduate level credits
        requirementList.add(new CreditRequirement("PHD_LEVEL_COURSES_PLANC", 6, new FilterSet(
                new DuplicateFilter(), // Remove duplicates
                new CourseFilter(true, doctoralThesis, mastersThesis), // Not including thesis credits
                new CourseFilter(8), // Advanced graduate-level courses
                new CourseFilter("CSCI"), // In-department
                new GradeFilter(Grade.C) // Must be C- minimum
                )));

        // Colloquium must be taken
        requirementList.add(new CourseRequirement("COLLOQUIUM", new FilterSet(new CourseFilter(colloquium), // Colloquium
                new GradeFilter(Grade.S, Grade.S) // Passed
                )));

        // Minimum GPA 3.25
        requirementList.add(new GpaRequirement("OVERALL_GPA_MS", (float) 3.25, new FilterSet(
                new GradeFilter(Grade.F, false) // Must be A-F
                )));

        // Minimum in-program GPA 3.25
        requirementList.add(new GpaRequirement("IN_PROGRAM_GPA_MS", (float) 3.25, new FilterSet(
                new CourseFilter(5), // Graduate-level courses
                new CourseFilter("CSCI"), // In-program
                new GradeFilter(Grade.F, false) // Must be A-F
                )));

        // Milestones
        requirementList.add(new MilestoneRequirement(Milestone.DPF_SUBMITTED));
        requirementList.add(new MilestoneRequirement(Milestone.DPF_APPROVED));
        requirementList.add(new MilestoneRequirement(Milestone.TRACKING_FORM_SUBMITTED));
        requirementList.add(new MilestoneRequirement(Milestone.TRACKING_FORM_APPROVED));
        requirementList.add(new MilestoneRequirement(Milestone.GRADUATION_PACKET_REQUESTED));

        // Breadth requirement
        Map<CourseArea, Integer> coursesInArea = new LinkedHashMap<CourseArea, Integer>();
        coursesInArea.put(CourseArea.THEORY_ALGORITHMS, 1);
        coursesInArea.put(CourseArea.ARCHITECTURE_SYSTEMS_SOFTWARE, 1);
        coursesInArea.put(CourseArea.APPLICATIONS, 1);
        requirementList.add(new BreadthRequirement("BREADTH_REQUIREMENT_MS", coursesInArea, 3, (float) 3.25));

        return requirementList;
    }
}
