package puzzles.tipover.model;

import solver.Configuration;
import util.Coordinates;
import util.Coordinates.Direction;
import util.Grid;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * One instance of the board in the game TipOver.
 *
 * STATIC VARIABLES (ONLY CHANGE WHEN NEW PUZZLE)
 * rows is the number of rows on the board (static)
 * columns is the number of columns on the board (static)
 * goal is the position of the goal (static)
 *
 * NORMAL VARIABLES (CHANGE WITH EACH NEW CONFIGURATION)
 * tipper is the position of the man
 * board is the state of the board
 *
 * @author Ming Creekmore mec5765
 * November 2021
 */
public class TipOverConfig implements Configuration {
    //initialize all the variables
    private static int rows;
    private static int columns;
    private static int[] goal;
    private int[] tipper;
    private Grid board;
    private Grid visited;

    /**
     * Reads in a file that creates the initial configuration of the game board
     * @param filename the file that is to be read
     * @throws FileNotFoundException
     */
    public TipOverConfig(String filename) throws FileNotFoundException {
        try (Scanner in = new Scanner(new File(filename))) {
            //getting board size, position of tipper, position of goal and initializing
            String input = in.nextLine();
            String[] fields = input.split("\\s");
            rows = Integer.parseInt(fields[0]);
            columns = Integer.parseInt(fields[1]);
            tipper = new int[]{Integer.parseInt(fields[2]), Integer.parseInt(fields[3])};
            goal = new int[]{Integer.parseInt(fields[4]), Integer.parseInt(fields[5])};
            board = new Grid("thing", rows, columns);
            visited = new Grid(false, rows, columns);

            input = in.nextLine();
            //loop through each line until blank
            int i = 0;
            while(!input.equals("")){
                int j = 0;
                input = input.substring(0, input.length());
                fields = input.split("\\s");
                //loop through the string board representation and assign values to each board coordinate
                for(String s: fields){
                    board.set(s, i, j);
                    visited.set(false, i, j);
                    j++;
                }
                i++;

                //see if can do next line or not
                try {
                    input = in.nextLine();
                }
                catch (NoSuchElementException e){
                    input = "";
                }
            }
            //set initial position as having been visited
            visited.set(true, tipper[0], tipper[1]);
        }
    }

    /**
     * makes a deep copy of the curent configuration
     * @param other configuration that will be copied
     */
    public TipOverConfig(TipOverConfig other){
        tipper = new int[]{other.tipper[0], other.tipper[1]};
        board = new Grid(other.board);
        visited = new Grid(other.visited);
    }

    /**
     * gets the possible next steps that the tipper can take
     * @return list of configurations
     */
    @Override
    public Collection<Configuration> getSuccessors() {
        LinkedList<Configuration> list = new LinkedList();
        //tipper can go in any four cardinal points
        for(Direction d: Coordinates.CARDINAL_NEIGHBORS){
            TipOverConfig temp = move(d);
            if(temp!=null){
                list.add(temp);
            }
        }//end of direction loop
        return list;
    }

    /**
     * Only solved when the tipper has reached the goal
     * @return whether the current configuration is the solution
     */
    @Override
    public boolean isSolution(){
        if(tipper[0]==goal[0] && tipper[1]==goal[1]){
            return true;
        }
        return false;
    }

    /**
     * if tipper is in a place where there is no tower or crate (0) then not valid
     * @return whether configuration is valid or not
     */
    @Override
    public boolean isValid(){
        if(board.get(tipper[0],tipper[1]).equals("0")){
            return false;
        }
        return true;
    }

    @Override
    public Grid<String> getGrid() {
        return null;
    }

    /**
     * determines whether the tipper is on a tower or not
     * If on a tower, then the
     * @return
     */
    public boolean canTip(String direction, int[] pos){
        //the height of the position where tipper is located
        int position = Integer.parseInt(board.get(pos[0], pos[1]).toString());
        //if tipper is on a tower, then see if you can tip it
        if(position>1){
            //determine whether the direction you are tipping is blank
            switch(direction){
                case "NORTH":
                    for(int i = 1; i <= position; i++){
                        if(board.legalCoords(pos[0]-i,pos[1])) {
                            if (!board.get(pos[0] - i, pos[1]).equals("0"))
                                return false;
                        }
                        //if not legal coords
                        else{
                            return false;
                        }
                    }
                    return true;
                case "EAST":
                    for(int i = 1; i <= position; i++){
                        if(board.legalCoords(pos[0], pos[1]+i)) {
                            if (!board.get(pos[0], pos[1] + i).equals("0"))
                                return false;
                        }
                        //if not legal coords
                        else{
                            return false;
                        }
                    }
                    return true;
                case "SOUTH":
                    for(int i = 1; i <= position; i++){
                        if(board.legalCoords(pos[0]+i, pos[1])) {
                            if (!board.get(pos[0] + i, pos[1]).equals("0"))
                                return false;
                        }
                        else{
                            //if not legal coords
                            return false;
                        }
                    }
                    return true;
                case "WEST":
                    for(int i = 1; i <= position; i++){
                        if(board.legalCoords(pos[0], pos[1]-i)) {
                            if (!board.get(pos[0], pos[1] - i).equals("0"))
                                return false;
                        }
                        else{
                            //if not legal coords
                            return false;
                        }
                    }
                    return true;
            }
        }
        return false;
    }

