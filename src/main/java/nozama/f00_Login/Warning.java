package nozama.f00_Login;

import nozama_database.sendRequest.DatabaseRequestManagment;

public class Warning {
    public Warning (int userID) {        
        if (DatabaseRequestManagment.numberOfWarnings(userID) >= 3) {
            DatabaseRequestManagment.modifyBannedVrb(userID, true);
        } else {
            DatabaseRequestManagment.modifyBannedVrb(userID, false);
        }
    }
}
