package nozama;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import nozama.f00_Login.LoginPage;
import nozama_database.sendRequest.DatabaseRequestManagment;
import nozama_database.setttingUp.DatabaseLinkTest;

public class Main extends Application {
    public static double LOGIN_WIDTH;
    public static double LOGIN_HEIGTH;

    public Main () {
        LOGIN_WIDTH = 969;
        LOGIN_HEIGTH = 588;
    }

    @Override
    public void start(Stage stage) throws Exception {
        // Link to the database
        DatabaseLinkTest.createDBandTB(3306);

        DatabaseRequestManagment.anadir("iyansd", "8445", true, "Iyan Sanchez", "640183448");

        // Instancia de FXMLLoader
        FXMLLoader loader = new FXMLLoader();
        // Se le da la ubicacion del fxml a usar
        loader.setLocation(getClass().getResource("/nozama/login/login.fxml"));

        // Se crea una instancia del controlador a usar (clase que va a interaccionar con el usuario)
        LoginPage controller = new LoginPage(stage);

        // Al loader se le da ese controller (Si simplemente en el FXMLLoader se hace un .load() 
        // se instancia automaticamente buscando un constructor sin parametros)
        loader.setController(controller);

        // Se crea un parent con el loader predefinido
        Parent login_frame = loader.load();

        // Se crea una escena con el parent y las dimensiones
        Scene s = new Scene(login_frame, LOGIN_WIDTH, LOGIN_HEIGTH);

        stage.setTitle("Nozama Express");
        stage.setResizable(false);;
        stage.centerOnScreen();
        stage.setScene(s);
        
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}