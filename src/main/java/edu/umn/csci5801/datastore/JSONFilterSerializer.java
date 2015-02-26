package edu.umn.csci5801.datastore;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import edu.umn.csci5801.filter.Filter;

/**
 * A JSON serializer and deserializer. This class is called by Gson's toJson and
 * fromJson methods when a Filter class is encountered. It will serialize the
 * filter type into the JSON and deserialize the filter type's class from the
 * JSON
 */
public class JSONFilterSerializer implements JsonSerializer<Filter>, JsonDeserializer<Filter> {

    // JSON property to store the filter type
    private static final String FILTER_TYPE = "filterType";

    // package name of the filter classes
    private static final String FILTER_TYPE_PACKAGE = "edu.umn.csci5801.filter.";

    /**
     * Deserializes the filter into the filter class specified by the property
     * FILTER_TYPE
     * @see com.google.gson.JsonDeserializer#deserialize(com.google.gson.JsonElement,
     *      java.lang.reflect.Type, com.google.gson.JsonDeserializationContext)
     */
    @Override
    public Filter deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        JsonObject jsonObj = json.getAsJsonObject();
        String className = jsonObj.get(FILTER_TYPE).getAsString();
        try {
            return context.deserialize(json, Class.forName(FILTER_TYPE_PACKAGE + className));
        } catch (ClassNotFoundException e) {
            throw new JsonParseException("Unable to find requirement type " + className, e);
        }
    }

    /**
     * Serializes the requirement into json with the requirement type property
     * @see com.google.gson.JsonSerializer#serialize(java.lang.Object,
     *      java.lang.reflect.Type, com.google.gson.JsonSerializationContext)
     */
    @Override
    public JsonElement serialize(Filter src, Type typeOfSrc, JsonSerializationContext context) {
        JsonElement result = context.serialize(src, src.getClass());
        result.getAsJsonObject().addProperty(FILTER_TYPE, src.getClass().getSimpleName());
        return result;
    }

}
