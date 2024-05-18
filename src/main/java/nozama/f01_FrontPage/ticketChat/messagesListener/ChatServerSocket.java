package nozama.f01_FrontPage.ticketChat.messagesListener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

import nozama.f01_FrontPage.adminPanel.ticketPanel.CentralizedTicketTemplates;
import nozama.f01_FrontPage.adminPanel.ticketPanel.TicketElement;
import nozama.f01_FrontPage.ticketChat.CentralizedChats;
import nozama.f01_FrontPage.ticketChat.ChatBoxController;

public class ChatServerSocket {
    private ServerSocket s;
    private int ticketID; 

    public ChatServerSocket () {
        try {
            s = new ServerSocket(25567);

            while (true) {
                Socket clientSocket = s.accept();
    
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                String clientInput = in.readLine();
                
                decrypt(clientInput);
            }
            
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private void decrypt(String in) {
        if (in != null) {
            StringBuilder sb = new StringBuilder();
            
            if (in.contains("popUpTicket")) {
                sb.delete(0, sb.length());
    
                int doubleDot = 13;
                for (int i = doubleDot; i < in.length(); i++) {
                    if (in.charAt(i) != '}') {
                        sb.append(in.charAt(i));
                    } else {
                        break;
                    }
                }
                int ticket = Integer.valueOf(sb.toString());

                if (in.contains("close")) {
                    noticeChats(ticket, false);
                } else {
                    noticeChats(ticket, true);
                }
    
            } else if (in.contains("user") || in.contains("admin")) {
                sb.delete(0, sb.length());
                replicateMessageToChats(messageFromAdmin(in), substractPrefix(in));
            }
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
        for (ChatBoxController chat : CentralizedChats.getChats()) {
            if (chat.getTicketData().getTicket_id() == ticketID) {
                chat.addMessage(fromAdmin, message);
            }
        }
    }
    
    private void noticeChats(int ticket_id, boolean notifyStatus) {
        for (TicketElement ticket : CentralizedTicketTemplates.getChats()) {
            if (ticket.getTicketData().getTicket_id() == ticket_id) {
                ticket.popUpNotice(notifyStatus);
            }
        }
    }

    public void stop() {
        try {s.close();} catch (IOException e) {}
    }
}