package jetbikerace.assetsManagers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import jetbikerace.roadComponents.Camera;
import jetbikerace.roadComponents.Road;
import jetbikerace.roadComponents.Segment;
import jetbikerace.scenes.GameScene;
import jetbikerace.scenes.GeneralScene;
import jetbikerace.sprites.Sprite;

public class RoadManager {

    private static final String BACKGROUND_IMAGE = "assets/BackgroundJetBikeRace.png";

    private final Color grassColor1 = Color.LIGHTGREEN;
    private final Color grassColor2 = Color.DARKGREEN;
    private final Color rumbleColor1 = Color.RED;
    private final Color rumbleColor2 = Color.WHITE;

    private Image background;
    private Road road;
    private Camera camera;
    private MainCharacterManager cronoManager;
    private OpponentManager johnnyManager;
    private NpcManager carManager;
    private GraphicsContext gc;

    // segmento e indice del segmento su cui è posizionata la telecamera
    private Segment baseSegment;
    private int baseIndex;

    // mantengo il numero di segmenti passati dalla telecamera dall'inizio della gara
    private int passedSegments, segmentsDistCameraToPlayer;

    public RoadManager(GraphicsContext gc, MainCharacterManager mcg, OpponentManager om) {
        loadBackground();
        road = new Road();
        camera = new Camera(road);
        cronoManager = mcg;
        johnnyManager = om;
        carManager = new NpcManager(gc);
        this.gc = gc;
        segmentsDistCameraToPlayer = (int) (camera.getDIST_TO_ROAD() / road.SEGMENT_DISTANCE);
        passedSegments = 0;
    }

    public RoadManager(GraphicsContext gc) {
        loadBackground();
        road = new Road();
        camera = new Camera(road);
        this.gc = gc;
        segmentsDistCameraToPlayer = (int) (camera.getDIST_TO_ROAD() / road.SEGMENT_DISTANCE);
        passedSegments = 0;
    }

