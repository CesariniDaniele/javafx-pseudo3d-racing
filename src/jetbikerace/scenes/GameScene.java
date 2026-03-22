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
import jetbikerace.assetsManagers.HUD;

import jetbikerace.JetBikeRace;
import jetbikerace.assetsManagers.MainCharacterManager;
import jetbikerace.assetsManagers.OpponentManager;
import jetbikerace.ai.RaceManager;
import jetbikerace.assetsManagers.RoadManager;
import jetbikerace.sprites.MainCharacter;

public class GameScene extends GeneralScene {

    private static final String WIN_IMAGE_PATH = "assets/win.png";
    private static final String GAME_OVER_IMAGE_PATH = "assets/game_over.png";

    public static final double SPRITE_WIDTH = 400;
    public static final double SPRITE_HEIGHT = 600;
    public static final double DECORATIVE_SPRITE_WIDTH = 600;
    public static final double DECORATIVE_SPRITE_HEIGHT = 1200;

    private Image winImage, gameOverImage, resultImage;

    private final HUD hud;
    private final RoadManager roadManager;
    private final MainCharacterManager cronoManager;
    private final OpponentManager johnnyManager;
    private final RaceManager raceManager;
    private long lastTime, initialTime;
    private double deltaTime;
    private long lastToggle;
    private boolean showMessage;
    // punti del giocatore
    private int points;
    // per il fade out di fine gara
    private double opacity;
    // posizione del messaggio di fine gara
    private double xResult, yResult;

    public GameScene() {
        super();
        loadImages();
        hud = new HUD();
        cronoManager = new MainCharacterManager(gc);
        johnnyManager = new OpponentManager(gc);
        roadManager = new RoadManager(gc, cronoManager, johnnyManager);
        raceManager = new RaceManager(roadManager, audioManager);
        lastTime = 0;
        initialTime = -1;
        // tempo trascorso fra ciascun frame e il precedente
        deltaTime = 0;
        points = 0;
        opacity = 0;
    }

    private void loadImages() {
        try {
            winImage = new Image(Files.newInputStream(Paths.get(WIN_IMAGE_PATH)));
            gameOverImage = new Image(Files.newInputStream(Paths.get(GAME_OVER_IMAGE_PATH)));
        } catch (IOException ex) {
            Logger.getLogger(GameScene.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void positionateResultImage(Image result) {
        xResult = (GAME_WIDTH - result.getWidth()) / 2;
        yResult = GAME_HEIGHT * 20 / 100;
    }

    private void showResultMessage() {
        if (resultImage != null && showMessage) {
            gc.drawImage(resultImage, xResult, yResult); // Titolo del gioco
        }
    }

    @Override
    public void draw() {
        audioManager.playBackgroundMusic();

        activeKeys.clear();

        reset();

        new AnimationTimer() {
            @Override
            public void handle(long currentNanoTime) {
                gc.setFill(Color.BLACK);
                gc.fillRect(0, 0, GAME_WIDTH, GAME_HEIGHT);
                if (initialTime == -1) {
                    initialTime = currentNanoTime;
                }

                if (lastTime > 0) {
                    //valore di delta time in secondi
                    deltaTime = (currentNanoTime - lastTime) / 1E9;
                }
                lastTime = currentNanoTime;

                raceManager.racePace(currentNanoTime - initialTime, deltaTime);
                roadManager.renderAll(deltaTime);

                calculatePoints();

                if (GameSession.isRaceStarted()) {
                    if (activeKeys.contains(KeyCode.LEFT)) {
                        cronoManager.move(MainCharacter.LEFT);
                    } else if (activeKeys.contains(KeyCode.RIGHT)) {
                        cronoManager.move(MainCharacter.RIGHT);
                    }
                }

                if (raceManager.raceIsOver()) {
                    // appena la gara finisce viene eseguito questo blocco, dopodichè l'opacità del fade out aumenta
                    if (opacity == 0) {
                        boolean result = raceManager.isVictory();
                        GameSession.updateWinner(result);
                        audioManager.stopBackgroundMusic();
                        audioManager.stopCountdownMusic();
                        if (result) {
                            audioManager.playVictoryMusic();
                            GameSession.updateBestScore(points);
                            resultImage = winImage;
                        } else {
                            audioManager.playGameOverMusic();
                            resultImage = gameOverImage;
                        }
                        positionateResultImage(resultImage);
                    }

                    // per aggiungere il lampeggio della scritta finale
                    long nowMillis = System.nanoTime() / 1_000_000;
                    if (nowMillis - lastToggle > 600) {
                        showMessage = !showMessage;
                        lastToggle = nowMillis;
                    }

                    showResultMessage();

                    if (endingFadeOut() > 1) {
                        this.stop();
                        JetBikeRace.setScene(JetBikeRace.CREDIT_SCENE);
                    }
                } else {
                    calculatePoints();
                    hud.drawPoints(gc, points);
                    hud.drawStylizedRace(gc, roadManager);
                }
            }
        }.start();
    }

    private void reset() {
        initialTime = -1;
        points = 0;
        lastTime = 0;
        deltaTime = 0;
        lastToggle = 0;
        showMessage = true;
        opacity = 0;
        resultImage = null;
        roadManager.restartAll();
        raceManager.restart();
        audioManager.playCountdownMusic();
    }

    private void calculatePoints() {
        //se lo sprite di Crono è davanti a Johnny
        if (cronoManager.getzPosition() > johnnyManager.getzPosition()) {
            points++;
            //se Crono è dietro a Johnny (i punti non scendono sotto a 0)
        } else if ((points > 0)) {
            points--;
        }
    }

    private double endingFadeOut() {
        gc.setFill(Color.rgb(0, 0, 0, opacity));
        gc.fillRect(0, 0, GAME_WIDTH, GAME_HEIGHT);
        if (opacity < 1.0) {
            opacity += 0.002;
        }
        return opacity;
    }
}
