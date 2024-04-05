package nozama.f00_Login.accountCreation;

import java.io.IOException;

import javax.swing.JOptionPane;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import nozama.NozamaWindowApp;
import nozama.f00_Login.LoginPage;
import nozama_database.sendRequest.DatabaseRequestManagment;

public class CreateAccount {
    private final Stage stage;
    private final FXMLLoader loader;
    private final LoginPage loginController;

    @FXML
    private TextField fxid_username_field_singup;
    @FXML
    private TextField fxid_password_field_singup;
    @FXML
    private TextField fxid_fullname_field;
    @FXML
    private TextField fxid_telf_field;

    private String username;
    private String password;
    private String fullname;
    private String telf;

    private Conditions conditions;
    
    public CreateAccount (Stage s, LoginPage l) {
        this.stage = s;
        this.loginController = l;
        this.loader = new FXMLLoader();
    }

    @FXML
    private void handleSingup () throws IOException {
        setStrings();
        // Se crea una instancia de conditions enviandole los String de cada campo
        this.conditions = new Conditions(username, fullname, telf, password);

        // Si todas las condiciones se cumplen añade el usuario
        if (conditions.usernameConditions() && conditions.fullNameConditions() && conditions.telfConditions() && conditions.passwordConditions()) {
            DatabaseRequestManagment.anadir(username, password, false, fullname, telf);

            loader.setLocation(getClass().getResource("/nozama/login/login.fxml"));
            loader.setController(loginController);

            Parent p = loader.load();
            Scene s = new Scene(p,NozamaWindowApp.LOGIN_WIDTH,NozamaWindowApp.LOGIN_HEIGTH);

            stage.setTitle("Nozama Express");
            stage.setScene(s);

            stage.show();
        } else {
            JOptionPane.showMessageDialog(null, "Usuario invalido");
        }  
    }

    /**
     * Metodo que carga un nuevo loader con su controlador 
     * y archivo dependiendo de lo que se quiera conseguir,
     * cuando se pulse el boton que tiene como id OnClick handleBack
     * ira a login, Login es una referencia ya inicializada en el constructor
     * @throws IOException
     */
    @FXML
    private void handleBack () throws IOException  {
        loader.setLocation(getClass().getResource("/nozama/login/login.fxml"));

        loader.setController(loginController);
        Parent p = loader.load();

        Scene s = new Scene(p,NozamaWindowApp.LOGIN_WIDTH,NozamaWindowApp.LOGIN_HEIGTH);
        stage.setScene(s);
    }

    private void setStrings() {
        this.username = fxid_username_field_singup.getText();
        this.password = fxid_password_field_singup.getText();
        this.fullname = fxid_fullname_field.getText();
        this.telf = fxid_telf_field.getText();
    }
}
