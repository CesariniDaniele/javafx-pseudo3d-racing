package jetbikerace;

import javafx.application.Application;
import javafx.stage.Stage;
import jetbikerace.assetsManagers.AudioManager;

import jetbikerace.scenes.*; 

public class JetBikeRace extends Application {
    public static final int MAX_SCENES = 3;
    public static final int WELCOME_SCENE = 0;
    public static final int GAME_SCENE = 1;
    public static final int CREDIT_SCENE = 2;
    private static AudioManager audioManager;
    
    public static final GeneralScene[] scenes
            = new GeneralScene[MAX_SCENES]; 

    public static Stage window; 
 
    public static void main(String[] args) { 
        launch(args); 
    }      

    @Override 
    public void start(Stage primaryStage) {
        window = primaryStage; 
        audioManager = new AudioManager();
        
        scenes[WELCOME_SCENE] = new WelcomeScene();   
        scenes[GAME_SCENE] = new GameScene();
        scenes[CREDIT_SCENE] = new CreditScene();   
        
        window.setTitle("Jet Bike Race");
        setScene(WELCOME_SCENE); 
        window.show();
    }
 
    public static void setScene(int numScene) {
        window.setScene(scenes[numScene]);
        scenes[numScene].draw();
    }

    public static void exit() { 
        window.hide();
    }

    public static AudioManager getAudioManager() {
        return audioManager;
    }
}
