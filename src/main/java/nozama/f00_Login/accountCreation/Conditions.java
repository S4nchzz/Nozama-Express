package nozama.f00_Login.accountCreation;

import java.sql.ResultSet;
import javax.swing.JOptionPane;

import nozama_database.sendRequest.DatabaseRequestManagment;

public class Conditions {
    private final String username;
    private final String fullName;
    private final String telf;
    private final String password;

    private ResultSet rs;

    public Conditions (String username, String fullName, String telf, String pass) {
        this.username = username;
        this.fullName = fullName;
        this.telf = telf;
        this.password = pass;
    }

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
            if (cNumbers < 9) {
                JOptionPane.showMessageDialog(null, "El numero de telefono tiene que ser de 9 cifras");
                return false;
            }
        }

        return true;
    } 
}