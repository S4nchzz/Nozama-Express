package nozama_database.sendRequest;

import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import nozama.f00_Login.ObtainIDFromUsername;
import nozama.f01_FrontPage.storeData.StoreProductData;
import nozama.f01_FrontPage.ticketChat.MessageData;
import nozama.f01_FrontPage.user_profile.SocialUserLinkData;
import nozama_database.credentials.PasswordComplexity;
import nozama_database.setttingUp.DatabaseSetup;

/**
 * Clase que gestiona todo el control de la base de datos, metodos (anadir(),
 * borrar(), cambiarContrasena(), acceder())
 */
public class DatabaseRequestManagment {
    // URL global de la base de datos obtenido del String estatico de DatabaseRequestManagment
    final static String url = DatabaseSetup.url;

    /**
     * Este metodo iniciara una conexion con la base de datos y creara un objeto de
     * tipo PreparedStatement el cual insertara la consulta para añadir este usuario
     * con sus correspondientes campos, generando el salt (esta generacion se encuentra
     * al principio de la clase) y generando la contraeña hasheada en forma de array de bytes 
     * @param name nombre del usuario a añadir en la base de datos
     * @param pass contraseña del usuario a añadir en la base de datos
     * @return true si se añadio correctamente false si hubo un error
     */
    public static boolean anadir(String name, String pass, boolean isAdmin, String fullName, String telf, String gender) {
        String salt = PasswordComplexity.saltGenerator();
        String passwordAndSalt = salt + pass;

        try {
            // Se establece la conexion real a usar
            final Connection conn = DriverManager.getConnection(url, "root", "");
            // Statement para dar ejecutar la consulta en la base de datos si se realiza con
            // exito la conexion
            PreparedStatement st = conn.prepareStatement(
                    "INSERT INTO USER (USERNAME, LOGIN_STATUS, SALT, PASS, ISADMIN, NAME, TELF, GENDER) VALUES (?, ?, ?, ?, ?, ?, ?, ?);");

            st.setString(1, name);
            st.setBoolean(2, false);
            st.setString(3, salt);
            // En el parametro 3 del PreparedStatement se envia el array de bytes con la conversion
            // de SHA-356
            st.setBytes(4, PasswordComplexity.sha256(passwordAndSalt));
            st.setBoolean(5, isAdmin);
            
            st.setString(6, fullName);
            st.setString(7, telf);
            st.setString(8, gender);
                    
            st.executeUpdate();
            st.close();

            PreparedStatement createProfile = conn.prepareStatement("INSERT INTO USER_PROFILE (ID) VALUES (?)");
            createProfile.setInt(1, ObtainIDFromUsername.getID(name));
            createProfile.executeUpdate();
            createProfile.close();
            
            return true;
        } catch (SQLException sqle) {
            System.out.println(sqle.getMessage());
        }

        return false;
    }

    /**
     * Este metodo iniciara una conexion con la base de datos y creara un objeto de
     * tipo PreparedStatement haciendo una consulta para hacer un DELETE con este usuario
     * @param userToRemove usuario a eliminar de la base de datos
     * @return true si la operacion se realizo correctamente, false si hubo un error
     */
    public static boolean borrar(String userToRemove) {
        try {
            // SE ESTABLECE LA CONEXION
            final Connection conn = DriverManager.getConnection(url, "root", "");
            // STATEMENT PARA ELIMINAR EL USUARIO ELEGIDO
            PreparedStatement st = conn.prepareStatement("DELETE FROM USER WHERE USERNAME LIKE ?");

            st.setString(1, userToRemove);

            st.executeUpdate();
            st.close();

            return true;
        } catch (SQLException sqle) {

        }
        return false;
    }

