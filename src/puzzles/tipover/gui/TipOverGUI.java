package puzzles.tipover.gui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import puzzles.tipover.model.TipOverModel;
import util.Observer;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * The GUI of the TipOver game based on the TipOverModel
 *
 * VARIABLES
 * model is the board model of the game
 * grid is the board displayed in GUI
 * text is the text that tells the player what has happened
 * win is a string that tells who won when the game has been solved
 *
 * @author Ming Creekmore mec5765
 * November 2021
 */
public class TipOverGUI extends Application implements Observer< TipOverModel, Object > {
    //instantiate variables
    private TipOverModel model;
    private GridPane grid;
    private Text text;
    private String win;

    /**
     * initialize the model and win
     * @throws Exception
     */
    @Override
    public void init() throws Exception{
        String parameter = getParameters().getUnnamed().toString();
        if(!parameter.equals("[]"))
            try {
                this.model = new TipOverModel(parameter.substring(1, parameter.length() - 1));
            }
            catch (FileNotFoundException e){
                this.model = new TipOverModel();
            }
        else
            this.model = new TipOverModel();
        this.model.addObserver(this);
        win = null;
        System.out.println("init: Initialize and connect to model!");
    }

    /**
     * makes a game gridpane of the board on the left side
     * makes the arrow, load, reload, and hint buttons on the right
     * Sets the text on the top
     * @param stage the stage the user will see and use
     * @throws Exception
     */
    @Override
    public void start( Stage stage ) throws Exception {

        //make new borderpane
        BorderPane borderPane = new BorderPane();
        FileChooser filechooser = new FileChooser();

        //setting up label at the top
        text = new Text("Reach the goal to win!");
        borderPane.setTop(text);
        BorderPane.setAlignment(text, Pos.BOTTOM_LEFT);

        //setting up the game board as a gridpane on the left
        grid = new GridPane();
        display_grid();
        borderPane.setLeft(grid);

        //setting up the right hand side (buttons)
        BorderPane arrows = make_arrow_buttons();
        Button load = new Button("LOAD");
        Button reload = new Button("RELOAD");
        Button hint = new Button("HINT");
        //allow user to choose a file on button click
        load.setOnAction(event -> {
            File file = filechooser.showOpenDialog(stage);
            if (file!=null){
                try{
                    this.model.load(file.getAbsolutePath());
                    text.setText("Reach the goal to win!");
                    win = null;
                }
                catch (FileNotFoundException e){
                    text.setText("Could not find file.");
                }
            }
        });
        //makes next move for user when button is clicked
        hint.setOnAction(event -> {
            if(this.model.getcanMove()){
                text.setText(this.model.hint());
            }
        });
        //reloads last file when clicked
        reload.setOnAction(event-> {
            try{
                this.model.reload();
                text.setText("Reach the goal to win!");
                win = null;
            }
            catch (FileNotFoundException f){
                text.setText("Could not find file.");
            }
            catch (NullPointerException n){
                text.setText("There is no game to reload. Plead load a file.");
            }
        });
        //adding all the buttons to the respective positions in the plane
        VBox rhs = new VBox(arrows,load, reload, hint);
        borderPane.setRight(rhs);

        //set the scene on the stage
        stage.setTitle( "Tip Over" );
        Scene scene = new Scene(borderPane);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    /**
     * Makes the buttons that control the movements of the tipper
     * @return the borderpane of arrow buttons
     */
    private BorderPane make_arrow_buttons(){
        BorderPane arrows = new BorderPane();

        //initializing buttons and putting them in respective places
        Button up = new Button("UP");
        Button down = new Button("DOWN");
        Button left = new Button("LEFT");
        Button right = new Button("RIGHT");
        arrows.setTop(up);
        BorderPane.setAlignment(up, Pos.CENTER);
        arrows.setBottom(down);
        BorderPane.setAlignment(down, Pos.CENTER);
        arrows.setLeft(left);
        arrows.setRight(right);

        //setting up the actions of all the arrow buttons
        up.setOnAction(event -> {
            if(this.model.getcanMove()) {
                if(!this.model.move("north"))
                    text.setText("Cannot move there.");
                else if(win != null)
                    text.setText(win);
                else
                    text.setText("");
            }
        });
        down.setOnAction(event -> {
            if(this.model.getcanMove()) {
                if(!this.model.move("south"))
                    text.setText("Cannot move there");
                else if(win != null)
                    text.setText(win);
                else
                    text.setText("");
            }
        });
        right.setOnAction(event -> {
            if(this.model.getcanMove()) {
                if(!this.model.move("east"))
                    text.setText("Cannot move there");
                else if(win != null)
                    text.setText(win);
                else
                    text.setText("");
            }
        });
        left.setOnAction(event -> {
            if(this.model.getcanMove()) {
                if(!this.model.move("west"))
                    text.setText("Cannot move there");
                else if(win != null)
                    text.setText(win);
                else
                    text.setText("");
            }
        });

        return arrows;
    }

    /**
     * makes the grid based on the model's board
     */
    private void display_grid(){
        for(int i=0; i<model.getBoard().getNRows(); i++){
            for(int j=0; j<model.getBoard().getNCols(); j++){
                Rectangle rect = new Rectangle(30,30);
                rect.setFill(Color.WHITE);
                Text num = new Text(model.getBoard().get(i,j).toString());
                num.setFont(Font.font(24));
                num.setX(9);
                num.setY(23);
                if(model.getTipper()[0]==i && model.getTipper()[1]==j){
                    rect.setFill(Color.CHOCOLATE);
                }
                else if(model.getGoal()[0]==i && model.getGoal()[1]==j){
                    rect.setFill(Color.GOLD);
                }
                Group group = new Group(rect, num);
                grid.add(group, j, i);
            }
        }
    }

    /**
     * replaces the grid with information from the updated model board
     * if the board has been solved, win will be changed to an actual string
     * @param tipOverModel the board model
     * @param o the string to display if the board has been solved (will be null if not solved)
     */
    @Override
    public void update( TipOverModel tipOverModel, Object o ) {
        if(Platform.isFxApplicationThread()){
            grid.getChildren().removeAll();
            display_grid();
            if(o!=null){
                win = (String) o;
            }
        }
        else{
            Platform.runLater(() -> {
                grid.getChildren().removeAll();
                display_grid();
            });
        }
    }

    public static void main( String[] args ) {
        Application.launch( args );
    }
}
