package horsie.chess.controller;

import horsie.chess.data.HorsieChessCacheData;
import horsie.chess.model.Position;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;


public class GameOverSceneController {

    HorsieChessCacheData cache = new HorsieChessCacheData();
    @FXML
    private Text gameOverText;

    @FXML
    private Button newGameButton;

    @FXML
    private Button exitButton;

    @FXML
    private void initialize(){
        gameOverText.setText(cache.getPlayerWon() + " won!");

        newGameButton.setOnMouseClicked(e -> newGame());

        exitButton.setOnMouseClicked(event -> quitGame());

    }

    private void quitGame(){
        System.exit(0);
    }

    private void newGame(){
        Stage gameStage = (Stage) exitButton.getScene().getWindow();
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("/javafx/newGame.fxml"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        gameStage.setScene(new Scene(root));
        gameStage.setResizable(false);
        gameStage.setTitle("New game");
        gameStage.show();
    }
}
