package nozama.f01_PageAfLog.adminPanel;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import nozama.NozamaWindowApp;
import nozama.f00_Login.LoginPage;
import nozama.f01_PageAfLog.FrontPage;
import nozama_database.sendRequest.DatabaseRequestManagment;

public class AdminPanel {
    private final Stage stage;
    private final FrontPage stageControllerFP;
    private final String username;
    private boolean showDatabase = true;
    private ResultSet rs;
    private boolean allInserted;
    private TableData td;

    @FXML
    private Text fxid_usernameAv;
    @FXML
    private ImageView fxid_logoIcon;
    @FXML
    private ImageView fxid_databaseAdminIcon;
    @FXML
    private TextField fxid_queryInjection;
    @FXML
    private Text fxid_errorDatabase;
    @FXML
    private ToggleButton fxid_deleteUser;

    @FXML
    private TableView<TableData> fxid_databaseAdmin;
    @FXML
    private TableColumn<TableData, String> fxid_tableUsername;
    @FXML
    private TableColumn<TableData, String> fxid_tableSalt;
    @FXML
    private TableColumn<TableData, String> fxid_tablePass;
    @FXML
    private TableColumn<TableData, String> fxid_tableisAdmin;
    @FXML
    private TableColumn<TableData, String> fxid_tableName;
    @FXML
    private TableColumn<TableData, String> fxid_tableTelf;
    @FXML
    private TableColumn<TableData, String> fxid_tableGender;

    public AdminPanel (Stage s, FrontPage stageControllerFP, String username) {
        this.stage = s;
        this.stageControllerFP = stageControllerFP;
        this.username = username;
        this.allInserted = false;
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
    private void goBackAction () {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/nozama/frontPage/frontPage.fxml"));
        loader.setController(this.stageControllerFP);

        try {
            Parent p = loader.load();
            Scene s = new Scene(p);

            stage.setTitle("Nozama Express");
            stage.setScene(s);
            stage.centerOnScreen();
            stage.show();
        } catch (IOException e) {

        }
    }

    @FXML
    private void showDatabaseAction () {
        fxid_databaseAdmin.setVisible(showDatabase);
        if (!allInserted) {
            insertRegistersOnTable();
        }
        showDatabase = !showDatabase;
    }

    private void insertRegistersOnTable () {
        this.rs = DatabaseRequestManagment.getAllRegisters();
        if (rs != null) {
            try {                
                while (rs.next()) {
                    td = new TableData(rs.getString(1), rs.getString(2), rs.getString(3), rs.getBoolean(4),
                            rs.getString(5), rs.getString(6), rs.getString(7));

                    fxid_databaseAdmin.getItems().add(td);
                }

                allInserted = true;
            } catch (SQLException sqle) {
                System.out.println(sqle.getMessage());
            }
        }
    }

    @FXML
    private void sendAdminQueryIconSearch () {
        sendAdminQuery();
    }

    @FXML
    private void sendAdminQuery () {
        DatabaseRequestManagment db = new DatabaseRequestManagment();

        if (fxid_queryInjection.getText().isEmpty()) {
            fxid_databaseAdmin.getItems().clear();
            insertRegistersOnTable();
        }

        QueryConditions qc = new QueryConditions(fxid_queryInjection.getText());

        if (qc.conditions()) {
            if (fxid_queryInjection.getText() != null) {
                // Retorno de tipo Object para comparar doble tipo de retorno
                Object obj = db.injectCustomQuery(fxid_queryInjection.getText());

                if (obj instanceof ResultSet) {
                    this.rs = (ResultSet)obj;
                    if (rs != null) {
                        fxid_databaseAdmin.getItems().clear();
                        try {
                            while (rs.next()) {
                                td = new TableData(rs.getString(1), rs.getString(2), rs.getString(3),
                                        rs.getBoolean(4),
                                        rs.getString(5), rs.getString(6), rs.getString(7));
        
                                fxid_databaseAdmin.getItems().add(td);
                            }
                        } catch (SQLException sqle) {
                            System.out.println(sqle.getMessage());
                        }
                    }
                } else if (obj instanceof String) {
                    fxid_errorDatabase.setText((String)obj);
                }
            }
        }
    }

    @FXML
    private void deleteUserAction () {
    
    }

    /**
     * Metood que es llamado cuando se cargan todos los elementos
     * de javafx, este metodo cambiara el nombre del usuario al lado
     * del avatar y por cada columna obtendra la propiedad del valor
     * a mostrar
     */
    @FXML
    public void initialize() {
        fxid_usernameAv.setText(username);
        fxid_tableUsername.setCellValueFactory(new PropertyValueFactory<>("username"));
        fxid_tableSalt.setCellValueFactory(new PropertyValueFactory<>("salt"));
        fxid_tablePass.setCellValueFactory(new PropertyValueFactory<>("pass"));
        fxid_tableisAdmin.setCellValueFactory(new PropertyValueFactory<>("isAdmin"));
        fxid_tableName.setCellValueFactory(new PropertyValueFactory<>("name"));
        fxid_tableTelf.setCellValueFactory(new PropertyValueFactory<>("telf"));
        fxid_tableGender.setCellValueFactory(new PropertyValueFactory<>("gender"));
    }
}