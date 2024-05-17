package nozama.f01_FrontPage.chat;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import nozama.f00_Login.UserData;
import nozama.f01_FrontPage.adminPanel.ticketPanel.TicketData;
import nozama.f01_FrontPage.chat.messageBox.AdminMessageBox;
import nozama.f01_FrontPage.chat.messageBox.UserMessageBox;
import nozama.f01_FrontPage.chat.messagesListener.AdminSocket;
import nozama.f01_FrontPage.chat.messagesListener.ChatServerSocket;
import nozama.f01_FrontPage.chat.messagesListener.UserSocket;
import nozama_database.sendRequest.DatabaseRequestManagment;

public class ChatBoxController {
    private static final EventHandler<WindowEvent> WindowEvent = null;
    private final TicketData td;
    private final UserData userData;
    private int messageAmount;
    private final boolean chatInstanceFromAdmin;

    @FXML
    private VBox fxid_chatVbox;
    @FXML
    private TextField fxid_sendMessage;

    public ChatBoxController (TicketData td, UserData userData, boolean chatInstanceFromAdmin) {
        this.td = td;
        this.userData = userData;
        this.messageAmount = DatabaseRequestManagment.getMessageAmount(td.getTicket_id());
        this.chatInstanceFromAdmin = chatInstanceFromAdmin;

        CentralizedChats c = new CentralizedChats();
        c.addChat(this);
    }

    @FXML
    private void sendMessageAction () {
        String message = fxid_sendMessage.getText();
        DatabaseRequestManagment dbr = new DatabaseRequestManagment();

        if (this.chatInstanceFromAdmin) {
            new AdminSocket(fxid_sendMessage.getText());
            dbr.sendMessage(td.getTicket_id(), userData.getUser_id(), "Admin", message);
        } else {
            new UserSocket(fxid_sendMessage.getText());
            dbr.sendMessage(td.getTicket_id(), userData.getUser_id(), "User", message);
        }
    }
    
    public void addMessage(boolean fromAdmin, String input) {
        String fromWho = fromAdmin ? "Admin" : "User";
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                if (fromAdmin) {
                    AdminMessageBox admin = new AdminMessageBox(fromWho, input);
                    fxid_chatVbox.getChildren().add(admin.getAdminPane());
                } else {
                    UserMessageBox user = new UserMessageBox(fromWho, input);
                    fxid_chatVbox.getChildren().add(user.getUserPane());
                }
            }
        });
    }

    @FXML
    private void initialize() {
        new Thread(() -> {
            new ChatServerSocket(this);
        }).start();
    }

    public TicketData getTicketData() {
        return this.td;
    }
}