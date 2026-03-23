package jetbikerace.ai;

import jetbikerace.assetsManagers.MainCharacterManager;
import jetbikerace.assetsManagers.OpponentManager;
import jetbikerace.assetsManagers.RoadManager;
import jetbikerace.scenes.GameScene;
import jetbikerace.scenes.GeneralScene;
import jetbikerace.sprites.AnimatedSprite;

public class OpponentDynamicAI {

    private RoadManager roadManager;
    private MainCharacterManager cronoManager;
    private OpponentManager johnnyManager;

    public OpponentDynamicAI(RoadManager roadManager) {
        this.roadManager = roadManager;
        this.cronoManager = roadManager.getCronoManager();
        this.johnnyManager = roadManager.getJohnnyManager();

    }

    protected void overtakeNpcs(double opponentSpeed, double opponentZPosition, double opponentXOffset, AnimatedSprite sprite, double deltaTime) {
        double spriteSpeed = sprite.getSpeed();
        double newOpponentZ = johnnyManager.getzPosition() + johnnyManager.getSpeed() * deltaTime;
        double newSpriteZ = sprite.getzPosition() + spriteSpeed * deltaTime;

        double xWorldOpponent = johnnyManager.getxOffset() * (roadManager.getRoad().ROAD_WIDTH / 2);
        double xWorldSprite = sprite.getOffset() * (roadManager.getRoad().ROAD_WIDTH / 2);
        double width = GameScene.SPRITE_WIDTH;
        // condizioni per avere spazio di sorpasso a sinistra e a destra
        boolean leftFree = xWorldSprite + roadManager.getRoad().ROAD_WIDTH / 2 >= width;
        boolean rightFree = roadManager.getRoad().ROAD_WIDTH / 2 - xWorldSprite >= width;
        width = width * 40 / 100;
        // controllo se opponent e sprite hanno almeno un'area di intersezione sull'asse X
        if (newOpponentZ >= newSpriteZ) {
            if ((xWorldOpponent - width / 2 >= xWorldSprite - width / 2) && (xWorldOpponent - width / 2 <= xWorldSprite + width / 2) && rightFree) {
                johnnyManager.move(AnimatedSprite.RIGHT);
            } else if ((xWorldOpponent + width / 2 >= xWorldSprite - width / 2) && (xWorldOpponent + width / 2 <= xWorldSprite + width / 2) && leftFree) {
                johnnyManager.move(AnimatedSprite.LEFT);
            } else {
                if (leftFree) {
                    johnnyManager.move(AnimatedSprite.LEFT);
                } else if (rightFree) {
                    johnnyManager.move(AnimatedSprite.RIGHT);
                }
            }
        }
    }
}
