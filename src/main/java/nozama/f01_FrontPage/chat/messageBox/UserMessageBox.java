package nozama.f01_FrontPage.chat.messageBox;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

public class UserMessageBox {
    private Pane userBox;
    private final String username;
    private final String message;

    @FXML
    private Text fxid_userResponse;
    @FXML
    private Text fxid_userFieldOnChat;

    public UserMessageBox (String userName, String message) {
        FXMLLoader userMessage = new FXMLLoader();
        userMessage.setController(this);
        userMessage.setLocation(getClass().getResource("/nozama/virtualChat/userRespondContainer.fxml"));
        try {
            userBox = userMessage.load();
        } catch (IOException ioe) {

        }

        this.username = userName;
        this.message = message;
    }

    public Pane getUserPane() {
        this.fxid_userFieldOnChat.setText(username);
        this.fxid_userResponse.setText(message);
        return this.userBox;
    }
}