package horsie.chess.data;

import com.google.gson.Gson;
import horsie.chess.model.PlayerTurn;
import org.tinylog.Logger;

import java.io.*;
import java.util.Objects;


/**
 * Cashes the global states of the game, like the player names and who won in a Redis db.
 */
public class HorsieChessCacheData {

    PlayerCache cache;

    private final Gson gson;

    public HorsieChessCacheData(){
        gson = new Gson();

        readFile();
    }



    /**
     * Set player name int the cache object
     * @param player1 white side
     * @param player2 black side
     */
    public void setPlayerNames(String player1, String player2){
        this.setPlayer1(player1);
        this.setPlayer2(player2);
        saveFile();
    }

    public String getPlayer1(){
        return cache.getPlayer1();
    }

    public void setPlayer1(String player1Name){
        cache.setPlayer1(player1Name);
        saveFile();
    }

    public String getPlayer2(){
        return cache.getPlayer2();
    }

    public void setPlayer2(String player2Name){
        cache.setPlayer2(player2Name);
        saveFile();
    }

    public String getPlayerWon(){
        return cache.getPlayerWon();
    }

    public void setPlayerWon(PlayerTurn winnerPlayer){
        switch (winnerPlayer){
            case WHITE -> cache.setPlayerWon(cache.getPlayer1());
            case BLACK -> cache.setPlayerWon(cache.getPlayer2());
            case NONE_OF_THEM -> {}
        }
        saveFile();
        Logger.info("Player set in the cache. {}", getPlayerWon());
    }


    private void readFile(){
        try {
            BufferedReader reader = new BufferedReader(new FileReader(Objects.requireNonNull(getClass().getResource("/cache.json")).getPath()));
            cache = gson.fromJson(reader, PlayerCache.class);
            Logger.info("Cache initialized: {} ", cache.toString());
            reader.close();
        } catch (IOException e) {
            Logger.error("Cache reader error: {}", e.toString());
            e.printStackTrace();
        }
    }


    private void saveFile(){
        try {
            FileWriter writer = new FileWriter(Objects.requireNonNull(getClass().getResource("/cache.json")).getPath());
            gson.toJson(cache, writer);
            Logger.info("Cache saved.");
            writer.close();
        } catch (IOException e) {
            Logger.error("Cache writer error: {}", e.toString());
            e.printStackTrace();
        }
    }

}
