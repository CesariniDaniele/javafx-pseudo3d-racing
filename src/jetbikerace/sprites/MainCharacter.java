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

public class MainCharacter extends AnimatedSprite {

    public static final int MAIN_CHARACTER_WIDTH = 50;
    public static final int MAIN_CHARACTER_HEIGHT = 70;
    public static final String IMAGE_PATH = "assets/cronoSprites.png";
    public static final int MAIN_CHARACTER_STEP = 5; //numero di pixel di spostamento del personaggio

    public MainCharacter() {
        super(MAIN_CHARACTER_WIDTH, MAIN_CHARACTER_HEIGHT);

        try {
            spriteImage = new Image(Files.newInputStream(Paths.get(IMAGE_PATH)));
        } catch (IOException ex) {
            Logger.getLogger(MainCharacter.class.getName()).log(Level.SEVERE, null, ex);
        }

        spriteX = 307;
        spriteY = 20;
    }
}
