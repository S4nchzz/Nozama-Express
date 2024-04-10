package nozama.f01_FrontPage.adminPanel.tables.itemType;

import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import nozama.f01_FrontPage.adminPanel.tables.Tables;
import nozama_database.sendRequest.DatabaseRequestManagment;

public class ItemTypeTable implements Tables {
    private TableDataItemType tIT;
    private ResultSet rs;

    @FXML
    private TableView<TableDataItemType> fxid_databaseItemType;

    public ItemTypeTable (TableView<TableDataItemType> fxid_databaseItemType) {
        this.fxid_databaseItemType = fxid_databaseItemType;
    }

    @SuppressWarnings("unchecked")
    @Override
    public TableView<TableDataItemType> insertRegistersOnTable() {
        if (fxid_databaseItemType.isVisible()) {
            this.rs = DatabaseRequestManagment.getAllRegisters(3);
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
