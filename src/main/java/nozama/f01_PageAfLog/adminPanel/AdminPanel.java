package nozama.f01_PageAfLog.adminPanel;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JOptionPane;

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
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import nozama.NozamaWindowApp;
import nozama.f00_Login.LoginPage;
import nozama.f01_PageAfLog.FrontPage;
import nozama.f01_PageAfLog.adminPanel.tables.stock.StockTable;
import nozama.f01_PageAfLog.adminPanel.tables.stock.TableDataStock;
import nozama.f01_PageAfLog.adminPanel.tables.users.TableDataUsers;
import nozama.f01_PageAfLog.adminPanel.tables.users.UserTable;
import nozama.f01_PageAfLog.adminPanel.queryInjection.QueryConditions;
import nozama.f01_PageAfLog.adminPanel.tables.Tables;
import nozama_database.sendRequest.DatabaseRequestManagment;

public class AdminPanel {
    private final Stage stage;
    private final FrontPage stageControllerFP;
    private final String username;
    private boolean showDatabaseUser = true;
    private boolean showDatabaseStock = true;
    private ResultSet rs;
    private boolean allInsertedUser;
    private boolean allInsertedStock;
    private TableDataUsers tdU;
    private TableDataStock tdS;
    private Tables tS;
    private Tables tU;
    private String query;
    private QueryConditions qc;

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
    private Pane fxid_queryPane;

    @FXML
    private TableView<TableDataUsers> fxid_databaseUser;
    @FXML
    private TableColumn<TableDataUsers, String> fxid_tableUsername;
    @FXML
    private TableColumn<TableDataUsers, String> fxid_tableSalt;
    @FXML
    private TableColumn<TableDataUsers, String> fxid_tablePass;
    @FXML
    private TableColumn<TableDataUsers, String> fxid_tableisAdmin;
    @FXML
    private TableColumn<TableDataUsers, String> fxid_tableName;
    @FXML
    private TableColumn<TableDataUsers, String> fxid_tableTelf;
    @FXML
    private TableColumn<TableDataUsers, String> fxid_tableGender;

    @FXML
    private TableView<TableDataStock> fxid_databaseStock;
    @FXML
    private TableColumn<TableDataStock, String> fxid_stockId;
    @FXML
    private TableColumn<TableDataStock, String> fxid_itemType;
    @FXML
    private TableColumn<TableDataStock, String> fxid_product;
    @FXML
    private TableColumn<TableDataStock, String> fxid_stockAmount;
    @FXML
    private TableColumn<TableDataStock, String> fxid_itemPrice;
    @FXML
    private TableColumn<TableDataStock, String> fxid_discount;

    public AdminPanel (Stage s, FrontPage stageControllerFP, String username) {
        this.stage = s;
        this.stageControllerFP = stageControllerFP;
        this.username = username;
        this.allInsertedUser = false;
        this.allInsertedStock = false;
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
    private void showDatabaseUsers () {
        fxid_queryPane.setVisible(true);
        fxid_databaseUser.setVisible(true);
        fxid_databaseStock.setVisible(false);
        if (!allInsertedUser) {
            tU = new UserTable(fxid_databaseUser, fxid_tableUsername, fxid_tableSalt, fxid_tablePass, fxid_tableisAdmin, fxid_tableName, fxid_tableTelf, fxid_tableGender);
            this.fxid_databaseUser = tU.insertRegistersOnTableUser();
            allInsertedUser = true;
        }
        showDatabaseUser = !showDatabaseUser;
    }

    @FXML
    private void showDatabaseStock() {
        fxid_queryPane.setVisible(true);
        fxid_databaseStock.setVisible(true);
        fxid_databaseUser.setVisible(false);
        if (!allInsertedStock && tS == null) {
            tS = new StockTable(fxid_databaseStock, fxid_itemType,fxid_stockId, fxid_product, fxid_stockAmount, fxid_itemPrice, fxid_discount);
            this.fxid_databaseStock = tS.insertRegistersOnTableUser();
            allInsertedStock = true;
        }
        showDatabaseStock = !showDatabaseStock;
    }

    @FXML
    private void sendAdminQueryIconSearch () {
        sendAdminQuery();
    }

    @FXML
    private void sendAdminQuery () {
        DatabaseRequestManagment db = new DatabaseRequestManagment();
        Object obj = null;
        
        this.query = fxid_queryInjection.getText().toUpperCase();
        
        if (fxid_databaseUser.isVisible()) {
            if (query.isEmpty() || query.isBlank()) {
                fxid_databaseUser.getItems().clear();
                this.fxid_databaseUser = tU.insertRegistersOnTableUser();
            }
        } else if (fxid_databaseStock.isVisible()) {
            if (query.isEmpty() || query.isBlank()) {
                fxid_databaseStock.getItems().clear();
                this.fxid_databaseStock = tS.insertRegistersOnTableUser();
            }
            
        }

        obj = db.injectCustomQuery(query);
        qc = new QueryConditions(query);
        if (qc.conditions()) {
            if (obj instanceof ResultSet) {
                this.rs = (ResultSet) obj;
                if (rs != null && query.contains("FROM USER")) {
                    fxid_databaseUser.getItems().clear();
                    this.fxid_errorDatabase.setText("");
                    try {
                        while (rs.next()) {
                            tdU = new TableDataUsers(rs.getString(1), rs.getString(2), rs.getString(3),
                                    rs.getBoolean(4),
                                    rs.getString(5), rs.getString(6), rs.getString(7));
        
                            fxid_databaseUser.getItems().add(tdU);
                        }
                    } catch (SQLException sqle) {
                        System.out.println(sqle.getMessage());
                    }
                } else if (rs != null && query.contains("FROM STOCK")) {
                    this.rs = (ResultSet) obj;
                    if (rs != null) {
                        fxid_databaseStock.getItems().clear();
                        this.fxid_errorDatabase.setText("");
                        try {
                            while (rs.next()) {
                                tdS = new TableDataStock(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getInt(4), rs.getDouble(5),
                                        rs.getInt(6));
                                fxid_databaseStock.getItems().add(tdS);
                            }
                        } catch (SQLException sqle) {
                            System.out.println(sqle.getMessage());
                        }
                    }
                }
            } else if (obj instanceof String) {
                this.fxid_errorDatabase.setText((String)obj);
            }
        } else {
            JOptionPane.showMessageDialog(null, "Instruccion SQL no permitida");
        }
    }

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

        fxid_stockId.setCellValueFactory(new PropertyValueFactory<>("stock_id"));
        fxid_itemType.setCellValueFactory(new PropertyValueFactory<>("itemType"));
        fxid_product.setCellValueFactory(new PropertyValueFactory<>("product"));
        fxid_stockAmount.setCellValueFactory(new PropertyValueFactory<>("stock_amount"));
        fxid_itemPrice.setCellValueFactory(new PropertyValueFactory<>("item_price"));
        fxid_discount.setCellValueFactory(new PropertyValueFactory<>("discount"));
    }
}