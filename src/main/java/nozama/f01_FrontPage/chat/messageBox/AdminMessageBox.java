package nozama.f01_FrontPage.chat.messageBox;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;

public class AdminMessageBox {
    private Pane adminBox;

    public AdminMessageBox () {
        FXMLLoader adminMessage = new FXMLLoader();
        adminMessage.setLocation(getClass().getResource("/nozama/virtualChat/adminRespondContainer.fxml"));
        try {
            adminBox = adminMessage.load();
        } catch (IOException ioe) {

        }
    }

    public Pane getAdminPane () {
        return this.adminBox;
    }
}
