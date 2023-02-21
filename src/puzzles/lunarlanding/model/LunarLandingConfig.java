package puzzles.lunarlanding.model;

import solver.Configuration;
import util.Coordinates;
import util.Grid;

import java.util.*;

/**
 * DESCRIPTION
 * @author Lucille Blain
 * November 2021
 */
public class LunarLandingConfig implements Configuration {
    private Grid<String> grid; // gird of the current onfig(helps get visual of what is happening)
    private Map<String, Coordinates> blocks; //map of blocks + their current coordinates
    private Coordinates goal; // block that is the goal
    private Coordinates explorer; //explore in blocks,but held seperatly
    private String current;
    private Boolean possible;
    //get sucessor for specific movement
    //get which figure is moving
    //get what direction is moving
    record Letter( char val ) {}
    final private String t ="_";

    public LunarLandingConfig(String h,String w){
        blocks = new HashMap<String, Coordinates>();
        possible = true;
        grid = new Grid<String>(t,Integer.parseInt(h),Integer.parseInt(w));
    }

    public Map<String, Coordinates> getBlocks() {
        return blocks;
    }

    private LunarLandingConfig(LunarLandingConfig other, String direction, String current){
        this.blocks = new HashMap<String, Coordinates>();
        this.grid = new Grid<>(other.grid);
        for (String a : other.blocks.keySet()){
            this.blocks.put(a, other.blocks.get(a));
        }
        this.goal = other.goal;

        this.current = current;
        if(direction == "E"){
            possible = checkEast(current);
            if(possible==true)
                MoveBlock(current,direction);
        }
        if (direction == "W"){
            possible = checkWest(current);
            if(possible == true)
                MoveBlock(current,direction);
        }
        if(direction == "N"){
            possible = checkNorth(current);
            if(possible == true)
                MoveBlock(current,direction);
        }

        if (direction == "S"){
            possible = checkSouth(current);
            if(possible)
                MoveBlock(current,direction);
        }
        this.explorer = blocks.get("E");
        for(String n : blocks.keySet()){
            if(blocks.get(n).equals(goal)) {
                grid.set("!" + n, goal);
                break;
            }else grid.set("!",goal);
        }

    }

    public void setBlocks(Map<String, Coordinates> blocks) {
        this.blocks = blocks;
    }


    public void addBlock(String na, Coordinates coord){

        blocks.put(na,coord);

        if(blocks.get(na).equals(goal)) {
            grid.set("!" + na, goal);

        }else grid.set(na,coord);
        if(na == "E")
            setExplorer(coord);
    }

    public Grid<String> getGrid() {
        return grid;
    }
    public void setGoal(Coordinates goal) {
        this.goal = goal;

        grid.set("!",goal);
    }
    public void setExplorer(Coordinates e){
        explorer =e;
    }

    @Override
    public Collection<Configuration> getSuccessors() {
        List<Configuration> list = new LinkedList<Configuration>();
        for(String a: blocks.keySet()){
            list.add(new LunarLandingConfig(this,"N",a));
            list.add(new LunarLandingConfig(this,"S",a));
            list.add(new LunarLandingConfig(this,"E",a));
            list.add(new LunarLandingConfig(this,"W",a));
        }
        return list;
    }
    public void MoveBlock(String a, String dir){
        Coordinates orginal = blocks.get(a);
        if(dir == "N") {
            for (int i = blocks.get(a).col(); i > 0; i--) {
                if (blocks.containsValue(new Coordinates(blocks.get(a).row(), i-1))) {
                    //this.grid.set(a, orginal.row(), i);
                    //this.grid.set(t, orginal);
                    blocks.replace(a,new Coordinates(orginal.row(),i));
                    break;
                }
            }
        }
        if(dir=="S"){
            for (int i = blocks.get(a).col(); i < grid.getNCols()-1; i++) {
                Coordinates x = new Coordinates(blocks.get(a).row(), i+1);
                if (blocks.containsValue(x)) {
                    //this.grid.set(a, orginal.row(), i);
                    //this.grid.set(t, orginal);
                    blocks.replace(a,new Coordinates(orginal.row(),i));
                    break;
                }
            }

        }
        if(dir == "W"){
            for(int i = blocks.get(a).row();i>0;i--){
                if(blocks.containsValue(new Coordinates(i-1,blocks.get(a).col()))){
                    //this.grid.set(a,i, orginal.col());
                    //this.grid.set(t, orginal);
                    blocks.replace(a,new Coordinates(i,orginal.col()));
                    break;
                }
            }
        }
        if(dir == "E"){
            for(int i = blocks.get(a).row();i< grid.getNRows()-1;i++){
                if(blocks.containsValue(new Coordinates(i+1,blocks.get(a).col()))){
                    //this.grid.set(a,i, orginal.col());
                    //this.grid.set(t, orginal);
                    blocks.replace(a,new Coordinates(i,orginal.col()));
                    break;
                }
            }
        }

        for(int i =0; i< grid.getNRows();i++){
            for(int j =0; j < grid.getNCols();j++){
                if(new Coordinates(i,j) != goal) {
                    grid.set(t, i, j);
                }else grid.set("!",goal);
            }
        }
        for(String e: blocks.keySet()){
            grid.set(e,blocks.get(e));
        }
    }
    //checks to see if there is a block in possible direction
    public boolean checkNorth(String a){
        for(int i = blocks.get(a).col()-1;i>=0;i--){
            if(grid.legalCoords(blocks.get(a).row(),i)){
                if(blocks.containsValue(new Coordinates(blocks.get(a).row(),i-1))){
                    return true;
                }
            }else return false;
        }
        return false;
    }
    public boolean checkSouth(String a){

        for(int i = blocks.get(a).col();i< grid.getNCols();i++){
            if(grid.legalCoords(blocks.get(a).row(),i)){
                if(blocks.containsValue(new Coordinates(blocks.get(a).row(),i+1))){
                    return true;
                }
            }else return false;
        }
        return false;
    }
    public boolean checkEast(String a){
        for(int i = blocks.get(a).row()+1;i< grid.getNRows();i++){
            if(grid.legalCoords(i,blocks.get(a).col())){
                if(blocks.containsValue(new Coordinates(i-1,blocks.get(a).col()))){
                    return true;
                }
            }else return false;
        }
        return false;
    }
    public boolean checkWest(String a){
        for(int i = blocks.get(a).row()-1;i>=0;i--){
            if(grid.legalCoords(i,blocks.get(a).col())){
                if(blocks.containsValue(new Coordinates(i-1,blocks.get(a).col()))){
                    return true;
                }
            }else return false;
        }
        return false;
    }

    public boolean isSolution() {
        if(goal.equals(explorer))
            return true;
        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LunarLandingConfig)) return false;
        LunarLandingConfig that = (LunarLandingConfig) o;
        return Objects.equals(grid, that.grid) && Objects.equals(blocks, that.blocks) && Objects.equals(goal, that.goal) && Objects.equals(explorer, that.explorer) && Objects.equals(current, that.current) && Objects.equals(possible, that.possible) && Objects.equals(t, that.t);

    }


    @Override
    public int hashCode() {
        return Objects.hash(grid);
    }

    //check if movement is valid
    @Override
    public boolean isValid() {
        if(this.possible ==false)
            return false;
        return true;
    }

    @Override
    public String toString() {
        return grid.toString();
    }
}
