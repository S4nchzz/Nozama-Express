package nozama.f01_FrontPage.adminPanel.ticketPanel;

import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.fxml.FXML;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class TicketPanel {
    private final ResultSet ticketData;

    @FXML
    private Text fxid_textTicketId;
    @FXML
    private Text fxid_textTicketStatus;

    public TicketPanel (ResultSet ticketData) {
        this.ticketData = ticketData;
    }

    @FXML
    private void initialize () {
        try {
            fxid_textTicketId.setText(fxid_textTicketId.getText() + ticketData.getInt(1));
            if (ticketData.getBoolean(2)) {
                fxid_textTicketStatus.setFill(Color.GREEN);
                fxid_textTicketStatus.setText("Ticket abierto");
            } else {
                fxid_textTicketStatus.setFill(Color.RED);
                fxid_textTicketStatus.setText("Ticket cerrado");
            }
        } catch (SQLException ioe) {
            
        }
    }
}
