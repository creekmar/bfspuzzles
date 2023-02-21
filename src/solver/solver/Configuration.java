package solver.solver;

import java.util.Collection;

/**
 * Configuration abstraction for the solver algorithm
 *
 * @author Lucille Blain
 */
public interface Configuration {



    /*
     * List here the methods that the configurations of all the
     * puzzles must implement.
     * The project writeup explains that there are other acceptable designs,
     * so use of this interface is not required. However, for full design
     * credit, use of a shared solver that requires the implementation of
     * a certain abstraction from all puzzles is required.
     */
    /**
     * Finds possible neighbors/successors
     * @return list of configs
     */
    public Collection<solver.Configuration> getSuccessors();
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



}
