package nozama.f01_FrontPage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

import nozama.f01_FrontPage.adminPanel.ticketPanel.CentralizedTicketElements;
import nozama.f01_FrontPage.adminPanel.ticketPanel.TicketElement;
import nozama.f01_FrontPage.ticketChat.CentralizedChats;
import nozama.f01_FrontPage.ticketChat.ChatBoxController;

public class NozamaServerSocket {
    private ServerSocket s;
    private int ticketID;
    private int userID;

    private final CentralizedChats centralizedChats;
    private final CentralizedTicketElements centralizedTicketElements;

    public NozamaServerSocket () {
        this.centralizedChats = CentralizedChats.getInstance();
        this.centralizedTicketElements = CentralizedTicketElements.getInstance();
        
        try {
            s = new ServerSocket(25567);

            while (true) {
                Socket clientSocket = s.accept();
    
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                String clientInput = in.readLine();
                
                if (clientInput.contains("admin") || clientInput.contains("user")) {
                    this.ticketID = substractTicketID(clientInput);
                    this.userID = substractUserID(clientInput);
                }
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
                boolean messageFromAdmin = false;
                if (in.contains("admin")) {
                    messageFromAdmin = true;
                }

                replicateMessageToChats(messageFromAdmin, substractPrefix(in), userID);
            }
        }
    }

    private int substractUserID(String in) {
        StringBuilder sb = new StringBuilder();

        for (int i = 1; i < in.length(); i++) {
            if (in.charAt(i) != ':') {
                sb.append(in.charAt(i));
            } else {
                break;
            }
        }

        return Integer.valueOf(sb.toString());
    }

    private int substractTicketID(String in) {
        StringBuilder sb = new StringBuilder();

        int countDoubleDot = 0;
        for (int i = 0; i < in.length(); i++) {
            if (in.charAt(i) == ':') {
                countDoubleDot++;
            }

            if (countDoubleDot == 2) {
                if (in.charAt(i) != '}') {
                    i++;
                    sb.append(in.charAt(i));
                } else {
                    break;
                }
            }
        }

        return Integer.valueOf(sb.toString());
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

    private void replicateMessageToChats(boolean fromAdmin, String message, int userID) {
        for (ChatBoxController chat : centralizedChats.getChats()) {
            if (chat.getTicketData().getTicket_id() == ticketID) {
                chat.addMessage(fromAdmin, message, userID);
            }
        }
    }
    
    private void noticeChats(int ticket_id, boolean notifyStatus) {
        for (TicketElement ticket : centralizedTicketElements.getChats()) {
            if (ticket.getTicketData().getTicket_id() == ticket_id) {
                ticket.popUpNotice(notifyStatus);
            }
        }
    }

    public void stop() {
        try {s.close();} catch (IOException e) {}
    }
}