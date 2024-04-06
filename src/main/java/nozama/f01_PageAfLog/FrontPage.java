package nozama.f01_PageAfLog;

import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class FrontPage {
    @FXML
    private Text fxid_usernameAv;

    @FXML
    private Pane fxid_leftPane;

    @FXML
    private Text fxid_leftTextAnimation;

    @FXML
    private ImageView fxid_browseImageAnimation;

    private final ResultSet rs;

    public FrontPage (ResultSet rs) {
        this.rs = rs;
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
    public void initialize() {
        try {
            fxid_usernameAv.setText(rs.getString(5));
        } catch (SQLException sqle) {
            System.out.println(sqle.getMessage());
        }
    }
}