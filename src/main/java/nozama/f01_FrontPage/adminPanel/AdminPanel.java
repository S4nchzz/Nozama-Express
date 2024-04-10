package nozama.f01_FrontPage.adminPanel;

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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import nozama.NozamaWindowApp;
import nozama.f00_Login.LoginPage;
import nozama.f01_FrontPage.FrontPage;
import nozama.f01_FrontPage.adminPanel.queryInjection.QueryConditions;
import nozama.f01_FrontPage.adminPanel.tables.Tables;
import nozama.f01_FrontPage.adminPanel.tables.itemType.ItemTypeTable;
import nozama.f01_FrontPage.adminPanel.tables.itemType.TableDataItemType;
import nozama.f01_FrontPage.adminPanel.tables.stock.StockTable;
import nozama.f01_FrontPage.adminPanel.tables.stock.TableDataStock;
import nozama.f01_FrontPage.adminPanel.tables.users.TableDataUsers;
import nozama.f01_FrontPage.adminPanel.tables.users.UserTable;
import nozama_database.sendRequest.DatabaseRequestManagment;

public class AdminPanel {
    private final Stage stage;
    private final FrontPage stageControllerFP;
    private final String username;
    private ResultSet rs;
    private boolean allInsertedUser;
    private boolean allInsertedStock;
    private boolean allInserted;
    private TableDataUsers tdU;
    private TableDataStock tdS;
    private TableDataItemType tdIT;
    private Tables tS;
    private Tables tU;
    private Tables tIT;
    private String query;
    private QueryConditions qc;

    @FXML
    private Text fxid_usernameAv;
    @FXML
    private TextField fxid_queryInjection;
    @FXML
    private Text fxid_errorDatabase;
    @FXML
    private Pane fxid_queryPane;
    @FXML
    private Pane fxid_stockPane;
    @FXML
    private Pane fxid_paneUser;

    @FXML
    private TableView<TableDataUsers> fxid_databaseUser;
    @FXML
    private TableColumn<TableDataUsers, String> fxid_tableUsername;
    @FXML
    private TableColumn<TableDataUsers, String> fxid_loginStatus;
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

    @FXML
    private TableView<TableDataItemType> fxid_databaseItemType;
    @FXML
    private TableColumn<TableDataItemType, String> fxid_itemTypeColumnExternal;
    @FXML
    private TableColumn<TableDataItemType, String> fxid_descriptionColumnExternal;

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
        fxid_paneUser.setVisible(true);
        fxid_stockPane.setVisible(false);

