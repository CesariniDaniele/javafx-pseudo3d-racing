package jetbikerace.sprites;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Sprite {
    protected double width, height; //larghezza e lunghezza dello sprite specifico nel file png
    protected double xScreen, yScreen; //posizione dello sprite nel Canvas
    protected double spriteX, spriteY; //posizione dello sprite specifico nel file png sorgente
    protected Image spriteImage; //file png contenente tutti gli sprites che si vogliono gestire
    
    protected double offset; //offset normalizzato rispetto alla strada
    
    public Sprite(double width, double height){
        this.width = width;
        this.height = height;
    }

    public Sprite(double width, double height, double spriteX, double spriteY, Image spriteImage) {
        this.width = width;
        this.height = height;
        this.spriteX = spriteX;
        this.spriteY = spriteY;
        this.spriteImage = spriteImage;
    }
    
    // copy costructor
    public Sprite(Sprite other){
        this.width = other.width;
        this.height = other.height;
        this.spriteX = other.spriteX;
        this.spriteY = other.spriteY;
        this.spriteImage = other.spriteImage;
    }

    public void moveTo(double x, double y){
        this.xScreen=x;
        this.yScreen=y;
    }

    public double getX() {
        return xScreen;
    }

    public double getY() {
        return yScreen;
    }
    
    public void drawSprite(GraphicsContext gc){
        gc.drawImage(spriteImage, spriteX, spriteY, width, height, xScreen, yScreen, width, height);
    }
    //drawImage(Image, coordinata x del top left corner della porzione di Image scelta, coordinata y, width del ritaglio, height del ritaglio, x nel Canvas, y nel Canvas, width nel Canvas, height nel Canvas)
    public void drawRescaledSprite(GraphicsContext gc, double reWidth, double reHeight){
        gc.drawImage(spriteImage, spriteX, spriteY, width, height, xScreen, yScreen, reWidth, reHeight);
    }
    
    public void drawMovedSprite(GraphicsContext gc, double reWidth, double reHeight, double x, double y){
        gc.drawImage(spriteImage, spriteX, spriteY, width, height, x, y, reWidth, reHeight);
    }

    public double getOffset() {
        return offset;
    }

    public void setOffset(double offset) {
        this.offset = offset;
    }

    public double getSpriteX() {
        return spriteX;
    }

    public double getSpriteY() {
        return spriteY;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }
    
}
