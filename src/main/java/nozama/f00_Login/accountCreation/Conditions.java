package nozama.f00_Login.accountCreation;

import javafx.scene.control.TextField;

public class Conditions {
    private final String username;
    private final String fullName;
    private final String telf;
    private final String password;

    public Conditions (TextField username, TextField fullName, TextField telf, TextField pass) {
        this.username = username.getText();
        this.fullName = fullName.getText();
        this.telf = telf.getText();
        this.password = pass.getText();
    }


    protected boolean usernameConditions () {
        if (!username.isEmpty() && !username.isBlank()) {

        if (username.length() < 5) {
            System.out.println("Usuario invalido debe de ser de 5 caracteres o mas");
            return false;
        } 
        
        for (int i = 0; i < username.length(); i++) {
            if (username.charAt(i) == ' ' || (username.charAt(i) < 48)
                    || (username.charAt(i) > 57 && username.charAt(i) < 65)
                    || (username.charAt(i) > 90 && username.charAt(i) < 97)
                    || (username.charAt(i)) > 122 && username.charAt(i) <= 127) {
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
            return false;
        }

        return true;
    }

    return false;
    }

    protected boolean passwordConditions () {
        if (password.length() < 5) {
            System.out.println("Contraseña pequeña, debe de ser de 5 caracteres o mas");
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
                if (telf.charAt(i) < 47 && telf.charAt(i) > 57) {
                    return false;
                } else {
                    cNumbers++;
                }
            }

            // Menor que el tamaño de un numero en españa +34
            if (cNumbers < 9) {
                return false;
            }
        }

        return true;
    } 
}