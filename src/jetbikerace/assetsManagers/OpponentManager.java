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
import jetbikerace.sprites.Opponent;

public class OpponentManager {

    private final double OPPONENT_OFFSET = 0.5; //offset è normalizzato fra [-1,1] lungo l'asse X (-1 è il bordo sinistro della strada, 1 il bordo destro)
    private final double OPPONENT_Z = 0; //distanza di partenza dal primo segmento di strada lungo l'asse Z
    private final double STEP_X = 0.020; //variazione dell'offset normalizzato per ciascun movimento dell'Opponent

    private Opponent johnny;
    private GraphicsContext gc;

    private double zPosition, xOffset, speed;
    private double percent; //posizione relativa normalizzata fra [0, 1] dell'Opponent rispetto al corrente segmento di strada in cui si trova
    private Segment opponentSegment; //primo segmento precedente alla posizione dell'Opponent
    private double scale, currentWidth, currentHeight;

    // mantengo il numero di segmenti passati da Johnny dall'inizio della gara
    private int passedSegments;

    public OpponentManager(GraphicsContext gc) {
        johnny = new Opponent();
        this.gc = gc;
        passedSegments = 0;
    }

    protected void drawOpponent(Road road, Segment prevSegment, Segment currSegment, int cameraPassedSegments) {
        // lo sprite viene disegnato solo se Johnny compare nella visuale di camera
        if (passedSegments > cameraPassedSegments) {
            scale = interpolate(prevSegment.getScale(), currSegment.getScale());
            double xScreen = interpolate(prevSegment.getxScreen(), currSegment.getxScreen()) + (road.ROAD_WIDTH * xOffset * (GeneralScene.GAME_WIDTH / 2) * scale);
            double yScreen = interpolate(prevSegment.getyScreen(), currSegment.getyScreen());
            currentWidth = (GameScene.SPRITE_WIDTH * scale) * (GeneralScene.GAME_WIDTH / 2);
            currentHeight = (GameScene.SPRITE_HEIGHT * scale) * (GeneralScene.GAME_HEIGHT / 2);
            johnny.moveTo(xScreen - currentWidth / 2, yScreen - currentHeight);
            johnny.drawRescaledSprite(gc, currentWidth, currentHeight);
        }
    }

    // aggiorna ad ogni frame posizione, percent e segmento dell'avversario
    protected void update(double deltaTime, Road road) {
        zPosition += speed * deltaTime;
        /*if(zPosition >= road.getSectionLength()){
            zPosition -= road.getSectionLength();
        }*/

        percent = (zPosition % road.SEGMENT_DISTANCE) / road.SEGMENT_DISTANCE;
        Segment newSegment = road.getSegment(zPosition);
        int newIndex = newSegment.getIndex();
        if (newSegment != opponentSegment) {
            passedSegments += newIndex - opponentSegment.getIndex();
            opponentSegment = newSegment;
        }
    }

    protected void restart(Road road, int segmentsDistCameraToPlayer) {
        zPosition = OPPONENT_Z;
        xOffset = OPPONENT_OFFSET;
        speed = 0;
        percent = 0;
        opponentSegment = road.getSegment(zPosition);
        passedSegments = segmentsDistCameraToPlayer;
    }

    private double interpolate(double start, double end) {
        return start + (end - start) * percent;
    }

    public void move(int movement) {
        if (movement == AnimatedSprite.LEFT) {
            xOffset -= Math.min(STEP_X, xOffset + 1);
        } else if (movement == AnimatedSprite.RIGHT) {
            xOffset += Math.min(STEP_X, 1 - xOffset);
        }
    }

    public Segment getOpponentSegment() {
        return opponentSegment;
    }

    public double getzPosition() {
        return zPosition;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public Opponent getJohnny() {
        return johnny;
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
