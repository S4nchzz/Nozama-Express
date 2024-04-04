package nozama.f00_Login.accountCreation;

import java.io.IOException;

import javax.swing.JOptionPane;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import nozama.Main;
import nozama.f00_Login.LoginPage;
import nozama_database.sendRequest.DatabaseRequestManagment;

public class CreateAccount {
    private final Stage stage;
    private final LoginPage loginPage;

    @FXML
    private TextField fxid_username_field_singup;
    @FXML
    private TextField fxid_password_field_singup;
    @FXML
    private TextField fxid_fullname_field;
    @FXML
    private TextField fxid_telf_field;
    
    public CreateAccount (Stage s, LoginPage l) {
        this.stage = s;
        this.loginPage = l;
    }

    @FXML
    private void handleSingup () throws IOException {
        if ((!fxid_username_field_singup.getText().isEmpty() || !fxid_password_field_singup.getText().isEmpty() 
                || !fxid_fullname_field.getText().isEmpty()) || (!fxid_username_field_singup.getText().isBlank()
                || !fxid_password_field_singup.getText().isBlank() || !fxid_fullname_field.getText().isBlank())) {

            // Instancia de condiciones que tiene que seguir el controlador CreateAccount (this)
            Conditions conditions = new Conditions(fxid_username_field_singup, fxid_fullname_field, fxid_telf_field, fxid_password_field_singup);
            
            // Si todas las condiciones se cumplen añade el usuario
            if (conditions.usernameConditions() && conditions.fullNameConditions() && conditions.telfConditions() && conditions.passwordConditions()) {
                DatabaseRequestManagment.anadir(fxid_username_field_singup.getText(),
                        fxid_password_field_singup.getText(), false, fxid_fullname_field.getText(),
                        fxid_telf_field.getText());

                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/nozama/login/login.fxml"));
                loader.setController(loginPage);

                Parent p = loader.load();
                Scene s = new Scene(p, Main.LOGIN_WIDTH, Main.LOGIN_HEIGTH);

                stage.setTitle("Nozama Express");
                stage.setScene(s);

                stage.show();
            } else {
                JOptionPane.showMessageDialog(null, "Usuario invalido");
            }  
        } else {
            JOptionPane.showMessageDialog(null, "Compruebe que los campos no estan vacios a excepcion de Telf");
        }
    }

    @FXML
    private void handleBack () throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/nozama/login/login.fxml"));

        loader.setController(loginPage);
        Parent p = loader.load();

        Scene s = new Scene(p, Main.LOGIN_WIDTH, Main.LOGIN_HEIGTH);
        stage.setScene(s);
    }
}
