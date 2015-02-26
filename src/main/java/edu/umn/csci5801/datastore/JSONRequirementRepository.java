package edu.umn.csci5801.datastore;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import edu.umn.csci5801.exception.GRADSDatastoreException;
import edu.umn.csci5801.exception.GRADSException;
import edu.umn.csci5801.exception.GRADSIllegalArgumentException;
import edu.umn.csci5801.model.Degree;
import edu.umn.csci5801.model.Department;
import edu.umn.csci5801.requirement.Requirement;

/**
 * An implementation of the RequirementRepository interface which uses a JSON
 * file to persist requirements.
 */
public class JSONRequirementRepository implements RequirementRepository {

    // JSON properties for the JSON requirements file
    private static final String DEGREE_PROP = "degree";
    private static final String DEPARTMENT_PROP = "department";
    private static final String REQUIREMENT_TYPE_PROP = "requirementType";
    private static final String REQUIREMENT_PROP = "requirement";

    // save the path to the requirements json file so we can save requirements
    // back to the file
    private final String requirementsJsonFilePath;

    // a map of the degree|department to their list of requirements, used for
    // fast lookup of requirements
    private Map<String, List<Requirement>> requirements;

    // saves a copy of the original json list of requirements to assist
    // persisting them later
    private JsonArray persistedRequirements;

    // the factory for creating requirements
    private final RequirementFactory requirementFactory;

    /**
     * Constructs a new requirements repository with the give JSON file
     * @param jsonFilePath path to the JSON file
     * @throws GRADSException if the file cannot be read or has invalid data
     */
    public JSONRequirementRepository(String jsonFilePath) throws GRADSException {
        if (jsonFilePath == null) {
            throw new GRADSIllegalArgumentException(
                    "jsonFilePath must not be null when constructing a JSONRequirementsRepository");
        }
        this.requirementsJsonFilePath = jsonFilePath;
        this.requirementFactory = new RequirementFactory();
        loadRequirements(this.requirementsJsonFilePath);
    }

    /**
     * Loads the requirements from the given json file into a map for fast
     * retrieval
     * @param jsonFilePath path to the requirement json file
     * @throws GRADSDatastoreException if the file cannot be read or contains
     *         invalid data
     */
    private void loadRequirements(String jsonFilePath) throws GRADSDatastoreException {
        try {
            // read the JSON file into a List of Requirements

            this.persistedRequirements = new JsonParser().parse(new FileReader(new File(jsonFilePath)))
                    .getAsJsonArray();
        } catch (Exception e) {
            throw new GRADSDatastoreException("Error reading JSON file " + jsonFilePath + " - " + e.getMessage());
        }

        // loop through the requirements, validate each one, and add them to
        // the map
        this.requirements = new HashMap<String, List<Requirement>>();
        for (JsonElement jsonElement : this.persistedRequirements) {
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            Degree degree = Degree.valueOf(jsonObject.get(DEGREE_PROP).getAsString());
            Department department = Department.valueOf(jsonObject.get(DEPARTMENT_PROP).getAsString());
            String requirementType = jsonObject.get(REQUIREMENT_TYPE_PROP).getAsString();
            JsonObject requirementJson = jsonObject.get(REQUIREMENT_PROP).getAsJsonObject();
            if (degree == null) {
                throw new GRADSDatastoreException("A valid " + DEGREE_PROP
                        + " property is required for each json requirement");
            }
            if (department == null) {
                throw new GRADSDatastoreException("A valid " + DEPARTMENT_PROP
                        + " property is required for each json requirement");
            }
            if (requirementType == null) {
                throw new GRADSDatastoreException("A valid " + REQUIREMENT_TYPE_PROP
                        + " property is required for each json requirement");
            }
            if (requirementJson == null) {
                throw new GRADSDatastoreException("A valid " + REQUIREMENT_PROP
                        + " property is required for each json requirement");
            }

            String degreeDepartmentKey = getDegreeDepartmentKey(degree, department);
            List<Requirement> requirementsForDegreeDept;
            // see if the degree|department already exists in the map
            if (this.requirements.containsKey(degreeDepartmentKey)) {
                requirementsForDegreeDept = this.requirements.get(degreeDepartmentKey);
            } else {
                requirementsForDegreeDept = new ArrayList<Requirement>();
                this.requirements.put(degreeDepartmentKey, requirementsForDegreeDept);
            }

            Requirement requirement = this.requirementFactory.createRequirement(requirementType, requirementJson);

            requirementsForDegreeDept.add(requirement);
        }
    }

    /**
     * returns a unique string key for the given degree and department which can
     * be used as a map key
     * @param degree the degree of the key
     * @param department the department of the key
     * @return the unique string
     */
    private String getDegreeDepartmentKey(Degree degree, Department department) {
        return degree.toString() + "|" + department.toString();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Requirement> find(Degree degree, Department department) throws GRADSIllegalArgumentException {
        if (degree == null) {
            throw new GRADSIllegalArgumentException("degree must not be null when finding a requirement");
        }
        if (department == null) {
            throw new GRADSIllegalArgumentException("department must not be null when finding a requirement");
        }
        String degreeDepartmentKey = getDegreeDepartmentKey(degree, department);
        if (this.requirements.containsKey(degreeDepartmentKey)) {
            return Utilities.cloneListThroughJson(this.requirements.get(degreeDepartmentKey));
        } else {
            return new ArrayList<Requirement>();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void save(Requirement requirement, Degree degree, Department department) throws GRADSException {
        if (degree == null) {
            throw new GRADSIllegalArgumentException("degree must not be null when saving a requirement");
        }
        if (department == null) {
            throw new GRADSIllegalArgumentException("department must not be null when saving a requirement");
        }
        if (requirement == null) {
            throw new GRADSIllegalArgumentException("requirement must not be null when saving a requirement");
        }

        JsonObject persistedRequirement = new JsonObject();
        persistedRequirement.addProperty(DEGREE_PROP, degree.toString());
        persistedRequirement.addProperty(DEPARTMENT_PROP, department.toString());
        persistedRequirement.addProperty(REQUIREMENT_TYPE_PROP, requirement.getClass().getSimpleName());

        JsonObject requirementJson = this.requirementFactory.getRequirementGson().toJsonTree(requirement)
                .getAsJsonObject();
        persistedRequirement.add(REQUIREMENT_PROP, requirementJson);
        this.persistedRequirements.add(persistedRequirement);

        // create the json text
        String jsonText = RequirementFactory.getRequirementGson().toJson(this.persistedRequirements);
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(this.requirementsJsonFilePath);
            fileWriter.write(jsonText);
            fileWriter.close();
        } catch (IOException e) {
            throw new GRADSDatastoreException("Error writing JSON file " + this.requirementsJsonFilePath + " - "
                    + e.getMessage());
        }

        // reload the requirements from the newly written requirements
        loadRequirements(this.requirementsJsonFilePath);

    }
}
