package puzzles.lunarlanding.ptui;

import javafx.stage.FileChooser;
import puzzles.lunarlanding.model.LunarLandingConfig;
import puzzles.lunarlanding.model.LunarLandingModel;
import util.Observer;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * DESCRIPTION
 * @author Lucille Blain
 * November 2021
 */
public class LunarLandingPTUI implements Observer<LunarLandingModel,Object> {

    private LunarLandingModel model;


    public LunarLandingPTUI(File f) throws FileNotFoundException {
        this.model = new LunarLandingModel();
        model.load(f);
        initializeView();
    }
    public void run() throws FileNotFoundException {

        Scanner scan = new Scanner(System.in);
        for(;;) {
            System.out.println("What is your command?(Type help for Command list)");
            String line = scan.nextLine();
            String[] words = line.split("\\s");
            model.announce(null);
            if (words[0].startsWith("r")) {
                model.reload();
                model.show();
            }
            if (words[0].startsWith("c")) {
                model.choose(words[1], words[2]);
            }
            if (words[0].startsWith("s")) {
                model.show();
            }
            if (words[0].equals("help")) {
                diplayCommands();
                model.show();
            }
            if (words[0].equals("h")) {
                model.hint();
                model.show();
            }
            if(words[0].equals("g")){
                model.go(String.valueOf(words[1].charAt(0)));

                model.show();
            }
            if ( words[ 0 ].startsWith( "q" ) ) {
                break;
            }
        }

    };
    /**
     * Initialize the view
     */
    public void initializeView() {
        this.model.addObserver(this);

        update( this.model,null );
    }
    private void diplayCommands(){
        System.out.println("(r)eload -- reload the file");
        System.out.println("(c)hoose -- choose what figure is moving");
        System.out.println(("(h)int -- have computer make next move for you"));
        System.out.println("(s) -- show the board");
        System.out.println("(q) -- quit the game");
    }
    public void  displayBoard(LunarLandingModel m) {
        m.show();
    }


    @Override
    public void update(LunarLandingModel lunarLandingModel, Object o) {

        if (model.atGoal()) {
            model.show();
            System.out.println("You wim");
        }

    }

    public static void main (String [] args) throws FileNotFoundException {
        File file = new File(args[0]);
        LunarLandingPTUI ptui = new LunarLandingPTUI(file);
        ptui.run();

    }
}
