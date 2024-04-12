package nozama.f01_FrontPage.controllersForTemplatesFP;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class AllTicketTemplate {
    private ResultSet ticketData;

    @FXML
    private VBox fxid_ticketContainer;

    public AllTicketTemplate (ResultSet ticketData) {
        this.ticketData = ticketData;

        FXMLLoader ticketMenu = new FXMLLoader();
        ticketMenu.setLocation(getClass().getResource("/nozama/frontPage/ticketMenus/allTicketMenu.fxml"));
        ticketMenu.setController(this);

        try {
            Parent p = ticketMenu.load();
            Scene s = new Scene(p);
            Stage stage = new Stage();

            stage.setScene(s);
            stage.setScene(s);
            stage.setTitle("Tus tickets");
            stage.centerOnScreen();

            stage.show();
        } catch (IOException e) {

        }
    }

    @FXML
    private void initialize() {
        try {
            while (ticketData.next()) {
                TicketTemplateCLLR tt = new TicketTemplateCLLR(ticketData);
                fxid_ticketContainer.getChildren().add(tt.getProcessedTicket());
            }
        } catch (SQLException sqle) {
            
        }
    }
}
