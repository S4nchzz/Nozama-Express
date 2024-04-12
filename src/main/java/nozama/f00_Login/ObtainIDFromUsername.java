package nozama.f00_Login;

import java.sql.ResultSet;
import java.sql.SQLException;

import nozama_database.sendRequest.DatabaseRequestManagment;

public class ObtainIDFromUsername {
    public static int getID (String username) {
        try {
            DatabaseRequestManagment dbr = new DatabaseRequestManagment();
            Object obj = dbr.injectCustomQuery("SELECT USER_ID FROM USER WHERE USERNAME LIKE \"" + username + "\""); 

            int id = 0;
            if (obj instanceof ResultSet) {
                ResultSet rs = (ResultSet)obj;
                
                while (rs.next()) {
                    id = rs.getInt(1);
                }
                rs.close();

                return id;
            }
        } catch (SQLException sqle) {

        }

        return 0;
    }
}
