package jetbikerace.assetsManagers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import jetbikerace.roadComponents.Camera;
import jetbikerace.sprites.AnimatedSprite;
import jetbikerace.roadComponents.Road;
import jetbikerace.roadComponents.Segment;
import jetbikerace.scenes.GameScene;
import jetbikerace.scenes.GeneralScene;

public class NpcManager {
    private final String NPC_IMAGE_PATH= "assets/hangOnSprites.png";
    
    private GraphicsContext gc; 
    
    private ArrayList<AnimatedSprite> npcs;
    private Image npcImage;
    private double scale; 
    private double npcSpeed;
    private int numNpc;
    private Segment baseSegment;
    
    // mantengo il numero di segmenti passati da ciascuno sprite dall'inizio della gara
    private int[] passedSegments;
    
    public NpcManager(GraphicsContext gc){
        npcs = new ArrayList<>();
        this.gc = gc;     
        try {
            npcImage = new Image(Files.newInputStream(Paths.get(NPC_IMAGE_PATH)));
        } catch (IOException ex) {
            Logger.getLogger(NpcManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        numNpc = 90;
        populateNpcList();
        passedSegments = new int[numNpc];
    }
    
    private void populateNpcList(){
        for(int i=0; i<numNpc; i++){
            AnimatedSprite npc = new AnimatedSprite(30, 54, 0, 0, npcImage);
            npc.setIndex(i);
            npcs.add(npc);
        }
    }
    
    protected void drawNpcs(Road road, Segment prevSegment, Segment currSegment, int cameraPassedSegments){ 
        double currentWidth, currentHeight;
        for(AnimatedSprite npc: npcs){
            if(npc.getCurrSegment() == prevSegment && passedSegments[npc.getIndex()] > cameraPassedSegments){
                scale = interpolate(prevSegment.getScale(), currSegment.getScale(), npc.getPercent());
                double xScreen = interpolate(prevSegment.getxScreen(), currSegment.getxScreen(), npc.getPercent()) + (road.ROAD_WIDTH*npc.getOffset()*(GeneralScene.GAME_WIDTH/2)*scale);
                double yScreen = interpolate(prevSegment.getyScreen(), currSegment.getyScreen(), npc.getPercent());
                currentWidth = (GameScene.SPRITE_WIDTH*scale)*(GeneralScene.GAME_WIDTH/2);
                currentHeight = (GameScene.SPRITE_HEIGHT*scale)*(GeneralScene.GAME_HEIGHT/2);
                npc.moveTo(xScreen - currentWidth/2, yScreen - currentHeight);
                npc.drawRescaledSprite(gc, currentWidth, currentHeight);
            }
        }
    }
    
    protected void update(double deltaTime, Road road, Camera camera){
        int newIndex;
        double zPosition, percent;
        Segment newSegment;
        ArrayList<AnimatedSprite> carsToRemove = new ArrayList<>();
        // aggiorno il segmento su cui è collocata la telecamera
        baseSegment = road.getSegment(camera.getCameraZ()); 
        
        for(AnimatedSprite npc: npcs){
            zPosition = npc.getzPosition() + npc.getSpeed()*deltaTime; 
            percent = (zPosition % road.SEGMENT_DISTANCE)/road.SEGMENT_DISTANCE;
            npc.setzPosition(zPosition);
            npc.setPercent(percent);
            newSegment = road.getSegment(zPosition);
            newIndex = newSegment.getIndex();
            // se l'NPC è arrivato al segmento della telecamera va cancellato dalla lista
            if(newSegment == baseSegment){
                carsToRemove.add(npc);
            } else if(npc.getCurrSegment() != newSegment){
                passedSegments[npc.getIndex()] += newIndex - npc.getCurrSegment().getIndex();
                npc.setCurrSegment(newSegment); 
            }           
        }
        // rimozione effettuata dopo la fine del ciclo sull'ArrayList per non generare eccezioni
        // cars.removeAll(carsToRemove);
        
    }
    
    protected void restart(Road road, Camera camera, int segmentsDistCameraToPlayer){
        double zPosition = 0;
        double xOffset, percent;
        double minZ = 0;
        double maxZ = 5000;
        double minOffset = -1;
        double maxOffset = 1;
        npcSpeed = road.MAX_SPEED*50/100;
        for(AnimatedSprite npc: npcs){
            //zPosition = minZ + Math.random()*(maxZ - minZ);
            zPosition += 1000;
            xOffset = minOffset + Math.random()*(maxOffset - minOffset);
            percent = (zPosition % road.SEGMENT_DISTANCE)/road.SEGMENT_DISTANCE;
            npc.setzPosition(zPosition);
            npc.setOffset(xOffset);
            npc.setPercent(percent);
            npc.setSpeed(npcSpeed);
            npc.setCurrSegment(road.getSegment(zPosition));
            passedSegments[npc.getIndex()] = npc.getCurrSegment().getIndex() + segmentsDistCameraToPlayer;
        }

        // inizializzo anche il segmento su cui è collocata la telecamera
        baseSegment = road.getSegment(camera.getCameraZ()); 
        
    }
    
    private double interpolate(double start, double end, double percent){
        return start +(end - start)*percent;
    }

    public ArrayList<AnimatedSprite> getNpcs() {
        return npcs;
    }

    public double getScale() {
        return scale;
    }

    public double getNpcSpeed() {
        return npcSpeed;
    }
}
