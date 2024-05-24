package nozama.f01_FrontPage.ticketChat.messagesListener;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;

import nozama.ServerSocketData;

public class UserSocket implements Serializable {
    public UserSocket (SocketChatComunicationData socketMessageData) {
        try {
            Socket s = new Socket(ServerSocketData.IP, ServerSocketData.PORT);
            ObjectOutputStream out = new ObjectOutputStream(s.getOutputStream());
            out.writeObject(socketMessageData);
            out.close();
            s.close();
        } catch (IOException e) {
            
        }
    }
}