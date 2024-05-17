package nozama.f01_FrontPage.chat.messageBox;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import nozama.f00_Login.UserData;

public class UserMessageBox {
    private Pane userBox;
    private final String message;
    private final UserData userData;

    @FXML
    private Text fxid_userResponse;
    @FXML
    private Text fxid_userFieldOnChat;

    public UserMessageBox (UserData userData, String message) {
        this.userData = userData;
        
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
        this.fxid_userFieldOnChat.setText(userData.getName());
        this.fxid_userResponse.setText(message);
        return this.userBox;
    }
}