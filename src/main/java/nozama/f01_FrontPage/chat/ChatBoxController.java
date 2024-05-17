package nozama.f01_FrontPage.chat;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import nozama.f00_Login.UserData;
import nozama.f01_FrontPage.adminPanel.ticketPanel.TicketData;
import nozama.f01_FrontPage.chat.messageBox.AdminMessageBox;
import nozama.f01_FrontPage.chat.messageBox.UserMessageBox;
import nozama.f01_FrontPage.chat.messagesListener.AdminSocket;
import nozama.f01_FrontPage.chat.messagesListener.ChatServerSocket;
import nozama.f01_FrontPage.chat.messagesListener.ServerThreadInfo;
import nozama.f01_FrontPage.chat.messagesListener.UserSocket;
import nozama_database.sendRequest.DatabaseRequestManagment;

public class ChatBoxController {
    private final TicketData td;
    private final UserData userData;
    private boolean chatInstanceFromAdmin;

    @FXML
    private VBox fxid_chatVbox;
    @FXML
    private TextField fxid_sendMessage;

    public ChatBoxController (TicketData td, UserData userData, boolean chatInstanceFromAdmin) {
        this.td = td;
        this.userData = userData;
        this.chatInstanceFromAdmin = chatInstanceFromAdmin;

        CentralizedChats.addChat(this);
    }

    @FXML
    private void sendMessageAction () {
        String message = fxid_sendMessage.getText();
        DatabaseRequestManagment dbr = new DatabaseRequestManagment();

        if (this.chatInstanceFromAdmin) {
            new AdminSocket(fxid_sendMessage.getText(), this.td.getTicket_id());
            dbr.sendMessage(td.getTicket_id(), userData.getUser_id(), "Admin", message);
        } else {
            new UserSocket(fxid_sendMessage.getText(), this.td.getTicket_id());
            dbr.sendMessage(td.getTicket_id(), userData.getUser_id(), "User", message);
        }
    }
    
    public void addMessage(boolean fromAdmin, String input) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                if (fromAdmin) {
                    AdminMessageBox admin = new AdminMessageBox(userData, input);
                    fxid_chatVbox.getChildren().add(admin.getAdminPane());
                } else {
                    UserMessageBox user = new UserMessageBox(userData, input);
                    fxid_chatVbox.getChildren().add(user.getUserPane());
                }
            }
        });
    }

    public void sendedFromAdmin(boolean isAdmin) {
        this.chatInstanceFromAdmin = isAdmin;
    }

    public TicketData getTicketData() {
        return this.td;
    }

    @FXML
    private void initialize() {
        if (!ServerThreadInfo.getServerThreadRunning()) {
            new Thread(() -> {
                ServerThreadInfo.setServerThreadRunning(true);
                new ChatServerSocket(this);
            }).start();
        }
    }

}