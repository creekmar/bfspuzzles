package puzzles.tipover;

import puzzles.tipover.model.TipOverConfig;
import solver.Configuration;
import solver.Solver;
import util.Coordinates;

import java.io.FileNotFoundException;
import java.util.List;

/**
 * DESCRIPTION
 * @author Ming Creekmore mec5765
 * November 2021
 */
public class TipOver {

    /*
     * code to read the file name from the command line and
     * run the solver on the puzzle
     */

    public static void main( String[] args ) throws FileNotFoundException {
        if(args.length!=1){
            System.err.println( "Usage: java TipOver file" );
        }
        else {
            try {
                System.out.println("Puzzle " + args[0]);
                //create the initial config from the file
                TipOverConfig init = new TipOverConfig(args[0]);

                //making solver to solve the puzzle
                Solver solver = new Solver(init);
                List<Configuration> e = solver.solve();

                //giving output to user for how to solve puzzle
                System.out.println("Total Configs: " + solver.getTotal());
                System.out.println("Unique Configs: " + solver.getUnique());
                if (e == null) {
                    System.out.println("No Solutions");
                } else {
                    for (int i = 0; i < e.size(); i++) {
                        System.out.println("Step " + i + ": \n" + e.get(i));
                    }
                }
            }
            catch (FileNotFoundException e){
                System.out.println("Incorrect file name. Usage: java TipOver file");
            }
        }

    }
}
