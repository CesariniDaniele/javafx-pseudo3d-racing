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
import jetbikerace.assetsManagers.HUD;
import jetbikerace.assetsManagers.RoadManager;

public class WelcomeScene extends GeneralScene {
    private static final String TITLE_IMAGE_PATH = "assets/title.png";
    private static final String START_IMAGE_PATH = "assets/start_race.png";
    private static final String POINTS_IMAGE_PATH = "assets/points.png";
    
    private long lastToggle;
    private boolean showMessage;
    private long lastTime, initialTime;
    private double deltaTime;
    
    // coordinate nel canvas delle 3 scritte
    private double xTitle, yTitle, xStart, yStart, xPoints, yPoints; 
   
    private Image titleImage;
    private Image startMessageImage;
    private Image pointsMessageImage;
    
    private final RoadManager roadManager;
    private final HUD hud;

    public WelcomeScene() {
        super();
        roadManager = new RoadManager(gc); 
        hud = new HUD();
        
        loadImages();
        positionateImages();
        showWelcomeImages();
    }
    
    private void loadImages() {
        try {
            titleImage = new Image(Files.newInputStream(Paths.get(TITLE_IMAGE_PATH)));
            startMessageImage = new Image(Files.newInputStream(Paths.get(START_IMAGE_PATH)));
            pointsMessageImage = new Image(Files.newInputStream(Paths.get(POINTS_IMAGE_PATH)));
        } catch (IOException ex) {
            Logger.getLogger(WelcomeScene.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void positionateImages(){
        xTitle = (GAME_WIDTH - titleImage.getWidth())/2;
        yTitle = GAME_HEIGHT*20/100;
        
        xStart = (GAME_WIDTH - startMessageImage.getWidth())/2;
        yStart = (GAME_HEIGHT - startMessageImage.getHeight())/2;
        
        xPoints = GAME_WIDTH*1/100;
        yPoints = (GAME_HEIGHT - pointsMessageImage.getHeight())*99/100;
    }
    
    private void showWelcomeImages() {
        if (titleImage != null)
            gc.drawImage(titleImage, xTitle, yTitle); // Titolo del gioco

        if (startMessageImage != null && showMessage)
            gc.drawImage(startMessageImage, xStart, yStart); // Messaggio "premi SPAZIO"

        if (pointsMessageImage != null)
            gc.drawImage(pointsMessageImage, xPoints, yPoints); 
        
        hud.drawBestScore(gc, pointsMessageImage);
    }

    //metodo che va richiamato ogni volta che si vuole fare un redraw sulla Scene principale, ossia ogni volta che cambia Scene
    @Override
    public void draw() {
        activeKeys.clear();
        
        reset();
        audioManager.playWelcomeMusic();
         
        new AnimationTimer() {
            @Override
            public void handle(long currentNanoTime) {
                //impostiamo un background nero per pulire la scena
                gc.setFill(Color.BLACK);
                //(x=horizontal coordinate of top left rect corner, y=vertical coordinate of top left rect corner, width, height)
                gc.fillRect(0, 0, GAME_WIDTH, GAME_HEIGHT);
                
                if (initialTime == -1) {
                    initialTime = currentNanoTime;
                }
                if (lastTime > 0) {
                    //valore di delta time in secondi
                    deltaTime = (currentNanoTime - lastTime) / 1E9; 
                }
                lastTime = currentNanoTime;
                
                roadManager.renderRoad(deltaTime);
                
                // per aggiungere il lampeggio della scritta di inizio
                long nowMillis = System.nanoTime() / 1_000_000;
                if (nowMillis - lastToggle > 600) {
                    showMessage = !showMessage;
                    lastToggle = nowMillis;
                }

                //showWelcomeMessage();
                showWelcomeImages();

                if (activeKeys.contains(KeyCode.SPACE)) //con il tasto SPACE si passa alla Game Scene
                {
                    audioManager.stopWelcomeMusic();
                    this.stop();
                    JetBikeRace.setScene(JetBikeRace.GAME_SCENE);
                } else if (activeKeys.contains(KeyCode.ESCAPE)) { //con il tasto ESCAPE si chiude l'applicazione
                    audioManager.stopWelcomeMusic();
                    this.stop();
                    JetBikeRace.exit();
                }
            }
        }.start();
    }
    
    private void reset() {
        initialTime = -1;
        lastTime = 0;
        deltaTime = 0;
        
        lastToggle = 0;
        showMessage = true;
        
        roadManager.restartRoad();
    }
    
}
