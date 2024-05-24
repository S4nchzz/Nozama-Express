package nozama.f01_FrontPage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.imageio.stream.ImageInputStream;

import javafx.scene.control.TextArea;
import com.gluonhq.charm.glisten.control.ToggleButtonGroup;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import java.awt.Desktop;

import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import nozama.NozamaWindowApp;
import nozama.f00_Login.LoginPage;
import nozama.f00_Login.UserData;
import nozama.f01_FrontPage.adminPanel.AdminPanel;
import nozama.f01_FrontPage.adminPanel.ticketPanel.TicketData;
import nozama.f01_FrontPage.adminPanel.ticketPanel.TicketElement;
import nozama.f01_FrontPage.user_profile.SocialUserLinkData;
import nozama.f01_FrontPage.user_profile.UserProfileData;
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
    
    private final FileChooser fileChooser;

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
    @FXML
    private ImageView fxid_supportNotification;

    // Profile elements
    @FXML
    private ImageView fxid_profileImage;
    @FXML
    private Pane fxid_userProfilePane;
    @FXML
    private Text fxid_profileName;
    @FXML
    private Hyperlink fxid_profileEmail;
    @FXML
    private Text fxid_ProfileUserName;
    @FXML
    private Text fxid_profileLocation;
    
    // Social account elements
    @FXML
    private Hyperlink fxid_socialAccount1;
    @FXML
    private Hyperlink fxid_socialAccount2;
    @FXML
    private Hyperlink fxid_socialAccount3;
    @FXML
    private Hyperlink fxid_socialAccount4;
    private ArrayList<Hyperlink> socialAccountList;

    private ArrayList<SocialUserLinkData> socialData;

    @FXML
    private Text fxid_socialAccountText;
    @FXML
    private ImageView fxid_platformLinkImage1;
    @FXML
    private ImageView fxid_platformLinkImage2;
    @FXML
    private ImageView fxid_platformLinkImage3;
    @FXML
    private ImageView fxid_platformLinkImage4;

    private void checkBanned() throws BannedException {
        if (DatabaseRequestManagment.isBanned(dataLoggedUser.getUser_id())
                && DatabaseRequestManagment.isLoggedIn(dataLoggedUser.getUser_id())) {
            throw new BannedException("El usuario ha sido baneado");
        }
    }

    public FrontPage(UserData dataLoggedUser, Stage s, boolean isAdmin) {
        this.dataLoggedUser = dataLoggedUser;
        this.stage = s;
        this.isAdmin = isAdmin;
        this.visibleSupport = true;
        this.fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File("C:/Users/" + System.getProperty("user.name") + "/Desktop"));

        this.ticketAmount = 0;

        this.ticketResultQuery = DatabaseRequestManagment.getAllTrueTicketsFromUser(dataLoggedUser.getUser_id());

        CentralizeFrontPage centralizeFrontPageAdd = CentralizeFrontPage.getInstance();
        centralizeFrontPageAdd.addFrontPage(this);

        // Runtime que crea un hilo antes de cerrar el programa para enviar por ultimo
        // estas instrucciones
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            DatabaseRequestManagment.modifyLoginStatus(this.dataLoggedUser.getUser_id(), false);

            CentralizeFrontPage centralizeFrontPageRemove = CentralizeFrontPage.getInstance();
            centralizeFrontPageRemove.delFrontPage(this);
        }));

        // Establece el booleano de login_status a true refiriendose a que el usuario
        // tiene la sesion iniciada
        DatabaseRequestManagment.modifyLoginStatus(this.dataLoggedUser.getUser_id(), true);
    }

    @FXML
    private void openProfileAction () {
        setVisiblePanes(false);
        this.fxid_userProfilePane.setVisible(true);

        byte [] picture = DatabaseRequestManagment.getProfilePicture(dataLoggedUser.getUser_id());
        String profileName = DatabaseRequestManagment.getProfileName(dataLoggedUser.getUser_id());
        String publicEmail = DatabaseRequestManagment.getProfilePublicEmail(dataLoggedUser.getUser_id());
        String location = DatabaseRequestManagment.getProfileLocation(dataLoggedUser.getUser_id());

        setProfileInfo(new UserProfileData(dataLoggedUser.getUser_id(), picture, profileName, publicEmail, location));
    }

    private void setProfileInfo (UserProfileData profile) {
        if (profile.getProfilePicture() != null) {
            ByteArrayInputStream arrBytes = new ByteArrayInputStream(profile.getProfilePicture());

            this.fxid_profileImage.setImage(new Image(arrBytes));
        }

        this.fxid_profileName.setText(profile.getFullName());
        this.fxid_ProfileUserName.setText(dataLoggedUser.getUsername());
        this.fxid_profileEmail.setText(profile.getPublicEmail());
        this.fxid_profileLocation.setText(profile.getLocation());

        this.fxid_profileEmail.setOnAction(event -> {
            try {Desktop.getDesktop().mail(new URI("mailto:" + profile.getPublicEmail()));} catch (URISyntaxException | IOException e) {}
        });

        socialData = DatabaseRequestManagment.getSocialUserLinkData(dataLoggedUser.getUser_id());
        if (socialData == null) {
            return;
        }

        setSocialDataURL(true, socialData);
    }

    @FXML
    private void changeImageAction() {
        File f = fileChooser.showOpenDialog(new Stage());
        if (f.isFile() && checkImageExtension(f.getName())) {
            // añadir la foto a la base de datos y cambiarla segun se actualice
            try {DatabaseRequestManagment.modifyProfilePicture(dataLoggedUser.getUser_id(), Files.readAllBytes(f.toPath()));} catch (IOException e) {}
            openProfileAction();
        }
    }

    private boolean checkImageExtension(String fileName) {
        if (fileName.endsWith(".png") || fileName.endsWith(".jpg") || fileName.endsWith(".jpeg")) {
            return true;
        }
        return false;
    }
    
    private void setSocialDataURL(boolean visibleStatus, ArrayList<SocialUserLinkData> socialData) {
        int iterator = 0; // Positon to change visibilty to the link image
        for (Hyperlink link : socialAccountList) {
            final int iteratorF = iterator;
            if (iterator < socialData.size()) {
                fxid_socialAccountText.setVisible(true);
                link.setVisible(visibleStatus);

                String networkDescribed = DatabaseRequestManagment.getNetWorkString(socialData.get(iterator).getNetworkId());

                if (networkDescribed == null) {
                    link.setText(socialData.get(iterator).getURL());
                } else {
                    link.setText(networkDescribed);
                }

                link.setOnAction(event -> {
                    try {Desktop.getDesktop().browse(new URI(socialData.get(iteratorF).getURL()));} catch (IOException |URISyntaxException e) {}
                });
    
                switch (iterator) {
                    case 0 -> {
                        this.fxid_platformLinkImage1.setVisible(visibleStatus);
                    }
    
                    case 1 -> {
                        this.fxid_platformLinkImage2.setVisible(visibleStatus);
                    }
    
                    case 2 -> {
                        this.fxid_platformLinkImage3.setVisible(visibleStatus);
                    }
    
                    case 3 -> {
                        this.fxid_platformLinkImage4.setVisible(visibleStatus);
                    }
                }
                iterator++;
            }
        }
    }

    private void generateArrayListSocialData() {
        this.socialAccountList = new ArrayList<>();

        this.fxid_socialAccountText.setVisible(false);
        this.socialAccountList.add(fxid_socialAccount1);
        fxid_socialAccount1.setVisible(false);
        fxid_platformLinkImage1.setVisible(false);

        this.socialAccountList.add(fxid_socialAccount2);
        fxid_socialAccount2.setVisible(false);
        fxid_platformLinkImage2.setVisible(false);

        this.socialAccountList.add(fxid_socialAccount3);
        fxid_socialAccount3.setVisible(false);
        fxid_platformLinkImage3.setVisible(false);

        this.socialAccountList.add(fxid_socialAccount4);
        fxid_platformLinkImage4.setVisible(false);
        fxid_socialAccount4.setVisible(false);
    }

    /**
     * Cuando el usuario le da al boton de logOf establece para
     * ese usuario el parametro de login_status a false
     */
    @FXML
    private void handleLogof() throws BannedException {
        checkBanned();
        DatabaseRequestManagment.modifyLoginStatus(this.dataLoggedUser.getUser_id(), false);

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

            CentralizeFrontPage centralizeFrontPageRemove = CentralizeFrontPage.getInstance();
            centralizeFrontPageRemove.delFrontPage(this);
        } catch (IOException e) {

        }
    }

    private ArrayList<Pane> getFrontPagePanes() {
        ArrayList<Pane> elementList = new ArrayList<>();
        elementList.add(this.fxid_supportPane);
        elementList.add(this.fxid_userProfilePane);

        return elementList;
    }

    private void setVisiblePanes (boolean status) {
        for (Pane pane : getFrontPagePanes()) {
            pane.setVisible(status);
        }
    }

    @FXML
    private void handleEnterAdminPanel() throws BannedException {
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
    private void handleSupportAction() throws SQLException, IOException, BannedException {
        checkBanned();
        setVisibleNotification(false);

        setVisiblePanes(false);
        this.fxid_supportPane.setVisible(true);

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
    private void goHomeLogo () {
        setVisiblePanes(false);
    }

    @FXML
    private void handleToggleSupport() throws BannedException {
        checkBanned();
        for (Toggle toggle : fxid_supportToggleButtons.getToggles()) {
            if (toggle.isSelected()) {
                ((ToggleButton) toggle).setStyle("-fx-background-color: rgb(196, 195, 195);");
                nameIDSupportButton = ((ToggleButton) toggle).getId();
            } else {
                ((ToggleButton) toggle).setStyle("-fx-background-color: white;");
            }
        }
    }

    @FXML
    private void handleSendTicket() throws BannedException {
        checkBanned();
        int ticketAmount = ticketLimitReached(dataLoggedUser.getUser_id());
        if (ticketAmount >= 3) {
            fxid_ticketResult.setFill(Color.RED);
            fxid_ticketResult.setText(
                    "Ya ha creado 3 tickets, no podra crear mas hasta que un adminsitrador cierre uno de estos");
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
                        fxid_ticketResult
                                .setText("Ticket abierto correctamente, espere a que un administrador le responda");

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

    private void setGraphicTicketsOnShow() throws BannedException {
        checkBanned();
        try {
            if (ticketAmount <= 3 && ticketAmount >= 1) {
                for (int i = 0; i < ticketAmount; i++) {
                    ticketResultQuery.next();
                    TicketData t = new TicketData(ticketResultQuery.getInt(1), ticketResultQuery.getBoolean(2),
                            ticketResultQuery.getString(3), ticketResultQuery.getInt(4), ticketResultQuery.getInt(5),
                            ticketResultQuery.getString(6), ticketResultQuery.getString(7));
                    TicketElement tt = new TicketElement(t , this.dataLoggedUser, this);
                    fxid_ticketGraphicPane.getChildren().add(tt.getProcessedTicket());
                }
            }
        } catch (SQLException sqle) {

        }
    }

    private void setGraphicTicketsOnCreate() throws BannedException {
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
        Object obj = dbr.injectCustomQuery(
                "SELECT COUNT(TICKET_ID) FROM SUPPORT_TICKET WHERE SOLICITANTE_ID = \"" + id + "\" AND STATUS = TRUE");

        ResultSet ticketCount = null;
        if (obj instanceof ResultSet) {
            ticketCount = (ResultSet) obj;
        } else {
            System.out.println((String) obj);
        }

        try {
            while (ticketCount != null && ticketCount.next()) {
                return ticketCount.getInt(1);
            }
        } catch (SQLException sqle) {

        }
        return -999;
    }

    public void setVisibleNotification(boolean status) {
        this.fxid_supportNotification.setVisible(status);
    }

    public UserData getDataLoggedUser() {
        return this.dataLoggedUser;
    }

    /**
     * Metodo que cuando se ejecuta el controlador y se cargan todos
     * los componentes de la clase con la anotacion @FXML, busca
     * initialize() para cargar el siguiente conteindo para que no
     * de errores null o distintos
     * 
     * Cambiara el contenido del elemento Text al lado de avatar
     * con el nombre del usuario
     */
    @FXML
    private void initialize() {
        if (isAdmin) {
            fxid_adminElement.setVisible(true);
        }

        generateArrayListSocialData();
    }
}