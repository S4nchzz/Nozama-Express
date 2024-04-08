package nozama.f01_PageAfLog.adminPanel.tables.stock;

import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import nozama.f01_PageAfLog.adminPanel.tables.Tables;
import nozama_database.sendRequest.DatabaseRequestManagment;

public class StockTable implements Tables {
    private TableDataStock tdS;
    private ResultSet rs;

    @FXML
    private TableView<TableDataStock> fxid_databaseStock;
    @FXML
    private TableColumn<TableDataStock, String> fxid_stockId;
    @FXML
    private TableColumn<TableDataStock, String> fxid_product;
    @FXML
    private TableColumn<TableDataStock, String> fxid_stockAmount;
    @FXML
    private TableColumn<TableDataStock, String> fxid_itemPrice;
    @FXML
    private TableColumn<TableDataStock, String> fxid_discount;

    public StockTable(TableView<TableDataStock> fxid_databaseStock,
                      TableColumn<TableDataStock, String> fxid_stockId,
                      TableColumn<TableDataStock, String> fxid_product,
                      TableColumn<TableDataStock, String> fxid_stockAmount,
                      TableColumn<TableDataStock, String> fxid_itemPrice,
                      TableColumn<TableDataStock, String> fxid_discount) {
        this.fxid_databaseStock = fxid_databaseStock;
        this.fxid_stockId = fxid_stockId;
        this.fxid_product = fxid_product;
        this.fxid_stockAmount = fxid_stockAmount;
        this.fxid_itemPrice = fxid_itemPrice;
        this.fxid_discount = fxid_discount;
    }

    @Override
    public TableView<TableDataStock> insertRegistersOnTableUser() {
        if (fxid_databaseStock.isVisible()) {
            this.rs = DatabaseRequestManagment.getAllRegisters(2);
            if (rs != null) {
                try {
                    while (rs.next()) {
                        tdS = new TableDataStock(rs.getInt(1), rs.getString(2), rs.getInt(3), rs.getDouble(4),
                                rs.getInt(5));

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