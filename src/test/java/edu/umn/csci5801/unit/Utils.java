package edu.umn.csci5801.unit;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import edu.umn.csci5801.model.Course;
import edu.umn.csci5801.model.CourseTaken;
import edu.umn.csci5801.model.Degree;
import edu.umn.csci5801.model.Department;
import edu.umn.csci5801.model.Grade;
import edu.umn.csci5801.model.Milestone;
import edu.umn.csci5801.model.MilestoneSet;
import edu.umn.csci5801.model.Professor;
import edu.umn.csci5801.model.Semester;
import edu.umn.csci5801.model.Student;
import edu.umn.csci5801.model.StudentRecord;
import edu.umn.csci5801.model.Term;

/**
 * 
 */
public class Utils {

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
    }

    /**
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void createPHDStudentTest() {
        try {
            StudentRecord record = new StudentRecord();
            record.setStudent(new Student("Saritha", "Pochampally", "pocha006"));
            record.setDepartment(Department.COMPUTER_SCIENCE);
            record.setDegreeSought(Degree.PHD);
            record.setTermBegan(new Term(Semester.FALL, 2010));
            record.setAdvisors(Arrays.asList(new Professor("mat", "heimdahl"), new Professor("nic", "Hopper")));
            record.setCommittee(Arrays.asList(new Professor("mat", "heimdahl"), new Professor("nic", "Hopper")));

            List<Course> courseList = new Gson().fromJson(new FileReader(new File("src/resources/courses.txt")),
                    new TypeToken<List<Course>>() {
                    }.getType());
            Map<String, Course> courseMap = new HashMap<String, Course>();
            for (Course course : courseList) {
                courseMap.put(course.getId(), course);
            }
            Term term = new Term(Semester.SPRING, 2010);

            List<CourseTaken> coursesTaken = new ArrayList<CourseTaken>();
            // Breadth Reqs
            coursesTaken.add(new CourseTaken(courseMap.get("csci5302"), term, Grade.A));
            coursesTaken.add(new CourseTaken(courseMap.get("csci5103"), term, Grade.A));
            coursesTaken.add(new CourseTaken(courseMap.get("csci5619"), term, Grade.A));
            coursesTaken.add(new CourseTaken(courseMap.get("csci5125"), term, Grade.A));
            coursesTaken.add(new CourseTaken(courseMap.get("csci5607"), term, Grade.A));
            // 24 credits of 8888
            coursesTaken.add(new CourseTaken(new Course("Thesis Credit: Doctoral", "csci8888", "24", null), term,
                    Grade.S));
            // Colloquium
            coursesTaken.add(new CourseTaken(courseMap.get("csci8970"), term, Grade.S));
            // 6 credits out of department
            coursesTaken.add(new CourseTaken(new Course("Algebra", "math5000", "3", null), term, Grade.S));
            coursesTaken.add(new CourseTaken(new Course("Geometry", "math6000", "3", null), term, Grade.S));
            // Intro to research
            coursesTaken.add(new CourseTaken(courseMap.get("csci8001"), term, Grade.A));
            coursesTaken.add(new CourseTaken(courseMap.get("csci8002"), term, Grade.A));
            // Total 31 credits, at least 16 csci
            coursesTaken.add(new CourseTaken(courseMap.get("csci5552"), term, Grade.A));
            coursesTaken.add(new CourseTaken(courseMap.get("csci5131"), term, Grade.A));
            coursesTaken.add(new CourseTaken(courseMap.get("csci5129"), term, Grade.A));
            coursesTaken.add(new CourseTaken(courseMap.get("csci5481"), term, Grade.A));
            coursesTaken.add(new CourseTaken(courseMap.get("csci5211"), term, Grade.A));
            coursesTaken.add(new CourseTaken(courseMap.get("csci5461"), term, Grade.A));

            record.setCoursesTaken(coursesTaken);

            // Set needed milestones
            List<MilestoneSet> milestonesSet = new ArrayList<MilestoneSet>();
            milestonesSet.add(new MilestoneSet(Milestone.PRELIM_COMMITTEE_APPOINTED, term));
            milestonesSet.add(new MilestoneSet(Milestone.WRITTEN_PE_SUBMITTED, term));
            milestonesSet.add(new MilestoneSet(Milestone.WRITTEN_PE_APPROVED, term));
            milestonesSet.add(new MilestoneSet(Milestone.ORAL_PE_PASSED, term));
            milestonesSet.add(new MilestoneSet(Milestone.DPF_SUBMITTED, term));
            milestonesSet.add(new MilestoneSet(Milestone.DPF_APPROVED, term));
            milestonesSet.add(new MilestoneSet(Milestone.THESIS_COMMITTEE_APPOINTED, term));
            milestonesSet.add(new MilestoneSet(Milestone.PROPOSAL_PASSED, term));
            milestonesSet.add(new MilestoneSet(Milestone.GRADUATION_PACKET_REQUESTED, term));
            milestonesSet.add(new MilestoneSet(Milestone.THESIS_SUBMITTED, term));
            milestonesSet.add(new MilestoneSet(Milestone.THESIS_APPROVED, term));
            milestonesSet.add(new MilestoneSet(Milestone.DEFENSE_PASSED, term));
            record.setMilestonesSet(milestonesSet);

            record.setNotes(Arrays.asList("note1", "note2", "note3"));

            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            System.out.println(gson.toJson(record));

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Test
    public void createMSAStudentTest() {
        try {
            StudentRecord record = new StudentRecord();
            record.setStudent(new Student("Kyle", "Christianson", "chri3310"));
            record.setDepartment(Department.COMPUTER_SCIENCE);
            record.setDegreeSought(Degree.MS_A);
            record.setTermBegan(new Term(Semester.FALL, 2010));
            record.setAdvisors(Arrays.asList(new Professor("mat", "heimdahl"), new Professor("nic", "Hopper")));
            record.setCommittee(Arrays.asList(new Professor("mat", "heimdahl"), new Professor("nic", "Hopper")));

            List<Course> courseList = new Gson().fromJson(new FileReader(new File("src/resources/courses.txt")),
                    new TypeToken<List<Course>>() {
                    }.getType());
            Map<String, Course> courseMap = new HashMap<String, Course>();
            for (Course course : courseList) {
                courseMap.put(course.getId(), course);
            }
            Term term = new Term(Semester.SPRING, 2010);

            List<CourseTaken> coursesTaken = new ArrayList<CourseTaken>();
            // Breadth Reqs
            coursesTaken.add(new CourseTaken(courseMap.get("csci5302"), term, Grade.A));
            coursesTaken.add(new CourseTaken(courseMap.get("csci5103"), term, Grade.A));
            coursesTaken.add(new CourseTaken(courseMap.get("csci5619"), term, Grade.A));
            // 10 credits of csci8777
            coursesTaken.add(new CourseTaken(new Course("Thesis Credit: Masters", "csci8777", "10", null), term,
                    Grade.S));
            // Colloquium
            coursesTaken.add(new CourseTaken(courseMap.get("csci8970"), term, Grade.S));
            // At least one 8xxx level course
            coursesTaken.add(new CourseTaken(courseMap.get("csci8101"), term, Grade.A));
            coursesTaken.add(new CourseTaken(courseMap.get("csci8102"), term, Grade.A));
            // Total 31 credits, at least 16 csci
            coursesTaken.add(new CourseTaken(courseMap.get("csci5552"), term, Grade.A));
            coursesTaken.add(new CourseTaken(courseMap.get("csci5131"), term, Grade.A));
            coursesTaken.add(new CourseTaken(courseMap.get("csci5129"), term, Grade.A));
            coursesTaken.add(new CourseTaken(courseMap.get("csci5125"), term, Grade.A));
            coursesTaken.add(new CourseTaken(courseMap.get("csci5607"), term, Grade.A));
            record.setCoursesTaken(coursesTaken);

            // Set needed milestones
            List<MilestoneSet> milestonesSet = new ArrayList<MilestoneSet>();
            milestonesSet.add(new MilestoneSet(Milestone.DPF_SUBMITTED, term));
            milestonesSet.add(new MilestoneSet(Milestone.DPF_APPROVED, term));
            milestonesSet.add(new MilestoneSet(Milestone.THESIS_COMMITTEE_APPOINTED, term));
            milestonesSet.add(new MilestoneSet(Milestone.GRADUATION_PACKET_REQUESTED, term));
            milestonesSet.add(new MilestoneSet(Milestone.THESIS_SUBMITTED, term));
            milestonesSet.add(new MilestoneSet(Milestone.THESIS_APPROVED, term));
            milestonesSet.add(new MilestoneSet(Milestone.DEFENSE_PASSED, term));
            record.setMilestonesSet(milestonesSet);

            record.setNotes(Arrays.asList("note1", "note2", "note3"));

            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            System.out.println(gson.toJson(record));

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Test
    public void createMSBStudentTest() {
        try {
            StudentRecord record = new StudentRecord();
            record.setStudent(new Student("Pete", "Wall", "wall0660"));
            record.setDepartment(Department.COMPUTER_SCIENCE);
            record.setDegreeSought(Degree.MS_B);
            record.setTermBegan(new Term(Semester.FALL, 2010));
            record.setAdvisors(Arrays.asList(new Professor("mat", "heimdahl"), new Professor("nic", "Hopper")));
            record.setCommittee(Arrays.asList(new Professor("mat", "heimdahl"), new Professor("nic", "Hopper")));

            List<Course> courseList = new Gson().fromJson(new FileReader(new File("src/resources/courses.txt")),
                    new TypeToken<List<Course>>() {
                    }.getType());
            Map<String, Course> courseMap = new HashMap<String, Course>();
            for (Course course : courseList) {
                courseMap.put(course.getId(), course);
            }
            Term term = new Term(Semester.SPRING, 2010);

            List<CourseTaken> coursesTaken = new ArrayList<CourseTaken>();
            // Breadth Reqs
            coursesTaken.add(new CourseTaken(courseMap.get("csci5302"), term, Grade.A));
            coursesTaken.add(new CourseTaken(courseMap.get("csci5103"), term, Grade.A));
            coursesTaken.add(new CourseTaken(courseMap.get("csci5619"), term, Grade.A));
            // Plan B project
            coursesTaken.add(new CourseTaken(courseMap.get("csci8760"), term, Grade.S));
            // Colloquium
            coursesTaken.add(new CourseTaken(courseMap.get("csci8970"), term, Grade.S));
            // At least one 8xxx level course
            coursesTaken.add(new CourseTaken(courseMap.get("csci8101"), term, Grade.A));
            coursesTaken.add(new CourseTaken(courseMap.get("csci8102"), term, Grade.A));
            // Total 31 credits, at least 16 csci
            coursesTaken.add(new CourseTaken(courseMap.get("csci5552"), term, Grade.A));
            coursesTaken.add(new CourseTaken(courseMap.get("csci5131"), term, Grade.A));
            coursesTaken.add(new CourseTaken(courseMap.get("csci5129"), term, Grade.A));
            coursesTaken.add(new CourseTaken(courseMap.get("csci5125"), term, Grade.A));
            coursesTaken.add(new CourseTaken(courseMap.get("csci5607"), term, Grade.A));
            record.setCoursesTaken(coursesTaken);

            // Set needed milestones
            List<MilestoneSet> milestonesSet = new ArrayList<MilestoneSet>();
            milestonesSet.add(new MilestoneSet(Milestone.DPF_SUBMITTED, term));
            milestonesSet.add(new MilestoneSet(Milestone.DPF_APPROVED, term));
            milestonesSet.add(new MilestoneSet(Milestone.PROJECT_COMMITTEE_APPOINTED, term));
            milestonesSet.add(new MilestoneSet(Milestone.GRADUATION_PACKET_REQUESTED, term));
            milestonesSet.add(new MilestoneSet(Milestone.DEFENSE_PASSED, term));
            record.setMilestonesSet(milestonesSet);

            record.setNotes(Arrays.asList("note1", "note2", "note3"));

            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            System.out.println(gson.toJson(record));

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Test
    public void createMSCStudentTest() {
        try {
            StudentRecord record = new StudentRecord();
            record.setStudent(new Student("Lucas", "Anderson", "ande2807"));
            record.setDepartment(Department.COMPUTER_SCIENCE);
            record.setDegreeSought(Degree.MS_C);
            record.setTermBegan(new Term(Semester.FALL, 2010));
            record.setAdvisors(Arrays.asList(new Professor("mat", "heimdahl"), new Professor("nic", "Hopper")));
            record.setCommittee(Arrays.asList(new Professor("mat", "heimdahl"), new Professor("nic", "Hopper")));

            List<Course> courseList = new Gson().fromJson(new FileReader(new File("src/resources/courses.txt")),
                    new TypeToken<List<Course>>() {
                    }.getType());
            Map<String, Course> courseMap = new HashMap<String, Course>();
            for (Course course : courseList) {
                courseMap.put(course.getId(), course);
            }
            Term term = new Term(Semester.SPRING, 2010);

            List<CourseTaken> coursesTaken = new ArrayList<CourseTaken>();
            // Breadth Reqs
            coursesTaken.add(new CourseTaken(courseMap.get("csci5302"), term, Grade.A));
            coursesTaken.add(new CourseTaken(courseMap.get("csci5103"), term, Grade.A));
            coursesTaken.add(new CourseTaken(courseMap.get("csci5619"), term, Grade.A));
            // Colloquium
            coursesTaken.add(new CourseTaken(courseMap.get("csci8970"), term, Grade.S));
            // Two 8xxx level courses
            coursesTaken.add(new CourseTaken(courseMap.get("csci8101"), term, Grade.A));
            coursesTaken.add(new CourseTaken(courseMap.get("csci8102"), term, Grade.A));
            // Total 31 credits, at least 16 csci
            coursesTaken.add(new CourseTaken(courseMap.get("csci5552"), term, Grade.A));
            coursesTaken.add(new CourseTaken(courseMap.get("csci5131"), term, Grade.A));
            coursesTaken.add(new CourseTaken(courseMap.get("csci5129"), term, Grade.A));
            coursesTaken.add(new CourseTaken(courseMap.get("csci5125"), term, Grade.A));
            coursesTaken.add(new CourseTaken(courseMap.get("csci5607"), term, Grade.A));
            record.setCoursesTaken(coursesTaken);

            // Set needed milestones
            List<MilestoneSet> milestonesSet = new ArrayList<MilestoneSet>();
            milestonesSet.add(new MilestoneSet(Milestone.DPF_SUBMITTED, term));
            milestonesSet.add(new MilestoneSet(Milestone.DPF_APPROVED, term));
            milestonesSet.add(new MilestoneSet(Milestone.TRACKING_FORM_SUBMITTED, term));
            milestonesSet.add(new MilestoneSet(Milestone.TRACKING_FORM_APPROVED, term));
            milestonesSet.add(new MilestoneSet(Milestone.GRADUATION_PACKET_REQUESTED, term));
            record.setMilestonesSet(milestonesSet);

            record.setNotes(Arrays.asList("note1", "note2", "note3"));

            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            System.out.println(gson.toJson(record));
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
