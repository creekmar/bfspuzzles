package puzzles.tipover.model;

import solver.Configuration;
import solver.Solver;
import util.Coordinates;
import util.Grid;
import util.Observer;

import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.List;

/**
 * A Model of the TipOver game (like the board to play the game)
 *
 * VARIABLES
 * game is the current state of the game
 * observers is the list of objects watching the game
 * file is the name of the last file to be loaded in
 * canMove determines whether the player can make moves (null file or already won)
 *
 * @author Ming Creekmore mec5765
 * November 2021
 */
public class TipOverModel {
    //initialize variables
    private TipOverConfig game;
    private List<Observer<TipOverModel, Object>> observers;
    private String file;
    private boolean canMove;

    /**
     * Constructor when there is a filename in the arguments
     * @param filename file that is used to determine initial state of the game
     */
    public TipOverModel(String filename) throws FileNotFoundException{
        canMove = false;
        observers = new LinkedList<>();
        file = filename;
        game = new TipOverConfig(file);
        canMove = true;
    }

    /**
     * Constructor when there is no filename in arguments
     * Game will be null
     */
    public TipOverModel() {
        canMove = false;
        game = null;
        file = null;
        System.out.println();
        observers = new LinkedList<>();
    }

    /**
     * Loads a new game with a new file
     * @param filename file with initial state of game
     */
    public void load(String filename) throws FileNotFoundException, NullPointerException{
        file = filename;
        game = new TipOverConfig(file);
        canMove = true;
        announce(false);
    }

    /**
     * loads in the most recently successfully loaded file
     */
    public void reload() throws FileNotFoundException, NullPointerException{
        load(file);
    }

    /**
     * Moves the tipper in specified direction unless it is illegal
     * @param direction is the direction that the tipper moves
     * @return whether the move is legal or illegal
     */
    public boolean move(String direction){
        TipOverConfig temp;
        switch (direction){
            case "north":
                temp = game.player_move(Coordinates.CARDINAL_NEIGHBORS[0]);
                if(temp!=null){
                    game = temp;
                    announce(false);
                    return true;
                }
                break;
            case "east":
                temp = game.player_move(Coordinates.CARDINAL_NEIGHBORS[1]);
                if(temp!=null){
                    game = temp;
                    announce(false);
                    return true;
                }
                break;
            case "south":
                temp = game.player_move(Coordinates.CARDINAL_NEIGHBORS[2]);
                if(temp!=null){
                    game = temp;
                    announce(false);
                    return true;
                }
                break;
            case "west":
                temp = game.player_move(Coordinates.CARDINAL_NEIGHBORS[3]);
                if(temp!=null){
                    game = temp;
                    announce(false);
                    return true;
                }
                break;
            //if the direction is not a direction, then output the directions it can move
            //returns true because the move can't be illegal if the direction doesn't exist
            default:
                System.out.println("Not a direction. Can move: north, east, south, west");
                return true;
        }
        return false;
    }

    /**
     * returns the state of the current board as a string
     * @return the current game board
     */
    public String show(){
        return game.toString();
    }

    /**
     * gets the board (for gui)
     * @return board
     */
    public Grid getBoard(){
        return game.getBoard();
    }

    /**
     * gets tipper (for gui)
     * @return game's tipper position
     */
    public int[] getTipper(){
        return game.getTipper();
    }

    /**
     * gets goal (for gui)
     * @return game's goal position
     */
    public int[] getGoal(){
        return game.getGoal();
    }

    /**
     * gets canMove
     * @return canMove
     */
    public boolean getcanMove(){
        return canMove;
    }

    /**
     * makes the next move for the player that brings the game closer to the goal
     */
    public String hint(){
        Solver solver = new Solver(game);
        List<Configuration> e = solver.solve();
        if(e!= null) {
            game = (TipOverConfig) e.get(1);
            announce(true);
            return "I made a move";
        }
        else{
            return "This puzzle is unsolvable";
        }
    }

    /**
     * adds an object to observe the model when it changes
     * @param obs the observer
     */
    public void addObserver(Observer<TipOverModel, Object> obs){
        this.observers.add(obs);
    }

    /**
     * announce to observers that the model has changed
     * @param hint whether the cpu made a move or it was the player
     */
    private void announce(boolean hint){
        String win;

        if(game.isSolution()){
            if(hint){
                win = "I WIN";
            }
            else {
                win = "YOU WIN";
            }
            canMove = false;
        }
        else{
            win = null;
        }

        for (var obs: this.observers){
            obs.update(this, win);
        }
    }
}
