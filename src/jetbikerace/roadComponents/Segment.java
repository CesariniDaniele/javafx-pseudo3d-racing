/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jetbikerace.roadComponents;

import java.util.ArrayList;
import javafx.scene.paint.Color;
import jetbikerace.sprites.Sprite;

public class Segment {
    private int index;
    private Color color;
    private double xWorld, yWorld, zWorld;
    private double xScreen, yScreen;
    private double widthScreen;
    private double scale;
    // lista di sprites estetici associati a ciascun segmento di strada
    private ArrayList<Sprite> sprites; 
    
    public Segment(int index, Color color, double zWorld){
        this.index = index;
        this.color = color;
        this.zWorld = zWorld;
        xWorld = 0;
        yWorld = 0;
        xScreen = 0;
        yScreen = 0;
        widthScreen = 0;
        scale = 0;
        sprites = new ArrayList<>();
    }
    
    public double getxWorld() {
        return xWorld;
    }

    public double getyWorld() {
        return yWorld;
    }

    public double getzWorld() {
        return zWorld;
    }

    public int getIndex() {
        return index;
    }

    public double getxScreen() {
        return xScreen;
    }

    public double getyScreen() {
        return yScreen;
    }

    public double getWidthScreen() {
        return widthScreen;
    }

    public Color getColor() {
        return color;
    }

    public double getScale() {
        return scale;
    }

    public ArrayList<Sprite> getSprites() {
        return sprites;
    }
    
    public void setxScreen(double xScreen) {
        this.xScreen = xScreen;
    }

    public void setyScreen(double yScreen) {
        this.yScreen = yScreen;
    }

    public void setWidthScreen(double widthScreen) {
        this.widthScreen = widthScreen;
    }
    
    public void setColor(Color color) {
        this.color = color;
    }

    public void setScale(double scale) {
        this.scale = scale;
    }
}

