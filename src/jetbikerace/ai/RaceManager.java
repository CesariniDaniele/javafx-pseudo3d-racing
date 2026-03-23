package jetbikerace.ai;

import java.util.ArrayList;
import jetbikerace.assetsManagers.AudioManager;
import jetbikerace.assetsManagers.NpcManager;
import jetbikerace.assetsManagers.MainCharacterManager;
import jetbikerace.assetsManagers.OpponentManager;
import jetbikerace.assetsManagers.RoadManager;
import jetbikerace.roadComponents.Camera;
import jetbikerace.scenes.GameScene;
import jetbikerace.scenes.GameSession;
import jetbikerace.sprites.AnimatedSprite;


public class RaceManager {
    //tempo in secondi del countdown iniziale di gara
    private final double COUNTDOWN_TIME = 3;  
    //tempo in secondi per raggiungere la velocità di gara 
    private final double SLOWSTART_TIME = 3; 
    //tempo in secondi per tornare alla velocità di gara dopo una collisione
    private final double REPRISE_TIME = 3; 

    private RoadManager roadManager;
    private MainCharacterManager cronoManager;
    private OpponentManager johnnyManager;
    private NpcManager carManager;
    private CollisionsManager collisionsManager;
    private OpponentDynamicAI opponentDynamicAI;
    private AudioManager audioManager;
    private Camera camera;
    
    // la prima velocità mantiene quella della telecamera che sta dietro al giocatore, la seconda quella dell'avversario
    private double currentSpeedRace, opponentSpeedRace;
    // flag per la gestione della collisione 
    private boolean collision, isOver, victory;
    
    public RaceManager(RoadManager roadManager, AudioManager audioManager) {
        this.roadManager = roadManager;
        this.audioManager = audioManager;
        opponentDynamicAI = new OpponentDynamicAI(roadManager);
        this.cronoManager = roadManager.getCronoManager();
        this.johnnyManager = roadManager.getJohnnyManager();
        this.carManager = roadManager.getCarManager();
        this.collisionsManager = new CollisionsManager(cronoManager);
        camera = roadManager.getCamera();
        currentSpeedRace = camera.getCurrentSpeed();
        opponentSpeedRace = camera.getCurrentSpeed();
        collision = false;
        isOver = false;
        victory = false;       
    }

    public void racePace(double elapsedTime, double deltaTime) {
        double currentTime = elapsedTime / 1E9; //tempo trascorso in secondi
        double cronoRaceSpeed = (roadManager.getRoad().MAX_SPEED)*95/100;
        double johnnyRaceSpeed = (roadManager.getRoad().MAX_SPEED)*90/100;
        //dopo i 3 secondi di countdown incrementa la velocità fin quando non raggiunge la velocità massima
        if (currentTime > COUNTDOWN_TIME && currentTime < COUNTDOWN_TIME + SLOWSTART_TIME) { 
            if(!GameSession.isRaceStarted()){
                GameSession.setRaceStarted(true);
            }
            slowStart(cronoRaceSpeed, johnnyRaceSpeed, deltaTime);
        } else if (currentTime > (COUNTDOWN_TIME + SLOWSTART_TIME)) {
            checkCollision(deltaTime);
            mantainSpeedRace(cronoRaceSpeed, deltaTime);
        }
    }

    private void slowStart(double cronoRaceSpeed, double johnnyRaceSpeed, double deltaTime) {
        currentSpeedRace += deltaTime * (cronoRaceSpeed / SLOWSTART_TIME);
        opponentSpeedRace += deltaTime * (johnnyRaceSpeed / SLOWSTART_TIME);
        camera.setCurrentSpeed(currentSpeedRace);
        cronoManager.setSpeed(currentSpeedRace);
        cronoManager.getCrono().setSpeed(currentSpeedRace);
        johnnyManager.setSpeed(opponentSpeedRace);
        johnnyManager.getJohnny().setSpeed(opponentSpeedRace);
    }

    public boolean raceIsOver() {
        if (((cronoManager.getzPosition() >= roadManager.getRoad().getSectionLength()) || (johnnyManager.getzPosition() >= roadManager.getRoad().getSectionLength())) && !isOver) {
            isOver = true;
            if(cronoManager.getzPosition() > johnnyManager.getzPosition()){
                victory = true;
            }
        }
        return isOver;
    }

    private void checkCollision(double deltaTime) {
        double cronoSpeed, opponentSpeed, collisionSpeed;
        double collisionZ;
        ArrayList<AnimatedSprite> npcs = carManager.getNpcs();
        cronoSpeed = cronoManager.getSpeed();
        opponentSpeed = johnnyManager.getSpeed();
        for (AnimatedSprite npc : npcs) {
            // collision(cronoSpeed, car, deltaTime);
            collision = collisionsManager.collision(roadManager, npc, deltaTime);
            if (collision) {
                audioManager.playEffect("collision");
                currentSpeedRace = npc.getSpeed() * 0.9;
                collisionZ = cronoManager.getzPosition() - 10;
                cronoManager.setSpeed(currentSpeedRace);
                cronoManager.setzPosition(collisionZ);
                camera.setCurrentSpeed(currentSpeedRace);
            }
            opponentDynamicAI.overtakeNpcs(opponentSpeed, johnnyManager.getzPosition(), johnnyManager.getxOffset(), npc, deltaTime);
        }
        opponentDynamicAI.overtakeNpcs(opponentSpeed, johnnyManager.getzPosition(), johnnyManager.getxOffset(), cronoManager.getCrono(), deltaTime);
    }
    
    private void mantainSpeedRace(double cronoRaceSpeed, double deltaTime){
        if(currentSpeedRace<cronoRaceSpeed){
            currentSpeedRace += deltaTime * (cronoRaceSpeed / REPRISE_TIME);
            camera.setCurrentSpeed(currentSpeedRace);
            cronoManager.setSpeed(currentSpeedRace);
        }
    }
    
    public void restart() {
        currentSpeedRace = camera.getCurrentSpeed();
        opponentSpeedRace = camera.getCurrentSpeed();
        isOver = false;
        victory = false;
        GameSession.setRaceStarted(false);
    }
    
    public boolean isVictory() {
        return victory;
    }
}
