
package jetbikerace.scenes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.AnimationTimer;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import jetbikerace.JetBikeRace;

public class CreditScene extends GeneralScene{
    private static final String WIN_IMAGE_PATH = "assets/win.png";
    private static final String GAME_OVER_IMAGE_PATH = "assets/game_over.png";
    private static final String RESTART_IMAGE_PATH = "assets/restart_race.png";
    
    private long lastToggle;
    private boolean showMessage;
    private double xResult, yResult, xRestart, yRestart;
    
    private Image winImage, gameOverImage, resultImage, restartImage;
    
    public CreditScene(){
        super();
        loadImages();
    }
    
    private void loadImages() {
        try {
            winImage = new Image(Files.newInputStream(Paths.get(WIN_IMAGE_PATH)));
            gameOverImage = new Image(Files.newInputStream(Paths.get(GAME_OVER_IMAGE_PATH)));
            restartImage = new Image(Files.newInputStream(Paths.get(RESTART_IMAGE_PATH)));
        } catch (IOException ex) {
            Logger.getLogger(GameScene.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void positionateImages() {
        if(GameSession.isVictory()){
            resultImage = winImage;
        }else resultImage = gameOverImage;
        xResult = (GAME_WIDTH - resultImage.getWidth()) / 2;
        yResult = GAME_HEIGHT * 20 / 100;
        
        xRestart = (GAME_WIDTH - restartImage.getWidth())/2;
        yRestart = (GAME_HEIGHT - restartImage.getHeight())/2;
    }
    
    private void showCreditMessage(){
        if (resultImage != null)
            gc.drawImage(resultImage, xResult, yResult); 

        if (restartImage != null && showMessage)
            gc.drawImage(restartImage, xRestart, yRestart);

    }

    @Override
    public void draw() {
        activeKeys.clear();
        reset();
        positionateImages();
        new AnimationTimer(){
            @Override
            public void handle(long currentNanoTime){
                gc.setFill(Color.BLACK);
                gc.fillRect(0, 0, GAME_WIDTH, GAME_HEIGHT);
                
                // per aggiungere il lampeggio della scritta di inizio
                long nowMillis = System.nanoTime() / 1_000_000;
                if (nowMillis - lastToggle > 600) {
                    showMessage = !showMessage;
                    lastToggle = nowMillis;
                }
                
                showCreditMessage();
                
                if(activeKeys.contains(KeyCode.SPACE))
                {       
                    if(GameSession.isVictory()){
                        audioManager.stopVictoryMusic();
                    } else audioManager.stopGameOverMusic();
                    
                    this.stop();
                    JetBikeRace.setScene(JetBikeRace.GAME_SCENE);
                } else if (activeKeys.contains(KeyCode.ESCAPE)) {
                    if(GameSession.isVictory()){
                        audioManager.stopVictoryMusic();
                    } else audioManager.stopGameOverMusic();
                    
                    this.stop();
                    JetBikeRace.setScene(JetBikeRace.WELCOME_SCENE);
                }
            }
        }.start();
    }
    
    private void reset() {
        lastToggle = 0;
        showMessage = true;
    }
}
