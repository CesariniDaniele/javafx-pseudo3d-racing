package jetbikerace.roadComponents;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import jetbikerace.sprites.Sprite;

public class Road {
    private final String DECORATIONS_IMAGE_PATH= "assets/millenialFair.png";
    private final String POLE_IMAGE_PATH= "assets/PolePositionSprites.png";
    
    public final Color roadColor1 = Color.LIGHTGRAY;
    public final Color roadColor2 = Color.DARKGRAY;
    
    public final double ROAD_WIDTH = 1200;
    public final double SEGMENT_DISTANCE = 100;
    public final int NUM_SEGMENTS = 2200; //numero di segmenti che formano una sezione
    public final int VISIBLE_SEGMENTS = 200; //numero di segmenti visibili dalla camera
    private final int RUMBLE_SEGMENTS = 5; //numero di segmenti che formano una striscia verticale
    public final double MAX_SPEED = SEGMENT_DISTANCE*60; //velocità massima di scorrimento (lungo l'asse Z)
    
    private ArrayList<Segment> segments;
    private double sectionLength;
    
    private ArrayList<Sprite> randomDecorations;
    private Image decorationsImage, poleImage;
    
    public Road(){
        segments = new ArrayList<>();
        sectionLength = NUM_SEGMENTS*SEGMENT_DISTANCE;
        randomDecorations = new ArrayList<>();
        try {
            decorationsImage = new Image(Files.newInputStream(Paths.get(DECORATIONS_IMAGE_PATH)));
            poleImage = new Image(Files.newInputStream(Paths.get(POLE_IMAGE_PATH)));
        } catch (IOException ex) {
            Logger.getLogger(Road.class.getName()).log(Level.SEVERE, null, ex);
        }
        populateDecorationsList();
    }
    
    public void createRoad(){
        segments.clear();
        for(int i=0; i<NUM_SEGMENTS; i++){ //creazione della sezione
            createSection();
        }
        
        /*for(int i=0; i<RUMBLE_SEGMENTS; i++){
            segments.get(i).setColor(Color.WHITE); //i primi segmenti della strada li coloriamo di bianco
            segments.get(segments.size() - 1- i).setColor(Color.BLACK); //gli ultimi di nero
        }     */
        locateDecorativeSprites();
    }
    
    private void createSection(){
        int index = segments.size();
        Sprite leftDecoration = randomDecorations.get(2);
        Sprite rightDecoration = randomDecorations.get(1);
        leftDecoration.setOffset(-1);
        rightDecoration.setOffset(1);
        // coloro alternati i segmenti  
        if(Math.floor(index/RUMBLE_SEGMENTS) % 2 == 0){ 
            segments.add(new Segment(index, roadColor1, index*SEGMENT_DISTANCE));
            //segments.getLast().getSprites().add(leftDecoration);
            //segments.getLast().getSprites().add(rightDecoration);
        } else segments.add(new Segment(index, roadColor2, index*SEGMENT_DISTANCE));       
    }
    
    public Segment getSegment(double positionZ){ 
        // la posizione può essere negativa dal momento che la camera è inizializzata prima del primo segmento
        if(positionZ<0){ 
            positionZ += sectionLength;
        }
        
        int index = (int)Math.floor(positionZ / SEGMENT_DISTANCE) % NUM_SEGMENTS;
        return segments.get(index);
    }
    
    private void populateDecorationsList(){
        Sprite tree = new Sprite(33, 62, 142, 769, decorationsImage);
        Sprite purpleTent = new Sprite(63, 110, 33, 0, decorationsImage);
        Sprite blueTent = new Sprite(63, 110, 96, 0, decorationsImage);
        Sprite flag = new Sprite(40, 80, 0, 120, decorationsImage);
        randomDecorations.add(tree);
        randomDecorations.add(purpleTent);
        randomDecorations.add(blueTent);
        randomDecorations.add(flag);
    }
    
    // popola gli arraylist contenenti gli sprites associati a ciascun segmento
    private void locateDecorativeSprites(){ 
        Sprite pole = new Sprite(260, 100, 0, 0, poleImage);
        segments.get(0).getSprites().add(pole);
        // per ciascun segmento viene randomizzato il posizionamento, locate = 0 significa "non posizionare", locate = 1 "posizionare"
        int locate, index; 
        // anche l'offset per ciascuno sprite è randomizzato
        double offset; 
        // limite inferiore per il posizionamento degli elementi decorativi
        double minOffset = 2.0;
        // limite superiore 
        double maxOffset = 20.0; 
        Sprite decoration;
        for(Segment segment: segments){
            locate = (int)(Math.random()*2); // sfruttando il casting si ottiene 0 o 1 in quanto Math.random restituisce un valore nell'intervallo [0.0, 1.0[ 
            if(locate == 1){
                // segmenti con indice pari hanno sprite decorativi a sinistra, con indice dispari a destra 
                if(segment.getIndex()%2 == 0){      
                    offset = -(minOffset + Math.random()*(maxOffset - minOffset));         
                } else offset = minOffset + Math.random()*(maxOffset - minOffset); 
                index = (int)(Math.random()*randomDecorations.size());
                decoration = new Sprite(randomDecorations.get(0));  
                decoration.setOffset(offset);
                if(segment.getIndex()!=0){
                     segment.getSprites().add(decoration);
                }
            }
        }
        
        Sprite leftDecoration = new Sprite(randomDecorations.get(0));
        leftDecoration.setOffset(-1.3);
        Sprite rightDecoration = new Sprite(randomDecorations.get(0));        
        rightDecoration.setOffset(1.3);
        
        for(int i=1; i<NUM_SEGMENTS; i=i+RUMBLE_SEGMENTS){
            segments.get(i).getSprites().add(leftDecoration);
            segments.get(i).getSprites().add(rightDecoration);
        }
    }

    public ArrayList<Segment> getSegments() {
        return segments;
    }

    public double getSectionLength() {
        return sectionLength;
    }    
}
