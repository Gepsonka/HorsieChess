package horsie.chess.data;

import horsie.chess.model.PlayerTurn;
import redis.clients.jedis.Jedis;

/**
 * Cashes the global states of the game, like the player names and who won in a Redis db.
 */
public class HorsieChessCacheData {
    Jedis jedis;


    public HorsieChessCacheData(){
        jedis = new Jedis("127.0.0.1", 6379);
    }

    public void setPlayerNames(String white, String black){
        setWhitePlayerName(white);
        setBlackPlayerName(black);
    }

    public void setWhitePlayerName(String name){
        jedis.set("white", name);
    }

    public String getWhitePlayerName(){
        return jedis.get("white");
    }

    public void setBlackPlayerName(String name){
        jedis.set("black", name);
    }

    public String getBlackPlayerName(){
        return jedis.get("black");
    }

    public void setPlayerWon(PlayerTurn player){
        switch (player){
            case WHITE -> jedis.set("player_won",
                    jedis.get("white"));

            case BLACK -> jedis.set("player_won",
                    jedis.get("black"));

            default -> jedis.set("player_won", "");
        }
    }

    public String getPlayerWon(){
        return jedis.get("player_won");
    }

}
