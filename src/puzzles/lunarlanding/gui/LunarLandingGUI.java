package puzzles.lunarlanding.gui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import puzzles.lunarlanding.model.LunarLandingModel;
import util.Observer;

import java.io.File;

/**
 * DESCRIPTION
 * @author YOUR NAME HERE
 * November 2021
 */
public class LunarLandingGUI extends Application
        implements Observer< LunarLandingModel, Object > {
    private  LunarLandingModel model;
    @Override
    public void init() throws Exception {
        model = new LunarLandingModel();
        model.load(new File(getParameters().getRaw().get(0)));
        model.addObserver(this);
    }

    @Override
    public void start( Stage stage ) throws Exception {
        stage.setTitle( "Tip Over" );

        Button temp = new Button();

        Scene scene = new Scene( temp, 640, 480 );
        stage.setScene( scene );
        stage.show();
    }

    @Override
    public void update( LunarLandingModel lunarLandingModel, Object o ) {
        System.out.println( "My model has changed! (DELETE THIS LINE)");
    }

    public static void main( String[] args ) {

        Application.launch( args );
    }
}
