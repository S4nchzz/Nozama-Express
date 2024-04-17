package nozama.f01_FrontPage.chat.messageBox;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

public class AdminMessageBox {
    private Pane adminBox;
    private final String username;
    private final String message;

    @FXML
    private Text fxid_adminResponse;
    @FXML
    private Text fxid_userFieldOnChat;

    public AdminMessageBox (String userName, String message) {
        FXMLLoader adminMessage = new FXMLLoader();
        adminMessage.setLocation(getClass().getResource("/nozama/virtualChat/adminRespondContainer.fxml"));
        try {
            adminBox = adminMessage.load();
        } catch (IOException ioe) {

        }

        this.username = userName;
        this.message = message;
    }

    public Pane getAdminPane () {
        this.fxid_userFieldOnChat.setText(username);
        this.fxid_adminResponse.setText(message);
        return this.adminBox;
    }
}
