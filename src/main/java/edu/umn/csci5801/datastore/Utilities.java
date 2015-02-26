package edu.umn.csci5801.datastore;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;

/**
 * Some common utilities for datastores
 */
public abstract class Utilities {
    /**
     * Clone an object by serializing it and deserializing. This will not work
     * for generic types like List<?>. For lists use cloneListThroughJson
     * @param t The object to clone
     * @return A copy of the object
     */
    @SuppressWarnings("unchecked")
    public static <T> T cloneThroughJson(T t) {
        if (t == null) {
            return null;
        }
        Gson gson = RequirementFactory.getRequirementGson();
        String json = gson.toJson(t);
        return (T) gson.fromJson(json, t.getClass());
    }

    /**
     * Clone a list of objects by serializing and deserializing the objects of
     * the list.
     * @param listOfT The list to clone
     * @return A copy of the list
     */
    public static <T> List<T> cloneListThroughJson(List<T> listOfT) {
        if (listOfT == null) {
            return null;
        }
        List<T> clonedList = new ArrayList<T>(listOfT.size());
        for (T t : listOfT) {
            clonedList.add(cloneThroughJson(t));
        }
        return clonedList;
    }
}
