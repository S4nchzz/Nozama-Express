package nozama.f01_FrontPage.adminPanel.ticketPanel;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import nozama.f00_Login.UserData;
import nozama.f01_FrontPage.ticketChat.CentralizedChats;
import nozama.f01_FrontPage.ticketChat.ChatBoxController;

public class TicketTemplateCLLR {
    private Pane graphicTicket;
    private final TicketData ticketData;
    private ChatBoxController chatToOpen;
    private final UserData userData;

    @FXML
    private Text fxid_type;
    @FXML
    private Text fxid_prob;

    public TicketTemplateCLLR(TicketData ticketData, UserData userData) {
        this.ticketData = ticketData;
        this.userData = userData;
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

    @FXML
    private void openTicketAction () {
        if (CentralizedChats.getChats().size() == 0 || CentralizedChats.getChats() == null) {
            return;
        }

        boolean chatFounded = false;
        for (ChatBoxController chat : CentralizedChats.getChats()) {
            if (chat.getTicketData().getTicket_id() == ticketData.getTicket_id()) {
                chatFounded = true;
            }
        }

        if (chatFounded) {
            chatToOpen = new ChatBoxController(ticketData, this.userData, false);
        } else {
            return;
        }

        chatToOpen.sendedFromAdmin(false);
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
    }
}