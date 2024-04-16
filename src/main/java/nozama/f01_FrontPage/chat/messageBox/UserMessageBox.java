package nozama.f01_FrontPage.chat.messageBox;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;

public class UserMessageBox {
    private Pane userBox;

    public UserMessageBox () {
        FXMLLoader adminMessage = new FXMLLoader();
        adminMessage.setLocation(getClass().getResource("/nozama/virtualChat/userRespondContainer.fxml"));
        try {
            userBox = adminMessage.load();
        } catch (IOException ioe) {

        }
    }

    public Pane getUserPane() {
        return this.userBox;
    }
}