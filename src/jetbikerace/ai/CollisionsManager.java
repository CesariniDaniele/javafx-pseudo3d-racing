package jetbikerace.ai;

import jetbikerace.assetsManagers.MainCharacterManager;
import jetbikerace.assetsManagers.RoadManager;
import jetbikerace.scenes.GameScene;
import jetbikerace.sprites.AnimatedSprite;

public class CollisionsManager {
    private MainCharacterManager cronoManager;
    private boolean collision;
    
    public CollisionsManager(MainCharacterManager cronoManager){
        this.cronoManager = cronoManager;
        collision = false;
    }
    
    // rileva collisione fra lo sprite player e l'NPC passato come parametro AnimatedSprite
    protected boolean collision(RoadManager roadManager, AnimatedSprite sprite, double deltaTime) {  
        collision = false;
        double playerZPosition = cronoManager.getzPosition();
        
        double carSpeed = sprite.getSpeed();
        double newPlayerZ = playerZPosition + cronoManager.getSpeed() * deltaTime;
        double newCarZ = sprite.getzPosition() + carSpeed * deltaTime;
        double deltaNowZ = sprite.getzPosition() - playerZPosition;
        double deltaNewZ = newCarZ - newPlayerZ;
        //System.out.println("Ora:" + deltaNowZ + " | Dopo:" + deltaNewZ);
        double xWorldPlayer = cronoManager.getxOffset()*(roadManager.getRoad().ROAD_WIDTH/2);
        double xWorldCar = sprite.getOffset()*(roadManager.getRoad().ROAD_WIDTH/2);
        double width = GameScene.SPRITE_WIDTH;
        // riduzione della hitbox del 30% sia dal lato destro sia dal lato sinistro
        xWorldPlayer = xWorldPlayer + width*30/100;
        xWorldCar = xWorldCar + width*30/100;
        width = width*40/100;
        if ((xWorldPlayer - width/2 >= xWorldCar - width/2 && xWorldPlayer - width/2 <= xWorldCar + width/2) || (xWorldPlayer + width/2 >= xWorldCar - width/2 && xWorldPlayer + width/2 <= xWorldCar + width/2)) {
            if (playerZPosition <= sprite.getzPosition()) {
                if (newPlayerZ >= newCarZ) {
                    collision = true;
                }
            }
        }     
        return collision;
    }
}
