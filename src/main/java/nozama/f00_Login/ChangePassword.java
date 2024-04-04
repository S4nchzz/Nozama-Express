package nozama.f00_Login;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import nozama.Main;
import nozama_database.sendRequest.DatabaseRequestManagment;

public class ChangePassword {
    private final Stage stage;
    private final LoginPage loginController;

    @FXML
    private TextField fxid_username_field_changepass;
    
    @FXML
    private TextField fxid_password_field_changepass;

    public ChangePassword (Stage s, LoginPage l) {
        this.stage = s;
        this.loginController = l;
    }

    @FXML
    public void handleChange () throws IOException {
        if ((!fxid_username_field_changepass.getText().isEmpty() || !fxid_password_field_changepass.getText().isEmpty())
                && (!fxid_username_field_changepass.getText().isBlank() || !fxid_password_field_changepass.getText().isBlank())) {
            DatabaseRequestManagment.cambiarContrasena(fxid_username_field_changepass.getText(), fxid_password_field_changepass.getText());

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/nozama/login/login.fxml"));
            loader.setController(loginController);

            Parent p = loader.load();
            Scene s = new Scene(p, Main.LOGIN_WIDTH, Main.LOGIN_HEIGTH);

            stage.centerOnScreen();
            stage.setTitle("Nozama Express");
            stage.setScene(s);

            stage.show();
        }
    }
}