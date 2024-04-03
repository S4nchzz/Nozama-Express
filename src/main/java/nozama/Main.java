package nozama;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent login_frame = FXMLLoader.load(getClass().getResource("/nozama/frame_login.fxml"));

        Scene s = new Scene(login_frame, 700, 400);

        primaryStage.setScene(s);
     
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}