package nozama.f00_Login;

import java.io.IOException;

import javax.swing.JOptionPane;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import nozama.f01_PageAfLog.FrontPage;
import nozama_database.sendRequest.DatabaseRequestManagment;

public class LoginPage {
    private final Stage stage;
    @FXML
    private TextField fxid_username_field;

    @FXML
    private TextField fxid_password_field;

    private String loginContent;
    private String passwordContent;

    public LoginPage (Stage stage) {
        this.stage = stage;
    }

    @FXML
    private void handleLogin() throws IOException {
        loginContent = fxid_username_field.getText();
        passwordContent = fxid_password_field.getText();

        if (DatabaseRequestManagment.acceder(loginContent, passwordContent)) {
            FXMLLoader frontPageLoader = new FXMLLoader();
            frontPageLoader.setLocation(getClass().getResource("/nozama/frontPage/frontPage.fxml"));

            FrontPage controller = new FrontPage();
            frontPageLoader.setController(controller);

            Parent p = frontPageLoader.load();

            Scene s = new Scene(p, 700, 400);
            stage.centerOnScreen();
            stage.setTitle("Nozama Express");
            stage.setScene(s);

            stage.show();
        } else {
            JOptionPane.showMessageDialog(null, "Contraseña invalida");
        }
    }

    @FXML
    private void handleCreateAccount() throws IOException {
        FXMLLoader singup_loader = new FXMLLoader();
        singup_loader.setLocation(getClass().getResource("/nozama/login/createAccount.fxml"));
        singup_loader.setController(new CreateAccount(stage));

        Parent p = singup_loader.load();

        Scene s = new Scene(p, 1000, 588);
        stage.centerOnScreen();
        stage.setTitle("Nozama Express");
        stage.setScene(s);

        stage.show();

    }

    public String getLoginContent() {
        return this.loginContent;
    }

    public String getPasswordContent() {
        return this.passwordContent;
    }
}