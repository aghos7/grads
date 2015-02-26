package edu.umn.csci5801.datastore;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import edu.umn.csci5801.exception.GRADSDatastoreException;
import edu.umn.csci5801.filter.Filter;
import edu.umn.csci5801.requirement.Requirement;

/**
 * Factory for constructing Requirement objects
 */
public class RequirementFactory {

    // the package of requirement classes
    private static final String REQUIREMENT_TYPE_PACKAGE = "edu.umn.csci5801.requirement.";

    // a Gson object which will do the parsing of JSON into the Requirement
    private static final Gson gson = new GsonBuilder().setPrettyPrinting()
            .registerTypeAdapter(Filter.class, new JSONFilterSerializer()).create();

    /**
     * Gets the Gson object which can parse Requirement objects
     * @return
     */
    public static Gson getRequirementGson() {
        return gson;
    }

    /**
     * Returns a Requirement of the given type populated by the data in the
     * JsonObject data
     * @param type the type of requirement represented by the data
     * @param data the json details of the requirement object
     * @return
     * @throws GRADSDatastoreException
     */
    public Requirement createRequirement(String type, JsonObject data) throws GRADSDatastoreException {

        try {
            @SuppressWarnings("unchecked")
            Class<? extends Requirement> c = (Class<? extends Requirement>) Class.forName(REQUIREMENT_TYPE_PACKAGE
                    + type);
            return getRequirementGson().fromJson(data, c);
        } catch (ClassNotFoundException e) {
            throw new GRADSDatastoreException("Could not find requiement type " + type + " - " + e.getMessage());
        }
    }
}
