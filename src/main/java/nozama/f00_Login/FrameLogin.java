package nozama.f00_Login;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import nozama_database.sendRequest.DatabaseRequestManagment;

public class FrameLogin {
    private final Stage stage;
    @FXML
    private TextField fxid_username_field;

    @FXML
    private TextField fxid_password_field;

    private String loginContent;
    private String passwordContent;

    private final DatabaseRequestManagment dbrs;

    public FrameLogin (Stage stage) {
        this.dbrs = new DatabaseRequestManagment();
        this.stage = stage;
    }

    @FXML
    private void handleLogin() {
        loginContent = fxid_username_field.getText();
        passwordContent = fxid_password_field.getText();

        if (DatabaseRequestManagment.acceder(loginContent, passwordContent)) {
            try {
                Parent front_page = FXMLLoader.load(getClass().getResource("/nozama/frontPage/frontPage.fxml"));
                Scene s = new Scene(front_page, 700, 400);

                stage.setScene(s);
                stage.show();

            } catch (IOException ioe) {
                ioe.getMessage();
            }
        } else {
            System.out.println("error al iniciar");
        }
    }

    public String getLoginContent() {
        return this.loginContent;
    }

    public String getPasswordContent() {
        return this.passwordContent;
    }
}