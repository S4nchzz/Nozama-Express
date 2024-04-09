package nozama.f01_FrontPage;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.gluonhq.charm.glisten.control.ToggleButtonGroup;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import nozama.NozamaWindowApp;
import nozama.f00_Login.LoginPage;
import nozama.f01_FrontPage.adminPanel.AdminPanel;

public class FrontPage {
    private final Stage stage;
    private final ResultSet rs;
    private final boolean isAdmin;
    private boolean visibleSupport;

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

    @FXML
    private ToggleButtonGroup fxid_supportToggleButtons;
    
    @FXML
    private Pane fxid_supportPane;

    public FrontPage (ResultSet rs, Stage s, boolean isAdmin) {
        this.rs = rs;
        this.stage = s;
        this.isAdmin = isAdmin;
        
        this.visibleSupport = true;
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
        loader.setController(new AdminPanel(this.stage, this, fxid_usernameAv.getText()));

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

    @FXML
    private void handleSupportAction () {
        fxid_supportPane.setVisible(visibleSupport);
        visibleSupport = !visibleSupport;
    }

    @FXML
    private void handleToggleSupport () {
        for (Toggle toggle: fxid_supportToggleButtons.getToggles()) {
            if (toggle.isSelected()) {
                ((ToggleButton) toggle).setStyle("-fx-background-color: rgb(196, 195, 195);");
            } else {
                ((ToggleButton) toggle).setStyle("-fx-background-color: white;");
            }
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