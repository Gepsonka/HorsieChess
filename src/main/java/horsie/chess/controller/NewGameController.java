package horsie.chess.controller;

import horsie.chess.data.HorsieChessCacheData;
import horsie.chess.model.PlayerTurn;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.nio.file.Paths;

public class NewGameController {

    HorsieChessCacheData cache = new HorsieChessCacheData();
    @FXML
    Button startGameButton;

    @FXML
    Button scoreBoardButton;

    @FXML
    TextField whitePlayerNameTextField;

    @FXML
    TextField blackPlayerNameTextField;

    @FXML
    private void initialize(){
        startGameButton.setOnMouseClicked(event -> {
            startGame();
        });

        scoreBoardButton.setOnMouseClicked(event -> {
            showScoreboard();
        });
    }

    private void startGame(){
        // Players must have names
        if (whitePlayerNameTextField.getText().length() == 0 || blackPlayerNameTextField.getText().length() == 0){
            noPlayerNameError();
            return;
        }

        // setting the credentials on the cache backends
        cache.setPlayerNames(whitePlayerNameTextField.getText(), blackPlayerNameTextField.getText());
        cache.setPlayerWon(PlayerTurn.NONE_OF_THEM);

        Stage gameStage = (Stage) startGameButton.getScene().getWindow();
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("/javafx/ui.fxml"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        gameStage.setScene(new Scene(root));
        gameStage.setResizable(false);
        gameStage.setTitle("Game");
        gameStage.show();

    }

    private void showScoreboard(){
        Parent root;
        try {
            root = FXMLLoader.load(getClass().getResource("/javafx/scoreboard.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Scoreboard");
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.show();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void noPlayerNameError(){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("No player name!");
        alert.setContentText("Both players must have a name!");
        alert.showAndWait();
    }
}

// TODO: connet to redis in func
// TODO: Create Connector class connecting the app and the database