package horsie.chess.data;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import org.jetbrains.annotations.NotNull;

public class PlayerScore implements Comparable<PlayerScore>{
    private String playerName;
    private int playerScore;


    public PlayerScore(String playerName, int playerScore){
        this.playerName = playerName;
        this.playerScore = playerScore;
    }


    /**
     * Must be implemented bc we would like to compare these objects to sort and
     * create scoreboard.
     * @param playerScore
     * @return
     */
    @Override
    public int compareTo(@NotNull PlayerScore playerScore) {
        if (this.getPlayerScore() == playerScore.getPlayerScore()){
            return 0;
        } else if (this.getPlayerScore() > playerScore.getPlayerScore()){
            return 1;
        } else {
            return -1;
        }
    }

    @Override
    public String toString(){
        return "PlayerScore: " + "name: " + getPlayerName() + ", score: " + getPlayerScore();
    }


    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String name) {
        playerName = name;
    }

    public void setPlayerScore(int playerScore) {
        this.playerScore = playerScore;
    }

    public int getPlayerScore() {
        return playerScore;
    }
}
