package nozama.f01_PageAfLog;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import nozama.NozamaWindowApp;
import nozama.f00_Login.LoginPage;
import nozama.f01_PageAfLog.adminPanel.AdminPanel;
public class FrontPage {
    private final Stage stage;
    private final ResultSet rs;
    private final boolean isAdmin;

    @FXML
    private Text fxid_usernameAv;

    @FXML
    private ImageView fxid_adminElement;

    @FXML
    private Pane fxid_paneA;
    @FXML
    private Pane fxid_paneB;
    @FXML
    private Pane fxid_paneC;
    @FXML
    private Pane fxid_paneD;
    @FXML
    private Pane fxid_paneE;


    public FrontPage (ResultSet rs, Stage s, boolean isAdmin) {
        this.rs = rs;
        this.stage = s;
        this.isAdmin = isAdmin;
    }

    @FXML
    private void handleLogof () {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/nozama/login/login.fxml"));
        loader.setController(new LoginPage(stage));

        try {
            Parent p = loader.load();
            Scene s = new Scene(p, NozamaWindowApp.LOGIN_WIDTH, NozamaWindowApp.LOGIN_HEIGTH);
            stage.setScene(s);
            stage.setTitle("Nozama Express");
            stage.centerOnScreen();

            stage.show();
        } catch (IOException e) {
            
        }
    }

    @FXML
    private void handleEnterAdminPanel () {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/nozama/frontPage/adminPanel.fxml"));
        loader.setController(new AdminPanel(this.stage, this));

        try {
            Parent p = loader.load();
            Scene s = new Scene(p);
            stage.setTitle("Nozama Express");
            stage.setScene(s);
            stage.setResizable(false);
            stage.centerOnScreen();
            stage.show();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Metodo que cuando se ejecuta el controlador y se cargan todos
     * los componentes de la clase con la anotacion @FXML, busca
     * initialize() para cargar el siguiente conteindo para que no 
     * de errores null o distintos
     * 
     * Cambiara el contenido del elemento Text al lado de avatar
     * con el nombre del usuario
     *  */ 
    @FXML
    private void initialize() {
        try {
            fxid_usernameAv.setText(rs.getString(5));
        } catch (SQLException sqle) {
            System.out.println(sqle.getMessage());
        }

        if (isAdmin) {
            fxid_adminElement.setVisible(true);
        }
    }
}