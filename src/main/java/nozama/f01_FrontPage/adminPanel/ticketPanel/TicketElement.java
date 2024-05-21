package nozama.f01_FrontPage.adminPanel.ticketPanel;

import java.io.IOException;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import nozama.f00_Login.UserData;
import nozama.f01_FrontPage.FrontPage;
import nozama.f01_FrontPage.ticketChat.CentralizedChats;
import nozama.f01_FrontPage.ticketChat.ChatBoxController;

public class TicketElement {
    private Pane graphicTicket;
    private final TicketData ticketData;
    private ChatBoxController chatToOpen;
    private final UserData userData;
    private final FrontPage frontpage;

    @FXML
    private Text fxid_type;
    @FXML
    private Text fxid_prob;
    @FXML
    private ImageView fxid_chatNotice;

    private final CentralizedChats centralizedChats;
    private final CentralizedTicketElements centralizedTicketElements;
    

    public TicketElement(TicketData ticketData, UserData userData, FrontPage frontPage) {
        this.ticketData = ticketData;
        this.userData = userData;
        this.frontpage = frontPage;
        
        this.centralizedChats = CentralizedChats.getInstance();
        this.centralizedTicketElements = CentralizedTicketElements.getInstance();

        Platform.runLater(() -> {
            hasActiveNotification();

        });
        
        centralizedTicketElements.addTicketTemplate(this);

        try {
            FXMLLoader ticket = new FXMLLoader();
            ticket.setLocation(getClass().getResource("/nozama/frontPage/templateElements/ticketTemplate.fxml"));
            ticket.setController(this);
            graphicTicket = ticket.load();

            fxid_prob.setText(ticketData.getProblem_desc());
            fxid_type.setText(ticketData.getTicket_type());
            
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Failed to load FXML file.");
        }
    }

    public Pane getProcessedTicket () {
        return this.graphicTicket;
    }

    private void hasActiveNotification() {
        for (ChatBoxController chat : centralizedChats.getChats()) {
            if (chat.getTicketData().getTicket_id() == this.ticketData.getTicket_id() && chat.chatInstanceFromAdmin()) {
                popUpNotice(true);
            }
        }
    }

    @FXML
    private void openTicketAction () {
        if (centralizedChats.getChats().size() == 0 || centralizedChats.getChats() == null) {
            return;
        }

        boolean chatFounded = false;
        for (ChatBoxController chat : centralizedChats.getChats()) {
            if (chat.getTicketData().getTicket_id() == ticketData.getTicket_id()) {
                chatFounded = true;
            }
        }

        if (chatFounded && !currentChatBoxAdminOpened()) {
            chatToOpen = new ChatBoxController(ticketData, this.userData, false);

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/nozama/virtualChat/chatBox.fxml"));
            loader.setController(chatToOpen);
            try {
                Parent p = loader.load();
                Scene s = new Scene(p);
                Stage ss = new Stage();
                ss.setScene(s);
                ss.centerOnScreen();
                ss.setTitle("Online Chat");
                ss.setResizable(false);
                ss.show();
            } catch (IOException e) {
    
            }
        } else {
            return;
        }
    }

    public void popUpNotice (boolean status) {
        fxid_chatNotice.setVisible(status);
        frontpage.setVisibleNotification(status);
        
    }

    private boolean currentChatBoxAdminOpened() {
        for (ChatBoxController chat : centralizedChats.getChats()) {
            if (chat.getTicketData().getTicket_id() == this.ticketData.getTicket_id() && !chat.chatInstanceFromAdmin()) {
                return true;
            }
        }

        return false;
    }

    public TicketData getTicketData () {
        return this.ticketData;
    }
}