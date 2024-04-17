package nozama;

import javax.swing.JOptionPane;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import nozama.f00_Login.LoginPage;
import nozama_database.sendRequest.DatabaseRequestManagment;
import nozama_database.setttingUp.DatabaseLinkTest;

public class NozamaWindowApp extends Application {
    public static double LOGIN_WIDTH = 969;
    public static double LOGIN_HEIGTH = 588;

    @Override
    public void start(Stage stage) throws Exception {
        // Check connectivity
        if (DatabaseLinkTest.createDBandTB(3310) || DatabaseLinkTest.createDBandTB(3310)) {
            DatabaseRequestManagment.anadir("a", "a", true, "Iyan", "1", "M");
            DatabaseRequestManagment.anadir("b", "a", true, "Iyan", "1", "M");
            DatabaseRequestManagment.anadir("c", "a", true, "Iyan", "1", "M");
            DatabaseRequestManagment.anadir("d", "a", true, "Iyan", "1", "M");
            DatabaseRequestManagment.anadir("e", "a", true, "Iyan", "1", "M");
            DatabaseRequestManagment.anadir("f", "a", true, "Iyan", "1", "M");
            // Link to the database    
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
        } else {
            JOptionPane.showMessageDialog(null, "No hay conexion con la base de datos");
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}