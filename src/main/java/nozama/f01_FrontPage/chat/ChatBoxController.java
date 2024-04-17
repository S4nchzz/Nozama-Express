package nozama.f01_FrontPage.chat;

import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import nozama.f00_Login.UserData;
import nozama.f01_FrontPage.adminPanel.ticketPanel.TicketData;
import nozama.f01_FrontPage.chat.messageBox.AdminMessageBox;
import nozama.f01_FrontPage.chat.messageBox.UserMessageBox;
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
    private void initialize() {
        new Thread (() -> {
            Platform.runLater(() -> {
                while (this.fxid_chatVbox.getChildren().size() < 5) {
                    ResultSet messages = DatabaseRequestManagment.getMessages(td.getTicket_id());
                    
                    try {
                        while (messages.next()) {
                            if (messages.getString(4).equals("admin")) {
                                new AdminMessageBox(DatabaseRequestManagment.getName(messages.getInt(3)), messages.getString(5));
                            } else if (messages.getString(4).equals("user")) {
                                new UserMessageBox(DatabaseRequestManagment.getName(messages.getInt(3)), messages.getString(5));
                            }
                        }
                        messages.close();
                    } catch (SQLException sqle) {
                        
                    }
                }
            });
        }).start();
    }
}