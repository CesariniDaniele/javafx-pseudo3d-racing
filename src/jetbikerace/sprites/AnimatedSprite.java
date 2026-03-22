package jetbikerace.sprites;

import javafx.scene.image.Image;
import jetbikerace.roadComponents.Segment;

public class AnimatedSprite extends Sprite {
    public static final int LEFT = 0;
    public static final int RIGHT = 1;
    
    private int index;
    private double zPosition, speed; // sprite animati hanno in aggiunta una posizione lungo z variabile e una velocità di movimento
    private double percent; // posizione relativa normalizzata fra [0, 1] dello sprite rispetto ai 2 segmenti fra cui è collocato
    private Segment currSegment; // segmento precedente alla posizione dello sprite
    
    public AnimatedSprite(double width, double height){
        super(width, height);
    }

    public AnimatedSprite(double width, double height, double spriteX, double spriteY, Image spriteImage) {
        super(width, height, spriteX, spriteY, spriteImage);
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

    public double getPercent() {
        return percent;
    }

    public void setPercent(double percent) {
        this.percent = percent;
    }

    public Segment getCurrSegment() {
        return currSegment;
    }

    public void setCurrSegment(Segment currSegment) {
        this.currSegment = currSegment;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
