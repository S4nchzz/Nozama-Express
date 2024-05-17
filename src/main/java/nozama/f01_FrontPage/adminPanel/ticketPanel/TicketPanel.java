package nozama.f01_FrontPage.adminPanel.ticketPanel;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JOptionPane;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import nozama.f00_Login.UserData;
import nozama.f01_FrontPage.ticketChat.ChatBoxController;
import nozama_database.sendRequest.DatabaseRequestManagment;

public class TicketPanel {
    private TicketData ticketData;
    private final DatabaseRequestManagment dbr;
    private final UserData userData;
    private final int adminID;

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
    @FXML
    private TextArea fxid_responseArea;

    // Closed Ticket?
    @FXML
    private Pane fxid_closedResponse;
    @FXML
    private Text fxid_responseIfClosed;

    public TicketPanel (TicketData ticketData, UserData userData, int adminID) {
        this.ticketData = ticketData;
        this.dbr = new DatabaseRequestManagment();
        this.userData = userData;
        this.adminID = adminID;
    }

    @FXML
    private void sendResponse () {
        DatabaseRequestManagment sendResponse = new DatabaseRequestManagment();
        if (sendResponse.updateTicket(adminID, fxid_responseArea.getText(), ticketData.getTicket_id())) {
            this.ticketData = updateTicketDataContent();
            if (ticketData != null) {
                initialize();
            }

            fxid_responseIfClosed.setText(dbr.getTicketResponse(ticketData.getTicket_id()));
        } else {
            JOptionPane.showMessageDialog(null, "Hubo un error a la hora de enviar el ticket");
        }
    }

    @FXML
    private void banUserAction () throws SQLException {
        if (ticketData.getSolicitante_id() > 0) {
            dbr.injectCustomQuery("UPDATE USER SET BANNED = TRUE WHERE USER_ID = " + ticketData.getSolicitante_id());
        }
    }

    @FXML
    private void warnUserAction () throws SQLException{
        if (ticketData.getSolicitante_id() > 0) {
            dbr.injectCustomQuery("UPDATE USER SET WARNS = WARNS + 1 WHERE USER_ID = " + ticketData.getSolicitante_id());
        }
    }

    private TicketData updateTicketDataContent () {
        Object obj = dbr.injectCustomQuery("SELECT * FROM SUPPORT_TICKET WHERE TICKET_ID = " + ticketData.getTicket_id());

        TicketData updateTicket = null;
        if (obj instanceof ResultSet) {
            ResultSet rs = (ResultSet)obj;

            try {
                while (rs.next()) {
                    updateTicket = new TicketData(rs.getInt(1), rs.getBoolean(2), rs.getString(3), rs.getInt(4),
                            rs.getInt(5), rs.getString(6), rs.getString(7));
                }

                rs.close();

                return updateTicket;
            } catch (SQLException sqle) {

            }

            return updateTicket;
        }

        return updateTicket;
    } 

    @FXML
    private void liveChatAction () {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/nozama/virtualChat/chatBox.fxml"));
        loader.setController(new ChatBoxController(ticketData, userData, true));
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
    @FXML
    private void initialize () {
        fxid_textTicketId.setText("");
        fxid_textTicketId.setText(fxid_textTicketId.getText() + ticketData.getTicket_id());
        if (ticketData.isStatus()) {
            fxid_textTicketStatus.setFill(Color.GREEN);
            fxid_textTicketStatus.setText("Ticket abierto");
        } else {
            fxid_textTicketStatus.setFill(Color.RED);
            fxid_textTicketStatus.setText("Ticket cerrado");
            fxid_closedResponse.setVisible(true);

            fxid_responseIfClosed.setText(ticketData.getProblem_response());

            if (ticketData.getRespondente_id() > 0) {
                fxid_whoRespond.setText(fxid_whoRespond.getText() + " " + ticketData.getRespondente_id());
            }

            fxid_sendResponsePane.setVisible(false);
            fxid_whoRespond.setVisible(true);
        }

            if (!DatabaseRequestManagment.isLoggedIn(ticketData.getSolicitante_id())) {
                fxid_userLoguedOrNot.setFill(Color.RED);
                fxid_userLoguedOrNot.setText(ticketData.getSolicitante_id() + " (Offline)");
            } else {
                fxid_userLoguedOrNot.setFill(Color.GREEN);
                fxid_userLoguedOrNot.setText(ticketData.getSolicitante_id() + " (Online)");
            }
        

        StringBuilder sb = new StringBuilder();
        for (int i = 5; i < ticketData.getTicket_type().length(); i++) {
            sb.append(ticketData.getTicket_type().charAt(i));
        }

        fxid_probType.setText("Type: " + sb.toString());
        fxid_probType.setText(String.valueOf(ticketData.getTicket_type()));
        fxid_problem_content.setText(ticketData.getProblem_desc());
            
    }
}