    /**
     * Este metodo iniciara una conexion con la base de datos y creara una variable
     * de tipo String la cual contendra el salt nuevo del usuario, una vez se tenga
     * el salt se creara un objeto de tipo PreparedStatement el cual usando un UPDATE
     * actualizara la base de datos con estea nueva password, hara lo mismo generando
     * una contraseña codificada en SHA-256 con ese salt en forma de array de bytes,
     * despues de tener este array se hara uso del metodo setBytes para insertarlo en
     * la consulta, al final se ejecutara los 2 updates
     * @param userName    usuario a cambiar la contraseña
     * @param newPassword contraseña nueva a dar al usuario
     * @return true si la operacion se realizo correctamente, false si hubo un error
     */
    public static boolean cambiarContrasena(String userName, String newPassword) {
        try {
            final Connection conn = DriverManager.getConnection(url, "root", "");

            // Actualizacion del salt de la contraseña
            String salt = PasswordComplexity.saltGenerator();

            PreparedStatement st = conn.prepareStatement("UPDATE USER SET SALT = ? WHERE USERNAME LIKE ?");
            st.setString(1, salt);
            st.setString(2, userName);

            // Actualizacion de la contraseña con el salt
            PreparedStatement st2 = conn.prepareStatement("UPDATE USER SET PASS = ? WHERE USERNAME LIKE ?");
            st2.setBytes(1, PasswordComplexity.sha256(salt + newPassword));
            st2.setString(2, userName);

            st.executeUpdate();
            st2.executeUpdate();
            
            return true;
        } catch (SQLException sqle) {
            return false;
        }
        
    }

    /**
     * Este metodo inicia una conexion a la base de datos y crea un objeto de tipo
     * PreparedStatement el cual tiene como consulta SELECT * FROM USER WHERE 
     * USERNAME = @param username esta consulta da como resultado todos los campos
     * del usuario @param username, el objeto ResultSet ejecuta la consulta y se
     * guarda en un objeto de este tipo todo lo relacionado con el resultado de la
     * consulta, luego se accede al primer reusltado de la consulta y si el metodo
     * rs.next() == true significa que existe un registro en la consulta realizada,
     * despues se guarda el salt de la consulta asi como el hash en una variable
     * bytes [], una vez se tengan estos datos se hace una llamada a SHA256() con el
     * salt del usuario que existe en la base de datos + @param password despues esto
     * se guarda en un array de bytes [], una vez acaba esto se hace un if con un
     * objeto MessageDigest accediendo al metodo estatico .isEqual() ingresando los
     * dos arrays de bytes, este metodo comparara los arrays y retornara true si 
     * son iguales y false si no lo son, haciendo esto se sabe si el hash de la contraseña 
     * ingresada por el usuario es igual a la contraseña de la base de datos 
     * @param username usuario para acceder
     * @param password contraseña para acceder
     * @return true si la operacion se realizo correctamente, false si hubo un error
     */
    public static boolean acceder(String username, String password) {
        try {
            Connection conn = DriverManager.getConnection(url, "root", "");
            // Prepared statement con el usuario salt y contraseña de la tabla user
            PreparedStatement stUSP = conn.prepareStatement("SELECT * FROM USER WHERE USERNAME = ?");
            stUSP.setString(1, username);

            // Se almacena los valores anteriores en una variable de tipo ResultSet
            ResultSet rs = stUSP.executeQuery();

            //Accedemos al primer registro
            if (rs.next()) {
                // Salt del usuario en la base de datos
                String saltFromUser = rs.getString(4);
                // Contraseña hasheada de la base de datos
                byte[] passwordDatabaseHashed = rs.getBytes(5);

                // Contraseña ingresada por el usuario y el salt del usuario correcto
                byte[] passwordToCheck = PasswordComplexity.sha256(saltFromUser + password);

                try {
                    // Si la contraseña hasheada con el salt del usuario
                    // es igual al hash completo de la base de datos entonces
                    // la contraesña sera la correcta
                    if (MessageDigest.isEqual(passwordDatabaseHashed, passwordToCheck)) {
                        return true;
                    }
                } catch (Exception e) {

                }
            }
        } catch (SQLException sqle) {

        }

        return false;
    }

