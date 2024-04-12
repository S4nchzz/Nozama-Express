package nozama.f01_FrontPage.adminPanel;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JOptionPane;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import nozama.NozamaWindowApp;
import nozama.f00_Login.LoginPage;
import nozama.f01_FrontPage.FrontPage;
import nozama.f01_FrontPage.adminPanel.queryConditions.QueryConditions;
import nozama.f01_FrontPage.adminPanel.tables.Tables;
import nozama.f01_FrontPage.adminPanel.tables.itemType.ItemTypeTable;
import nozama.f01_FrontPage.adminPanel.tables.itemType.TableDataItemType;
import nozama.f01_FrontPage.adminPanel.tables.stock.StockTable;
import nozama.f01_FrontPage.adminPanel.tables.stock.TableDataStock;
import nozama.f01_FrontPage.adminPanel.tables.support.SupportTable;
import nozama.f01_FrontPage.adminPanel.tables.support.TableDataSupport;
import nozama.f01_FrontPage.adminPanel.tables.users.TableDataUsers;
import nozama.f01_FrontPage.adminPanel.tables.users.UserTable;
import nozama.f01_FrontPage.adminPanel.ticketPanel.TicketPanel;
import nozama_database.sendRequest.DatabaseRequestManagment;

public class AdminPanel {
    private final Stage stage;
    private final FrontPage stageControllerFP;
    private final String username;
    private ResultSet rs;
    private boolean allInsertedUser;
    private boolean allInsertedStock;
    private boolean allInsertedSupport;
    private TableDataUsers tdU;
    private TableDataStock tdS;
    private TableDataItemType tdIT;
    private TableDataSupport tdST;
    private Tables tS;
    private Tables tU;
    private Tables tIT;
    private Tables tST;
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
    private ImageView fxid_reloadComponent;
    @FXML
    private Pane fxid_ticketPane;
    @FXML
    private Text fxid_problem_Desc;
    @FXML
    private TextField fxid_idSearch;
    @FXML
    private Button watchProblem;
    @FXML
    private Text fxid_ticketErrorQuery;

    // Tabla Users
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
    private TableColumn<TableDataUsers, String> fxid_banned;
    @FXML
    private TableColumn<TableDataUsers, String> fxid_warnings;

    // Tabla stock
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

    // Tabla Item_type
    @FXML
    private TableView<TableDataItemType> fxid_databaseItemType;
    @FXML
    private TableColumn<TableDataItemType, String> fxid_itemTypeColumnExternal;
    @FXML
    private TableColumn<TableDataItemType, String> fxid_descriptionColumnExternal;

    // Tabla Support_ticket
    @FXML
    private TableView<TableDataSupport> fxid_databaseSupport;
    @FXML
    private TableColumn<TableDataSupport, String> fxid_ticket_id;
    @FXML
    private TableColumn<TableDataSupport, String> fxid_ticketStatus;
    @FXML
    private TableColumn<TableDataSupport, String> fxid_ticketType;
    @FXML
    private TableColumn<TableDataSupport, String> fxid_solicitante_id;
    @FXML
    private TableColumn<TableDataSupport, String> fxid_respondente_id;
    @FXML
    private TableColumn<TableDataSupport, String> fxid_problem_desc;
    @FXML
    private TableColumn<TableDataSupport, String> fxid_problem_response;

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
        fxid_ticketPane.setVisible(false);
        fxid_reloadComponent.setVisible(true);

