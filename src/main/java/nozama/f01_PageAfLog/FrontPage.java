package nozama.f01_PageAfLog;

import java.sql.ResultSet;

import javafx.fxml.FXML;
import javafx.scene.text.Text;
import nozama_database.sendRequest.DatabaseRequestManagment;

public class FrontPage {
    @FXML
    private Text fxid_username_aflog;

    private final ResultSet rs;
    private final String username;

    public FrontPage (String username) {
        this.username = username;
        rs = DatabaseRequestManagment.getQueryResult(username);
    }

    @FXML
    public void modifyUserNameText() {
        fxid_username_aflog.setText(username);
    }
}