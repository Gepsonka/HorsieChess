package horsie.chess;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class HorsieChessApplication extends Application{
    @Override
    public void start(Stage stage) throws IOException {
        var res = getClass().getResource("/javafx/ui.fxml");
        System.out.print(res.toString());
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("javafx/newGame.fxml"));
        stage.getIcons().add(new Image(getClass().getClassLoader().getResource("media/white_horsie.png")
                .toString()));
        stage.setTitle("Horsie Chess");
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

}
