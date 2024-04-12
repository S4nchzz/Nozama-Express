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
import nozama.f00_Login.ObtainIDFromUsername;
import nozama_database.sendRequest.DatabaseRequestManagment;

/**
 * Clase para cambiar la contraseña de un usuario
 */
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
    private int userID;

    /**
     * Constructor de la clase con el Stage actual y
     * el controlador LoginPage para no instanciar todo
     * el rato nuevas clases
     * @param s Stage principal
     * @param l Controlador del login.fxml
     */
    public ChangePassword (Stage s, LoginPage l) {
        this.stage = s;
        this.loginController = l;
        this.userID = userID;
    }

    /**
     * Metodo que cuando es llamado se comprueba si la sintaxis 
     * del usuario y la contraseña son correctos y se hace una llamada
     * al metodo estatico isAdmin para ver si el usuario introducido con
     * privilegios es administrador, una vez esto se cumpla se comprueba si
     * el usuario a cambiar puede acceder al sistema con las credenciales
     * establecidas, si esto se cumple se le dira por un JOptionPane que
     * no puede usar la misma contraseña que tiene puesta, si no se cambia
     * la contraseña y se carga en pantalla con el laoder y el stage el login.fxml 
     * @throws IOException
     */
    @FXML
    public void handleChange () throws IOException {
        setStrings();

        if (checkFieldSyntax() && DatabaseRequestManagment.isAdmin(ObtainIDFromUsername.getID(userAdmin), passAdmin)) {
            if (!DatabaseRequestManagment.acceder(user, pass)) {
                if (pass.length() > 5) {
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
                    JOptionPane.showMessageDialog(null, "La constraseña no puede tener menos de 5 caracteres");
                }
            } else {
                JOptionPane.showMessageDialog(null, "Asegurate de que no usas la misma contraseña");
            }
        } else {
            JOptionPane.showMessageDialog(null, "El usuario administrador no tiene los permisos suficientes");
        }
    }

    /**
     * Condiciones a cumplir del usuario y la contraseña
     * @return
     */
    private boolean checkFieldSyntax() {
        if ((!user.isEmpty() || !pass.isEmpty()) && (!user.isBlank() || !pass.isBlank()) &&
                (!userAdmin.isEmpty() || !passAdmin.isEmpty()) && (!userAdmin.isBlank() || !passAdmin.isBlank())) {
            return true;
        }

        return false;
    }

    /**
     * Metodo que cuando es llamado simplemente carga en escena
     * el login.fxml con su propio controlador (simula volver atras)
     * @throws IOException
     */
    @FXML
    private void handleBack() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/nozama/login/login.fxml"));

        loader.setController(loginController);
        Parent p = loader.load();

        Scene s = new Scene(p, NozamaWindowApp.LOGIN_WIDTH, NozamaWindowApp.LOGIN_HEIGTH);
        stage.setScene(s);
    }

    /**
     * Clase que cambia los TextField.getText() a String directo
     */
    private void setStrings() {
        this.userAdmin = fxid_admin_username_field_changepass.getText();
        this.passAdmin = fxid_admin_password_field_changepass.getText();
        this.user = fxid_username_field_changepass.getText();
        this.pass = fxid_password_field_changepass.getText();
    }
}