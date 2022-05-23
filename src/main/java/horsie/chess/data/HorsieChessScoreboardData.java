package horsie.chess.data;


import com.google.gson.Gson;
import com.google.gson.JsonParser;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import org.tinylog.Logger;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Stores player history of winning game.
 * After each win a player's score gets incremented
 * Stored in scoreboard.json
 */
public final class HorsieChessScoreboardData {

    private final Gson gson;

    private List<PlayerScore> scoreboard;

    public HorsieChessScoreboardData(){
        gson = new Gson();

        readFile();
    }


    /**
     * Returns the sorted scoreboard.
     * @return {@link List<PlayerScore>} List of players on the scoreboard
     */
    public List<PlayerScore> getSortedScoreboard(){
        sortAndReverseScoreboard();
        return scoreboard;
    }

    /**
     * If the player is already has a score, increment that score
     * else add the player to the scoreboard with the value of 1
     * @param name The player's name. {@code PlayerScore.getPlayerName()}
     */
    public void incrementPlayerScore(String name){
        for (var i: scoreboard){
            if (i.getPlayerName().equals(name)) {
                i.setPlayerScore(i.getPlayerScore() + 1);
                saveFile();
                Logger.info("{}'s score has been incremented by 1", name);

                return;
            }
        }

        scoreboard.add(new PlayerScore(name, 1));
        saveFile();
        Logger.info("{}'s got put into the scoreboard with the score of 1.", name);


    }


    /**
     * Sorts the scoreboard in descending order
     */
    public void sortAndReverseScoreboard(){
        Collections.sort(scoreboard);
        // must reverse bc we need desc order
        Collections.reverse(scoreboard);
    }


    private void readFile(){
        try {
            BufferedReader reader = new BufferedReader(new FileReader(Objects.requireNonNull(getClass()
                    .getResource("/scoreboard.json")).getPath()));
            scoreboard = gson.fromJson(reader, new TypeToken<ArrayList<PlayerScore>>(){}.getType());
            Logger.debug("Scoreboard initialized.");
            reader.close();
        } catch (IOException e) {
            Logger.error("Scoreboard reader error: {}", e.toString());
            e.printStackTrace();
        }
        Logger.debug("scoreboard.json has been loaded into the memory.");
    }

    private void saveFile(){
        try {
            FileWriter writer = new FileWriter(Objects.requireNonNull(getClass()
                    .getResource("/scoreboard.json"))
                    .getPath());
            gson.toJson(scoreboard, writer);
            Logger.info("Scoreboard saved.");
            writer.close();
        } catch (IOException e) {
            Logger.error("Scoreboard writer error: {}", e.toString());
            e.printStackTrace();
        }

        Logger.debug("scoreboard.json has been saved.");

    }

}
