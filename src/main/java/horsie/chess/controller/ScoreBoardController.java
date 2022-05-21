package horsie.chess.controller;

import horsie.chess.data.HorsieChessScoreboardData;
import horsie.chess.data.PlayerScore;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.tinylog.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;


public class ScoreBoardController {

    private final HorsieChessScoreboardData playersScore = new HorsieChessScoreboardData();

    private ObservableList<PlayerScore> scoreboardData;

    @FXML
    private TableView scoreboardTable;

    @FXML
    private TableColumn<PlayerScore, String> playerNameTableColumn;

    @FXML
    private TableColumn<PlayerScore, Integer> playerScoreTableColumn;


    @FXML
    ObservableList<PlayerScore> items;

    @FXML
    private void initialize(){
        var scoreboard = playersScore.getSortedScoreboard();
        Logger.debug("Content of sorted json: {}", scoreboard.toString());

        scoreboardData = FXCollections.observableArrayList(scoreboard);

        playerNameTableColumn.setCellValueFactory(new PropertyValueFactory<PlayerScore, String>("playerName"));
        playerScoreTableColumn.setCellValueFactory(new PropertyValueFactory<PlayerScore, Integer>("playerScore"));

        scoreboardTable.setItems(scoreboardData);
    }

}
