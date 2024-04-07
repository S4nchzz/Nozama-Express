package nozama.f00_Login;

import java.io.IOException;

import javax.swing.JOptionPane;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import nozama.NozamaWindowApp;
import nozama.f00_Login.accountCreation.CreateAccount;
import nozama.f00_Login.passwordChanger.ChangePassword;
import nozama.f01_PageAfLog.FrontPage;
import nozama_database.sendRequest.DatabaseRequestManagment;

/**
 * Clase que permite al usuario loguearse en el sistema
 */
public class LoginPage {
    private final Stage stage;

    @FXML
    private TextField fxid_username_field;

    @FXML
    private TextField fxid_password_field;

    private String loginContent;
    private String passwordContent;

    /**
     * Constructor con el Stage a usar
     * @param stage
     */
    public LoginPage (Stage stage) {
        this.stage = stage;
    }

    /**
     * Metodo que cuando es llamado intenta acceder con
     * el usuario y contraseña elegidos hacieno una llamada
     * a DatabaseRequestManagement.acceder, si esto se cumple
     * se carga en escena el frontpage.fxml con el controlador
     * FrontPage (nueva instancia)
     * @throws IOException
     */
    @FXML
    private void handleLogin() throws IOException {
        loginContent = fxid_username_field.getText();
        passwordContent = fxid_password_field.getText();

        if (DatabaseRequestManagment.acceder(loginContent, passwordContent)) {
            FXMLLoader frontPageLoader = new FXMLLoader();
            frontPageLoader.setLocation(getClass().getResource("/nozama/frontPage/frontPage.fxml"));

            FrontPage controller = new FrontPage(DatabaseRequestManagment.getQueryResult(loginContent), stage, DatabaseRequestManagment.isAdmin(loginContent, passwordContent));
            frontPageLoader.setController(controller);

            Parent p = frontPageLoader.load();

            Scene s = new Scene(p);
            stage.setTitle("Nozama Express");
            stage.setScene(s);
            stage.setResizable(false);
            stage.centerOnScreen();

            stage.show();
        } else {
            JOptionPane.showMessageDialog(null, "Usuario o contraseña invalidao");
        }
    }

    /**
     * Metodo que cuando es llamado se pone en escena
     * el createAccount.fxml con su controlador
     * @throws IOException
     */
    @FXML
    private void handleCreateAccount() throws IOException {
        FXMLLoader singup_loader = new FXMLLoader();
        singup_loader.setLocation(getClass().getResource("/nozama/login/createAccount.fxml"));
        singup_loader.setController(new CreateAccount(stage, this));

        Parent p = singup_loader.load();

        Scene s = new Scene(p,NozamaWindowApp.LOGIN_WIDTH,NozamaWindowApp.LOGIN_HEIGTH);
        stage.setTitle("Nozama Express");
        stage.requestFocus();
        stage.setScene(s);

        stage.show();

    }

    /**
     * Metodo que cuando es llamado se pone en escena
     * el changePass.fxml con su controlador
     * @throws IOException
     */
    @FXML
    private void handleChangePass() throws IOException {
        FXMLLoader singup_loader = new FXMLLoader();
        singup_loader.setLocation(getClass().getResource("/nozama/login/changePassword.fxml"));
        singup_loader.setController(new ChangePassword(stage, this));

        Parent p = singup_loader.load();

        Scene s = new Scene(p,NozamaWindowApp.LOGIN_WIDTH,NozamaWindowApp.LOGIN_HEIGTH);
        stage.setTitle("Nozama Express");
        stage.requestFocus();
        stage.setScene(s);

        stage.show();
    }
}