        if (!allInsertedUser) {
            tU = new UserTable(fxid_databaseUser, fxid_tableUsername, fxid_loginStatus, fxid_tableSalt, fxid_tablePass, fxid_tableisAdmin, fxid_tableName, fxid_tableTelf, fxid_tableGender);
            this.fxid_databaseUser = tU.insertRegistersOnTable();
            allInsertedUser = true;
        }
    }

    @FXML
    private void showDatabaseStock() {
        fxid_queryPane.setVisible(true);
        fxid_paneUser.setVisible(false);
        fxid_stockPane.setVisible(true);

        if (!allInsertedStock && tS == null) {
            tS = new StockTable(fxid_databaseStock, fxid_itemType,fxid_stockId, fxid_product, fxid_stockAmount, fxid_itemPrice, fxid_discount);
            this.fxid_databaseStock = tS.insertRegistersOnTable();

            tIT = new ItemTypeTable(fxid_databaseItemType, fxid_itemTypeColumnExternal, fxid_descriptionColumnExternal);
            this.fxid_databaseItemType = tIT.insertRegistersOnTable();
            allInsertedStock = true;
        }
    }

    @FXML
    private void handleReloadOption () {
        if (!allInserted) {
            tU = new UserTable(fxid_databaseUser, fxid_tableUsername, fxid_loginStatus, fxid_tableSalt, fxid_tablePass,
                    fxid_tableisAdmin, fxid_tableName, fxid_tableTelf, fxid_tableGender);
            this.fxid_databaseUser = tU.insertRegistersOnTable();
            tS = new StockTable(fxid_databaseStock, fxid_itemType, fxid_stockId, fxid_product, fxid_stockAmount,
                    fxid_itemPrice, fxid_discount);
            this.fxid_databaseStock = tS.insertRegistersOnTable();
    
            tIT = new ItemTypeTable(fxid_databaseItemType, fxid_itemTypeColumnExternal, fxid_descriptionColumnExternal);
            this.fxid_databaseItemType = tIT.insertRegistersOnTable();
            allInserted = true;
        }
    }

    @FXML
    private void sendAdminQueryIconSearch () {
        sendAdminQuery();
    }

    /**
     * Metodo que permite enviar consultas y obtener en tiempo real el resultado
     */
    @FXML
    private void sendAdminQuery () {
        DatabaseRequestManagment db = new DatabaseRequestManagment();
        Object obj = null;
        
        this.query = fxid_queryInjection.getText().toUpperCase();
        
        // Si las tablas son visibles y el contenido de la consulta esta vacio se actualizan con los valores actuales
        if (fxid_databaseUser.isVisible()) {
            if (query.isEmpty() || query.isBlank()) {
                fxid_databaseUser.getItems().clear();
                this.fxid_databaseUser = tU.insertRegistersOnTable();
            }
        } else if (fxid_databaseStock.isVisible()) {
            if (query.isEmpty() || query.isBlank()) {
                fxid_databaseStock.getItems().clear();
                this.fxid_databaseStock = tS.insertRegistersOnTable();
            }
        } else if (fxid_databaseItemType.isVisible()) {
            fxid_databaseItemType.getItems().clear();
            this.fxid_databaseItemType = tIT.insertRegistersOnTable();
        }

        obj = db.injectCustomQuery(query);
        qc = new QueryConditions(query);

        if (qc.conditions()) {
            // Si obj es una instancia de tipo ResultSet querra decir que la consulta funciono correctamente
            if (obj instanceof ResultSet) {
                // Si la consulta es un select se aplica la variable booleana false queriendo
                // decir que si se le da al boton de recargar saldran nuevos datos
                if (query.contains("FROM USER") || query.contains("FROM STOCK") || query.contains("FROM ITEM_TYPE")) {
                    allInserted = false;
                }

                this.rs = (ResultSet) obj;
                if (rs != null && query.contains("FROM USER") || query.contains("INTO USER")) {                    
                    fxid_databaseUser.getItems().clear();
                    this.fxid_errorDatabase.setText("");

                    // Si la consulta esta relacionada con la tabla USER se obtiene todos los
                    // datos de la misma con la query solicitada
                    try {
                        while (rs.next()) {
                            tdU = new TableDataUsers(rs.getString(1), rs.getBoolean(2), rs.getString(3), rs.getString(4),
                                    rs.getBoolean(5), rs.getString(6), rs.getString(7), rs.getString(8));
        
                            fxid_databaseUser.getItems().add(tdU);
                        }
                    } catch (SQLException sqle) {
                        System.out.println(sqle.getMessage());
                    }
                } else if (rs != null && query.contains("FROM STOCK") || query.contains("INTO STOCK")) {
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
                } else if (rs != null && query.contains("FROM ITEM_TYPE") || query.contains("INTO ITEM_TYPE")) {
                    fxid_databaseItemType.getItems().clear();
                    this.fxid_errorDatabase.setText("");
                    try {
                        while (rs.next()) {
                            tdIT = new TableDataItemType(rs.getString(1), rs.getString(2));
                            fxid_databaseItemType.getItems().add(tdIT);
                        }
                    } catch (SQLException sqle) {
                        System.out.println(sqle.getMessage());
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
        fxid_loginStatus.setCellValueFactory(new PropertyValueFactory<>("loginStatus"));
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

        fxid_itemTypeColumnExternal.setCellValueFactory(new PropertyValueFactory<>("type"));
        fxid_descriptionColumnExternal.setCellValueFactory(new PropertyValueFactory<>("description"));
    }
}