package horsie.chess.data;

public class PlayerCache {
    private String player1;

    private String player2;

    private String playerWon;

    public PlayerCache(String player1, String player2, String playerWon){
        this.player1 = player1;
        this.player2 = player2;
        this.playerWon = playerWon;
    }

    @Override
    public String toString(){
        return "[" + player1 + ", " + player2 + "]";
    }

    public String getPlayerWon() {
        return playerWon;
    }

    public void setPlayerWon(String playerWon) {
        this.playerWon = playerWon;
    }

    public String getPlayer1() {
        return player1;
    }

    public void setPlayer1(String player1) {
        this.player1 = player1;
    }

    public String getPlayer2() {
        return player2;
    }

    public void setPlayer2(String player2) {
        this.player2 = player2;
    }
}
