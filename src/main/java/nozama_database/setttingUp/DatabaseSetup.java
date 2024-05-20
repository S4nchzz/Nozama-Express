package nozama_database.setttingUp;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Esta clase sirve para ver si el servidor se encuentra activo
 * y si se encuentra la base de datos nozama_ex, si no esta la crea
 */
public class DatabaseSetup {
    public static String url = "jdbc:mariadb://127.0.0.1"; // URL estatica para poder se accesible desde otros metodos

    /**
     * Este metodo iniciara una conexion al servidor de mariadb, si hay un
     * error ira al catch si no proseguira y realizara otra conexion que se
     * cerrara instantaneamente, esta sirve para comprobar si la base de datos
     * nozama_ex existe, si no ira al catch la cual la creara haciendo uso de
     * 3 objetos de tipo PreparedStatement
     * 
     * @return
     */
    public static boolean createDBandTB(int port) {
        url += ":" + port;
        try {
            // Prueba de conexion al SERVIDOR
            Connection dbServer = DriverManager.getConnection(url, "root", "");

            url += "/nozama_ex";

            try {
                // Prueba de conexion a la base de datos
                Connection checkDatabase = DriverManager.getConnection(url, "root", "");
                checkDatabase.close(); // Se cierra la conexion instantaneamente.
                System.out.println("Database nozama_ex founded");
            } catch (SQLException sqleNoDatabaseFound) {
                System.out.println("Creating database nozama_ex...");
                // Creation database if not exist
                PreparedStatement setUpDatabase = dbServer.prepareStatement(
                        "CREATE DATABASE IF NOT EXISTS nozama_ex;");

                PreparedStatement useDatabase = dbServer.prepareStatement("USE nozama_ex;");
                PreparedStatement createTableUser = dbServer.prepareStatement(
                        "CREATE TABLE user (USER_ID INTEGER PRIMARY KEY AUTO_INCREMENT, USERNAME VARCHAR(15) NOT NULL, LOGIN_STATUS BOOLEAN NOT NULL, SALT VARCHAR(16) NOT NULL, PASS BINARY(32) NOT NULL, ISADMIN BOOLEAN NOT NULL, NAME VARCHAR(20) NOT NULL, TELF VARCHAR(9), GENDER VARCHAR(1) NOT NULL, BANNED BOOLEAN NOT NULL DEFAULT 0, WARNS INT NOT NULL DEFAULT 0, CONSTRAINT uniqueName UNIQUE (username));");

                PreparedStatement itemtype = dbServer.prepareStatement(
                        "CREATE TABLE item_Type (TYPE VARCHAR(10) PRIMARY KEY, DESCRIPTION VARCHAR(100) NOT NULL);");
                PreparedStatement itemStock = dbServer.prepareStatement(
                        "CREATE TABLE stock (STOCK_ID INT AUTO_INCREMENT PRIMARY KEY, ITEM_TYPE VARCHAR(10), PRODUCT VARCHAR(20) NOT NULL, STOCK_AMOUNT INTEGER NOT NULL, ITEM_PRICE DOUBLE NOT NULL, DISCOUNT INTEGER NOT NULL, FOREIGN KEY (item_type) REFERENCES item_type(type))");

                PreparedStatement supportTickets = dbServer.prepareStatement(
                        "CREATE TABLE support_Ticket (TICKET_ID INTEGER PRIMARY KEY AUTO_INCREMENT, STATUS BOOLEAN NOT NULL, TICKET_TYPE VARCHAR(20) NOT NULL, SOLICITANTE_ID INTEGER NOT NULL, RESPONDENTE_ID INTEGER, PROBLEM_DESC VARCHAR(200) NOT NULL, PROBLEM_RESPONSE VARCHAR(200), FOREIGN KEY (SOLICITANTE_ID) REFERENCES USER(USER_ID), FOREIGN KEY (RESPONDENTE_ID) REFERENCES USER(USER_ID))");

                PreparedStatement chatMessage = dbServer.prepareStatement("CREATE TABLE chat_messages (MESSAGE_ID INTEGER PRIMARY KEY AUTO_INCREMENT, TICKET_ID INTEGER REFERENCES SUPPORT_TICKET(TICKET_ID), SENDER_ID INTEGER REFERENCES USER(USER_ID), SENDER_ROLE VARCHAR(5) NOT NULL, MESSAGE VARCHAR(100) NOT NULL, DATE TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP)");
                
                PreparedStatement userProfile = dbServer.prepareStatement("CREATE TABLE USER_PROFILE (ID INTEGER PRIMARY KEY, PROFILE_PICTURE MEDIUMBLOB, FULL_NAME VARCHAR(20), PUBLIC_EMAIL VARCHAR(50), LOCATION VARCHAR(50), FOREIGN KEY (ID) REFERENCES USER(USER_ID))");
                
                PreparedStatement social_Platforms = dbServer.prepareStatement("CREATE TABLE SOCIAL_PLATFORMS (ID INTEGER PRIMARY KEY, NETWORK VARCHAR(200) NOT NULL UNIQUE)");
                
                PreparedStatement social_user_links = dbServer.prepareStatement("CREATE TABLE SOCIAL_USER_LINKS (ID INTEGER PRIMARY KEY, NETWORK_ID INTEGER NOT NULL, URL VARCHAR(255) NOT NULL, USER_ID INTEGER NOT NULL, FOREIGN KEY (USER_ID) REFERENCES USER_PROFILE(ID), FOREIGN KEY (NETWORK_ID) REFERENCES SOCIAL_PLATFORMS(ID))");
                
                setUpDatabase.executeUpdate();
                useDatabase.executeUpdate();
                createTableUser.executeUpdate();
                itemtype.executeUpdate();
                itemStock.executeUpdate();
                supportTickets.executeUpdate();
                chatMessage.executeUpdate();
                userProfile.executeUpdate();
                social_Platforms.executeUpdate();
                social_user_links.executeUpdate();

                social_user_links.close();
                social_Platforms.close();
                userProfile.close();
                supportTickets.close();
                itemStock.close();
                itemtype.close();
                createTableUser.close();
                useDatabase.close();
                chatMessage.close();
                setUpDatabase.close();
                dbServer.close();
            }

            dbServer.close();
            return true;
        } catch (SQLException sqle) {
            System.out.println("Error while trying establish connection to the database");
            return false; 
        }
    }
}