    /**
     *  Metodo que retorna todos los datos del registro
     * de un usuario concreto
     * @param username Usuario a buscar
     * @return ResultSet
     */
    public static ResultSet getQueryResult(int username) {
        try {
            Connection conn = DriverManager.getConnection(url, "root", "");

            PreparedStatement st = conn.prepareStatement("SELECT * FROM USER WHERE USER_ID = ?");
            st.setInt(1, username);

            ResultSet rs = st.executeQuery();

            if (rs.next()) {
                return rs;
            }
        } catch (SQLException sqle) {
            
        }
        return null;
    }

    /**
     * 1 for user
     * 2 for stock
     * @param table
     * @return
     */
    public static ResultSet getAllRegisters (int table) {
        switch (table) {
            case 1:
                try {
                    Connection conn = DriverManager.getConnection(url, "root", "");
                    PreparedStatement st = conn.prepareStatement("SELECT * FROM USER");

                    return st.executeQuery();
                } catch (SQLException sqle) {
                    System.out.println(sqle.getMessage());
                }
            break;

            case 2:
                try {
                    Connection conn = DriverManager.getConnection(url, "root", "");
                    PreparedStatement st = conn.prepareStatement("SELECT * FROM STOCK");

                    return st.executeQuery();
                } catch (SQLException sqle) {
                    System.out.println(sqle.getMessage());
                }
            break;

            case 3:
                try {
                    Connection conn = DriverManager.getConnection(url, "root", "");
                    PreparedStatement st = conn.prepareStatement("SELECT * FROM ITEM_TYPE");

                    return st.executeQuery();
                } catch (SQLException sqle) {
                    System.out.println(sqle.getMessage());
                }
            break;

            case 4:
                try {
                    Connection conn = DriverManager.getConnection(url, "root", "");
                    PreparedStatement st = conn.prepareStatement("SELECT * FROM SUPPORT_TICKET");

                    return st.executeQuery();
                } catch (SQLException sqle) {
                    System.out.println(sqle.getMessage());
                }
        }
        return null;
    }

    /**
     * Metodo que comprueba si un usuario es administrador o no
     * @param username Usuario a buscarf
     * @param password Contraseña del usuario
     * @return true = admin; false = notAdmin
     */
    public static boolean isAdmin(int user_ID) {
        try {
            Connection conn = DriverManager.getConnection(url, "root", "");
            PreparedStatement st = conn.prepareStatement("SELECT ISADMIN FROM USER WHERE USER_ID = USER_ID");
            ResultSet rs = st.executeQuery();

            if (rs.next()) {
                return rs.getBoolean(1);
            }

            rs.close();
            st.close();
            conn.close();
        } catch (SQLException sqle) {

        }

        return false;
    }

    /**
     * Metodo que retornara un objeto de tipo ResultSet
     * si la consulta se realiza correctamente, si no
     * devolvera un String con el error reflejado ocultando
     * (con=xxx)
     * @param query Query a analizar
     * @return Object
     */
    public Object injectCustomQuery (String query) {
        try {
            Connection conn = DriverManager.getConnection(url, "root", "");
            PreparedStatement st = conn.prepareStatement(query);
            
            if (query.startsWith("DELETE FROM") || query.startsWith("INSERT INTO")) {
                StringBuilder sb = new StringBuilder();
                for (int charPos = 12; charPos < query.length(); charPos++) {
                    if (query.charAt(charPos) != ' ') {
                        sb.append(query.charAt(charPos));
                    } else {
                        break;
                    }
                }

                st.executeQuery();
                PreparedStatement resultAfterDelete = conn.prepareStatement("SELECT * FROM " + sb.toString());
                return resultAfterDelete.executeQuery();
            }
            return st.executeQuery();

        } catch (SQLException sqle) {
            StringBuilder sb = new StringBuilder();
            
            int posToContinue = 0;
            for (int i = 0; i < sqle.getMessage().length(); i++) {
                if (sqle.getMessage().charAt(i) == ')') {
                    posToContinue = i;
                    break;
                }
            }

            for (int i = posToContinue + 1; i < sqle.getMessage().length(); i++) {
                sb.append(sqle.getMessage().charAt(i));
            }
            return sb.toString();
        }
    }

    
    public static ResultSet getAllTrueTicketsFromUser(int username) {
        try {
            Connection conn = DriverManager.getConnection(url, "root", "");
            PreparedStatement st = conn.prepareStatement("SELECT * FROM SUPPORT_TICKET WHERE SOLICITANTE_ID LIKE ? AND STATUS = TRUE");
            st.setInt(1, username);

            ResultSet rs = st.executeQuery();

            return rs;
        } catch (SQLException sqle) {

        }
        return null;
    }