        if (!allInsertedUser) {
            tU = new UserTable(fxid_databaseUser);
            this.fxid_databaseUser = tU.insertRegistersOnTable();
            allInsertedUser = true;
        }
    }

    @FXML
    private void showDatabaseStock() {
        fxid_queryPane.setVisible(true);
        fxid_paneUser.setVisible(false);
        fxid_ticketPane.setVisible(false);
        fxid_stockPane.setVisible(true);
        fxid_reloadComponent.setVisible(true);

        if (!allInsertedStock && tS == null) {
            tS = new StockTable(fxid_databaseStock);
            this.fxid_databaseStock = tS.insertRegistersOnTable();

            tIT = new ItemTypeTable(fxid_databaseItemType);
            this.fxid_databaseItemType = tIT.insertRegistersOnTable();
            allInsertedStock = true;
        }
    }

    @FXML
    private void showDatabaseSupport () {
        fxid_queryPane.setVisible(true);
        fxid_ticketPane.setVisible(true);
        fxid_paneUser.setVisible(false);
        fxid_stockPane.setVisible(false);
        fxid_reloadComponent.setVisible(true);

        if (!allInsertedSupport && tST == null) {
            tST = new SupportTable(fxid_databaseSupport);
            this.fxid_databaseSupport = tST.insertRegistersOnTable();
            allInsertedSupport = true;
        }
    }

    @FXML
    private void handleReloadOption () {
        if (fxid_paneUser.isVisible()) {
            tU = new UserTable(fxid_databaseUser);
            this.fxid_databaseUser.getItems().clear();
            this.fxid_databaseUser = tU.insertRegistersOnTable();

        } else if (fxid_stockPane.isVisible()) {
            tS = new StockTable(fxid_databaseStock);
            this.fxid_databaseStock.getItems().clear();
            this.fxid_databaseStock = tS.insertRegistersOnTable();

            tIT = new ItemTypeTable(fxid_databaseItemType);
            this.fxid_databaseItemType.getItems().clear();
            this.fxid_databaseItemType = tIT.insertRegistersOnTable();
        } else if (fxid_ticketPane.isVisible()) {
            tST = new SupportTable(fxid_databaseSupport);
            fxid_databaseSupport.getItems().clear();
            this.fxid_databaseSupport = tST.insertRegistersOnTable();
        }
    }

    /**
     * Metodo que llama a sendAdminQuery()
     */
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
        } else if (fxid_ticketPane.isVisible()) {
            fxid_databaseSupport.getItems().clear();
            this.fxid_databaseSupport = tST.insertRegistersOnTable();
        }

        obj = db.injectCustomQuery(query);
        qc = new QueryConditions(query);

        if (qc.conditions()) {
            // Si obj es una instancia de tipo ResultSet querra decir que la consulta funciono correctamente
            if (obj instanceof ResultSet) {
                this.rs = (ResultSet) obj;
                if (query.contains("FROM USER") || query.contains("INTO USER")) {                    
                    fxid_databaseUser.getItems().clear();
                    this.fxid_errorDatabase.setText("");

                    // Si la consulta esta relacionada con la tabla USER se obtiene todos los
                    // datos de la misma con la query solicitada
                    try {
                        while (rs.next()) {
                            tdU = new TableDataUsers(rs.getString(1), rs.getBoolean(2), rs.getString(3), rs.getString(4),
                                    rs.getBoolean(5), rs.getString(6), rs.getString(7), rs.getString(8), rs.getBoolean(9), rs.getInt(10));
        
                            fxid_databaseUser.getItems().add(tdU);
                        }
                    } catch (SQLException sqle) {
                        System.out.println(sqle.getMessage());
                    }
                } else if (query.contains("FROM STOCK") || query.contains("INTO STOCK")) {
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
                } else if (query.contains("FROM ITEM_TYPE") || query.contains("INTO ITEM_TYPE")) {
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
                } else if (query.contains("FROM SUPPORT_TICKET") || query.contains("INTO SUPPORT_TICKET")) {
                    fxid_databaseSupport.getItems().clear();
                    this.fxid_errorDatabase.setText("");
                    try {
                        while (rs.next()) {
                            tdST = new TableDataSupport(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7));
                            fxid_databaseSupport.getItems().add(tdST);
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
    private void showTicketStageData () throws SQLException, IOException {
        DatabaseRequestManagment db = new DatabaseRequestManagment();
        if (!fxid_idSearch.getText().isEmpty() && !fxid_idSearch.getText().isBlank()) {
            Object obj = db.injectCustomQuery("SELECT * FROM SUPPORT_TICKET WHERE TICKET_ID = " + fxid_idSearch.getText());

            if (obj instanceof ResultSet) {
                ResultSet rs = (ResultSet)obj;

                if (!rs.next()) {
                    fxid_ticketErrorQuery.setFill(Color.RED);
                    fxid_ticketErrorQuery.setText("Ticket no encontrado");
                } else {
                    FXMLLoader ticketLoader = new FXMLLoader();
                    ticketLoader.setLocation(getClass().getResource("/nozama/frontPage/ticketMenu.fxml"));
                    ticketLoader.setController(new TicketPanel(rs));
                        Parent p = ticketLoader.load();
                        Scene s = new Scene(p);
                        Stage ticketStage = new Stage();
                        ticketStage.setScene(s);
                        ticketStage.setTitle("Ticket Administration");
                        ticketStage.setScene(s);
                        ticketStage.setResizable(false);
                        ticketStage.centerOnScreen();
                        ticketStage.show();
                }
            } else if (obj instanceof String){
                fxid_ticketErrorQuery.setText((String)obj);
            }
        }
    }

    @FXML
    private void initialize() {
        fxid_usernameAv.setText(username);
        fxid_tableUsername.setCellValueFactory(new PropertyValueFactory<>("username"));
        fxid_loginStatus.setCellValueFactory(new PropertyValueFactory<>("loginStatus"));
        fxid_tableSalt.setCellValueFactory(new PropertyValueFactory<>("salt"));
        fxid_tablePass.setCellValueFactory(new PropertyValueFactory<>("pass"));
        fxid_tableisAdmin.setCellValueFactory(new PropertyValueFactory<>("admin"));
        fxid_tableName.setCellValueFactory(new PropertyValueFactory<>("name"));
        fxid_tableTelf.setCellValueFactory(new PropertyValueFactory<>("telf"));
        fxid_tableGender.setCellValueFactory(new PropertyValueFactory<>("gender"));
        fxid_banned.setCellValueFactory(new PropertyValueFactory<>("banned"));
        fxid_warnings.setCellValueFactory(new PropertyValueFactory<>("warnings"));

        fxid_stockId.setCellValueFactory(new PropertyValueFactory<>("stock_id"));
        fxid_itemType.setCellValueFactory(new PropertyValueFactory<>("itemType"));
        fxid_product.setCellValueFactory(new PropertyValueFactory<>("product"));
        fxid_stockAmount.setCellValueFactory(new PropertyValueFactory<>("stock_amount"));
        fxid_itemPrice.setCellValueFactory(new PropertyValueFactory<>("item_price"));
        fxid_discount.setCellValueFactory(new PropertyValueFactory<>("discount"));

        fxid_itemTypeColumnExternal.setCellValueFactory(new PropertyValueFactory<>("type"));
        fxid_descriptionColumnExternal.setCellValueFactory(new PropertyValueFactory<>("description"));

        fxid_ticket_id.setCellValueFactory(new PropertyValueFactory<>("ticket_id"));
        fxid_ticketStatus.setCellValueFactory(new PropertyValueFactory<>("ticket_status"));
        fxid_ticketType.setCellValueFactory(new PropertyValueFactory<>("ticket_type"));
        fxid_solicitante_id.setCellValueFactory(new PropertyValueFactory<>("solicitante_id"));
        fxid_respondente_id.setCellValueFactory(new PropertyValueFactory<>("respondente_id"));
        fxid_problem_desc.setCellValueFactory(new PropertyValueFactory<>("problem_desc"));
        fxid_problem_response.setCellValueFactory(new PropertyValueFactory<>("problem_response"));
    }
}