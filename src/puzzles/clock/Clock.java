package puzzles.clock;

import solver.Configuration;
import solver.Solver;

import java.util.List;

/**
 * Main class for the "puzzles.clock" puzzle.
 *
 * @author Lucille Blain
 */
public class Clock {


    /**
     * Run an instance of the puzzles.clock puzzle.
     * @param args [0]: number of hours on the puzzles.clock;
     *             [1]: starting time on the puzzles.clock;
     *             [2]: goal time to which the puzzles.clock should be set.
     */


    public static void main( String[] args ) {
        int hours =0;
        int start = 0;
        int end =0;
        if ( args.length != 3 ) {
            System.out.println( "Usage: java Clock hours start end" );
        }
        else {
            // YOUR MAIN CODE HERE
            hours = Integer.parseInt(args[0]);
            start = Integer.parseInt(args[1]);
            end = Integer.parseInt(args[2]);

        }
        ClockConfig clock = new ClockConfig(hours,start,end);
        Solver s= new Solver(clock);
        List<Configuration> e = s.solve();
        System.out.println("Usage: java Clock "+hours+" "+ start+" " +end);
        System.out.println("Total configs: "+ s.getTotal() );
        System.out.println("Unique configs: "+ s.getUnique());
        if (e == null){
            System.out.println("No Solutions");
        }else{
            for(int i=0;i<e.size();i++){
                System.out.println("Step "+i+": " + e.get(i));
            }

        }

    }

}
