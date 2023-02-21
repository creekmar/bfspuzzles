package puzzles.water;

import solver.Configuration;
import solver.Solver;

import java.util.List;

/**
 * Main class for the water buckets puzzle.
 *
 * @author Lucille Blain
 */
public class Water {

    /**
     * Run an instance of the water buckets puzzle.
     * @param args [0]: desired amount of water to be collected;
     *             [1..N]: the capacities of the N available buckets.
     */
    public static void main( String[] args ) {
        if ( args.length < 2 ) {
            System.out.println(
                    ( "Usage: java Water amount bucket1 bucket2 ..." )
            );
        }
        else {
            int amount = Integer.parseInt(args[0]);
            WaterConfig w = new WaterConfig(amount);
            String header = "Usage: java Water " + amount;
             for( int i =1; i< args.length;i++){
                 header = header + " " + args[i];
                // System.out.println(new Bucket(Integer.parseInt(args[i])));
                 w.addBucket(new Bucket(Integer.parseInt(args[i])));
             }
             Solver s = new Solver(w);
             List<Configuration> e = s.solve();
             System.out.println(header);
             System.out.println("Total configs: "+ s.getTotal() );
             System.out.println("Unique configs: "+ s.getUnique());

             if (e ==null){

                 System.out.println("No Solution!");
             } else{
                 for(int i=0;i<e.size();i++){
                     System.out.println("Step "+i+": " + e.get(i));
                 }
             }


        }
    }
}
