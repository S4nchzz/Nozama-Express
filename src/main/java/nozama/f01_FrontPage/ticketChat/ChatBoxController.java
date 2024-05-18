package nozama.f01_FrontPage.ticketChat;

import java.util.ArrayList;
import java.util.Random;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import nozama.f00_Login.UserData;
import nozama.f01_FrontPage.adminPanel.ticketPanel.TicketData;
import nozama.f01_FrontPage.ticketChat.messageBox.AdminMessageBox;
import nozama.f01_FrontPage.ticketChat.messageBox.UserMessageBox;
import nozama.f01_FrontPage.ticketChat.messagesListener.AdminSocket;
import nozama.f01_FrontPage.ticketChat.messagesListener.ChatServerSocket;
import nozama.f01_FrontPage.ticketChat.messagesListener.PopupMessageShower;
import nozama.f01_FrontPage.ticketChat.messagesListener.ServerThreadInfo;
import nozama.f01_FrontPage.ticketChat.messagesListener.UserSocket;
import nozama_database.sendRequest.DatabaseRequestManagment;

public class ChatBoxController {
    private final TicketData td;
    private final UserData userData;
    private boolean chatInstanceFromAdmin;

    private final int token;

    @FXML
    private VBox fxid_chatVbox;
    @FXML
    private TextField fxid_sendMessage;
    @FXML
    private AnchorPane fxid_anchorPaneResizeable;

    public ChatBoxController(TicketData td, UserData userData, boolean chatInstanceFromAdmin) {
        this.token = generateToken();
        this.td = td;
        this.userData = userData;
        this.chatInstanceFromAdmin = chatInstanceFromAdmin;

        CentralizedChats.addChat(this);

        Platform.runLater(() -> {
            Scene scene = fxid_chatVbox.getScene();
            Stage stage = (Stage) scene.getWindow();

            stage.setOnHidden(event -> {
                ChatBoxController chatToDelete = null;
                for (ChatBoxController chat : CentralizedChats.getChats()) {
                    if (chat.getToken() == token) {
                        chatToDelete = chat;
                    }
                }

                if (chatToDelete == null) {
                    return;
                }
 
                CentralizedChats.delChat(chatToDelete);
                if (chatInstanceFromAdmin) {
                    new PopupMessageShower(this.td.getTicket_id(), true);
                }
            });
        });

        if (!ServerThreadInfo.getServerThreadRunning()) {
            new Thread(() -> {
                ServerThreadInfo.setServerThreadRunning(true);
                new ChatServerSocket();
            }).start();
        }

        // Verify amount of instances of ChatBoxController, if there is 1 the user
        // cannot send messages whereas if there is 2 he would
        checkBothUsersConnected();
        checkTicketOpened();
        reWriteMessages(DatabaseRequestManagment.getMessageDataByTicket(this.td.getTicket_id()));
        
        // Aplicated because when the user create a instance of this() it will call a popupMessageShower setting visible the chat
        // and sending TCP request to the serverSocket
        // Not necesary because the popup will only show at the first creation of the instance of this() maded by an administrator
        // a user cannot open a ticketChat himself
        if (chatInstanceFromAdmin) {
            new PopupMessageShower(this.td.getTicket_id(), false);
        }
    }
    
    @FXML
    private void sendMessageAction() {
        checkBothUsersConnected();
        if (this.fxid_chatVbox.getChildren().size() > 5 && !fxid_sendMessage.getText().equals("")) {
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

        this.fxid_sendMessage.clear();
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

    private void reWriteMessages(ArrayList<MessageData> messageList) {
        if (messageList != null) {
            Platform.runLater(() -> {
                if (messageList.size() > 5) {
                    modifyAnchorPane(fxid_anchorPaneResizeable.getWidth(), (messageList.size() + 1) * 69);
                }
            });
            for (MessageData message : messageList) {
                if (message.getSender_Role().equalsIgnoreCase("admin")) {
                    addMessage(true, message.getMessage());
                } else if (message.getSender_Role().equalsIgnoreCase("user")) {
                    addMessage(false, message.getMessage());
                }
            }
        }
    }

    private int generateToken() {
        Random rm = new Random();
        return rm.nextInt(999999999);
    }

    public int getToken() {
        return this.token;
    }

    private void checkBothUsersConnected() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
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

    private boolean checkTicketOpened() {
        return DatabaseRequestManagment.isTicketOpen(this.td.getTicket_id());
            
    }

    public TicketData getTicketData() {
        return this.td;
    }

    protected void modifyWriterField(boolean status) {
        this.fxid_sendMessage.setDisable(status);
    }

    protected void modifyAnchorPane(double width, double heigth) {
        fxid_anchorPaneResizeable.setPrefHeight(heigth);
    }

    public boolean chatInstanceFromAdmin () {
        return this.chatInstanceFromAdmin;
    }
}