package nozama.f01_FrontPage.ticketChat.messageBox;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import nozama_database.sendRequest.DatabaseRequestManagment;

public class UserMessageBox {
    private Pane userBox;
    private final String message;
    private final int userID;

    @FXML
    private Text fxid_userResponse;
    @FXML
    private Text fxid_userFieldOnChat;

    public UserMessageBox (int userID, String message) {
        this.userID = userID;
        
        FXMLLoader userMessage = new FXMLLoader();
        userMessage.setController(this);
        userMessage.setLocation(getClass().getResource("/nozama/virtualChat/userRespondContainer.fxml"));
        try {
            userBox = userMessage.load();
        } catch (IOException ioe) {

        }

        this.message = message;
    }

    public Pane getUserPane() {
        this.fxid_userFieldOnChat.setText(DatabaseRequestManagment.getName(userID));
        this.fxid_userResponse.setText(message);
        return this.userBox;
    }
}