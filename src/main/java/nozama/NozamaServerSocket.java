package nozama;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

import nozama.f01_FrontPage.adminPanel.ticketPanel.CentralizedTicketElements;
import nozama.f01_FrontPage.adminPanel.ticketPanel.TicketElement;
import nozama.f01_FrontPage.ticketChat.CentralizedChats;
import nozama.f01_FrontPage.ticketChat.ChatBoxController;
import nozama.f01_FrontPage.ticketChat.messagesListener.PopupMessageShower;
import nozama.f01_FrontPage.ticketChat.messagesListener.SocketChatComunicationData;

public class NozamaServerSocket {
    private ServerSocket s;

    private final CentralizedChats centralizedChats;
    private final CentralizedTicketElements centralizedTicketElements;

    public NozamaServerSocket () {
        this.centralizedChats = CentralizedChats.getInstance();
        this.centralizedTicketElements = CentralizedTicketElements.getInstance();

        try {
            s = new ServerSocket(ServerSocketData.PORT);

            while (true) {
                Socket clientSocket = s.accept();
                ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());

                Object obj = in.readObject();

                if (obj instanceof SocketChatComunicationData) {
                    SocketChatComunicationData messageData = (SocketChatComunicationData) obj;
                    replicateMessageToChats(messageData.getSendedFromAdmin(),
                            messageData.getContent(), messageData.getUserID(), messageData.getTicketID());
                } else if (obj instanceof PopupMessageShower) {
                    PopupMessageShower popUp = (PopupMessageShower) obj;
                    noticeChats(popUp.getTicketID(), popUp.getClosed());
                }
            }
            
        } catch (IOException | ClassNotFoundException e) {
            System.out.println(e.toString());
        }
    }

    private void replicateMessageToChats(boolean fromAdmin, String message, int userID, int ticketID) {
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