package solver;

import util.Grid;

import java.util.Collection;

/**
 * Configuration abstraction for the solver algorithm
 *
 * @author YOUR NAME HERE
 * November 2021
 */
public interface Configuration {

    // Tips
    // Include methods
    // - for the solver: is-goal, get-successors
    // - for get-successors: a copy constructor (can't declare here)
    // - for equality comparison and hashing
    // - for creating a displayable version the configurationc
    /**
     * Finds possible neighbors/successors
     * @return list of configs
     */
    Collection<Configuration> getSuccessors();
    /**
     * checks if config has solution
     * @return true if correct, false if wrond
     */
    boolean isSolution();
    /**
     * checks if configuration  can get its answe
     * @return true if possible, false, if not
     */
    boolean isValid();

    Grid<String> getGrid();

}
