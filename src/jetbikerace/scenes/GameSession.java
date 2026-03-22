
package jetbikerace.scenes;

public class GameSession {
    private static int bestScore = 0;
    private static boolean victory = false;
    private static boolean raceStarted = false;

    public static int getBestScore() {
        return bestScore;
    }

    public static void updateBestScore(int score) {
        if (score > bestScore) {
            bestScore = score;
        }
    }
    
    public static boolean isVictory(){
        return victory;
    }
    
    public static void updateWinner(boolean result){
        victory = result;
    }

    public static boolean isRaceStarted() {
        return raceStarted;
    }

    public static void setRaceStarted(boolean raceStarted) {
        GameSession.raceStarted = raceStarted;
    }
}
