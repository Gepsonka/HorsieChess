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

    private final Gson gson = new Gson();

    private JsonReader reader;

    Type playerScoreListType = new TypeToken<ArrayList<PlayerScore>>(){}.getType();
    private List<PlayerScore> scoreboard;

    public HorsieChessScoreboardData(){
        try {
            reader = new JsonReader(new FileReader(getClass().getResource("/scoreboard.json").getFile()));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        scoreboard = gson.fromJson(reader, new TypeToken<ArrayList<PlayerScore>>() {}.getType());
        Logger.debug("contents of scoreboard: {}", scoreboard.toString());
    }

    public List<PlayerScore> getSortedScoreboard(){
        Collections.sort(scoreboard);
        return scoreboard;
    }
    public void incrementPlayerScore(String name){
        for (var i: scoreboard){
            if (i.getPlayerName() == name) {
                i.setPlayerScore(i.getPlayerScore() + 1);
                return;
            }
        }

        scoreboard.add(new PlayerScore(name, 1));
    }

}
