
package jetbikerace.assetsManagers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import jetbikerace.scenes.GameSession;
import jetbikerace.scenes.GeneralScene;
import jetbikerace.sprites.Sprite;

public class HUD {
    private final String NUMBER_IMAGE_PATH= "assets/numbers.png";
    private final String CRONOICON_IMAGE_PATH= "assets/cronoSprites.png";
    private final String JOHNNYICON_IMAGE_PATH= "assets/johnnySprites.png";
    private final String RACEICON_IMAGE_PATH= "assets/raceIconSprites.png";
    
    private final double NUMBER_WIDTH = 102;
    private final double NUMBER_HEIGHT = 138;
    
    private final Sprite cronoIcon, johnnyIcon, startIcon, goalIcon;
    private Sprite numbers[];
    
    private Image numbersImage;
    private Image cronoImage;
    private Image johnnyImage;
    private Image raceImage;
    
    
    public HUD(){
        numbers = new Sprite[10];
        try {
            numbersImage = new Image(Files.newInputStream(Paths.get(NUMBER_IMAGE_PATH)));
            cronoImage = new Image(Files.newInputStream(Paths.get(CRONOICON_IMAGE_PATH)));
            johnnyImage = new Image(Files.newInputStream(Paths.get(JOHNNYICON_IMAGE_PATH)));
            raceImage = new Image(Files.newInputStream(Paths.get(RACEICON_IMAGE_PATH)));
        } catch (IOException ex) {
            Logger.getLogger(HUD.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        numbers[0] = new Sprite(NUMBER_WIDTH, NUMBER_HEIGHT,0,0, numbersImage);
        numbers[1] = new Sprite(NUMBER_WIDTH, NUMBER_HEIGHT,408,138, numbersImage);
        numbers[2] = new Sprite(NUMBER_WIDTH, NUMBER_HEIGHT,102,0, numbersImage);
        numbers[3] = new Sprite(NUMBER_WIDTH, NUMBER_HEIGHT,204,0, numbersImage);
        numbers[4] = new Sprite(NUMBER_WIDTH, NUMBER_HEIGHT,306,0, numbersImage);
        numbers[5] = new Sprite(NUMBER_WIDTH, NUMBER_HEIGHT,408,0, numbersImage);
        numbers[6] = new Sprite(NUMBER_WIDTH, NUMBER_HEIGHT,0,138, numbersImage);
        numbers[7] = new Sprite(NUMBER_WIDTH, NUMBER_HEIGHT,102,138, numbersImage); 
        numbers[8] = new Sprite(NUMBER_WIDTH, NUMBER_HEIGHT,204,138, numbersImage);
        numbers[9] = new Sprite(NUMBER_WIDTH, NUMBER_HEIGHT,306,138, numbersImage);
        
        cronoIcon = new Sprite(70, 70, 165, 20, cronoImage);
        johnnyIcon = new Sprite(33, 37, 75, 111, johnnyImage);
        startIcon = new Sprite(117, 57, 0, 115, raceImage);
        goalIcon = new Sprite(117, 57, 0, 0, raceImage);
    }
    
    public void drawPoints(GraphicsContext gc, int points){
        double pointWidth = 20;
        double pointHeight = 40;
        
       int units, dozens, hundreds, thousands;
       Sprite unitsNumber, dozensNumber, hundredsNumber, thousandsNumber;
       
       thousands = (points/1000)*1000;
       hundreds = ((points - thousands)/100)*100;
       dozens = ((points - thousands - hundreds)/10)*10;
       units = points - thousands - hundreds- dozens;
       
       thousandsNumber = numbers[thousands/1000];
       thousandsNumber.moveTo(0, 0);
       thousandsNumber.drawRescaledSprite(gc, pointWidth, pointHeight);
       
       hundredsNumber = numbers[hundreds/100];
       hundredsNumber.moveTo(25, 0);
       hundredsNumber.drawRescaledSprite(gc, pointWidth, pointHeight);
       
       dozensNumber = numbers[dozens/10];
       dozensNumber.moveTo(50, 0);
       dozensNumber.drawRescaledSprite(gc, pointWidth, pointHeight);
       
       unitsNumber = numbers[units];
       unitsNumber.moveTo(75, 0);
       unitsNumber.drawRescaledSprite(gc, pointWidth, pointHeight);
    }
    
    public void drawStylizedRace(GraphicsContext gc, RoadManager roadManager){
        MainCharacterManager cronoManager = roadManager.getCronoManager();
        OpponentManager johnnyManager = roadManager.getJohnnyManager();
        
        double iconWidth = 40; //larghezza ed altezza delle due icone dei personaggi
        double iconHeight = 40;
        
        double goalStartWidth = 120; //larghezza, altezza, e posizione delle due icone di partenza ed arrivo
        double goalStartHeight = 60;
        double xGoalIcon = 10;
        double xStartIcon = GeneralScene.GAME_WIDTH - 130;
        double yGoalStartIcon = GeneralScene.GAME_HEIGHT - 70; //le due icone di partenza e arrivo hanno stessa coordinata y
        
        goalIcon.moveTo(xGoalIcon, yGoalStartIcon);
        goalIcon.drawRescaledSprite(gc, goalStartWidth, goalStartHeight);
        startIcon.moveTo(xStartIcon, yGoalStartIcon);
        startIcon.drawRescaledSprite(gc, goalStartWidth, goalStartHeight);
        
        double raceStylizedDistance = startIcon.getX()-(goalIcon.getX() + goalStartWidth);
        double raceEffectiveDistance = roadManager.getRoad().getSectionLength();
        
        double xCronoIcon = xStartIcon - (cronoManager.getzPosition()*raceStylizedDistance)/raceEffectiveDistance;
        double xJohnnyIcon = xStartIcon - (johnnyManager.getzPosition()*raceStylizedDistance)/raceEffectiveDistance;
        double yCronoIcon = (cronoManager.getxOffset()*(goalStartHeight - iconHeight/2))/2;
        double yJohnnyIcon = (johnnyManager.getxOffset()*(goalStartHeight - iconHeight/2))/2;
        
        johnnyIcon.moveTo(xJohnnyIcon, yGoalStartIcon - yJohnnyIcon);
        johnnyIcon.drawRescaledSprite(gc, iconWidth, iconHeight);
        cronoIcon.moveTo(xCronoIcon,  yGoalStartIcon - yCronoIcon);
        cronoIcon.drawRescaledSprite(gc, iconWidth, iconHeight);
    }
    
    public void drawBestScore(GraphicsContext gc, Image pointsMessageImage){
        int bestScore = GameSession.getBestScore();
        
        double pointWidth = pointsMessageImage.getHeight();
        double pointHeight = pointWidth;
        
        double spaceBetween =pointWidth + pointWidth/5;
        double xThousands = pointsMessageImage.getWidth() + GeneralScene.GAME_WIDTH/100;
        double xHundreds = xThousands + spaceBetween;
        double xDozens = xHundreds + spaceBetween;
        double xUnits = xDozens + spaceBetween;
        double yPoints = (GeneralScene.GAME_HEIGHT - pointsMessageImage.getHeight())*99/100;
        
        int units, dozens, hundreds, thousands;
        Sprite unitsNumber, dozensNumber, hundredsNumber, thousandsNumber;
        
       thousands = (bestScore/1000)*1000;
       hundreds = ((bestScore - thousands)/100)*100;
       dozens = ((bestScore - thousands - hundreds)/10)*10;
       units = bestScore - thousands - hundreds- dozens;
       
       thousandsNumber = numbers[thousands/1000];
       thousandsNumber.moveTo(xThousands, yPoints);
       thousandsNumber.drawRescaledSprite(gc, pointWidth, pointHeight);
       
       hundredsNumber = numbers[hundreds/100];
       hundredsNumber.moveTo(xHundreds, yPoints);
       hundredsNumber.drawRescaledSprite(gc, pointWidth, pointHeight);
       
       dozensNumber = numbers[dozens/10];
       dozensNumber.moveTo(xDozens, yPoints);
       dozensNumber.drawRescaledSprite(gc, pointWidth, pointHeight);
       
       unitsNumber = numbers[units];
       unitsNumber.moveTo(xUnits, yPoints);
       unitsNumber.drawRescaledSprite(gc, pointWidth, pointHeight);
    }
}
