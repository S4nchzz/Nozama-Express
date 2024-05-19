package nozama.f01_FrontPage.ticketChat.messageBox;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import nozama_database.sendRequest.DatabaseRequestManagment;

public class AdminMessageBox {
    private Pane adminBox;
    private final String message;
    private final int userID;

    @FXML
    private Text fxid_adminResponse;
    @FXML
    private Text fxid_userFieldOnChat;

    public AdminMessageBox (int userID, String message) {
        this.userID = userID;
        
        FXMLLoader adminMessage = new FXMLLoader();
        adminMessage.setController(this);
        adminMessage.setLocation(getClass().getResource("/nozama/virtualChat/adminRespondContainer.fxml"));
        try {
            adminBox = adminMessage.load();
        } catch (IOException ioe) {
            
        }
        
        this.message = message;
    }

    public Pane getAdminPane () {
        this.fxid_userFieldOnChat.setText(DatabaseRequestManagment.getName(userID));
        this.fxid_adminResponse.setText(message);
        return this.adminBox;
    }
}
