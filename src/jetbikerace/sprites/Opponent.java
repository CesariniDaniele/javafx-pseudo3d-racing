package jetbikerace.sprites;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.image.Image;
import jetbikerace.roadComponents.Road;

import jetbikerace.scenes.GameScene;
import jetbikerace.scenes.GeneralScene;

public class Opponent extends AnimatedSprite {

    public static final int OPPONENT_WIDTH = 33;
    public static final int OPPONENT_HEIGHT = 37;
    public static final String IMAGE_PATH = "assets/johnnySprites.png";
    public static final int OPPONENT_STEP = 5; //numero di pixel di spostamento del personaggio

    public Opponent() {
        super(OPPONENT_WIDTH, OPPONENT_HEIGHT);

        try {
            spriteImage = new Image(Files.newInputStream(Paths.get(IMAGE_PATH)));
        } catch (IOException ex) {
            Logger.getLogger(GameScene.class.getName()).log(Level.SEVERE, null, ex);
        }

        spriteX = 30;
        spriteY = 37;
    }
}
