package nozama.f01_FrontPage.adminPanel.tables.support;

import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import nozama.f01_FrontPage.adminPanel.tables.Tables;
import nozama_database.sendRequest.DatabaseRequestManagment;

public class SupportTable implements Tables {
    private TableDataSupport tdST;
    private ResultSet rs;

    @FXML
    private TableView<TableDataSupport> fxid_databaseSupport;

    public SupportTable(TableView<TableDataSupport> fxid_databaseSupport) {
        this.fxid_databaseSupport = fxid_databaseSupport;
    }

    @SuppressWarnings("unchecked")
    @Override
    public TableView<TableDataSupport> insertRegistersOnTable() {
        if (fxid_databaseSupport.isVisible()) {
            this.rs = DatabaseRequestManagment.getAllRegisters(4);
            if (rs != null) {
                try {
                    while (rs.next()) {
                        tdST = new TableDataSupport(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4),
                                rs.getString(5), rs.getString(6), rs.getString(7));

                        fxid_databaseSupport.getItems().add(tdST);
                    }

                    try {rs.close();} catch (SQLException q) {}
                    return this.fxid_databaseSupport;
                } catch (SQLException sqle) {
                    System.out.println(sqle.getMessage());
                }
            }
        }
        return null;
    }
}