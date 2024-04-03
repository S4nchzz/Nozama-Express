package nozama_database.credentials;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Esta clase codifica un String y retorna un array
 * de bytes de este y un salt aleatorio.
 */
public class PasswordComplexity {
    /**
     * Este metodo crea una instancia del objeto MessageDigest
     * llamando a getInstance() con el algoritmo SHA-256 que sera
     * la forma de codificacion del String
     * @param s String a codificar
     * @return bytes [] si la operacion se ejecuta correctamente null si no
     */
    public static byte[] sha256(String s) {
        final byte[] b = s.getBytes();

        try {
            MessageDigest m = MessageDigest.getInstance("SHA-256");
            return m.digest(b);
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }

    /**
     * Este metodo crea un objeto StringBuilder para concatenar
     * caracteres entre el 33 y el 94 en la tabla ascii
     * @return toString() del objeto StringBuilder
     */
    public static String saltGenerator() {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < 15; i++) {
            sb.append((char) (33 + (int) (Math.random() * 94)));
        }

        return sb.toString();
    }
}