    public static ResultSet getAllTicketsFromUser(int username) {
        try {
            Connection conn = DriverManager.getConnection(url, "root", "");
            PreparedStatement st = conn
                    .prepareStatement("SELECT * FROM SUPPORT_TICKET WHERE SOLICITANTE_ID = ?");
            st.setInt(1, username);

            ResultSet rs = st.executeQuery();
            
            return rs;
        } catch (SQLException sqle) {
            
        }
        return null;
    }

    /**
     * Metodo que actualiza dependiendo del numero de warnings el valor de banned de user
     * tood esto controlado desde la clase Warning
     * @param userID ID del usuario
     * @param newStatus Nuevo status true = banned | false = not banned
     */
    public static void modifyBannedVrb(int userID, boolean newStatus) {
        try {
            Connection conn = DriverManager.getConnection(url, "root", "");
            PreparedStatement st = conn.prepareStatement("UPDATE USER SET BANNED ? WHERE USER_ID = ?");
            st.setInt(1, userID);
            st.setBoolean(2, newStatus);
            st.executeUpdate();
            st.close();
            conn.close();
        } catch (SQLException sqle) {

        }
    }
    
    public static boolean isBanned (int username) {
        Connection conn = null;
        PreparedStatement st = null;
        ResultSet rs = null;
        boolean banned;

        try {
            conn = DriverManager.getConnection(url, "root", "");
            st = conn
                    .prepareStatement("SELECT BANNED FROM USER WHERE USER_ID = ?");
            st.setInt(1, username);

            rs = st.executeQuery();

            if (!rs.next()) {
                banned = false;
            } else {
                banned = rs.getBoolean(1);

                if (rs.next()) {
                    banned = false;
                }
            }

        } catch (SQLException sqle) {
            banned = false;
        }

        if (rs != null) {
            try {rs.close();} catch (SQLException e) {}
        }

        if (st != null) {try {st.close();} catch (SQLException e) {}
        }

        if (conn != null) {
            try {conn.close();} catch (SQLException e) {}
        }

        return banned;
    }

