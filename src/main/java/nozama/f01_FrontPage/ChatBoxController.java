package nozama.f01_FrontPage;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import nozama.f01_FrontPage.adminPanel.ticketPanel.TicketData;
import nozama_database.sendRequest.DatabaseRequestManagment;

public class ChatBoxController {
    private final TicketData td;
    @FXML
    private VBox fxid_chatVbox;
    @FXML
    private TextField fxid_sendMessage;
    
    public ChatBoxController (TicketData td) {
        this.td = td;
    }

    @FXML
    private void sendMessageAction () {
        String message = fxid_sendMessage.getText();

    }
}
