package nozama.f01_FrontPage.adminPanel.ticketPanel;

import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.fxml.FXML;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import nozama_database.sendRequest.DatabaseRequestManagment;

public class TicketPanel {
    private final ResultSet ticketData;
    private final DatabaseRequestManagment dbr;

    @FXML
    private Text fxid_textTicketId;
    @FXML
    private Text fxid_textTicketStatus;
    @FXML
    private Text fxid_problem_content;
    @FXML
    private Text fxid_userLoguedOrNot;
    @FXML
    private Text fxid_probType;
    @FXML
    private Text fxid_whoRespond;
    @FXML
    private Pane fxid_sendResponsePane;

    // Closed Ticket?
    @FXML
    private Pane fxid_closedResponse;
    @FXML
    private Text fxid_responseIfClosed;

    public TicketPanel (ResultSet ticketData) {
        this.ticketData = ticketData;
        this.dbr = new DatabaseRequestManagment();
    }

    @FXML
    private void sendResponse () {
        
    }

    @FXML
    private void banUserAction () throws SQLException {
        dbr.injectCustomQuery("UPDATE USER SET BANNED = TRUE WHERE USER_ID = " + ticketData.getInt(4));
    }

    @FXML
    private void warnUserAction () throws SQLException{
        dbr.injectCustomQuery("UPDATE USER SET WARNS = WARNS + 1 WHERE USER_ID = " + ticketData.getInt(4));
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
                fxid_closedResponse.setVisible(true);

                fxid_responseIfClosed.setText(ticketData.getString(7));
                fxid_whoRespond.setText(fxid_whoRespond.getText() + " " + ticketData.getInt(5));
                fxid_sendResponsePane.setVisible(false);
                fxid_whoRespond.setVisible(true);
            }

            Object obj = dbr.injectCustomQuery("SELECT LOGIN_STATUS FROM USER WHERE USER_ID = " + ticketData.getInt(4));
            boolean user_status;

            if (obj instanceof ResultSet) {
                ResultSet rs = (ResultSet)obj;
                rs.next();
                user_status = rs.getBoolean(1);
                if (!user_status) {
                    fxid_userLoguedOrNot.setFill(Color.RED);
                    fxid_userLoguedOrNot.setText(ticketData.getInt(4) + " (Offline)");
                } else {
                    fxid_userLoguedOrNot.setFill(Color.GREEN);
                    fxid_userLoguedOrNot.setText(ticketData.getInt(4) + " (Online)");
                }
                rs.close();
            }

            StringBuilder sb = new StringBuilder();
            for (int i = 5; i < ticketData.getString(3).length(); i++) {
                sb.append(ticketData.getString(3).charAt(i));
            }

            fxid_probType.setText("Type: " + sb.toString());
            fxid_probType.setText(String.valueOf(ticketData.getInt(3)));
            fxid_problem_content.setText(ticketData.getString(6));
        } catch (SQLException ioe) {
            
        }
    }

    @FXML
    private void liveChatAction () {
        
    }
}