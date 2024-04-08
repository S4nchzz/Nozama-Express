package nozama.f01_PageAfLog.adminPanel.tables.users;

import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import nozama.f01_PageAfLog.adminPanel.tables.Tables;
import nozama_database.sendRequest.DatabaseRequestManagment;

public class UserTable implements Tables {
    private TableDataUsers tdU;
    private ResultSet rs;
    
    @FXML
    private TableView<TableDataUsers> fxid_databaseAdmin;
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

    public UserTable(TableView<TableDataUsers> fxid_databaseAdmin,
            TableColumn<TableDataUsers, String> fxid_tableUsername,
            TableColumn<TableDataUsers, String> fxid_tableSalt, TableColumn<TableDataUsers, String> fxid_tablePass,
            TableColumn<TableDataUsers, String> fxid_tableisAdmin, TableColumn<TableDataUsers, String> fxid_tableName,
            TableColumn<TableDataUsers, String> fxid_tableTelf, TableColumn<TableDataUsers, String> fxid_tableGender) {
        this.fxid_databaseAdmin = fxid_databaseAdmin;
        this.fxid_tableUsername = fxid_tableUsername;
        this.fxid_tableSalt = fxid_tableSalt;
        this.fxid_tablePass = fxid_tablePass;
        this.fxid_tableisAdmin = fxid_tableisAdmin;
        this.fxid_tableName = fxid_tableName;
        this.fxid_tableTelf = fxid_tableTelf;
        this.fxid_tableGender = fxid_tableGender;

    }

    @SuppressWarnings("unchecked")
    @Override
    public TableView<TableDataUsers> insertRegistersOnTableUser() {
        if (fxid_databaseAdmin.isVisible()) {
            this.rs = DatabaseRequestManagment.getAllRegisters(1);
            if (rs != null) {
                try {                
                    while (rs.next()) {
                        tdU = new TableDataUsers(rs.getString(1), rs.getString(2), rs.getString(3), rs.getBoolean(4),
                            rs.getString(5), rs.getString(6), rs.getString(7));
    
                        fxid_databaseAdmin.getItems().add(tdU);
                    }
                    return fxid_databaseAdmin;
                } catch (SQLException sqle) {
                    System.out.println(sqle.getMessage());
                }
            }
        }

        return null;
    }
}