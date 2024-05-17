package nozama.f01_FrontPage.ticketChat;

import java.util.ArrayList;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import nozama.f00_Login.UserData;
import nozama.f01_FrontPage.adminPanel.ticketPanel.TicketData;
import nozama.f01_FrontPage.ticketChat.messageBox.AdminMessageBox;
import nozama.f01_FrontPage.ticketChat.messageBox.UserMessageBox;
import nozama.f01_FrontPage.ticketChat.messagesListener.AdminSocket;
import nozama.f01_FrontPage.ticketChat.messagesListener.ChatServerSocket;
import nozama.f01_FrontPage.ticketChat.messagesListener.ServerThreadInfo;
import nozama.f01_FrontPage.ticketChat.messagesListener.UserSocket;
import nozama_database.sendRequest.DatabaseRequestManagment;

public class ChatBoxController {
    private final TicketData td;
    private final UserData userData;
    private boolean chatInstanceFromAdmin;

    @FXML
    private VBox fxid_chatVbox;
    @FXML
    private TextField fxid_sendMessage;
    @FXML
    private AnchorPane fxid_anchorPaneResizeable;

    public ChatBoxController (TicketData td, UserData userData, boolean chatInstanceFromAdmin) {
        this.td = td;
        this.userData = userData;
        this.chatInstanceFromAdmin = chatInstanceFromAdmin;

        CentralizedChats.addChat(this);

        // Verify amount of instances of ChatBoxController, if there is 1 the user cannot send messages whereas if there is 2 he would
        Platform.runLater(new Runnable() {
            @Override
            public void run () {
                int countInstancesOfThisTicket = 0;
                
                ArrayList<ChatBoxController> usedChatsWithSameTicket = new ArrayList<>();

                for (ChatBoxController chat : CentralizedChats.getChats()) {
                    if (chat.getTicketData().getTicket_id() == td.getTicket_id()) {
                        countInstancesOfThisTicket++;
                        usedChatsWithSameTicket.add(chat);
                    }
                }

                if (usedChatsWithSameTicket.size() != 0) {
                    if (countInstancesOfThisTicket < 2) {
                        for (ChatBoxController chat : usedChatsWithSameTicket) {
                            chat.modifyWriterField(true);
                        }
                    } else {
                        for (ChatBoxController chat : usedChatsWithSameTicket) {
                            chat.modifyWriterField(false);
                        }
                    }
                }
            }
        });
    }

    @FXML
    private void sendMessageAction () {
        if (this.fxid_chatVbox.getChildren().size() > 5) {
            // Ancho del AnchorPane lleno, aumentando tamaño en todos los chatBox con ese ID
            for (ChatBoxController chat : CentralizedChats.getChats()) {
                if (chat.getTicketData().getTicket_id() == this.getTicketData().getTicket_id()) {
                    chat.modifyAnchorPane(fxid_anchorPaneResizeable.getWidth(), fxid_anchorPaneResizeable.getHeight() + 69);
                }
            }
        }

        String message = fxid_sendMessage.getText();
        DatabaseRequestManagment dbr = new DatabaseRequestManagment();

        if (!message.equals("")) {
            if (this.chatInstanceFromAdmin) {
                new AdminSocket(fxid_sendMessage.getText(), this.td.getTicket_id());
                dbr.sendMessage(td.getTicket_id(), userData.getUser_id(), "Admin", message);
            } else {
                new UserSocket(fxid_sendMessage.getText(), this.td.getTicket_id());
                dbr.sendMessage(td.getTicket_id(), userData.getUser_id(), "User", message);
            }
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

    protected void modifyWriterField(boolean status) {
        this.fxid_sendMessage.setDisable(status);
    }

    protected void modifyAnchorPane (double width, double heigth) {
        fxid_anchorPaneResizeable.setPrefHeight(heigth);
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