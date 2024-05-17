package nozama.f01_FrontPage.chat.messagesListener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

import nozama.f01_FrontPage.chat.CentralizedChats;
import nozama.f01_FrontPage.chat.ChatBoxController;

public class ChatServerSocket {
    private ServerSocket s;
    private int ticketID; 

    public ChatServerSocket (ChatBoxController c) {
        try {
            s = new ServerSocket(25567);
            Socket clientSocket = s.accept();

            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            String clientInput = in.readLine();

            replicateMessageToChats(messageFromAdmin(clientInput), substractPrefix(clientInput));
            
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private String substractPrefix(String message) {
        StringBuilder sb = new StringBuilder();

        boolean closedBraket = false;

        for (int i = 0; i < message.length(); i++) {
            if (closedBraket) {
                sb.append(message.charAt(i));
            }

            if (message.charAt(i) == '}') {
                closedBraket = true;
            }
        }

        return sb.toString();
        
    }
    private boolean messageFromAdmin(String clientInput) {
        StringBuilder sb = new StringBuilder();
        
        int finalBracket = 0;

        for (int i = 0; i < clientInput.length(); i++) {
            if (clientInput.charAt(i) == '}') {
                finalBracket = i;
                break;
            }
        }

        String entirePrefix = clientInput.substring(0, finalBracket + 1);

        if (entirePrefix.contains("admin")) {
            for (int i = 7; i < finalBracket; i++) {
                sb.append(clientInput.charAt(i));
            }

            this.ticketID = Integer.valueOf(sb.toString());
            return true;
        } else if (entirePrefix.contains("user")) {
            for (int i = 6; i < finalBracket; i++) {
                sb.append(clientInput.charAt(i));                
            }

            this.ticketID = Integer.valueOf(sb.toString());
            return false;
        }
        return false;
    }

    private void replicateMessageToChats(boolean fromAdmin, String message) {
        for (ChatBoxController chat : CentralizedChats.getInstance().getChats()) {
            if (chat.getTicketData().getTicket_id() == ticketID) {
                chat.addMessage(fromAdmin, message);
            }
        }
    }

    public void stop() {
        try {s.close();} catch (IOException e) {}
    }
}
