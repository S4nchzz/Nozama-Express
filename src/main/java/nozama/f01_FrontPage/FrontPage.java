package nozama.f01_FrontPage;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.scene.control.TextArea;
import com.gluonhq.charm.glisten.control.ToggleButtonGroup;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import nozama.NozamaWindowApp;
import nozama.f00_Login.LoginPage;
import nozama.f01_FrontPage.adminPanel.AdminPanel;
import nozama.f01_FrontPage.controllersForTemplatesFP.AllTicketTemplate;
import nozama.f01_FrontPage.controllersForTemplatesFP.TicketTemplateCLLR;
import nozama_database.sendRequest.DatabaseRequestManagment;

public class FrontPage {
    private final Stage stage;
    private final ResultSet dataloguedUser;
    private final boolean isAdmin;
    private boolean visibleSupport;
    private String nameIDSupportButton;
    private DatabaseRequestManagment dbr;
    private int ticketAmount;
    private ResultSet ticketResultQuery;

    @FXML
    private Text fxid_usernameAv;

    @FXML
    private ImageView fxid_adminElement;

    @FXML
    private Pane fxid_paneA;
    @FXML
    private Pane fxid_paneB;
    @FXML
    private Pane fxid_paneC;
    @FXML
    private Pane fxid_paneD;
    @FXML
    private Pane fxid_paneE;

    // Suport elements
    @FXML
    private ToggleButtonGroup fxid_supportToggleButtons;
    @FXML
    private TextArea fxid_problemField;
    @FXML
    private Button fxid_sendSupport;
    @FXML
    private Pane fxid_supportPane;
    @FXML
    private ToggleButton fxid_Acc;
    @FXML
    private ToggleButton fxid_IveBeenHacked;
    @FXML
    private ToggleButton fxid_Refounds;
    @FXML
    private ToggleButton fxid_Sugestions;
    @FXML
    private ToggleButton fxid_Other;
    @FXML
    private Text fxid_ticketResult;
    @FXML   
    private Text fxid_ticketsCreatedNum;
    @FXML
    private Pane fxid_ticketGraphicPane;

