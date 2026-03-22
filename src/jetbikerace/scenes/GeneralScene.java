
package jetbikerace.scenes;

import java.util.HashSet;
import java.util.Set;

import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import jetbikerace.JetBikeRace;
import jetbikerace.assetsManagers.AudioManager;

public abstract class GeneralScene extends Scene{
    public static final double GAME_WIDTH = 1000;
    public static final double GAME_HEIGHT = 800;  
    
    private StackPane root;
    protected GraphicsContext gc;
    protected Set<KeyCode> activeKeys;
    protected Set<KeyCode> releasedKeys;
    protected AudioManager audioManager;
    
    public GeneralScene(){
        super(new StackPane(), GAME_WIDTH, GAME_HEIGHT);
        
        root = new StackPane(); 
        this.setRoot(root); 
        
        //inizializzazione canvas e graphics context
        Canvas canvas = new Canvas(GAME_WIDTH, GAME_HEIGHT);
        root.getChildren().add(canvas);
        gc = canvas.getGraphicsContext2D();
        
        //inizializzazione set di keys attualmente premute e rilasciate
        activeKeys = new HashSet<>();
        releasedKeys = new HashSet<>();
        this.setOnKeyPressed(e -> {
            activeKeys.add(e.getCode());
        });
        this.setOnKeyReleased(e -> {
            activeKeys.remove(e.getCode());
            releasedKeys.add(e.getCode());
        });
        
        audioManager = JetBikeRace.getAudioManager();
    }
    
    public abstract void draw();
}
