package nozama.f01_FrontPage.adminPanel.tables.stock;

import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import nozama.f01_FrontPage.adminPanel.tables.Tables;
import nozama_database.sendRequest.DatabaseRequestManagment;

public class StockTable implements Tables {
    private TableDataStock tdS;
    private ResultSet rs;

    @FXML
    private TableView<TableDataStock> fxid_databaseStock;

    public StockTable(TableView<TableDataStock> fxid_databaseStock) {
        this.fxid_databaseStock = fxid_databaseStock;
    }

    @SuppressWarnings("unchecked")
    @Override
    public TableView<TableDataStock> insertRegistersOnTable() {
        if (fxid_databaseStock.isVisible()) {
            this.rs = DatabaseRequestManagment.getAllRegisters(2);
            if (rs != null) {
                try {
                    while (rs.next()) {
                        tdS = new TableDataStock(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getInt(4),
                                rs.getDouble(5), rs.getInt(6));

                        fxid_databaseStock.getItems().add(tdS);
                    }

                    return this.fxid_databaseStock;
                } catch (SQLException sqle) {
                    System.out.println(sqle.getMessage());
                }
            }
        }
        return null;
    }
}