    private void loadBackground() {
        try {
            background = new Image(Files.newInputStream(Paths.get(BACKGROUND_IMAGE)));
        } catch (IOException ex) {
            Logger.getLogger(GameScene.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void project3D(Segment segment, double offsetZ) {
        double correctedCameraZ = camera.getCameraZ() - offsetZ;
        //passaggio 1) TRASLAZIONE
        double xCamera = segment.getxWorld() - camera.getCameraX();
        double yCamera = segment.getyWorld() - camera.getCameraY();
        double zCamera = segment.getzWorld() - correctedCameraZ;

        //passaggio 2) PROIEZIONE 
        double scaleProj = camera.getDistToPlane() / zCamera;
        segment.setScale(scaleProj);

        double xProj = xCamera * scaleProj;
        double yProj = yCamera * scaleProj;
        double widthProj = road.ROAD_WIDTH * scaleProj; //metà lunghezza del segmento proiettato sul piano normalizzato

        //passaggio 3) SCALATURA
        double xScreen = GeneralScene.GAME_WIDTH / 2 + (GeneralScene.GAME_WIDTH / 2) * xProj;
        double yScreen = GeneralScene.GAME_HEIGHT / 4 - (GeneralScene.GAME_HEIGHT / 4) * yProj;
        double widthScreen = (GeneralScene.GAME_WIDTH / 2) * widthProj;

        segment.setxScreen(xScreen);
        segment.setyScreen(yScreen);
        segment.setWidthScreen(widthScreen);
    }

    private void drawRoad() {
        gc.clearRect(0, 0, GeneralScene.GAME_WIDTH, GeneralScene.GAME_HEIGHT);
        drawBackground();

        ArrayList<Segment> segments = road.getSegments();
        
        Segment currSegment, prevSegment, newBaseSegment;
        int currIndex, prevIndex, newBaseIndex, shiftedBaseIndex, shiftedNewBaseIndex;

        double clipBottomLine = GeneralScene.GAME_HEIGHT;
        double currBottomLine, offsetZ;

        //segmento su cui è collocata la camera
        newBaseSegment = road.getSegment(camera.getCameraZ());
        newBaseIndex = newBaseSegment.getIndex();
        // aggiornamento e calcolo dei segmenti attraversati con shiting dovuto alla posizione iniziale della telecamera 
        if (baseSegment != newBaseSegment) {
            shiftedBaseIndex = (baseIndex + segmentsDistCameraToPlayer) % road.NUM_SEGMENTS;
            shiftedNewBaseIndex = (newBaseIndex + segmentsDistCameraToPlayer) % road.NUM_SEGMENTS;
            passedSegments += shiftedNewBaseIndex - shiftedBaseIndex;
            baseSegment = newBaseSegment;
            baseIndex = newBaseIndex;
        }

        for (int i = 0; i < road.VISIBLE_SEGMENTS; i++) {
            currIndex = (baseIndex + i) % road.NUM_SEGMENTS;
            currSegment = segments.get(currIndex);

            if (currIndex < baseIndex) {
                offsetZ = road.getSectionLength();
            } else {
                offsetZ = 0;
            }

            project3D(currSegment, offsetZ);

            currBottomLine = currSegment.getyScreen();

            if ((i > 0) && (currBottomLine < clipBottomLine)) {
                if (currIndex > 0) {
                    prevIndex = currIndex - 1;
                } else {
                    prevIndex = road.NUM_SEGMENTS - 1;
                }

                prevSegment = segments.get(prevIndex);

                drawPolygon(prevSegment, currSegment);
               
                clipBottomLine = currBottomLine;
            }
        }
    }

    private void drawPolygon(Segment prevSegment, Segment currSegment) {
        if (currSegment.getColor() == road.roadColor1) {
            gc.setFill(grassColor1);
        } else {
            gc.setFill(grassColor2);
        }
        gc.fillRect(0, currSegment.getyScreen(), GeneralScene.GAME_WIDTH, prevSegment.getyScreen() - currSegment.getyScreen());

        double xPoints[] = new double[4]; //coordinata x di ciascuno dei vertici del poligono, da quello in basso a sinistra in senso ANTIORARIO (anche se è indifferente l'ordine)
        double yPoints[] = new double[4]; //idem coordinata y

        xPoints[0] = prevSegment.getxScreen() - prevSegment.getWidthScreen();
        xPoints[1] = prevSegment.getxScreen() + prevSegment.getWidthScreen();
        xPoints[2] = currSegment.getxScreen() + currSegment.getWidthScreen();
        xPoints[3] = currSegment.getxScreen() - currSegment.getWidthScreen();

        yPoints[0] = prevSegment.getyScreen();
        yPoints[1] = prevSegment.getyScreen();
        yPoints[2] = currSegment.getyScreen();
        yPoints[3] = currSegment.getyScreen();

        gc.setFill(currSegment.getColor());
        gc.fillPolygon(xPoints, yPoints, 4);
        drawRumbleStrips(prevSegment, currSegment);
    }

    // segue la logica del painters algorithm e disegna sia sprite decorativi sia npc
    private void drawRacers() {
        ArrayList<Segment> segments = road.getSegments();
        Segment currSegment, prevSegment;
        int currIndex, prevIndex;

        for (int n = road.VISIBLE_SEGMENTS - 1; n > 0; n--) {
            currSegment = segments.get((baseSegment.getIndex() + n) % segments.size());
            currIndex = currSegment.getIndex();

            drawDecorativeSprites(currSegment);

            if (currIndex > 0) {
                prevIndex = currIndex - 1;
            } else {
                prevIndex = road.NUM_SEGMENTS - 1;
            }

            prevSegment = segments.get(prevIndex);

            carManager.drawNpcs(road, prevSegment, currSegment, passedSegments);

            if (cronoManager.getMainCharacterSegment() == prevSegment) {
                cronoManager.drawMainCharacter(road, prevSegment, currSegment);
            }
            if (johnnyManager.getOpponentSegment() == prevSegment) {
                johnnyManager.drawOpponent(road, prevSegment, currSegment, passedSegments);
            }
        }
    }

    // segue la logica del painters algorithm e disegna solo sprite decorativi
    private void drawDecorations() {
        ArrayList<Segment> segments = road.getSegments();
        Segment currSegment;

        for (int n = road.VISIBLE_SEGMENTS - 1; n > 0; n--) {
            currSegment = segments.get((baseSegment.getIndex() + n) % segments.size());
            drawDecorativeSprites(currSegment);
        }
    }

    private void drawDecorativeSprites(Segment currSegment) {
        double currScale = currSegment.getScale();
        double currentWidth;
        if(currSegment.getIndex() == 0){
            currentWidth = (3200 * currScale) * (GeneralScene.GAME_WIDTH / 2); 
        }else currentWidth = (GameScene.DECORATIVE_SPRITE_WIDTH * currScale) * (GeneralScene.GAME_WIDTH / 2);     
        double currentHeight = (GameScene.DECORATIVE_SPRITE_HEIGHT * currScale) * (GeneralScene.GAME_HEIGHT / 2);
        double xScreen = currSegment.getxScreen();
        // ciclo tutti gli sprite associati al segmento
        for (Sprite sprite : currSegment.getSprites()) {
            // la posizione lungo x è calcolata in base all'offset normalizzato associato allo sprite
            sprite.moveTo((xScreen + (road.ROAD_WIDTH * (GeneralScene.GAME_WIDTH / 2) * currScale * sprite.getOffset()) - currentWidth / 2), (currSegment.getyScreen() - currentHeight));
            sprite.drawRescaledSprite(gc, currentWidth, currentHeight);
        }
    }

    private void drawRumbleStrips(Segment prev, Segment curr) {
        double rumbleWPrev = prev.getWidthScreen() / 8;
        double rumbleWCurr = curr.getWidthScreen() / 8;
        double xPoints[] = new double[4];
        double yPoints[] = new double[4];

        if (curr.getColor() == road.roadColor1) {
            gc.setFill(rumbleColor1);
        } else {
            gc.setFill(rumbleColor2);
            xPoints[0] = prev.getxScreen() - rumbleWPrev / 2;
            xPoints[1] = prev.getxScreen() + rumbleWPrev / 2;
            xPoints[2] = curr.getxScreen() + rumbleWCurr / 2;
            xPoints[3] = curr.getxScreen() - rumbleWCurr / 2;

            yPoints[0] = prev.getyScreen();
            yPoints[1] = prev.getyScreen();
            yPoints[2] = curr.getyScreen();
            yPoints[3] = curr.getyScreen();

            gc.fillPolygon(xPoints, yPoints, 4); //rombo di sinistra
        }

        xPoints[0] = prev.getxScreen() - prev.getWidthScreen() - rumbleWPrev;
        xPoints[1] = prev.getxScreen() - prev.getWidthScreen();
        xPoints[2] = curr.getxScreen() - curr.getWidthScreen();
        xPoints[3] = curr.getxScreen() - curr.getWidthScreen() - rumbleWCurr;

        yPoints[0] = prev.getyScreen();
        yPoints[1] = prev.getyScreen();
        yPoints[2] = curr.getyScreen();
        yPoints[3] = curr.getyScreen();

        gc.fillPolygon(xPoints, yPoints, 4); //rombo di sinistra

        xPoints[0] = prev.getxScreen() + prev.getWidthScreen() + rumbleWPrev;
        xPoints[1] = prev.getxScreen() + prev.getWidthScreen();
        xPoints[2] = curr.getxScreen() + curr.getWidthScreen();
        xPoints[3] = curr.getxScreen() + curr.getWidthScreen() + rumbleWCurr;

        yPoints[0] = prev.getyScreen();
        yPoints[1] = prev.getyScreen();
        yPoints[2] = curr.getyScreen();
        yPoints[3] = curr.getyScreen();

        gc.fillPolygon(xPoints, yPoints, 4); //rombo di destra
    }

    private void drawBackground() {
        Sprite firstLayer = new Sprite(255, 64, 10, 0, background);
        // Sprite secondLayer = new Sprite(255, 64, 310, 0, background);
        Sprite thirdLayer = new Sprite(250, 60, 601, 8, background);
        thirdLayer.moveTo(0, 0);
        thirdLayer.drawRescaledSprite(gc, 1000, 240);
        firstLayer.moveTo(0, 110);
        firstLayer.drawRescaledSprite(gc, 500, 120);
        firstLayer.moveTo(500, 110);
        firstLayer.drawRescaledSprite(gc, 500, 120);
    }

    // render per tutto
    public void renderAll(double deltaTime) {
        camera.update(deltaTime);
        cronoManager.update(deltaTime, road);
        johnnyManager.update(deltaTime, road);
        carManager.update(deltaTime, road, camera);
        drawRoad();
        drawRacers();
    }

    // rendering per la sola strada e decorazioni
    public void renderRoad(double deltaTime) {
        camera.update(deltaTime);
        drawRoad();
        drawDecorations();

    }

    // inizializzazione per tutto
    public void restartAll() {
        road.createRoad();
        camera.restart();
        cronoManager.restart(road);
        johnnyManager.restart(road, segmentsDistCameraToPlayer);
        carManager.restart(road, camera, segmentsDistCameraToPlayer);
        baseSegment = road.getSegment(camera.getCameraZ());
        baseIndex = baseSegment.getIndex();
        passedSegments = 0;
    }

    // inizializzazione per la sola strada e decorazioni
    public void restartRoad() {
        road.createRoad();
        camera.setCurrentSpeed(road.MAX_SPEED / 2);
        baseSegment = road.getSegment(camera.getCameraZ());
        baseIndex = baseSegment.getIndex();
        passedSegments = 0;
    }

    public Road getRoad() {
        return road;
    }

    public Camera getCamera() {
        return camera;
    }

    public MainCharacterManager getCronoManager() {
        return cronoManager;
    }

    public OpponentManager getJohnnyManager() {
        return johnnyManager;
    }

    public NpcManager getCarManager() {
        return carManager;
    }
}
