package nozama.f00_Login.accountCreation;

import java.io.IOException;

import javax.swing.JOptionPane;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.stage.Stage;
import nozama.NozamaWindowApp;
import nozama.f00_Login.LoginPage;
import nozama_database.sendRequest.DatabaseRequestManagment;

/**
 * Clase que crea una cuenta dependiendo de unos 
 * valores dados por el usuario
 */
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
    @FXML
    private ToggleButton fxid_womanButton;
    @FXML
    private ToggleButton fxid_manButton;

    private String username;
    private String password;
    private String fullname;
    private String telf;
    private String gender;
    private int userID;

    private Conditions conditions;
    
    /**
     * Constructor de la clase que inicializa stage, loginController
     * y el loader
     * @param s Stage actual para cambiar la ventana actual en vez de
     * crear otra nueva
     * @param l Referencia de la clase LoginPage
     */
    public CreateAccount (Stage s, LoginPage l, int userID) {
        this.stage = s;
        this.loginController = l;
        this.loader = new FXMLLoader();
        this.userID = userID;
    }

    /**
     * Cuando se pulse el boton de crearCuenta se hace una llamada a setStrings()
     * para poder tener el codigo mas limpio, se crea una instancia de la clase
     * Conditions con los valores y se comprueba que los valores establecidos
     * son correctos y no tienen error, una vez se comprueben se añade el usuario
     * y al loader que se cargo en el constructor se le da una location del fxml
     * para volver al login y se le da el controlador que se le paso al constructor
     * @throws IOException
     */
    @FXML
    private void handleSingup () throws IOException {
        setStrings();
        // Se crea una instancia de conditions enviandole los String de cada campo
        this.conditions = new Conditions(username, fullname, telf, password, userID);

        // Si todas las condiciones se cumplen añade el usuario
        if (conditions.usernameConditions() && conditions.fullNameConditions() && conditions.telfConditions() && conditions.passwordConditions()) {
            if (gender != null) {
                DatabaseRequestManagment.anadir(username, password, false, fullname, telf, gender);
    
                loader.setLocation(getClass().getResource("/nozama/login/login.fxml"));
                loader.setController(loginController);
    
                Parent p = loader.load();
                Scene s = new Scene(p,NozamaWindowApp.LOGIN_WIDTH,NozamaWindowApp.LOGIN_HEIGTH);
    
                stage.setTitle("Nozama Express");
                stage.setScene(s);
    
                stage.show();
            } else {
                JOptionPane.showMessageDialog(null, "Elije un genero");
            }
        }
    }

    /**
     * Cuando este metodo sea llamado se establecera dependiendo 
     * del boton pulsado en el grupo de botones de genero M || W
     * ademas se le cambiara el color del fondo del boton para que
     * sea mas sencillo ver la opcion seleccionada
     */
    @FXML
    private void handleGender () {
        if (fxid_manButton.isSelected()) {
            fxid_manButton.setStyle("-fx-background-color: rgb(196, 195, 195);");
            fxid_womanButton.setStyle("-fx-background-color: white;");
            this.gender = "M";
        } else if (fxid_womanButton.isSelected()) {
            fxid_womanButton.setStyle("-fx-background-color: rgb(196, 195, 195);");
            fxid_manButton.setStyle("-fx-background-color: white;");
            this.gender = "W";
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

    /**
     * Cambio de TextField.getText() a Strings normales 
     */
    private void setStrings() {
        this.username = fxid_username_field_singup.getText();
        this.password = fxid_password_field_singup.getText();
        this.fullname = fxid_fullname_field.getText();
        this.telf = fxid_telf_field.getText();
    }
}
