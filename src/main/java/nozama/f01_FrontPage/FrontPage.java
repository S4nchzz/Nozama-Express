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
import nozama.f00_Login.UserData;
import nozama.f01_FrontPage.adminPanel.AdminPanel;
import nozama.f01_FrontPage.controllersForTemplatesFP.TicketTemplateCLLR;
import nozama_database.sendRequest.DatabaseRequestManagment;

public class FrontPage {
    private final Stage stage;
    private final UserData dataLoggedUser;
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

    private void checkBanned () throws BannedException {
        if (DatabaseRequestManagment.isBanned (dataLoggedUser.getUser_id()) && DatabaseRequestManagment.isLoggedIn(dataLoggedUser.getUser_id())) {
            throw new BannedException("El usuario ha sido baneado");
        }
    }

    public FrontPage (UserData dataLoggedUser, Stage s, boolean isAdmin) {
        this.dataLoggedUser = dataLoggedUser;
        this.stage = s;
        this.isAdmin = isAdmin;
        this.visibleSupport = true;
        
        this.ticketAmount = 0;


        this.ticketResultQuery = DatabaseRequestManagment.getAllTrueTicketsFromUser(dataLoggedUser.getUser_id());

        
        // Runtime que crea un hilo antes de cerrar el programa para enviar por ultimo estas instrucciones
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            dbr = new DatabaseRequestManagment();

            dbr.injectCustomQuery("UPDATE USER SET LOGIN_STATUS = FALSE WHERE USER_ID = " + dataLoggedUser.getUser_id());

        }));
        
        // Establece el booleano de login_status a true refiriendose a que el usuario tiene la sesion iniciada
        dbr = new DatabaseRequestManagment();

        dbr.injectCustomQuery("UPDATE USER SET LOGIN_STATUS = TRUE WHERE USER_ID = " + dataLoggedUser.getUser_id());
    }

    /**
     * Cuando el usuario le da al boton de logOf establece para 
     * ese usuario el parametro de login_status a false
     */
    @FXML
    private void handleLogof () throws BannedException {
        checkBanned ();
        dbr = new DatabaseRequestManagment();
        dbr.injectCustomQuery(
                "UPDATE USER SET LOGIN_STATUS = FALSE WHERE USERNAME = \"" + dataLoggedUser.getUser_id());;


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
    private void handleEnterAdminPanel () throws BannedException {
        checkBanned();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/nozama/frontPage/adminPanel.fxml"));
        loader.setController(new AdminPanel(this.stage, this, dataLoggedUser));

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
    private void handleSupportAction () throws SQLException, IOException, BannedException {
        checkBanned();
        fxid_supportPane.setVisible(visibleSupport);

        ticketAmount = ticketLimitReached(dataLoggedUser.getUser_id());
        
        if (fxid_supportPane.isVisible()) {
            fxid_ticketGraphicPane.getChildren().clear();
            ticketResultQuery = DatabaseRequestManagment.getAllTrueTicketsFromUser(dataLoggedUser.getUser_id());
            setGraphicTicketsOnShow();
        }
        
        fxid_ticketsCreatedNum.setText("Tickets: " + String.valueOf(ticketAmount) + "/3");

        visibleSupport = !visibleSupport;
    }

    @FXML
    private void handleToggleSupport () throws BannedException {
        checkBanned();
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
    private void handleSendTicket () throws BannedException {
        checkBanned();
        int ticketAmount = ticketLimitReached(dataLoggedUser.getUser_id());
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
                    Object obj = dbr.injectCustomQuery(
                        "INSERT INTO SUPPORT_TICKET (STATUS, TICKET_TYPE, SOLICITANTE_ID, PROBLEM_DESC) VALUES (true, \""
                        + nameIDSupportButton + "\", \"" + dataLoggedUser.getUser_id() + "\", \""
                        + problemDesc + "\");");

                        // ReChecking if the ticket was created
                        if (obj instanceof ResultSet) {
                            fxid_ticketResult.setFill(Color.GREEN);
                            fxid_ticketResult.setText("Ticket abierto correctamente, espere a que un administrador le responda");
                            
                            this.ticketAmount = ticketLimitReached(dataLoggedUser.getUser_id());
                            setGraphicTicketsOnCreate();

                            fxid_ticketsCreatedNum.setText("Tickets: " + String.valueOf(ticketAmount + 1) + "/3");
                        } else {
                            fxid_ticketResult.setFill(Color.RED);
                            fxid_ticketResult.setText("Hubo un problema al enviar el ticket");
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
    }

    private void setGraphicTicketsOnShow () throws BannedException {
        checkBanned();
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

    private void setGraphicTicketsOnCreate () throws BannedException {
        checkBanned();
        fxid_ticketGraphicPane.getChildren().clear();
        ticketResultQuery = DatabaseRequestManagment.getAllTrueTicketsFromUser(dataLoggedUser.getUser_id());

        setGraphicTicketsOnShow();
    }

    private boolean checkIfButtonStillSelected(String id) throws BannedException {
        checkBanned();
        for (Toggle toggle : fxid_supportToggleButtons.getToggles()) {
            if (((ToggleButton) toggle).getId().equals(id) && toggle.isSelected()) {
                return true;
            }
        }
        return false;
    }

    private int ticketLimitReached(int id) throws BannedException {
        checkBanned();
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
        fxid_usernameAv.setText(dataLoggedUser.getName());

        if (isAdmin) {
            fxid_adminElement.setVisible(true);
        }
    }
}