    public FrontPage (ResultSet rs, Stage s, boolean isAdmin) {
        this.dataloguedUser = rs;
        this.stage = s;
        this.isAdmin = isAdmin;
        this.visibleSupport = true;
        
        this.ticketAmount = 0;

        try {
            this.ticketResultQuery = DatabaseRequestManagment.getAllTrueTicketsFromUser(dataloguedUser.getString(1));
        } catch (SQLException sqle) {

        }
        
        // Runtime que crea un hilo antes de cerrar el programa para enviar por ultimo estas instrucciones
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            dbr = new DatabaseRequestManagment();
            try {
                dbr.injectCustomQuery("UPDATE USER SET LOGIN_STATUS = FALSE WHERE USERNAME = \"" + dataloguedUser.getString(1) + "\"");
            } catch (SQLException sqle) {

            }
        }));
        
        // Establece el booleano de login_status a true refiriendose a que el usuario tiene la sesion iniciada
        dbr = new DatabaseRequestManagment();
        try {
            dbr.injectCustomQuery("UPDATE USER SET LOGIN_STATUS = TRUE WHERE USERNAME = \"" + dataloguedUser.getString(1) + "\"");
        } catch (SQLException sqle2) {

        }
    }

    /**
     * Cuando el usuario le da al boton de logOf establece para 
     * ese usuario el parametro de login_status a false
     */
    @FXML
    private void handleLogof () {
        try {
            dbr = new DatabaseRequestManagment();
            dbr.injectCustomQuery(
                    "UPDATE USER SET LOGIN_STATUS = FALSE WHERE USERNAME = \"" + dataloguedUser.getString(1) + "\"");
        } catch (SQLException sqle) {

        }

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/nozama/login/login.fxml"));
        loader.setController(new LoginPage(stage));

        try {
            Parent p = loader.load();
            Scene s = new Scene(p, NozamaWindowApp.LOGIN_WIDTH, NozamaWindowApp.LOGIN_HEIGTH);
            stage.setScene(s);
            stage.setTitle("Nozama Express");
            stage.centerOnScreen();

            stage.show();
        } catch (IOException e) {
            
        }
    }

    @FXML
    private void handleEnterAdminPanel () {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/nozama/frontPage/adminPanel.fxml"));
        loader.setController(new AdminPanel(this.stage, this, fxid_usernameAv.getText()));

        try {
            Parent p = loader.load();
            Scene s = new Scene(p);
            stage.setTitle("Nozama Express");
            stage.setScene(s);
            stage.setResizable(false);
            stage.centerOnScreen(); 
            stage.show();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    @FXML
    private void handleSupportAction () throws SQLException, IOException {
        fxid_supportPane.setVisible(visibleSupport);

        ticketAmount = ticketLimitReached(dataloguedUser.getString(1));
        
        if (fxid_supportPane.isVisible()) {
            fxid_ticketGraphicPane.getChildren().clear();
            ticketResultQuery = DatabaseRequestManagment.getAllTrueTicketsFromUser(dataloguedUser.getString(1));
            setGraphicTicketsOnShow();
        }
        
        fxid_ticketsCreatedNum.setText("Tickets: " + String.valueOf(ticketAmount) + "/3");

        visibleSupport = !visibleSupport;
    }

    @FXML
    private void handleToggleSupport () {
        for (Toggle toggle: fxid_supportToggleButtons.getToggles()) {
            if (toggle.isSelected()) {
                ((ToggleButton) toggle).setStyle("-fx-background-color: rgb(196, 195, 195);");
                nameIDSupportButton = ((ToggleButton) toggle).getId();
            } else {
                ((ToggleButton) toggle).setStyle("-fx-background-color: white;");
            }
        }
    }

    @FXML
    private void handleSendTicket () {
        try {
            int ticketAmount = ticketLimitReached(dataloguedUser.getString(1));
            if (ticketAmount >= 3) {
                fxid_ticketResult.setFill(Color.RED);
                fxid_ticketResult.setText("Ya ha creado 3 tickets, no podra crear mas hasta que un adminsitrador cierre uno de estos");
            } else {
                if (!fxid_problemField.getText().isEmpty() || !fxid_problemField.getText().isBlank()) {
                    String problemDesc = fxid_problemField.getText();
                    if (problemDesc.length() >= 200) {
                        fxid_ticketResult.setFill(Color.RED);
                        fxid_ticketResult.setText("La descripcion del problema no puede superar los 200 caracteres");
                    } else if (checkIfButtonStillSelected(nameIDSupportButton)) {
                        dbr = new DatabaseRequestManagment();
                        try {
                            Object obj = dbr.injectCustomQuery(
                                "INSERT INTO SUPPORT_TICKET (STATUS, TICKET_TYPE, SOLICITANTE_ID, PROBLEM_DESC) VALUES (true, \""
                                + nameIDSupportButton + "\", \"" + dataloguedUser.getString(1) + "\", \""
                                + problemDesc + "\");");

                                // ReChecking if the ticket was created
                                if (obj instanceof ResultSet) {
                                    fxid_ticketResult.setFill(Color.GREEN);
                                    fxid_ticketResult.setText("Ticket abierto correctamente, espere a que un administrador le responda");
                                    
                                    this.ticketAmount = ticketLimitReached(dataloguedUser.getString(1));
                                    setGraphicTicketsOnCreate();

                                    fxid_ticketsCreatedNum.setText("Tickets: " + String.valueOf(ticketAmount + 1) + "/3");
                                } else {
                                    fxid_ticketResult.setFill(Color.RED);
                                    fxid_ticketResult.setText("Hubo un problema al enviar el ticket");
                                }

                        } catch (SQLException sqle) {

                        }
                    } else {
                        fxid_ticketResult.setFill(Color.RED);
                        fxid_ticketResult.setText("Elija una de las opciones");
                    }

                } else {
                    fxid_ticketResult.setText("");
                    fxid_ticketResult.setFill(Color.RED);
                    fxid_ticketResult.setText("Describa el problema antes de enviar el ticket");
                }
            }
        } catch (SQLException sqle) {

        } 
    }

    private void setGraphicTicketsOnShow () {
        try {
            if (ticketAmount <= 3 && ticketAmount >= 1) {
                for (int i = 0; i < ticketAmount; i++) {
                    ticketResultQuery.next();
                    TicketTemplateCLLR tt = new TicketTemplateCLLR(ticketResultQuery);
                    fxid_ticketGraphicPane.getChildren().add(tt.getProcessedTicket());
                }
            }
        } catch (SQLException sqle) {

        }
    }

    private void setGraphicTicketsOnCreate () {
        fxid_ticketGraphicPane.getChildren().clear();
        try {
            ticketResultQuery = DatabaseRequestManagment.getAllTrueTicketsFromUser(dataloguedUser.getString(1));
        } catch (SQLException sqle) {

        }

        setGraphicTicketsOnShow();
    }

    private boolean checkIfButtonStillSelected(String id) {
        for (Toggle toggle : fxid_supportToggleButtons.getToggles()) {
            if (((ToggleButton) toggle).getId().equals(id) && toggle.isSelected()) {
                return true;
            }
        }
        return false;
    }

    private int ticketLimitReached(String id) {
        dbr = new DatabaseRequestManagment();
        Object obj = dbr.injectCustomQuery("SELECT COUNT(TICKET_ID) FROM SUPPORT_TICKET WHERE SOLICITANTE_ID = \"" + id + "\" AND STATUS = TRUE");
        
        ResultSet ticketCount = null;
        if (obj instanceof ResultSet) {
            ticketCount = (ResultSet)obj;
        } else {
            System.out.println((String)obj);
        }
 
        try {
            while(ticketCount != null && ticketCount.next()) {
                return ticketCount.getInt(1);
            }
        } catch (SQLException sqle) {
        
        }
        return -999;
    }
    /**
     * Metodo que cuando se ejecuta el controlador y se cargan todos
     * los componentes de la clase con la anotacion @FXML, busca
     * initialize() para cargar el siguiente conteindo para que no 
     * de errores null o distintos
     * 
     * Cambiara el contenido del elemento Text al lado de avatar
     * con el nombre del usuario
     *  */ 
    @FXML
    private void initialize() {
        try {
            fxid_usernameAv.setText(dataloguedUser.getString(6));
        } catch (SQLException sqle) {
            System.out.println(sqle.getMessage());
        }

        if (isAdmin) {
            fxid_adminElement.setVisible(true);
        }
    }
}