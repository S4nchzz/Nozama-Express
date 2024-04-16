package nozama.f01_FrontPage.chat;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import nozama.f00_Login.UserData;
import nozama.f01_FrontPage.adminPanel.ticketPanel.TicketData;
import nozama_database.sendRequest.DatabaseRequestManagment;

public class ChatBoxController {
    private final TicketData td;
    private final UserData userData;

    @FXML
    private VBox fxid_chatVbox;
    @FXML
    private TextField fxid_sendMessage;
    
    public ChatBoxController (TicketData td, UserData userData) {
        this.td = td;
        this.userData = userData;
    }

    @FXML
    private void sendMessageAction () {
        String message = fxid_sendMessage.getText();
        String admin = "";

        if (DatabaseRequestManagment.isAdmin(userData.getUser_id())) {
            admin = "admin";
        } else {
            admin = "user";
        }

        DatabaseRequestManagment dbr = new DatabaseRequestManagment();
        dbr.sendMessage(td.getTicket_id(), userData.getUser_id(), admin, message);
    }

    @FXML
    private void initialize () {
        new Thread (() -> {
            while(DatabaseRequestManagment.isLoggedIn(userData.getUser_id())) {
                
            }
        });
    }
}