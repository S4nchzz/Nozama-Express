package nozama.f01_PageAfLog.adminPanel.tables.itemType;

import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import nozama.f01_PageAfLog.adminPanel.tables.Tables;
import nozama_database.sendRequest.DatabaseRequestManagment;

public class ItemTypeTable implements Tables {
    private TableDataItemType tIT;
    private ResultSet rs;

    @FXML
    private TableView<TableDataItemType> fxid_databaseItemType;
    @FXML
    private TableColumn<TableDataItemType, String> fxid_itemTypeColumnExternal;
    @FXML
    private TableColumn<TableDataItemType, String> fxid_descriptionColumnExternal;

    public ItemTypeTable (TableView<TableDataItemType> fxid_databaseItemType,
                      TableColumn<TableDataItemType, String> fxid_itemTypeColumnExternal,
                      TableColumn<TableDataItemType, String> fxid_descriptionColumnExternal) {
        this.fxid_databaseItemType = fxid_databaseItemType;
        this.fxid_itemTypeColumnExternal = fxid_itemTypeColumnExternal;
        this.fxid_descriptionColumnExternal = fxid_descriptionColumnExternal;
    }

    @SuppressWarnings("unchecked")
    @Override
    public TableView<TableDataItemType> insertRegistersOnTable() {
        if (fxid_databaseItemType.isVisible()) {
            this.rs = DatabaseRequestManagment.getAllRegisters(2);
            if (rs != null) {
                try {
                    while (rs.next()) {
                        tIT = new TableDataItemType(rs.getString(1), rs.getString(2));

                        fxid_databaseItemType.getItems().add(tIT);
                    }

                    return this.fxid_databaseItemType;
                } catch (SQLException sqle) {
                    System.out.println(sqle.getMessage());
                }
            }
        }
        return null;
    }
 
}
