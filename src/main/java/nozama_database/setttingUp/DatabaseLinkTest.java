package nozama_database.setttingUp;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Esta clase sirve para ver si el servidor se encuentra activo
 * y si se encuentra la base de datos nozama_ex, si no esta la crea
 */
public class DatabaseLinkTest {
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
        url += ":" + port + "/";
        try {
            // Prueba de conexion al SERVIDOR
            Connection dbServer = DriverManager.getConnection(url, "root", "");

            url += "nozama_ex";

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
                        "CREATE TABLE user (USERNAME VARCHAR(15) PRIMARY KEY, SALT VARCHAR(16) NOT NULL, PASS BINARY(32) NOT NULL, ISADMIN BOOLEAN NOT NULL, NAME VARCHAR(20) NOT NULL, TELF VARCHAR(9), GENDER VARCHAR(1) NOT NULL );");

                PreparedStatement itemStock = dbServer.prepareStatement(
                    "CREATE TABLE stock (STOCK_ID INTEGER PRIMARY KEY AUTO_INCREMENT, PRODUCT VARCHAR(20) NOT NULL, STOCK_AMOUNT INTEGER NOT NULL, ITEM_PRICE DOUBLE NOT NULL, DISCOUNT INTEGER)");

                setUpDatabase.executeQuery();
                useDatabase.executeQuery();
                createTableUser.executeQuery();
                itemStock.executeQuery();

                setUpDatabase.close();
                useDatabase.close();
                createTableUser.close();
                itemStock.close();
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