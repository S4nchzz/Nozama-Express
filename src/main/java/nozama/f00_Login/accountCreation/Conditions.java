package nozama.f00_Login.accountCreation;

import java.sql.ResultSet;
import javax.swing.JOptionPane;

import nozama_database.sendRequest.DatabaseRequestManagment;

/**
 * Clase que controla las condiciones de los valores al crear una cuenta
 */
public class Conditions {
    private final String username;
    private final String fullName;
    private final String telf;
    private final String password;

    private ResultSet rs;

    /**
     * Constructor de la clase con los parametros de los valores
     * @param username Usuario
     * @param fullName Nombre completo del usuario
     * @param telf Telefono +34
     * @param pass Contraseña del usuario
     */
    public Conditions (String username, String fullName, String telf, String pass) {
        this.username = username;
        this.fullName = fullName;
        this.telf = telf;
        this.password = pass;
    }

    /**
     * Condiciones del usuario
     * 
     * Condiciones :
     * - Que no este vacio
     * - Que no tenga espacios en blanco
     * - Que el resultado de la query sea null (que no exista el usuario)
     * - Que la longitud sea mas de 5 caracteres
     * - Uso solo de letras y numeros
     * - Que los numeros del usuario no supere los caracteres alfabeticos     * 
     * @return false si alguna condicion no se cumple
     */
    protected boolean usernameConditions () {
        if (!username.isEmpty() && !username.isBlank()) {
            this.rs = DatabaseRequestManagment.getQueryResult(username);
            
            // Si rs es distinto de null querra decir que se encontro un usuario en la base
            // de datos con el mismo nombre de usuario a elegir
            if (rs != null) {
                JOptionPane.showMessageDialog(null, "Usuario en uso");
                return false;
            }
            
            
            if (username.length() < 5) {
                JOptionPane.showMessageDialog(null, "El nombre de usuario debe tener mas de 5 caracteres");
                return false;
            } 
            
            for (int i = 0; i < username.length(); i++) {
                if (username.charAt(i) == ' ' || (username.charAt(i) < 48)
                        || (username.charAt(i) > 57 && username.charAt(i) < 65)
                        || (username.charAt(i) > 90 && username.charAt(i) < 97)
                        || (username.charAt(i)) > 122 && username.charAt(i) <= 127) {
                        
                        JOptionPane.showMessageDialog(null, "Usa solo letras y numeros en tu nombre de usuario (sin espacios)");
                    return false;
                }
            }

            int cNumbers = 0;
            int cLetters = 0;

            for (int i = 0; i < username.length(); i++) {
                if (username.charAt(i) > 47 && username.charAt(i) < 58) {
                    cNumbers++;
                } else {
                    cLetters++;
                }
            }

            if (cNumbers >= cLetters) {
                JOptionPane.showMessageDialog(null, "No puede haber mas numeros que letras en el usuario");
                return false;
            }

            return true;
        }

        return false;
    }

    /**
     * Condiciones de la contraseña
     * 
     * Condiciones :
     * - Que no este vacio
     * - Que no tenga espacios en blanco
     * @return false si alguna condicion no se cumple
     */
    protected boolean passwordConditions () {
        if (!password.isEmpty() && !password.isBlank()) {
            if (password.length() < 5) {
                JOptionPane.showMessageDialog(null, "La contraseña debe de ser como minimo de 5 caracteres");
                return false;
            } 
        } else {
            return false;
        }
        return true;
    }

    /**
     * Condiciones del nombre completo
     * 
     * Condiciones :
     * - Que no este vacio
     * - Que no tenga espacios en blanco
     * - Que sean unicamente caracteres alfabetico
     * - Que no hayan 2 espacios juntos
     * 
     * @return false si alguna condicion no se cumple
     */
    protected boolean fullNameConditions () {
        int cSpace = 0;
        if (!fullName.isBlank() && !fullName.isEmpty()) {
            for (int i = 0; i < fullName.length(); i++) {
                if (fullName.charAt(i) > 47 && fullName.charAt(i) < 58) {
                    return false;
                }

                if (fullName.charAt(i) == ' ') {
                    cSpace++;
                } else if (cSpace >= 2) {
                    JOptionPane.showMessageDialog(null, "Asegurate de que no hay espacios extra en tu nombre completo");
                    return false;
                } else {
                    cSpace = 0;
                }
            }
        }

        return true;
    }

    /**
     * Condiciones del telefonno
     * 
     * Condiciones :
     * - Que no este vacio
     * - Que sean unicamente caracteres alfanumericos
     * - Que sea una longitud exacta de 9 caracteres
     * @return false si alguna condicion no se cumple
     */
    protected boolean telfConditions () {
        int cNumbers = 0;
        if (!telf.isBlank()) {
            for (int i = 0; i < telf.length(); i++) {
                if (telf.charAt(i) < 47 || telf.charAt(i) > 57) {
                    JOptionPane.showMessageDialog(null, "Utiliza solo numeros en el telefono");
                    return false;
                } else {
                    cNumbers++;
                }
            }

            // Menor que el tamaño de un numero en españa +34
            if (cNumbers != 9) {
                JOptionPane.showMessageDialog(null, "El numero de telefono tiene que ser de 9 cifras");
                return false;
            }
        }

        return true;
    } 
}