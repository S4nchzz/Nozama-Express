package nozama_database.sendRequest;

import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import nozama_database.credentials.PasswordComplexity;
import nozama_database.setttingUp.DatabaseLinkTest;

/**
 * Clase que gestiona todo el control de la base de datos, metodos (anadir(),
 * borrar(), cambiarContrasena(), acceder())
 */
public class DatabaseRequestManagment {
    // URL global de la base de datos obtenido del String estatico de DatabaseRequestManagment
    final static String url = DatabaseLinkTest.url;

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
                    "INSERT INTO USER (USERNAME, SALT, PASS, ISADMIN, NAME, TELF, GENDER) VALUES (?, ?, ?, ?, ?, ?, ?);");

            st.setString(1, name);
            st.setString(2, salt);
            // En el parametro 3 del PreparedStatement se envia el array de bytes con la conversion
            // de SHA-356
            st.setBytes(3, PasswordComplexity.sha256(passwordAndSalt));
            st.setBoolean(4, isAdmin);

            st.setString(5, fullName);
            st.setString(6, telf);
            st.setString(7, gender);
            
            st.executeUpdate();
            st.close();
            
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
                String saltFromUser = rs.getString(2);
                // Contraseña hasheada de la base de datos
                byte[] passwordDatabaseHashed = rs.getBytes(3);

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
    public static ResultSet getQueryResult(String username) {
        try {
            Connection conn = DriverManager.getConnection(url, "root", "");

            PreparedStatement st = conn.prepareStatement("SELECT * FROM USER WHERE USERNAME = ?");
            st.setString(1, username);

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
        }
        return null;
    }

    /**
     * Metodo que comprueba si un usuario es administrador o no
     * @param username Usuario a buscarf
     * @param password Contraseña del usuario
     * @return true = admin; false = notAdmin
     */
    public static boolean isAdmin(String username, String password) {
        if (acceder(username, password)) {
            ResultSet rs = getQueryResult(username);

            if (rs != null) {
                try {
                    return rs.getBoolean(4);
                } catch (SQLException sqle) {
                    System.out.println(sqle.getMessage());
                }
            }
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
}