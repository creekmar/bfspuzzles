package puzzles.lunarlanding.model;

import puzzles.lunarlanding.LunarLanding;
import solver.Configuration;
import solver.Solver;
import util.Coordinates;
import util.Grid;
import util.Observer;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

/**
 * DESCRIPTION
 * @author Lucille Blain
 * November 2021
 */
public class LunarLandingModel {


    private List<Observer<LunarLandingModel,Object>> observer;
    Grid<String> grid;
    private LunarLandingConfig lu;
    private  File current;
    private String block;
    private Map<String,Coordinates> positions;
    final private String t ="_";
    private Coordinates goal;
    private Coordinates explorer;

    /**
     * loads thegraph
     */
    public void load(File lfile) throws FileNotFoundException {
        observer =new ArrayList<>();
        current = lfile;
        Scanner scan = new Scanner(lfile);
        String[] gridlines = scan.nextLine().split(" ");
        lu = new LunarLandingConfig(gridlines[1], gridlines[0]);

        lu.setGoal(new Coordinates(Integer.parseInt(gridlines[2]), Integer.parseInt(gridlines[3])));
        goal = new Coordinates(Integer.parseInt(gridlines[2]), Integer.parseInt(gridlines[3]));

        while (scan.hasNextLine()) {
            String[] block = scan.nextLine().split(" ");
            if (block[0].equals("")) break;
            else lu.addBlock(block[0], new Coordinates(Integer.parseInt(block[1]), Integer.parseInt(block[2])));
        }
        grid = lu.getGrid();
        positions = lu.getBlocks();
        explorer = positions.get("E");
    }

    public void hint(){
        Solver s = new Solver(lu);
        List<Configuration> e = s.solve();
        if(!e.isEmpty()) {
            for (int i = 0; i < e.size(); i++) {
                if (grid.equals(e.get(i).getGrid())) {
                    grid = e.get(i + 1).getGrid();
                    break;
                }
            }
        }
        announce(null);



    }
    public void reload() throws FileNotFoundException {
        Scanner scan = new Scanner(current);
        String[] gridlines = scan.nextLine().split(" ");
        lu = new LunarLandingConfig(gridlines[0], gridlines[1]);

        lu.setGoal(new Coordinates(Integer.parseInt(gridlines[2]), Integer.parseInt(gridlines[3])));

        while (scan.hasNextLine()) {
            String[] block = scan.nextLine().split(" ");
            if (block[0].equals("")) break;
            else lu.addBlock(block[0], new Coordinates(Integer.parseInt(block[1]), Integer.parseInt(block[2])));
        }
        grid = lu.getGrid();
        announce(null);

    }
    public void choose(String row, String col){
        Coordinates  c= new Coordinates(Integer.parseInt(row),Integer.parseInt(col));
        if(grid.legalCoords(c) && grid.get(c) != "_") {
            block = grid.get(Integer.parseInt(row), Integer.parseInt(col));
            if (block.contains("!")){
                block = String.valueOf(block.charAt(1));
            }
            System.out.println("Current block: " + block);
        }else System.out.println("Invalid Coordinate");
        announce(null);


    }
    public void go(String direction){
        MoveBlock(block,direction);
        announce(null);
    }

    public void MoveBlock(String a, String dir){
        Coordinates orginal = positions.get(a);
        if(dir.equalsIgnoreCase("N")) {
            for (int i = orginal.row(); i >0; i++) {
                if (positions.containsValue(new Coordinates(i-1, orginal.col()))) {
                    this.grid.set(a, i, orginal.col());
                    this.grid.set(t, orginal);
                    positions.replace(a, new Coordinates(i, i));
                    break;
                }
            }
        }
        if (dir.equalsIgnoreCase( "S")) {
            for (int i = orginal.row(); i <grid.getNRows(); i++) {
                if (positions.containsValue(new Coordinates(i+1, orginal.col()))) {
                    this.grid.set(a, i, orginal.col());
                    this.grid.set(t, orginal);
                    positions.replace(a, new Coordinates(i, i));
                    break;
                }
            }
        }
        if (dir.equalsIgnoreCase("W")) {
            for (int i = orginal.col(); i>0; i--) {
                if (positions.containsValue(new Coordinates(orginal.row(), i -1))) {
                    this.grid.set(a, orginal.row(), i);
                    this.grid.set(t, orginal);
                    positions.replace(a, new Coordinates(orginal.row(), i));
                    break;
                }
            }
        }
        if(dir.equalsIgnoreCase("E")){
            for (int i = orginal.col(); i < grid.getNCols(); i++) {
                if (positions.containsValue(new Coordinates(orginal.row(), i+1))) {
                    this.grid.set(a, orginal.row(), i);
                    this.grid.set(t, orginal);
                    positions.replace(a,new Coordinates(orginal.row(),i));
                    break;
                }
            }
        }

        this.explorer = positions.get("E");
    }
    public void show(){
        int j =0;
        System.out.print("\t");
        for(int i =0;i< grid.getNRows();i++){
            System.out.print(i+" ");
        }
        System.out.print("\n\t");
        for(int i =0;i< grid.getNRows();i++){
            System.out.print("__");
        }
        System.out.println();

        for(int i =0;i< grid.getNRows();i++){
            System.out.print(j+" |");
            for(int c =0;c< grid.getNCols();c++){
                if(goal.equals(new Coordinates(i,c)) && String.valueOf(grid.get(i, c).charAt(1))!="!"){
                   System.out.print(" !");
                   if(grid.get(i, c).equals("_")){
                       System.out.print(" !");
                   }
                }else{
                System.out.print(" "+grid.get(i,c));}
            }
            System.out.println();
            j++;
        }
    }
    public Boolean atGoal(){
        if(goal.equals(positions.get("E")))
        return true;
        else return false;
    }


    public void addObserver (Observer<LunarLandingModel,Object>obs){this.observer.add(obs);}
    public void announce(String arg){
        for(var obs:this.observer){
            obs.update(this,arg);
        }
    }
    /*
     * Code here includes...
     * Additional data variables for anything needed beyond what is in
     *   the config object to describe the current state of the puzzle
     * Methods to support the controller part of the GUI, e.g., load, move
     * Methods and data to support the "subject" side of the Observer pattern
     *
     * WARNING: To support the hint command, you will likely have to do
     *   a cast of Config to LunarLandingConfig somewhere, since the solve
     *   method works with, and returns, objects of type Configuration.
     */
}
