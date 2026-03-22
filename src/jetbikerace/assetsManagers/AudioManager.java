package jetbikerace.assetsManagers;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import javafx.scene.media.Media;
import javafx.scene.media.AudioClip;
import javafx.scene.media.MediaPlayer;

public class AudioManager {

    private static final String WELCOME_SONG = "assets/bgMusic/RecklessRoboGangJohnny.mp3";
    private static final String RACE_SONG = "assets/bgMusic/BikeChase.mp3";
    private static final String VICTORY_SONG = "assets/bgMusic/victory.mp3";
    private static final String GAME_OVER_SONG = "assets/bgMusic/gameOver.mp3";
    private static final String COUNTDOWN_SOUND = "assets/soundEffect/race_countdown.mp3";
    private static final String COLLISION_SOUND = "assets/soundEffect/car_crash.wav";

    private MediaPlayer welcomeMusicPlayer, bgMusicPlayer, victoryMusicPlayer, gameOverMusicPlayer, raceCountdownPlayer;
    private final Map<String, AudioClip> soundEffects = new HashMap<>();

    public AudioManager() {
        try {
            loadSoundtracks();
            Media raceCountdown = new Media(new File(COUNTDOWN_SOUND).toURI().toString());
            raceCountdownPlayer = new MediaPlayer(raceCountdown);
            raceCountdownPlayer.setVolume(0.5);
        } catch (Exception e) {
            System.err.println("Errore nel caricamento della musica: " + e.getMessage());
        }

        loadEffect("collision", COLLISION_SOUND);
    }

    private void loadSoundtracks() {
        Media welcomeMusic = new Media(new File(WELCOME_SONG).toURI().toString());
        welcomeMusicPlayer = new MediaPlayer(welcomeMusic);
        // loop infinito
        welcomeMusicPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        // volume iniziale (0.0–1.0)
        welcomeMusicPlayer.setVolume(0.8);

        Media bgMusic = new Media(new File(RACE_SONG).toURI().toString());
        bgMusicPlayer = new MediaPlayer(bgMusic);
        bgMusicPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        bgMusicPlayer.setVolume(0.5);

        Media victoryMusic = new Media(new File(VICTORY_SONG).toURI().toString());
        victoryMusicPlayer = new MediaPlayer(victoryMusic);
        victoryMusicPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        victoryMusicPlayer.setVolume(0.8);

        Media gameOverMusic = new Media(new File(GAME_OVER_SONG).toURI().toString());
        gameOverMusicPlayer = new MediaPlayer(gameOverMusic);
        gameOverMusicPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        gameOverMusicPlayer.setVolume(1);
    }

    private void loadEffect(String key, String resourcePath) {
        try {
            AudioClip clip = new AudioClip(new File(resourcePath).toURI().toString());
            clip.setVolume(0.5);
            soundEffects.put(key, clip);
        } catch (Exception e) {
            System.err.println("Errore caricando effetto '" + key + "': " + e.getMessage());
        }
    }

    public void playWelcomeMusic() {
        if (welcomeMusicPlayer != null) {
            welcomeMusicPlayer.play();
        }
    }

    public void pauseWelcomeMusic() {
        if (welcomeMusicPlayer != null) {
            welcomeMusicPlayer.pause();
        }
    }

    public void stopWelcomeMusic() {
        if (welcomeMusicPlayer != null) {
            welcomeMusicPlayer.stop();
        }
    }

    public void playBackgroundMusic() {
        if (bgMusicPlayer != null) {
            bgMusicPlayer.play();
        }
    }

    public void pauseBackgroundMusic() {
        if (bgMusicPlayer != null) {
            bgMusicPlayer.pause();
        }
    }

    public void stopBackgroundMusic() {
        if (bgMusicPlayer != null) {
            bgMusicPlayer.stop();
        }
    }
    
    public void playVictoryMusic() {
        if (victoryMusicPlayer != null) {
            victoryMusicPlayer.play();
        }
    }

    public void pauseVictoryMusic() {
        if (victoryMusicPlayer != null) {
            victoryMusicPlayer.pause();
        }
    }

    public void stopVictoryMusic() {
        if (victoryMusicPlayer != null) {
            victoryMusicPlayer.stop();
        }
    }
    
    public void playGameOverMusic() {
        if (gameOverMusicPlayer != null) {
            gameOverMusicPlayer.play();
        }
    }

    public void pauseGameOverMusic() {
        if (gameOverMusicPlayer != null) {
            gameOverMusicPlayer.pause();
        }
    }

    public void stopGameOverMusic() {
        if (gameOverMusicPlayer != null) {
            gameOverMusicPlayer.stop();
        }
    }
    public void setMusicVolume(double volume) {
        if (bgMusicPlayer != null) {
            bgMusicPlayer.setVolume(clamp(volume, 0.0, 1.0));
        }
    }

    public void playCountdownMusic() {
        if (raceCountdownPlayer != null) {
            raceCountdownPlayer.play();
        }
    }

    public void stopCountdownMusic() {
        if (raceCountdownPlayer != null) {
            raceCountdownPlayer.stop();
        }
    }

    public void playEffect(String key) {
        AudioClip clip = soundEffects.get(key);
        if (clip != null) {
            clip.play();
        }
    }

    public void setEffectVolume(String key, double volume) {
        AudioClip clip = soundEffects.get(key);
        if (clip != null) {
            clip.setVolume(clamp(volume, 0.0, 1.0));
        }
    }

    private double clamp(double value, double min, double max) {
        if (value < min) {
            return min;
        }
        if (value > max) {
            return max;
        }
        return value;
    }
}
