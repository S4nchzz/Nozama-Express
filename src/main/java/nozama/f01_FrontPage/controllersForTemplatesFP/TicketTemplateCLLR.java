package nozama.f01_FrontPage.controllersForTemplatesFP;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

public class TicketTemplateCLLR {
    Pane graphicTicket;

    @FXML
    private Text fxid_type;
    @FXML
    private Text fxid_prob;

    public TicketTemplateCLLR(ResultSet ticketData) {
        try {
            FXMLLoader ticket = new FXMLLoader();
            ticket.setLocation(getClass().getResource("/nozama/frontPage/templateElements/ticketTemplate.fxml"));
            ticket.setController(this);
            graphicTicket = ticket.load();

            try {
                fxid_prob.setText(ticketData.getString(6));
                fxid_type.setText(ticketData.getString(3));
            } catch (SQLException sqle) {
                
            }
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
        System.out.println("ASDasdasdsda");
    }
}
