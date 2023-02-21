package puzzles.tipover.ptui;

import puzzles.tipover.model.TipOverModel;
import util.Observer;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Text game of TipOver based on the TipOverModel
 *
 * VARIABLES
 * model is the board model of the game
 *
 * @author Ming Creekmore mec5765
 * November 2021
 */
public class TipOverPTUI implements Observer<TipOverModel, Object> {
    //initialize variables
    private TipOverModel model;

    /**
     * Constructor makes a new TipOverPTUI with a TipOverModel given the file
     * @param file
     */
    public TipOverPTUI(String file){
        //if there is no file, then have null model
        //cannot move
        if(file.equals("")){
            model = new TipOverModel();
        }
        //if there is a file, then make a new model based on the file
        else{
            try {
                model = new TipOverModel(file);
                update(model, null);
            }
            catch (FileNotFoundException e){
                System.out.println("File could not be found!");
            }
        }
        //add this as an observer
        model.addObserver(this);
    }

    /**
     * Running the game commands until quit
     */
    private void run() {
        Scanner in = new Scanner( System.in );
        for ( ; ; ) {
            System.out.print( "game command: " );
            String line = in.nextLine();
            String[] words = line.split( "\\s+" );
            if ( words.length > 0 ) {
                //quits game
                if ( words[ 0 ].startsWith( "q" ) ) {
                    break;
                }
                //loads new game
                else if ( words[ 0 ].startsWith( "l" ) ) {
                    if(words.length>1) {
                        try {
                            this.model.load(words[1]);
                        } catch (FileNotFoundException e) {
                            System.out.println("File could not be found");
                        }
                    }
                    else{
                        displayHelp();
                    }
                }
                //reloads last game
                else if ( words[ 0 ].startsWith( "r" ) ) {
                    try{
                        this.model.reload();
                    }
                    catch (FileNotFoundException e){
                        System.out.println("File could not be found");
                    }
                    catch (NullPointerException n){
                        System.out.println("There is no file in the system to reload. Please load a new file.");
                    }
                }
                //CPU makes next move unless already won
                else if ( words[ 0 ].startsWith( "h" ) ) {
                    if(this.model.getcanMove())
                        System.out.println(this.model.hint());
                    else
                        System.out.println("You have already reached the goal!");
                }
                //show the current state of the board
                else if ( words[ 0 ].startsWith( "s" ) ) {
                    System.out.println(this.model.show());
                }
                //player moves the tipper in a specified direction unless already won
                else if ( words[ 0 ].startsWith( "m" ) ) {
                    if(this.model.getcanMove()) {
                        if(words.length>1) {
                            if (!this.model.move(words[1])) {
                                System.out.println("Illegal move");
                            }
                        }
                        else{
                            displayHelp();
                        }
                    }
                    else{
                        System.out.println("You have already reached the goal!");
                    }
                }
                //display all the commands
                else {
                    displayHelp();
                }
            }
        }
    }

    /**
     * displays all possible game commands and what they do
     */
    private void displayHelp() {
        System.out.println( "m(ove) {north, east, south, west}  -- move tipper in direction" );
        System.out.println( "h(int)      == let the CPU make the next move toward the goal for you");
        System.out.println( "s(how)      -- show the current state of the board");
        System.out.println( "l(oad) filename      -- load new file (start a new game)" );
        System.out.println( "r(eload)      -- reload game (restart)" );
        System.out.println( "q(uit)     -- quit the game" );
    }

    /**
     * displays updated model to the player
     * @param o the board model
     * @param arg whether or not you have won
     */
    public void update(TipOverModel o, Object arg){
        System.out.println(this.model.show());
        if(arg != null){
            System.out.println(arg);
        }
    }


    public static void main( String[] args ) {
        TipOverPTUI ptui;
        if(args.length==1){
            ptui = new TipOverPTUI(args[0]);
        }
        else{
            ptui = new TipOverPTUI("");
        }

        ptui.run();
    }
}
