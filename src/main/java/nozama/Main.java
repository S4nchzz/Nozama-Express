package nozama;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import nozama_database.setttingUp.DatabaseLinkTest;

public class Main extends Application {
    @Override
    public void start(Stage frame) throws Exception {

        // INICAIR BASE DE DATOS
        DatabaseLinkTest.createDBandTB(3306);

        Parent login_frame = FXMLLoader.load(getClass().getResource("/nozama/login_frame/frame_login.fxml"));
        Scene s = new Scene(login_frame, 700, 400);

        frame.setTitle("Nozama Express");
        frame.setScene(s);

        frame.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}