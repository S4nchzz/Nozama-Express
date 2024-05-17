package nozama.f01_FrontPage.chat.messagesListener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

import nozama.f01_FrontPage.chat.ChatBoxController;

public class ChatServerSocket {
    private ServerSocket s;
    public ChatServerSocket (ChatBoxController c) {
        try {
            s = new ServerSocket(25567);
            Socket clientSocket = s.accept();

            BufferedReader input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            String clientInput = input.readLine();

            c.addMessage(messageFromAdmin(clientInput), substractPrefix(clientInput));
            
        } catch (IOException e) {
            
        }
    }

    private String substractPrefix(String message) {
        StringBuilder sb = new StringBuilder();

        int firstBraket = 7;

        for (; firstBraket < message.length(); firstBraket++) {
            sb.append(message.charAt(firstBraket));
        }

        return sb.toString();
    }

    private boolean messageFromAdmin(String clientInput) {
        StringBuilder sb = new StringBuilder();
        
        int firstBraket = 1;
        int finalBraket = 5;
        
        for (; firstBraket <= finalBraket; firstBraket++) {
            sb.append(clientInput.charAt(firstBraket));
        }
        
        String prefix = sb.toString();

        if (prefix != null && prefix.equals("user")) {
            return false;
        } else if (prefix != null && prefix.equals("admin")) {
            return true;
        }

        return false;
    }

    public void stop() {
        try {s.close();} catch (IOException e) {}
    }
}
