package edu.umn.csci5801.datastore;

import java.util.List;

import edu.umn.csci5801.exception.GRADSException;
import edu.umn.csci5801.exception.GRADSIllegalArgumentException;
import edu.umn.csci5801.model.Degree;
import edu.umn.csci5801.model.Department;
import edu.umn.csci5801.requirement.Requirement;

/**
 * An interface for managing requirements persistence
 */
public interface RequirementRepository {
    /**
     * Find the list of requirements for a specific degree and department
     * @param degree The degree
     * @param department The department
     * @return The list of requirements or an empty list if no requirements exist for the
     *         specified degree and department
     * @throws GRADSIllegalArgumentException
     */
    List<Requirement> find(Degree degree, Department department) throws GRADSIllegalArgumentException;

    /**
     * Save the requirement for the specified degree and department
     * @param requirement The requirement to save
     * @param degree The degree the requirement is associated with
     * @param department The department the requirement is associated with
     * @throws GRADSException
     */
    void save(Requirement requirement, Degree degree, Department department) throws GRADSException;
}
