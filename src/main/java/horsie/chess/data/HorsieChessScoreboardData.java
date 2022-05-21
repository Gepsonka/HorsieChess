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

public final class HorsieChessScoreboardData {

    private final Gson gson;

    private List<PlayerScore> scoreboard;

    public HorsieChessScoreboardData(){
        gson = new Gson();

        readFile();
    }


    public void incrementPlayerScore(String name){
        for (var i: scoreboard){
            if (i.getPlayerName().equals(name)) {
                i.setPlayerScore(i.getPlayerScore() + 1);
                return;
            }
        }

        scoreboard.add(new PlayerScore(name, 1));
        saveFile();

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
    }

    public List<PlayerScore> getSortedScoreboard(){
        Collections.sort(scoreboard);
        return scoreboard;
    }

}
