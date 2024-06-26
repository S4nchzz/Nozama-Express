package nozama.f01_FrontPage.adminPanel.tables.users;

import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import nozama.f01_FrontPage.adminPanel.tables.Tables;
import nozama_database.sendRequest.DatabaseRequestManagment;

public class UserTable implements Tables {
    private TableDataUsers tdU;
    private ResultSet rs;
    
    @FXML
    private TableView<TableDataUsers> fxid_databaseUser;

    public UserTable(TableView<TableDataUsers> fxid_databaseUser) {
        this.fxid_databaseUser = fxid_databaseUser;

    }

    @SuppressWarnings("unchecked")
    @Override
    public TableView<TableDataUsers> insertRegistersOnTable() {
        if (fxid_databaseUser.isVisible()) {
            this.rs = DatabaseRequestManagment.getAllRegisters(1);
            if (rs != null) {
                try {
                    while (rs.next()) {
                        tdU = new TableDataUsers(rs.getInt(1), rs.getString(2), rs.getBoolean(3), rs.getString(4),
                                rs.getString(5), rs.getBoolean(6), rs.getString(7), rs.getString(8), rs.getString(9), rs.getBoolean(10), rs.getInt(11));

                        fxid_databaseUser.getItems().add(tdU);
                    }

                    try {rs.close();} catch (SQLException q) {}
                    return fxid_databaseUser;
                } catch (SQLException sqle) {
                    System.out.println(sqle.getMessage());
                }
            }
        }

        return null;
    }
}