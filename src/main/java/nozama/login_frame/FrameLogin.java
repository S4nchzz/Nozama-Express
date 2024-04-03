package nozama.login_frame;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import nozama_database.sendRequest.DatabaseRequestManagment;

public class FrameLogin {
    @FXML
    private TextField fxid_username_field;

    @FXML
    private TextField fxid_password_field;

    private String loginContent;
    private String passwordContent;

    private final DatabaseRequestManagment dbrs;

    public FrameLogin () {
        this.dbrs = new DatabaseRequestManagment();
    }

    @FXML
    private void handleLogin() {
        loginContent = fxid_username_field.getText();
        passwordContent = fxid_password_field.getText();

        
    }

    public String getLoginContent() {
        return this.loginContent;
    }

    public String getPasswordContent() {
        return this.passwordContent;
    }
}