    public static int numberOfWarnings(int username) {
        try {
            Connection conn = DriverManager.getConnection(url, "root", "");
            PreparedStatement st = conn
                    .prepareStatement("SELECT WARNS FROM USER WHERE USER_ID = ?");
            st.setInt(1, username);

            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (SQLException sqle) {
        
        }

        return 0;
    }
    
    public static boolean isLoggedIn (int username) {
        Connection conn = null;
        PreparedStatement st = null;
        ResultSet rs = null;
        boolean sesion;

        try {
            conn = DriverManager.getConnection(url, "root", "");
            st = conn
                    .prepareStatement("SELECT LOGIN_STATUS FROM USER WHERE USER_ID = ?");
            st.setInt(1, username);

            rs = st.executeQuery();

            if (!rs.next ()) {
                sesion = false;
            } else {
                sesion = rs.getBoolean(1);

                if (rs.next ()) {
                    sesion = false;
                }
            }
            
        } catch (SQLException sqle) {
            sesion = false;
        }

        if (rs != null) {
            try { rs.close(); } catch (SQLException e) {}
        }

        if (st != null) {
            try { st.close(); } catch (SQLException e) {}
        }

        if (conn != null) {
            try { conn.close(); } catch (SQLException e) {}
        }

        return sesion;
    }

    public void sendMessage(int ticketID, int senderID, String senderRole, String message) {
        try {
            Connection conn = DriverManager.getConnection(url, "root", "");
            PreparedStatement st = conn.prepareStatement("INSERT INTO CHAT_MESSAGES (TICKET_ID, SENDER_ID, SENDER_ROLE, MESSAGE) VALUES (?, ?, ?, ?)");
            st.setInt(1, ticketID);
            st.setInt(2, senderID);
            st.setString(3, senderRole);
            st.setString(4, message);
            st.executeUpdate();

            st.close();
        
        } catch (SQLException q) {

        }
    }

    public static int getMessageAmount(int ticketID) {
        int ticketAmount = 0;
        try {
            Connection conn = DriverManager.getConnection(url, "root", "");
            PreparedStatement st = conn.prepareStatement(
                    "SELECT COUNT(MESSAGE_ID) FROM CHAT_MESSAGES WHERE TICKET_ID = ?");
            st.setInt(1, ticketID);
            ResultSet rs = st.executeQuery();

            rs.next();
            ticketAmount = rs.getInt(1);
            st.close();
            rs.close();
            conn.close();

            return ticketAmount;

        } catch (SQLException q) {

        }

        return ticketAmount;
    }

    public static ResultSet getMessages(int ticketID) {
        try {
            Connection conn = DriverManager.getConnection(url, "root", "");
            PreparedStatement st = conn.prepareStatement("SELECT * FROM CHAT_MESSAGES WHERE TICKET_ID = ?");
            st.setInt(1, ticketID);
            
            return st.executeQuery();
        } catch (SQLException sqle) {

        }

        return null;
    }

    public static String getName (int id) {
        try {
            Connection conn = DriverManager.getConnection(url, "root", "");
            PreparedStatement st = conn.prepareStatement("SELECT NAME FROM USER WHERE USER_ID = ?");
            st.setInt(1, id);

            ResultSet rs = st.executeQuery();
            rs.next();
            return rs.getString(1);
        } catch (SQLException sqle) {

        }

        return null;
    }

    public boolean updateTicket(int adminID, String problem, int ticketID) {
        try {
            Connection conn = DriverManager.getConnection(url, "root", "");
            PreparedStatement st1 = conn.prepareStatement("UPDATE SUPPORT_TICKET SET RESPONDENTE_ID = ? WHERE TICKET_ID = ?");
            PreparedStatement st2 = conn.prepareStatement("UPDATE SUPPORT_TICKET SET PROBLEM_RESPONSE = ? WHERE TICKET_ID = ?");
            
            st1.setInt(1, adminID);
            st1.setInt(2, ticketID);

            st2.setString(1, problem);
            st2.setInt(2, ticketID);

            st1.executeUpdate();
            st2.executeUpdate();

            st1.close();
            st2.close();
            conn.close();

            return true;
        } catch (SQLException sqle) {
            
        }

        return false;
    }

    public boolean closeTicket(int ticket_ID) {
        try {
            Connection conn = DriverManager.getConnection(url, "root", "");
            PreparedStatement st1 = conn
                    .prepareStatement("UPDATE SUPPORT_TICKET SET STATUS = 0 WHERE TICKET_ID = ?");
            st1.setInt(1, ticket_ID);

            st1.executeUpdate();

            st1.close();
            conn.close();
            return true;
        } catch (SQLException sqle) {

        }

        return false;
    }

    public static void modifyLoginStatus (int userID, boolean loginStatus) {
        try {
            Connection conn = DriverManager.getConnection(url, "root", "");
            PreparedStatement st = conn.prepareStatement("UPDATE USER SET LOGIN_STATUS = ? WHERE USER_ID = ?");
            st.setBoolean(1, loginStatus);
            st.setInt(2, userID);

            st.executeUpdate();
            st.close();
            conn.close();
        } catch (SQLException sqle) {

        }
    }

    public static ArrayList<MessageData> getMessageDataByTicket(int ticketID) {
        try {
            Connection conn = DriverManager.getConnection(url, "root", "");
            PreparedStatement st = conn.prepareStatement("SELECT * FROM CHAT_MESSAGES WHERE TICKET_ID = ? ORDER BY DATE ASC");
            st.setInt(1, ticketID);
            ResultSet rs = st.executeQuery();

            ArrayList<MessageData> messageList = new ArrayList<>();
            while (rs.next()) {
                messageList.add(new MessageData(rs.getInt(1), rs.getInt(2), rs.getInt(3), rs.getString(4), rs.getString(5), rs.getString(6)));
            }

            rs.close();
            st.close();
            conn.close();
            return messageList;
        } catch (SQLException sqle) {

        }
        return null;
    }

    public String getTicketResponse(int ticketID) {
        try {
            Connection conn = DriverManager.getConnection(url, "root", "");
            PreparedStatement st1 = conn
                    .prepareStatement("SELECT PROBLEM_RESPONSE FROM SUPPORT_TICKET WHERE TICKET_ID = ?");
            st1.setInt(1, ticketID);

            ResultSet rs = st1.executeQuery();

            String response = null;
            while (rs.next()) {
                response = rs.getString(1);
            }

            conn.close();

            return response;
        } catch (SQLException sqle) {

        }

        return null;
    }

    public static boolean isTicketOpen(int ticketid) {
        try {
            Connection conn = DriverManager.getConnection(url, "root", "");
            PreparedStatement st1 = conn
                    .prepareStatement("SELECT * FROM SUPPORT_TICKET WHERE TICKET_ID = ?");
            st1.setInt(1, ticketid);
            ResultSet rs = st1.executeQuery();

            boolean ticketStatus = false;

            if (rs.next()) {
                ticketStatus = rs.getBoolean(2);
            }

            st1.close();
            rs.close();
            conn.close();

            return ticketStatus;
        } catch (SQLException sqle) {

        }

        return false;
    }

    public static String getProfileName (int userID) {
        try {
            Connection conn = DriverManager.getConnection(url, "root", "");
            PreparedStatement st = conn.prepareStatement("SELECT FULL_NAME FROM USER_PROFILE WHERE ID = ?");
            st.setInt(1, userID);

            ResultSet rs = st.executeQuery();

            if (rs.next()) {
                return rs.getString(1);
            }

            st.close();
            conn.close();
        } catch (SQLException sqle) {

        }

        return null;
    }

    public static byte [] getProfilePicture (int userID) {
        try {
            Connection conn = DriverManager.getConnection(url, "root", "");
            PreparedStatement st = conn.prepareStatement("SELECT PROFILE_PICTURE FROM USER_PROFILE WHERE ID = ?");
            st.setInt(1, userID);

            ResultSet rs = st.executeQuery();

            if (rs.next()) {
                return rs.getBytes(1);
            }

            st.close();
            conn.close();
        } catch (SQLException sqle) {

        }

        return null;
    }

    public static String getProfilePublicEmail (int userID) {
        try {
            Connection conn = DriverManager.getConnection(url, "root", "");
            PreparedStatement st = conn.prepareStatement("SELECT PUBLIC_EMAIL FROM USER_PROFILE WHERE ID = ?");
            st.setInt(1, userID);

            ResultSet rs = st.executeQuery();

            if (rs.next()) {
                return rs.getString(1);
            }

            st.close();
            conn.close();
        } catch (SQLException sqle) {

        }

        return null;
    }

    public static String getProfileLocation(int userID) {
        try {
            Connection conn = DriverManager.getConnection(url, "root", "");
            PreparedStatement st = conn.prepareStatement("SELECT LOCATION FROM USER_PROFILE WHERE ID = ?");
            st.setInt(1, userID);

            ResultSet rs = st.executeQuery();

            if (rs.next()) {
                return rs.getString(1);
            }

            st.close();
            conn.close();
        } catch (SQLException sqle) {

        }

        return null;
    }

    public static void setPro (byte [] a, int userID) {
        try {
            Connection conn = DriverManager.getConnection(url, "root", "");
            PreparedStatement st = conn.prepareStatement("UPDATE USER_PROFILE SET PROFILE_PICTURE WHERE ID = ?");
            st.setInt(1, userID);

            st.executeUpdate();
            st.close();
            conn.close();
        } catch (SQLException sqle) {

        }
    }

    public static ArrayList<SocialUserLinkData> getSocialUserLinkData(int userID) {
        try {
            Connection conn = DriverManager.getConnection(url, "root", "");
            PreparedStatement st = conn.prepareStatement("SELECT * FROM SOCIAL_USER_LINKS WHERE USER_ID = ?");
            st.setInt(1, userID);
            ResultSet rs = st.executeQuery();

            ArrayList<SocialUserLinkData> linkData = new ArrayList<>();

            while (rs.next()) {
                linkData.add(new SocialUserLinkData(rs.getInt(1), rs.getInt(2), rs.getString(3)));
            }

            return linkData;
        } catch (SQLException sqle) {

        }

        return null;
    }

    public static String getNetWorkString(int networkID) {
        try {
            Connection conn = DriverManager.getConnection(url, "root", "");
            PreparedStatement st = conn.prepareStatement("SELECT NETWORK FROM SOCIAL_PLATFORMS WHERE ID = ?");
            st.setInt(1, networkID);

            ResultSet rs = st.executeQuery();

            if (rs.next()) {
                return rs.getString(1);
            }
        } catch (SQLException sqle) {

        }

        return null;
    }

    public static void modifyProfilePicture(int user_id, byte[] allBytes) {
        try {
            Connection conn = DriverManager.getConnection(url, "root", "");
            PreparedStatement st = conn.prepareStatement("UPDATE USER_PROFILE SET PROFILE_PICTURE = ? WHERE ID = ?");
            st.setBytes(1, allBytes);
            st.setInt(2, user_id);
            st.executeUpdate();
            
            st.close();
            conn.close();
        } catch (SQLException sqle) {

        }
    }

    public static ArrayList<StoreProductData> getAmountOfProducts() {
        try {
            Connection conn = DriverManager.getConnection(url, "root", "");
            PreparedStatement st = conn.prepareStatement("SELECT * FROM STOCK");
            ResultSet rs = st.executeQuery();

            ArrayList<StoreProductData> products = new ArrayList<>();
            while (rs.next()) {
                products.add(new StoreProductData(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getInt(4), rs.getInt(5), rs.getInt(6), rs.getString(7)));
            }
            
            rs.close();
            st.close();
            conn.close();

            return products;
        } catch (SQLException sqle) {

        }

        return null;
    }

    public static byte[] getProductImage(int stock_ID) {
        try {
            Connection conn = DriverManager.getConnection(url, "root", "");
            PreparedStatement st = conn.prepareStatement("SELECT PRODUCT_IMAGE FROM STOCK WHERE STOCK_ID = ?");
            st.setInt(1, stock_ID);
            ResultSet rs = st.executeQuery();

            byte [] image = null;
            if (rs.next()) {
                image = rs.getBytes(1);
            }

            rs.close();
            st.close();
            conn.close();

            if (image != null) {
                return image;
            }

            new Exception("No image founded");
        } catch (SQLException sqle) {

        }

        return null;
    }
}