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
    private int messageAmount;
    private AdminMessageBox abox;
    private UserMessageBox ubox;

    @FXML
    private VBox fxid_chatVbox;
    @FXML
    private TextField fxid_sendMessage;
    
    public ChatBoxController (TicketData td, UserData userData) {
        this.td = td;
        this.userData = userData;
        this.messageAmount = DatabaseRequestManagment.getMessageAmount(td.getTicket_id());

        new Thread (() -> {
            while (td.isStatus() && fxid_chatVbox.getChildren().size() <= 5) {
                if (DatabaseRequestManagment.getMessageAmount(td.getTicket_id()) > 0) {
                    int i = 0;
                    for (; i < DatabaseRequestManagment.getMessageAmount(td.getTicket_id()); i++) {
                        ResultSet messages = DatabaseRequestManagment.getMessages(td.getTicket_id());
                        try {
                            while (messages.next()) {
                                if (messages.getString(4).equals("admin")) {
                                    abox = new AdminMessageBox(DatabaseRequestManagment.getName(messages.getInt(3)), messages.getString(5));
                                    this.fxid_chatVbox.getChildren().add(abox.getAdminPane());
                                } else if (messages.getString(4).equals("user")) {
                                    ubox =new UserMessageBox(DatabaseRequestManagment.getName(messages.getInt(3)), messages.getString(5));
                                    this.fxid_chatVbox.getChildren().add(ubox.getUserPane());
                                }
                            }
                            messages.close();
                        } catch (SQLException sqle) {

                        }
                    }
                } else {
                    ResultSet messages = DatabaseRequestManagment.getMessages(td.getTicket_id());

                    try {
                        while (messages.next()) {
                            if (messages.getString(4).equals("admin")) {
                                new AdminMessageBox(DatabaseRequestManagment.getName(messages.getInt(3)),
                                        messages.getString(5));
                            } else if (messages.getString(4).equals("user")) {
                                new UserMessageBox(DatabaseRequestManagment.getName(messages.getInt(3)),
                                        messages.getString(5));
                            }
                        }
                        messages.close();
                    } catch (SQLException sqle) {

                    }
                }
                this.messageAmount = DatabaseRequestManagment.getMessageAmount(td.getTicket_id());
                try {Thread.sleep(0);} catch (InterruptedException q) {};
            }

        }).start();
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
        this.messageAmount = DatabaseRequestManagment.getMessageAmount(td.getTicket_id());
    }

    @FXML
    private void initialize() {
            // while (this.fxid_chatVbox.getChildren().size() < 5) {
            //     ResultSet messages = DatabaseRequestManagment.getMessages(td.getTicket_id());
                
            //     try {
            //         while (messages.next()) {
            //             if (messages.getString(4).equals("admin")) {
            //                 new AdminMessageBox(DatabaseRequestManagment.getName(messages.getInt(3)), messages.getString(5));
            //             } else if (messages.getString(4).equals("user")) {
            //                 new UserMessageBox(DatabaseRequestManagment.getName(messages.getInt(3)), messages.getString(5));
            //             }
            //         }
            //         messages.close();
            //     } catch (SQLException sqle) {
                    
            //     }
            // }
    }
}