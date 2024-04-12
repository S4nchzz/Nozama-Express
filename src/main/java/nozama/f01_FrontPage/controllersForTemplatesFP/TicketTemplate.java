package nozama.f01_FrontPage.controllersForTemplatesFP;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

public class TicketTemplate {
    Pane graphicTicket;

    @FXML
    private Text fxid_type;
    @FXML
    private Text fxid_prob;

    public TicketTemplate(String type, String problem) {
        try {
            FXMLLoader ticket = new FXMLLoader();
            ticket.setLocation(getClass().getResource("/nozama/frontPage/templateElements/ticketTemplate.fxml"));
            ticket.setController(this);
            graphicTicket = ticket.load();

            fxid_type.setText(type);
            fxid_prob.setText(problem);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Failed to load FXML file.");
        }
    }

    public Pane getProcessedTicket () {
        return this.graphicTicket;
    }
}
