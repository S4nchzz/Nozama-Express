package nozama.f00_Login.passwordChanger;

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

public class ChangePassword {
    private final Stage stage;
    private final LoginPage loginController;

    @FXML
    private TextField fxid_username_field_changepass;
    
    @FXML
    private TextField fxid_password_field_changepass;

    @FXML
    private TextField fxid_admin_username_field_changepass;

    @FXML
    private TextField fxid_admin_password_field_changepass;

    private String userAdmin;
    private String passAdmin;
    private String user;
    private String pass;

    public ChangePassword (Stage s, LoginPage l) {
        this.stage = s;
        this.loginController = l;
    }

    @FXML
    public void handleChange () throws IOException {
        this.userAdmin = fxid_admin_username_field_changepass.getText();
        this.passAdmin = fxid_admin_password_field_changepass.getText();
        this.user = fxid_username_field_changepass.getText();
        this.pass = fxid_password_field_changepass.getText();

        if (checkFieldSyntax() && DatabaseRequestManagment.isAdmin(userAdmin, passAdmin)) {
            if (!DatabaseRequestManagment.acceder(user, pass)) {

                DatabaseRequestManagment.cambiarContrasena(user, pass);
    
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/nozama/login/login.fxml"));
                loader.setController(loginController);
    
                Parent p = loader.load();
                Scene s = new Scene(p,NozamaWindowApp.LOGIN_WIDTH,NozamaWindowApp.LOGIN_HEIGTH);
    
                stage.setTitle("Nozama Express");
                stage.setScene(s);
    
                stage.show();
            } else {
                JOptionPane.showMessageDialog(null, "Asegurate de que no usas la misma contraseña");
            }
        } else {
            JOptionPane.showMessageDialog(null, "El usuario administrador no tiene los permisos suficientes");
        }
    }

    private boolean checkFieldSyntax() {
        if ((!user.isEmpty() || !pass.isEmpty()) && (!user.isBlank() || !pass.isBlank()) &&
                (!userAdmin.isEmpty() || !passAdmin.isEmpty()) && (!userAdmin.isBlank() || !passAdmin.isBlank())) {
            return true;
        }

        return false;
    }

    @FXML
    private void handleBack() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/nozama/login/login.fxml"));

        loader.setController(loginController);
        Parent p = loader.load();

        Scene s = new Scene(p, NozamaWindowApp.LOGIN_WIDTH, NozamaWindowApp.LOGIN_HEIGTH);
        stage.setScene(s);
    }
}