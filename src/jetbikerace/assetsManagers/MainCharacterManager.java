/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jetbikerace.assetsManagers;

import javafx.scene.canvas.GraphicsContext;
import jetbikerace.roadComponents.Road;
import jetbikerace.roadComponents.Segment;
import jetbikerace.scenes.GameScene;
import jetbikerace.scenes.GeneralScene;
import jetbikerace.sprites.AnimatedSprite;
import jetbikerace.sprites.MainCharacter;

public class MainCharacterManager {
    private final double MAIN_CHARACTER_OFFSET = -0.5; //offset è normalizzato fra [-1,1] lungo l'asse X (-1 è il bordo sinistro della strada, 1 il bordo destro)
    private final double MAIN_CHARACTER_Z = 0; //distanza di partenza dal primo segmento di strada lungo l'asse Z
    private final double STEP_X = 0.02; //variazione dell'offset normalizzato per ciascun movimento dell'Opponent
    
    private MainCharacter crono;
    private GraphicsContext gc; 
    
    private double zPosition, xOffset, speed;
    private double percent; //posizione relativa normalizzata fra [0, 1] del MainCharacter rispetto al corrente segmento di strada in cui si trova
    private Segment mainCharacterSegment; //primo segmento precedente alla posizione del MainCharacter
    private double scale, currentWidth, currentHeight;
    
    public MainCharacterManager(GraphicsContext gc){
        crono = new MainCharacter();
        this.gc = gc; 
    }
    
    protected void drawMainCharacter(Road road, Segment prevSegment, Segment currSegment){ 
        scale = interpolate(prevSegment.getScale(), currSegment.getScale());
        double xScreen = interpolate(prevSegment.getxScreen(), currSegment.getxScreen()) + (road.ROAD_WIDTH*xOffset*(GeneralScene.GAME_WIDTH/2)*scale);
        double yScreen = interpolate(prevSegment.getyScreen(), currSegment.getyScreen());
        currentWidth = (GameScene.SPRITE_WIDTH*scale)*(GeneralScene.GAME_WIDTH/2);
        currentHeight = (GameScene.SPRITE_HEIGHT*scale)*(GeneralScene.GAME_HEIGHT/2);
        crono.moveTo(xScreen - currentWidth/2, yScreen - currentHeight);
        crono.drawRescaledSprite(gc, currentWidth, currentHeight);
    }
    
    // aggiorna ad ogni frame posizione, percent e segmento del Main Character      
    protected void update(double deltaTime, Road road){   
        zPosition += speed*deltaTime; 
        crono.setzPosition(zPosition);
        
        percent = (zPosition % road.SEGMENT_DISTANCE)/road.SEGMENT_DISTANCE;
        crono.setPercent(percent);
        Segment newSegment = road.getSegment(zPosition);
        if(mainCharacterSegment != newSegment){
            mainCharacterSegment = newSegment;
            crono.setCurrSegment(newSegment);
        }
    }
    
    protected void restart(Road road){ 
        zPosition = MAIN_CHARACTER_Z;
        crono.setzPosition(zPosition);
        xOffset = MAIN_CHARACTER_OFFSET;
        crono.setOffset(xOffset);
        speed = 0;
        crono.setSpeed(speed);
        percent = 0;
        crono.setPercent(percent);
        mainCharacterSegment = road.getSegment(zPosition);
        crono.setCurrSegment(mainCharacterSegment);
    }
    
    private double interpolate(double start, double end){
        return start +(end - start) * percent;
    }
    
    public void move(int movement){
        if(movement==AnimatedSprite.LEFT){
            xOffset -= Math.min(STEP_X, xOffset + 1);
        } else if(movement==AnimatedSprite.RIGHT){
            xOffset += Math.min(STEP_X, 1 - xOffset);
        }
        crono.setOffset(xOffset);
    }

    public Segment getMainCharacterSegment() {
        return mainCharacterSegment;
    }

    public double getzPosition() {
        return zPosition;
    }

    public void setzPosition(double zPosition) {
        this.zPosition = zPosition;
    }
    
    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public MainCharacter getCrono() {
        return crono;
    }

    public double getxOffset() {
        return xOffset;
    }

    public double getCurrentWidth() {
        return currentWidth;
    }

    public double getScale() {
        return scale;
    }
}