    /**
     * Tip the tower in the direction that the person is going
     * @param direction cardinal direction that the person is going
     * @param tower the coordinates of the tower that will be tipped
     */
    public void tip(String direction, int[] tower) {
        int position = Integer.parseInt(board.get(tower[0], tower[1]).toString());
        board.set("0", tower[0],tower[1]);
        //turning the blank spaces into "crates", signifying the tower if knocked down
        switch (direction) {
            case "NORTH":
                for (int i = 1; i <= position; i++) {
                    board.set("1", tower[0] - i, tower[1]);
                }
                break;
            case "EAST":
                for (int i = 1; i <= position; i++) {
                    board.set("1", tower[0], tower[1] + i);
                }
                break;
            case "SOUTH":
                for (int i = 1; i <= position; i++) {
                    board.set("1", tower[0] + i, tower[1]);
                }
                break;
            case "WEST":
                for (int i = 1; i <= position; i++) {
                    board.set("1", tower[0], tower[1] - i);
                }
                break;
        }
    }

    /**
     * if can be tipped, if there exists another place on the board that has been visited
     * and can be tipped, then reset visited (now another direction that you can move)
     */
    public void isAnotherDirection(){
        for(int i = 0; i<rows; i++){
            for(int j = 0; j<columns; j++){
                if(Boolean.valueOf(visited.get(i, j).toString())){
                    if(Integer.parseInt(board.get(i, j).toString())>1)
                        for(Direction wind: Coordinates.CARDINAL_NEIGHBORS){
                            if(canTip(wind.toString(), new int[]{i, j})){
                                //reset visited
                                for(int n = 0; n<rows; n++)
                                    for(int m = 0; m<columns; m++)
                                        visited.set(false, n, m);
                            }
                        }//end of looping through directions
                }
            }
        }
    }

    /**
     * returns new tipoverconfig with tipper moved in specific direction (for CPU only)
     * uses visited for solving
     * @param d the direction to move toward
     * @return a new tipoverconfig
     */
    public TipOverConfig move(Direction d){
        TipOverConfig temp = new TipOverConfig(this);
        temp.tipper[0] = tipper[0]+d.coords.row();
        temp.tipper[1] = tipper[1]+d.coords.col();
        //only add to next steps if legal coordinates and the place hasn't been visited
        if(temp.board.legalCoords(temp.tipper[0], temp.tipper[1])) {
            if(!Boolean.valueOf(visited.get(temp.tipper[0],temp.tipper[1]).toString())) {
                temp.visited.set(true, temp.tipper[0], temp.tipper[1]);
                //only able to go to 0 if the tower can be tipped in that direction
                if (temp.board.get(temp.tipper[0], temp.tipper[1]).equals("0")) {
                    if (canTip(d.toString(), tipper)) {
                        temp.tip(d.toString(), tipper);
                        temp.isAnotherDirection();
                        return temp;
                    }//end of tip if statement
                } else {
                    //jump to another tower
                    return temp;
                }
            }
        }
        return null;
    }

    /**
     * returns new tipoverconfig with tipper moved in specific direction (for player only)
     * does not use visited since not solving on configuration
     * @param d direction to go
     * @return the new configuration of player moved in that direction
     */
    public TipOverConfig player_move(Direction d){
        TipOverConfig temp = new TipOverConfig(this);
        temp.tipper[0] = tipper[0]+d.coords.row();
        temp.tipper[1] = tipper[1]+d.coords.col();
        //only add to next steps if legal coordinates and the place hasn't been visited
        if(temp.board.legalCoords(temp.tipper[0], temp.tipper[1])) {
            //only able to go to 0 if the tower can be tipped in that direction
            if (temp.board.get(temp.tipper[0], temp.tipper[1]).equals("0")) {
                if (canTip(d.toString(), tipper)) {
                    temp.tip(d.toString(), tipper);
                    temp.isAnotherDirection();
                    return temp;
                }//end of tip if statement
            } else {
                //jump to another tower
                return temp;
            }
        }
        return null;
    }

    /**
     * get the board (specifically for the model to give to gui)
     * @return board
     */
    public Grid getBoard(){
        return board;
    }

    /**
     * get position of the tipper (specifically for the model to give to gui)
     * @return tipper
     */
    public int[] getTipper() { return tipper; }

    /**
     * get position of goal (specifically for model to give to gui)
     * @return goal
     */
    public int[] getGoal() {return goal; }

    /**
     * Display the board in specific format
     * @return the board as a string
     */
    @Override
    public String toString(){
        //start with first line and display the number of columns
        String displayboard = "    ";
        for(int i = 0; i<columns; i++){
            displayboard += " " + i + " ";
        }

        //make line of underscores
        displayboard += "\n\t";
        for(int i = 0; i<columns; i++){
            displayboard += "___";
        }
        displayboard += "\n";

        //print out each row (the values in them too; basically display board)
        for(int i = 0; i<rows; i++){
            displayboard += "" + i + " | ";
            for(int j = 0; j<columns; j++){
                //whether this is where tipper or goal is
                if(tipper[0]==i && tipper[1]==j)
                    displayboard += "*";
                else if(goal[0]==i && goal[1]==j)
                    displayboard += "!";
                else
                    displayboard += " ";

                //print out value
                String value = board.get(i, j).toString();
                if(value.equals("0"))
                    displayboard += "_";
                else
                    displayboard += value;
                displayboard +=" ";
            }
            //go to next row
            displayboard += "\n";
        }

        return displayboard;
    }
}
