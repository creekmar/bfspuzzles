package puzzles.lunarlanding;

import puzzles.lunarlanding.model.LunarLandingConfig;
import solver.Configuration;
import solver.Solver;
import util.Coordinates;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * DESCRIPTION
 * @author Lucille Blain
 * November 2021
 */
public class LunarLanding {

    /*
     * code to read the file name from the command line and
     * run the solver on the puzzle
     */


    public static void main(String[] args) throws FileNotFoundException {
        File lfile = new File(args[0]);
        Scanner scan = new Scanner(lfile);
        String[] gridlines = scan.nextLine().split(" ");
        LunarLandingConfig lu = new LunarLandingConfig(gridlines[0], gridlines[1]);

        lu.setGoal(new Coordinates(Integer.parseInt(gridlines[2]), Integer.parseInt(gridlines[3])));

        while (scan.hasNextLine()) {
            String[] block = scan.nextLine().split(" ");
            if (block[0].equals("")) break;
                else lu.addBlock(block[0], new Coordinates(Integer.parseInt(block[1]), Integer.parseInt(block[2])));
        }
        Solver s = new Solver(lu);
        List<Configuration> e = s.solve();
        if (e == null) {
            System.out.println("No Solutions");
        } else {
            for (int i = 0; i < e.size(); i++) {
                System.out.println("Step " + (i) + ":\n" + e.get(i));
            }

        }

    }
}
